<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" %>
<%@ attribute name="orderDirection" required="true" %>

<a href="?order=${order}&orderDirection=${orderDirection}&query=${param.query}"
   style="text-decoration: none; ${order eq param.order and orderDirection eq param.orderDirection ? 'font-weight: bold' : ''}">
    ${orderDirection eq "asc" ? "&uArr;" : "&dArr;"}
</a>