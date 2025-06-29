<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Customer Registration</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f7f7f7;
        }
        .container {
            width: 400px;
            margin: 40px auto;
            padding: 25px;
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .container h2 {
            text-align: center;
        }
        input[type="text"], input[type="email"], input[type="password"], textarea {
            width: 100%;
            padding: 10px;
            margin-top: 8px;
            margin-bottom: 16px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        button {
            width: 100%;
            padding: 10px;
            background-color: #0056b3;
            color: white;
            border: none;
            border-radius: 5px;
            font-weight: bold;
        }
        button:hover {
            background-color: #003f7f;
        }
        .error {
            color: red;
            font-size: 0.9em;
            margin-bottom: 10px;
        }
    </style>

    <script>
        function validateForm() {
            const form = document.forms["registerForm"];
            const email = form["email"].value.trim();
            const password = form["password_hash"].value.trim();
            const address = form["address"].value.trim();
            const errorDiv = document.getElementById("error");

            errorDiv.innerHTML = "";

            const emailRegex = /^[^@]+@[^@]+\.[^@]+$/;

            if (!emailRegex.test(email)) {
                errorDiv.innerHTML = "Invalid email address.";
                return false;
            }

            if (password.length < 6) {
                errorDiv.innerHTML = "Password must be at least 6 characters.";
                return false;
            }

            if (address === "") {
                errorDiv.innerHTML = "Address is required.";
                return false;
            }

            return true;
        }
    </script>
</head>
<body>
    <% if (request.getAttribute("error") != null) { %>
    <div style="background-color: #f8d7da; color: #721c24; padding: 10px; border-radius: 5px;">
        <%= request.getAttribute("error") %>
    </div>
<% } %>

    <div class="container">
        <h2>Customer Registration</h2>

        <div id="error" class="error"></div>
        <% String error = (String) request.getAttribute("error"); %>
<% if (error != null) { %>
    <div class="error"><%= error %></div>
<% } %>

        <form name="registerForm" method="post" action="RegisterServlet" onsubmit="return validateForm();">
            <input type="hidden" name="userType" value="customer" />

            <label for="username">Username:</label>
            <input type="text" name="username" required />

            <label for="first_name">First Name:</label>
            <input type="text" name="firstName" required />

            <label for="last_name">Last Name:</label>
            <input type="text" name="lastName" required />

            <label for="email">Email:</label>
            <input type="email" name="email" required />

            <label for="contactNumber">Contact Number:</label>
            <input type="text" name="contact_number" required />

            <label for="address">Address:</label>
            <textarea name="address" rows="3" required></textarea>

            <label for="password">Password:</label>
            <input type="password" name="password_hash" required />

            <button type="submit">Register</button>
        </form>
    </div>
</body>
</html>
