<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ attribute name="label" required="true" %>
<%@ attribute name="name" required="true" %>

<tr>
    <td>${label}</td>
    <td><form:input path="${name}"/></td>
</tr>
<tr>
    <td/>
    <td><form:errors path="${name}" cssStyle="color: red"/></td>
</tr>