<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Edit Customer</title>
</head>
<body>
    <h2>Edit Customer</h2>

    <c:if test="${not empty error}">
        <div style="color: red;">${error}</div>
    </c:if>

    <form method="post" action="EditCustomerServlet">
        <input type="hidden" name="id" value="${customer.id}" />

        <label>Username:</label><br/>
        <input type="text" name="username" value="${customer.username}" readonly /><br/><br/>

        <label>First Name:</label><br/>
        <input type="text" name="firstName" value="${customer.firstName}" required /><br/><br/>

        <label>Last Name:</label><br/>
        <input type="text" name="lastName" value="${customer.lastName}" required /><br/><br/>

        <label>Email:</label><br/>
        <input type="email" name="email" value="${customer.email}" required /><br/><br/>

        <label>Contact Number:</label><br/>
        <input type="text" name="contactNumber" value="${customer.contactNumber}" /><br/><br/>

        <label>Address:</label><br/>
        <textarea name="address">${customer.address}</textarea><br/><br/>

        <button type="submit">Update Customer</button>
        <a href="AdminManageUsersServlet">Cancel</a>
    </form>
</body>
</html>
