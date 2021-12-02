<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Product Details">
    <div>
        <a class="btn btn-primary" href="<c:url value="/productList"/>">
            Back to product list
        </a>
    </div>
    <div>
        <h2>${phone.model}</h2>
        <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
        <div>
                ${phone.description}
        </div>
        <div>
            <p>Price</p>
            <input id="quantity-${phone.id}" name="quantity"/>
            <div id="quantityMessage-${phone.id}"></div>
            <button class="btn-light" id="addButton-${phone.id}" onclick="addToCart(${phone.id})">
                Add to cart
            </button>
        </div>
        <div>
            <p>Display</p>
            <table class="table table-light table-bordered">
                <tr>
                    <th>Size</th>
                    <td>${phone.displaySizeInches}"</td>
                </tr>
                <tr>
                    <th>Resolution</th>
                    <td>${phone.displayResolution}</td>
                </tr>
                <tr>
                    <th>Technology</th>
                    <td>${phone.displayTechnology}</td>
                </tr>
                <tr>
                    <th>Pixel density</th>
                    <td>${phone.pixelDensity}</td>
                </tr>
            </table>
        </div>
        <div>
            <p>Dimensions & weight</p>
            <table class="table table-light table-bordered">
                <tr>
                    <th>Size</th>
                    <td>${phone.lengthMm}mm</td>
                </tr>
                <tr>
                    <th>Width</th>
                    <td>${phone.widthMm}mm</td>
                </tr>
                <tr>
                    <th>Weight</th>
                    <td>${phone.weightGr}</td>
                </tr>
            </table>
        </div>
        <div>
            <p>Camera</p>
            <table class="table table-light table-bordered">
                <tr>
                    <th>Front</th>
                    <td>${phone.frontCameraMegapixels} megapixels</td>
                </tr>
                <tr>
                    <th>Back</th>
                    <td>${phone.backCameraMegapixels} megapixels</td>
                </tr>
            </table>
        </div>
        <div>
            <p>Battery</p>
            <table class="table table-light table-bordered">
                <tr>
                    <th>Talk time</th>
                    <td>${phone.talkTimeHours} hours</td>
                </tr>
                <tr>
                    <th>Stand by time</th>
                    <td>${phone.standByTimeHours} hours</td>
                </tr>
                <tr>
                    <th>Battery capacity</th>
                    <td>${phone.batteryCapacityMah}mAh</td>
                </tr>
            </table>
        </div>
        <div>
            <p>Other</p>
            <table class="table table-light table-bordered">
                <tr>
                    <th>Colors</th>
                    <td>
                        <c:forEach var="color" items="${phone.colors}">
                            ${color.code}<br/>
                        </c:forEach>
                    </td>
                </tr>
                <tr>
                    <th>Device type</th>
                    <td>${phone.deviceType}</td>
                </tr>
                <tr>
                    <th>Bluetooth</th>
                    <td>${phone.bluetooth}</td>
                </tr>
                <tr>
                    <th>Pixel density</th>
                    <td>Pixel density</td>
                </tr>
            </table>
        </div>
    </div>
</tags:master>