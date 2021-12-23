<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tags:master pageTitle="Login">
    <div>
        <c:if test="${not logined}">
            <p style="color: red"><spring:message code="login.message"/></p>
        </c:if>
        <c:if test="${haveError}">
            <p style="color: red"><spring:message code="login.error"/></p>
        </c:if>
        <c:if test="${logout}">
            <p style="color: green"><spring:message code="logout.success"/></p>
        </c:if>
        <c:if test="${not empty username}">
            <p><spring:message code="login.username"/>"${username}"</p>
        </c:if>
    </div>
    <div>
        <form method="post">
            <div>
                <span><spring:message code="login.username"/> </span>
                <input type="text" name="username"/>
            </div>
            <div>
                <span><spring:message code="login.password"/> </span>
                <input type="text" name="password"/>
            </div>
            <div>
                <button type="submit"><spring:message code="login.login"/></button>
            </div>
        </form>
    </div>
</tags:master>