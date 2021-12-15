<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tags:master pageTitle="Order">
    <div>
        <a class="btn btn-primary" href="<c:url value="/cart"/>">
            <spring:message code="order.back"/>
        </a>
    </div>
    <form:form method="post" commandName="userInfoWrapper">
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
            <div style="color: red">${error}</div>
            <c:set var="firstName"><spring:message code="order.firstName"/></c:set>
            <c:set var="lastName"><spring:message code="order.lastName"/></c:set>
            <c:set var="address"><spring:message code="order.address"/></c:set>
            <c:set var="phone"><spring:message code="order.phone"/></c:set>
            <table>
                <thead>
                <tags:orderFormRow label="${firstName}" name="firstName"/>
                <tags:orderFormRow label="${lastName}" name="lastName"/>
                <tags:orderFormRow label="${address}" name="address"/>
                <tags:orderFormRow label="${phone}" name="phone"/>
                </thead>
            </table>
            <div>
                <form:textarea path="comment"/>
                <br/>
                <form:errors path="comment" cssStyle="color: red"/>
            </div>
            <button class="btn btn-primary" type="submit">
                <spring:message code="main.order"/>
            </button>
        </div>
    </form:form>
</tags:master>