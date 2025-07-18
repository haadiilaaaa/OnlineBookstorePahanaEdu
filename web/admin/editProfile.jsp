<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="dto.AdminDTO" %>
<%
    AdminDTO admin = (AdminDTO) request.getAttribute("admin");
%>

<html>
<head>
    <title>Edit Profile</title>
</head>
<body>
    <h2>Edit Profile</h2>

    <c:if test="${not empty success}">
        <p style="color: green">${success}</p>
    </c:if>

    <c:if test="${not empty error}">
        <p style="color: red">${error}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/EditAdminProfileServlet" method="post">
        <input type="hidden" name="id" value="${admin.id}" />
        
        Username: <input type="text" name="username" value="${admin.username}" required /><br/>
        First Name: <input type="text" name="firstName" value="${admin.firstName}" required /><br/>
        Last Name: <input type="text" name="lastName" value="${admin.lastName}" required /><br/>
        Email: <input type="email" name="email" value="${admin.email}" required /><br/>
        Contact: <input type="text" name="contactNumber" value="${admin.contactNumber}" required /><br/>

        <button type="submit">Update Profile</button>
    </form>

    <a href="${pageContext.request.contextPath}/Admin_DashboardServlet">Back to Dashboard</a>
</body>
</html>
