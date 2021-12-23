<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="pageTitle" required="true" %>

<html>
<head>
    <title>${pageTitle}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
          rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
          crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/addToCart.js"></script>
</head>
<script type="text/javascript">
    $(document).ready(function () {
        getMiniCart()
    });
</script>
<body>
<header>
    <br/>
    <nav class="navbar navbar-light bg-light">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/productList">Phonify</a>
            <form class="d-flex" action="${pageContext.request.contextPath}/cart">
                <a class="btn btn-outline-success" type="submit" id="miniCart"
                   href="<c:url value="/cart"/>"></a>
            </form>
            <c:choose>
                <c:when test="${logined}">
                    <a href="<c:url value="/security_logout"/>">
                        <spring:message code="logout.logout"/>
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="<c:url value="/login"/>">
                        <spring:message code="login.login"/>
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </nav>
    <b>
        <hr size="3px"/>
    </b>
</header>
<main>
    <jsp:doBody/>
</main>
</body>
</html>
