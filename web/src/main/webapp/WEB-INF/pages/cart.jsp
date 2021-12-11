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
    <form:form method="put" modelAttribute="productListWrapper">
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
                <c:forEach var="cartItem" items="${cart.itemList}" varStatus="status">
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
                            <form:input path="list[${status.index}].quantity"
                                        value="${cartItem.quantity}"/>
                            <form:hidden path="list[${status.index}].id"
                                         value="${cartItem.phone.id}"/>
                            <div>
                                <form:errors path="list[${status.index}].quantity" cssStyle="color: red"/>
                                <form:errors path="list[${status.index}].id" cssStyle="color: red"/>
                                <span style="color: red">${errors[cartItem.phone.id]}</span>
                            </div>
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
        <c:if test="${not empty cart.itemList}">
            <div>
                <button class="btn btn-primary" type="submit">
                    <spring:message code="main.update"/>
                </button>
                <a class="btn btn-primary" href="<c:url value="/order"/>"/>
                <spring:message code="main.order"/>
                </a>
            </div>
        </c:if>
    </form:form>
    <form:form method="delete" id="deleteCartItemForm"/>
</tags:master>