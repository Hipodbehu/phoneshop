function addToCart(phoneId) {
    var quantity = $('#quantity-' + phoneId).val();
    var quantityMessageId = '#quantityMessage-' + phoneId;
    var id = phoneId;
    var addProductInput = {};
    addProductInput["id"] = id;
    addProductInput["quantity"] = quantity;
    $.ajax({
        type : "POST",
        data : JSON.stringify(addProductInput),
        contentType : "application/json",
        dataType : "json",
        url : "ajaxCart",
        async : false,
        success: function (data) {
            if (data.successful) {
                            $(quantityMessageId).text(data.message).css({'color': 'green'});
            } else {
                            $(quantityMessageId).text(data.message).css({'color': 'red'});
                        }
            loadMiniCart(data.miniCart)
        }
    })
}

function getMiniCart() {
    $(document).ready(function () {
        $.get('/phoneshop-web/ajaxCart', function (data) {
            loadMiniCart(data)
        })
    });
}

function loadMiniCart(miniCart) {
    $("#miniCart").text("My cart: " + miniCart.totalQuantity + " items " + miniCart.totalCost + " $");
}