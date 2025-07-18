<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Delivery Partner Registration</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f9f9f9;
            padding: 20px;
        }
        .registration-form {
            max-width: 500px;
            margin: 0 auto;
            background: #fff;
            padding: 25px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .registration-form h2 {
            margin-bottom: 20px;
            text-align: center;
        }
        .registration-form label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
        }
        .registration-form input[type="text"],
        .registration-form input[type="email"],
        .registration-form input[type="password"],
        .registration-form input[type="tel"] {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
            box-sizing: border-box;
        }
        .registration-form button {
            margin-top: 25px;
            width: 100%;
            padding: 10px;
            background-color: #0078D7;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
        }
        .registration-form button:hover {
            background-color: #005a9e;
        }
        .error-message {
            color: red;
            margin-top: 10px;
            text-align: center;
        }
        .success-message {
            color: green;
            margin-top: 10px;
            text-align: center;
        }
    </style>
</head>
<body>

<div class="registration-form">
    <h2>Register as Delivery Partner</h2>
    
    <form action="${pageContext.request.contextPath}/RegisterServlet" method="post">
          <input type="hidden" name="userType" value="delivery" />
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required maxlength="50" />

        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName" required maxlength="50" />

        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName" required maxlength="50" />

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required maxlength="100" />

        <label for="contactNumber">Contact Number:</label>
        <input type="tel" id="contactNumber" name="contactNumber" required maxlength="15" pattern="[0-9]+" title="Only digits allowed" />

        <label for="password">Password:</label>
<input type="password" id="password" name="password" required minlength="6" />

<label for="confirmPassword">Confirm Password:</label> <!-- ✅ Add this -->
<input type="password" id="confirmPassword" name="confirmPassword" required minlength="6" />


        <label for="vehicleNumber">Vehicle Number:</label>
        <input type="text" id="vehicleNumber" name="vehicleNumber" required maxlength="20" />

        <button type="submit">Register</button>
    </form>

    <!-- Display messages -->
  <c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>
<c:if test="${not empty success}">
    <p style="color: green;">${success}</p>
</c:if>

</div>

</body>
</html>
