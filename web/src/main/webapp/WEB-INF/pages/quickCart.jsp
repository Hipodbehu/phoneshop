<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tags:master pageTitle="Quick cart">
    <p style="color: green;">${successMessage}</p>
    <p style="color: red;">${errorMessage}</p>
    <div>
        <form:form method="post" modelAttribute="modelListWrapper">
            <table>
                <thead>
                <tr>
                    <td><spring:message code="main.model"/></td>
                    <td><spring:message code="main.quantity"/></td>
                </tr>
                </thead>
                <c:forEach var="i" begin="0" end="9" varStatus="status">
                    <tr>
                        <td>
                            <form:input path="list[${status.index}].model" value="${modelList[status.index].model}"/>
                            <br/>
                            <form:errors path="list[${status.index}].model" cssStyle="color: red"/>
                        </td>
                        <td>
                            <form:input path="list[${status.index}].quantity" value="${modelList[status.index].quantity}"/>
                            <br/>
                            <form:errors path="list[${status.index}].quantity" cssStyle="color: red"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <div>
                <button class="btn btn-primary" type="submit">
                    <spring:message code="main.addToCart"/>
                </button>
            </div>
        </form:form>
    </div>
</tags:master>