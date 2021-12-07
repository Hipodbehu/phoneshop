<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="cart" type="com.es.core.cart.Cart" scope="session"/>

<form action="${pageContext.request.contextPath}/cart">
    <button type="submit" id="minicart">
        <spring:message code="minicart.cart"/> ${cart.totalQuantity} <spring:message code="minicart.items"/> ${cart.totalCost} $
    </button>
</form>