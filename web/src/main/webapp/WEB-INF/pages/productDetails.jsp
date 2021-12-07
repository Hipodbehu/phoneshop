<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tags:master pageTitle="Product Details">
    <div>
        <a class="btn btn-primary" href="<c:url value="/productList"/>">
            <spring:message code="main.back"/>
        </a>
    </div>
    <div>
        <h2>${phone.model}</h2>
        <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
        <div>
                ${phone.description}
        </div>
        <div>
            <p><spring:message code="main.price"/></p>
            <input id="quantity-${phone.id}" name="quantity"/>
            <div id="quantityMessage-${phone.id}"></div>
            <button class="btn-light" id="addButton-${phone.id}" onclick="addToCart(${phone.id})">
                <spring:message code="main.addToCart"/>
            </button>
        </div>
        <div>
            <p><spring:message code="main.display"/></p>
            <table class="table table-light table-bordered">
                <tr>
                    <th><spring:message code="main.size"/></th>
                    <td>${phone.displaySizeInches}"</td>
                </tr>
                <tr>
                    <th><spring:message code="main.resolution"/></th>
                    <td>${phone.displayResolution}</td>
                </tr>
                <tr>
                    <th><spring:message code="main.technology"/></th>
                    <td>${phone.displayTechnology}</td>
                </tr>
                <tr>
                    <th><spring:message code="main.pixelDensity"/></th>
                    <td>${phone.pixelDensity}</td>
                </tr>
            </table>
        </div>
        <div>
            <p><spring:message code="main.dimensionsWeight"/></p>
            <table class="table table-light table-bordered">
                <tr>
                    <th><spring:message code="main.size"/></th>
                    <td>${phone.lengthMm}mm</td>
                </tr>
                <tr>
                    <th><spring:message code="main.width"/></th>
                    <td>${phone.widthMm}mm</td>
                </tr>
                <tr>
                    <th><spring:message code="main.weight"/></th>
                    <td>${phone.weightGr}</td>
                </tr>
            </table>
        </div>
        <div>
            <p><spring:message code="main.camera"/></p>
            <table class="table table-light table-bordered">
                <tr>
                    <th><spring:message code="main.front"/></th>
                    <td>${phone.frontCameraMegapixels} <spring:message code="main.megapixels"/></td>
                </tr>
                <tr>
                    <th><spring:message code="main.back"/></th>
                    <td>${phone.backCameraMegapixels} <spring:message code="main.megapixels"/></td>
                </tr>
            </table>
        </div>
        <div>
            <p><spring:message code="main.battery"/></p>
            <table class="table table-light table-bordered">
                <tr>
                    <th><spring:message code="main.talkTime"/></th>
                    <td>${phone.talkTimeHours} <spring:message code="main.hours"/></td>
                </tr>
                <tr>
                    <th><spring:message code="main.standByTime"/></th>
                    <td>${phone.standByTimeHours} <spring:message code="main.hours"/></td>
                </tr>
                <tr>
                    <th><spring:message code="main.batteryCapacity"/></th>
                    <td>${phone.batteryCapacityMah}<spring:message code="main.mAh"/></td>
                </tr>
            </table>
        </div>
        <div>
            <p><spring:message code="main.other"/></p>
            <table class="table table-light table-bordered">
                <tr>
                    <th><spring:message code="main.colors"/></th>
                    <td>
                        <c:forEach var="color" items="${phone.colors}">
                            ${color.code}<br/>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <th><spring:message code="main.deviceType"/></th>
                    <td>${phone.deviceType}</td>
                </tr>
                <tr>
                    <th><spring:message code="main.bluetooth"/></th>
                    <td>${phone.bluetooth}</td>
                </tr>
                <tr>
                    <th><spring:message code="main.pixelDensity"/></th>
                    <td>${phone.pixelDensity}</td>
                </tr>
            </table>
        </div>
    </div>
</tags:master>