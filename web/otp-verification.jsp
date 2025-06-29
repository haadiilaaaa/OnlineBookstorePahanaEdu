<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>OTP Verification</title></head>
<body>
<%
    String userId = request.getParameter("userId");
    String userType = request.getParameter("userType");

    if (userId == null || userType == null) {
        userId = (String) request.getAttribute("userId");
        userType = (String) request.getAttribute("userType");
    }
%>

<form action="OtpVerificationServlet" method="post">
    <input type="hidden" name="userId" value="<%= userId %>" />
    <input type="hidden" name="userType" value="<%= userType %>" />
    <label for="otp">Enter OTP:</label>
    <input type="text" name="otp" required />
    <button type="submit">Verify</button>
</form>

<% if (request.getAttribute("error") != null) { %>
    <p style="color:red;"><%= request.getAttribute("error") %></p>
    
<% } %>
<% if (request.getAttribute("error") != null) { %>
    <p style="color:red;"><%= request.getAttribute("error") %></p>
<% } else if (request.getAttribute("success") != null) { %>
    <p style="color:green;"><%= request.getAttribute("success") %></p>
<% } %>

</body>
</html>
