<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tags:master pageTitle="Orders">
    <div>
        <table class="table table-light table-bordered">
            <tr>
                <td><spring:message code="orders.number"/></td>
                <td><spring:message code="orders.customer"/></td>
                <td><spring:message code="orders.phone"/></td>
                <td><spring:message code="orders.address"/></td>
                <td><spring:message code="orders.date"/></td>
                <td><spring:message code="orders.total"/></td>
                <td><spring:message code="orders.status"/></td>
            </tr>
            <c:forEach var="order" items="${orders}">
                <tr>
                    <td>
                        <a href="<c:url value="/admin/orders/${order.id}"/>">
                                ${order.id}
                        </a>
                    </td>
                    <td>${order.firstName} ${order.lastName}</td>
                    <td>${order.contactPhoneNo}</td>
                    <td>${order.deliveryAddress}</td>
                    <td>${order.date}"</td>
                    <td>${order.totalPrice}$</td>
                    <td>${order.status.name()}</td>
                </tr>
            </c:forEach>
        </table>
    </div>
</tags:master>