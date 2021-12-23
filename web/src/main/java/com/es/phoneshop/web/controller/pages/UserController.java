package com.es.phoneshop.web.controller.pages;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
  public static final String HAVE_ERROR_PARAM = "haveError";
  public static final String LOGOUT_PARAM = "logout";
  public static final String USERNAME_ATTRIBUTE = "username";
  public static final String LOGINED_ATTRIBUTE = "logined";
  public static final String LOGIN_PAGE = "login";

  @RequestMapping(method = RequestMethod.GET)
  public String showLoginPage(Model model, Authentication authentication,
                              @RequestParam(value = HAVE_ERROR_PARAM, required = false) Boolean haveError,
                              @RequestParam(value = LOGOUT_PARAM, required = false) Boolean logout) {
    model.addAttribute(HAVE_ERROR_PARAM, haveError);
    model.addAttribute(LOGOUT_PARAM, logout);
    boolean logined;
    if (authentication != null) {
      logined = authentication.isAuthenticated();
      model.addAttribute(USERNAME_ATTRIBUTE, authentication.getName());
    } else {
      logined = false;
    }
    model.addAttribute(LOGINED_ATTRIBUTE, logined);
    return LOGIN_PAGE;
  }
}
