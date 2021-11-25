<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="phonesPageNumber" required="true" %>

<c:set var="buttonsNumber" value="9"/>
<c:set var="currentPage" value="${not empty param.pageNumber ? param.pageNumber : 1}"/>
<c:set var="pageBegin" value="${currentPage > 5 ? currentPage - 5 : 1}"/>
<c:set var="pageEnd"
       value="${currentPage < phonesPageNumber - 5 ? currentPage + 5 : phonesPageNumber}"/>

<nav aria-label="Page navigation example">
    <ul class="pagination">
        <li class="page-item ${currentPage != 1 ? '' : 'disabled'}">
            <a class="page-link"
               href="?pageNumber=${currentPage - 1}&order=${param.order}&orderDirection=${param.orderDirection}&query=${param.query}">
                &laquo;
            </a>
        </li>
        <c:forEach begin="${pageBegin}" end="${pageEnd}" var="pageNum">
            <li class="page-item ${pageNum eq currentPage ? 'active' : ''}">
                <a class="page-link"
                   href="?pageNumber=${pageNum}&order=${param.order}&orderDirection=${param.orderDirection}&query=${param.query}">
                    ${pageNum}
                </a>
            </li>
        </c:forEach>
        <li class="page-item ${currentPage != phonesPageNumber ? '' : 'disabled'}">
            <a class="page-link"
               href="?pageNumber=${currentPage + 1}&order=${param.order}&orderDirection=${param.orderDirection}&query=${param.query}">
                &raquo;
            </a>
        </li>
    </ul>
</nav>
