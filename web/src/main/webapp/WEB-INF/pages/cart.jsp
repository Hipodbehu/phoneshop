<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tags:master pageTitle="Cart">
    <div>
        <a class="btn btn-primary" href="<c:url value="/productList"/>">
            <spring:message code="main.back"/>
        </a>
    </div>
    <form:form method="put">
        <div>
            <table class="table table-light table-bordered">
                <tr>
                    <td><spring:message code="main.brand"/></td>
                    <td><spring:message code="main.model"/></td>
                    <td><spring:message code="main.colors"/></td>
                    <td><spring:message code="main.displaySize"/></td>
                    <td><spring:message code="main.price"/></td>
                    <td><spring:message code="main.quantity"/></td>
                    <td><spring:message code="main.action"/></td>
                </tr>
                <c:forEach var="cartItem" items="${cart.itemList}">
                    <tr>
                        <td>${cartItem.phone.brand}</td>
                        <td>
                            <a href="<c:url value="/productDetails/${cartItem.phone.id}"/>">
                                    ${cartItem.phone.model}
                            </a>
                        </td>
                        <td>
                            <c:forEach var="color" items="${cartItem.phone.colors}">
                                ${color.code}<br/>
                            </c:forEach>
                        </td>
                        <td>${cartItem.phone.displaySizeInches}"</td>
                        <td>$ ${cartItem.phone.price}</td>
                        <td>
                            <input name="quantityList" value="${cartItem.quantity}"/>
                            <input type="hidden" name="idList" value="${cartItem.phone.id}">
                            <div style="color: red">${errors[cartItem.phone.id]}</div>
                        </td>
                        <td>
                            <button type="submit" form="deleteCartItemForm"
                                    formaction="<c:url value="/cart/${cartItem.phone.id}"/>" class="btn-light">
                                <spring:message code="main.remove"/>
                            </button>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <div>
            <button class="btn btn-primary" type="submit">
                <spring:message code="main.update"/>
            </button>
        </div>
    </form:form>
    <form:form method="delete" id="deleteCartItemForm"/>
</tags:master>