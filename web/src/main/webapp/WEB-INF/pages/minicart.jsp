<%@ page contentType="text/html;charset=UTF-8" %>

<jsp:useBean id="cart" type="com.es.core.cart.Cart" scope="session"/>

<form action="${pageContext.request.contextPath}/cart">
    <button type="submit" id="minicart">
        cart: ${cart.totalQuantity} items ${cart.totalCost} $
    </button>
</form>