<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tags:master pageTitle="Product List">
    <form>
        <p>
            <spring:message code="main.phones"/> <input name="query" value="${param.query}" type="search" placeholder="Search...">
        </p>
    </form>
    <div>
        <table class="table table-light table-bordered">
            <tr>
                <td><spring:message code="main.image"/></td>
                <td><spring:message code="main.brand"/>
                    <tags:sortLink sort="brand" sortDirection="asc"/>
                    <tags:sortLink sort="brand" sortDirection="desc"/>
                </td>
                <td><spring:message code="main.model"/>
                    <tags:sortLink sort="model" sortDirection="asc"/>
                    <tags:sortLink sort="model" sortDirection="desc"/>
                </td>
                <td><spring:message code="main.colors"/></td>
                <td><spring:message code="main.displaySize"/>
                    <tags:sortLink sort="display_size" sortDirection="asc"/>
                    <tags:sortLink sort="display_size" sortDirection="desc"/>
                </td>
                <td><spring:message code="main.price"/>
                    <tags:sortLink sort="price" sortDirection="asc"/>
                    <tags:sortLink sort="price" sortDirection="desc"/>
                </td>
                <td><spring:message code="main.quantity"/></td>
                <td><spring:message code="main.action"/></td>
            </tr>
            <c:forEach var="phone" items="${phones}">
                <tr>
                    <td>
                        <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                    </td>
                    <td>${phone.brand}</td>
                    <td>
                        <a href="<c:url value="/productDetails/${phone.id}"/>">
                                ${phone.model}
                        </a>
                    </td>
                    <td>
                        <c:forEach var="color" items="${phone.colors}">
                            ${color.code}<br/>
                        </c:forEach>
                    </td>
                    <td>${phone.displaySizeInches}"</td>
                    <td>$ ${phone.price}</td>
                    <td><input id="quantity-${phone.id}" name="quantity"/>
                        <div id="quantityMessage-${phone.id}"></div>
                    </td>
                    <td>
                        <button class="btn-light" id="addButton-${phone.id}" onclick="addToCart(${phone.id})">
                            <spring:message code="main.addToCart"/>
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <tags:pagination phonesPageNumber="${phonesPageNumber}" page="${pageNumber}"/>
</tags:master>