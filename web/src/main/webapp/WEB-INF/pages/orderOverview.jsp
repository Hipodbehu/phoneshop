<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tags:master pageTitle="Overview">
    <c:if test="${admin}">
        <spring:message code="admin.number"/> ${order.id} <spring:message code="admin.status"/> ${order.status}
    </c:if>
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
                <p><spring:message code="orderOverview.firstName"/> ${order.firstName}</p>
                <p><spring:message code="orderOverview.lastName"/> ${order.lastName}</p>
                <p><spring:message code="orderOverview.address"/> ${order.deliveryAddress}</p>
                <p><spring:message code="orderOverview.phone"/> ${order.contactPhoneNo}</p>
                <p>${order.comment}</p>
            </form>
        </div>
    </form:form>
    <div>
        <a class="btn btn-primary" href="<c:url value="/admin/orders"/>">
            <spring:message code="admin.back"/>
        </a>
        <c:if test="${admin}">
            <form:form method="put">
                <button class="btn btn-primary" type="submit">
                    <spring:message code="admin.delivered"/>
                </button>
                <input type="hidden" name="status" value="DELIVERED"/>
            </form:form>
            <form:form method="put">
                <button class="btn btn-primary" type="submit">
                    <spring:message code="admin.rejected"/>
                </button>
                <input type="hidden" name="status" value="REJECTED"/>
            </form:form>
        </c:if>
    </div>
</tags:master>