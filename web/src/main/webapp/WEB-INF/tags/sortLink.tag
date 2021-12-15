<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="sortDirection" required="true" %>

<a href="?sort=${sort}&sortDirection=${sortDirection}&query=${param.query}"
   style="text-decoration: none; ${order eq param.order and orderDirection eq param.orderDirection ? 'font-weight: bold' : ''}">
    ${sortDirection eq "asc" ? "&uArr;" : "&dArr;"}
</a>