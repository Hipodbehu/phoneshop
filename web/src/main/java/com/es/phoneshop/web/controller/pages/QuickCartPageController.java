package com.es.phoneshop.web.controller.pages;

import com.es.core.cart.Cart;
import com.es.core.cart.CartService;
import com.es.core.model.phone.Phone;
import com.es.core.model.product.ProductDao;
import com.es.core.model.product.ProductNotFoundException;
import com.es.core.order.OutOfStockException;
import com.es.phoneshop.web.controller.validation.ModelInputWrapper;
import com.es.phoneshop.web.controller.validation.ModelListWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

@Controller
@RequestMapping(value = "/quickCart")
public class QuickCartPageController {
  public static final String QUICK_CART_PAGE = "quickCart";
  public static final String MODEL_LIST_WRAPPER_ATTRIBUTE = "modelListWrapper";
  public static final String MODEL_LIST_ATTRIBUTE = "modelList";
  public static final String MODEL_ERROR_FIELD = "list[%d].model";
  public static final String QUANTITY_ERROR_FIELD = "list[%d].quantity";
  public static final String PRODUCT_NOT_FOUND_MESSAGE = "Product not found";
  public static final String NEW_LINE = "\n";
  public static final String SUCCESS_MESSAGE_ATTRIBUTE = "successMessage";
  public static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
  public static final String ADD_SUCCESS = "product added successfully";
  public static final String ERRORS_MESSAGE = "There where some errors";

  @Resource
  private CartService cartService;

  @Resource
  private ProductDao productDao;

  @RequestMapping(method = RequestMethod.GET)
  public String getQuickCart(Model model) {
    model.addAttribute(MODEL_LIST_WRAPPER_ATTRIBUTE, new ModelListWrapper());
    return QUICK_CART_PAGE;
  }

  @RequestMapping(method = RequestMethod.POST)
  public String addItems(HttpSession session, Model model,
                         @Valid @ModelAttribute ModelListWrapper modelListWrapper,
                         BindingResult bindingResult) {
    List<ModelInputWrapper> modelInputWrappers = modelListWrapper.getList();
    StringBuilder successMessage = new StringBuilder();
    LongStream.iterate(0, number -> number + 1)
            .limit(modelInputWrappers.size())
            .forEach(number -> {
              String phoneModel = modelListWrapper.getList().get((int) number).getModel();
              if (!StringUtils.isEmpty(phoneModel)) {
                Optional<Phone> optionalPhone = productDao.findByModel(modelListWrapper.getList()
                        .get((int) number).getModel());
                if (optionalPhone.isPresent()) {
                  Cart cart = cartService.getCart(session);
                  Phone phone = optionalPhone.get();
                  try {
                    Integer quantity = Integer.parseInt(modelInputWrappers.get((int) number).getQuantity());
                    if (quantity >= 0) {
                      addPhone(modelInputWrappers, successMessage, cart, phone, quantity, number, bindingResult);
                    }
                  } catch (NumberFormatException e) {
                  }
                } else {
                  addModelErrors(bindingResult, number, PRODUCT_NOT_FOUND_MESSAGE);
                }
              }
            });
    model.addAttribute(MODEL_LIST_ATTRIBUTE, modelListWrapper.getList());
    model.addAttribute(SUCCESS_MESSAGE_ATTRIBUTE, successMessage.toString());
    if (bindingResult.hasErrors()) {
      model.addAttribute(ERROR_MESSAGE_ATTRIBUTE, ERRORS_MESSAGE);
    }
    return QUICK_CART_PAGE;
  }

  private void addPhone(List<ModelInputWrapper> modelInputWrappers, StringBuilder successMessage,
                        Cart cart, Phone phone, Integer quantity, Long number, BindingResult bindingResult) {
    try {
      cartService.addPhone(cart, phone.getId(), quantity);
      successMessage.append(phone.getModel())
              .append(ADD_SUCCESS)
              .append(NEW_LINE);
      modelInputWrappers.get(number.intValue()).setModel("");
      modelInputWrappers.get(number.intValue()).setQuantity("");
    } catch (OutOfStockException exception) {
      addQuantityErrors(bindingResult, number, exception.getMessage());
    } catch (ProductNotFoundException exception) {
      addModelErrors(bindingResult, number, exception.getMessage());
    }
  }

  private void addModelErrors(BindingResult bindingResult, Long number, String message) {
    bindingResult.addError(
            new FieldError(MODEL_LIST_WRAPPER_ATTRIBUTE,
                    String.format(MODEL_ERROR_FIELD, number), message));
  }

  private void addQuantityErrors(BindingResult bindingResult, Long number, String message) {
    bindingResult.addError(
            new FieldError(MODEL_LIST_WRAPPER_ATTRIBUTE,
                    String.format(QUANTITY_ERROR_FIELD, number), message));
  }
}
