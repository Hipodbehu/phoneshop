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
                <c:forEach var="orderItem" items="${cart.itemList}">
                    <tr>
                        <td>${orderItem.phone.brand}</td>
                        <td>
                            <a href="<c:url value="/productDetails/${orderItem.phone.id}"/>">
                                    ${orderItem.phone.model}
                            </a>
                        </td>
                        <td>
                            <c:forEach var="color" items="${orderItem.phone.colors}">
                                ${color.code}<br/>
                            </c:forEach>
                        </td>
                        <td>${orderItem.phone.displaySizeInches}"</td>
                        <td>$ ${orderItem.phone.price}</td>
                        <td>
                            <input name="quantityList" value="${orderItem.quantity}"/>
                            <input type="hidden" name="idList" value="${orderItem.phone.id}">
                            <div style="color: red">${errors[orderItem.phone.id]}</div>
                        </td>
                        <td>
                            <button type="submit" form="deleteCartItemForm"
                                    formaction="<c:url value="/cart/${orderItem.phone.id}"/>" class="btn-light">
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