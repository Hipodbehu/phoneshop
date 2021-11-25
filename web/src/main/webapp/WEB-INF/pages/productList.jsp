<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Product List">
    <form>
        <p>
            Phones <input name="query" value="${param.query}" type="search" placeholder="Search...">
        </p>
    </form>
    <div>
        <table class="table table-light table-bordered">
            <tr>
                <td>Image</td>
                <td>Brand
                    <tags:sortLink order="brand" orderDirection="asc"/>
                    <tags:sortLink order="brand" orderDirection="desc"/>
                </td>
                <td>Model
                    <tags:sortLink order="model" orderDirection="asc"/>
                    <tags:sortLink order="model" orderDirection="desc"/>
                </td>
                <td>Colors</td>
                <td>Display size
                    <tags:sortLink order="displaySizeInches" orderDirection="asc"/>
                    <tags:sortLink order="displaySizeInches" orderDirection="desc"/>
                </td>
                <td>Price
                    <tags:sortLink order="price" orderDirection="asc"/>
                    <tags:sortLink order="price" orderDirection="desc"/>
                </td>
                <td>Quantity</td>
                <td>Action</td>
            </tr>
            <c:forEach var="phone" items="${phones}">
                <tr>
                    <td>
                        <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                    </td>
                    <td>${phone.brand}</td>
                    <td>${phone.model}</td>
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
                        <button class="btn-light" id="addButton-${phone.id}" onclick="addToCart(${phone.id})">Add to
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <tags:pagination phonesPageNumber="${phonesPageNumber}"/>
</tags:master>