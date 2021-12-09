<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tags:master pageTitle="Cart">
    <div>
        <a class="btn btn-primary" href="<c:url value="/cart"/>">
            <spring:message code="order.back"/>
        </a>
    </div>
    <form:form method="post">
        <div>
            <table class="table table-light table-bordered">
                <tr>
                    <td><spring:message code="main.brand"/></td>
                    <td><spring:message code="main.model"/></td>
                    <td><spring:message code="main.colors"/></td>
                    <td><spring:message code="main.displaySize"/></td>
                    <td><spring:message code="main.quantity"/></td>
                    <td><spring:message code="main.price"/></td>
                </tr>
                <c:forEach var="orderItem" items="${order.orderItems}">
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
                        <td>
                                ${orderItem.quantity}
                            <div style="color: red">${errors[orderItem.phone.id]}</div>
                        </td>
                        <td>$ ${orderItem.phone.price}</td>
                    </tr>
                </c:forEach>
                <tr>
                    <td/>
                    <td/>
                    <td/>
                    <td/>
                    <td>
                        <spring:message code="order.subtotal"/>
                    </td>
                    <td>
                            ${order.subtotal}
                    </td>
                </tr>
                <tr>
                    <td/>
                    <td/>
                    <td/>
                    <td/>
                    <td>
                        <spring:message code="order.delivery"/>
                    </td>
                    <td>
                            ${order.deliveryPrice}
                    </td>
                </tr>
                <tr>
                    <td/>
                    <td/>
                    <td/>
                    <td/>
                    <td>
                        <spring:message code="order.total"/>
                    </td>
                    <td>
                            ${order.totalPrice}
                    </td>
                </tr>
            </table>
            <form>
                <p><spring:message code="order.firstName"/> <input name="firstName"></p>
                <div style="color: red">${errors["firstName"]}</div>
                <p><spring:message code="order.lastName"/> <input name="lastName"></p>
                <div style="color: red">${errors["lastName"]}</div>
                <p><spring:message code="order.address"/> <input name="address"></p>
                <div style="color: red">${errors["address"]}</div>
                <p><spring:message code="order.phone"/> <input name="phone"></p>
                <div style="color: red">${errors["phone"]}</div>
                <button class="btn btn-primary" type="submit">
                    <spring:message code="main.order"/>
                </button>
            </form>
        </div>
    </form:form>
</tags:master>