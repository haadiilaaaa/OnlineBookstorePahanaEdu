<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Manage Customers</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ccc; padding: 8px; }
        th { background: #f5f5f5; }
    </style>
</head>
<body>
    <h2>Customer Management</h2>
    <table>
        <tr>
            <th>ID</th><th>Username</th><th>Name</th><th>Email</th><th>Actions</th>
        </tr>
        <c:forEach var="c" items="${customers}">
            <tr>
                <td>${c.id}</td>
                <td>${c.username}</td>
                <td>${c.firstName} ${c.lastName}</td>
                <td>${c.email}</td>
                <td>
                    <form method="post" action="DeleteCustomerServlet" style="display:inline;">
                        <input type="hidden" name="customerId" value="${c.id}" />
                        <button type="submit">Delete</button>
                    </form>
                    <a href="EditCustomerServlet?customerId=${c.id}">Edit</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
