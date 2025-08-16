<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="util.contannts.AttributeKeys" %>
<!DOCTYPE html>
<html>
<head>
    <title>Reset Password</title>
    <style>
        /* Same styles as forgotPassword.jsp for consistency */
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: 'Segoe UI', sans-serif;
            background: linear-gradient(145deg, #f3ede5, #eae7e3);
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 30px 15px;
            min-height: 100vh;
        }

        .container {
            background: #fffdf8;
            padding: 40px 30px;
            border-radius: 16px;
            box-shadow: 0 18px 40px rgba(0, 0, 0, 0.08);
            width: 100%;
            max-width: 400px;
            border-left: 6px solid #6d4c41;
            transition: all 0.3s ease-in-out;
        }

        .container h2 {
            text-align: center;
            background: linear-gradient(to right, #6d4c41, #8d6e63);
            color: #fff;
            padding: 12px;
            border-radius: 8px;
            font-size: 22px;
            margin-bottom: 30px;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
            letter-spacing: 0.5px;
        }

        .form-group {
            position: relative;
            margin-bottom: 20px;
        }

        .form-group input {
            width: 100%;
            padding: 12px;
            font-size: 14px;
            border: 1px solid #d7ccc8;
            border-radius: 8px;
            background: #fdfaf6;
            outline: none;
            transition: all 0.3s ease;
        }

        .form-group input:focus {
            border-color: #8d6e63;
            background-color: #fff;
            box-shadow: 0 0 6px rgba(141, 110, 99, 0.3);
        }

        button {
            width: 100%;
            padding: 12px;
            border: none;
            border-radius: 8px;
            background: linear-gradient(to right, #6d4c41, #8d6e63);
            color: #fff;
            font-size: 15px;
            font-weight: 600;
            letter-spacing: 0.4px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        button:hover {
            background: linear-gradient(to right, #5d4037, #795548);
            transform: translateY(-1px);
            box-shadow: 0 6px 16px rgba(93, 64, 55, 0.2);
        }

        .error-message {
            background-color: #e8f5e9;
            color: #2e7d32;
            padding: 10px;
            border-radius: 6px;
            font-size: 14px;
            margin-bottom: 15px;
            text-align: center;
            animation: fadeIn 0.4s ease-in-out;
        }

        .success {
            background-color: #e8f5e9;
            color: #2e7d32;
            padding: 10px;
            border-radius: 6px;
            font-size: 14px;
            margin-bottom: 15px;
            text-align: center;
            animation: fadeIn 0.4s ease-in-out;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-5px); }
            to { opacity: 1; transform: translateY(0); }
        }

        @media (max-width: 480px) {
            .container {
                padding: 25px 20px;
            }

            .container h2 {
                font-size: 20px;
            }
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Reset Password</h2>

    <% if(request.getAttribute(AttributeKeys.ERROR) != null) { %>
    <div class="error-message"><%= request.getAttribute(AttributeKeys.ERROR) %></div>
<% } %>

    <% if(request.getAttribute("success") != null) { %>
        <div class="success"><%= request.getAttribute("success") %></div>
    <% } %>

    <form action="ResetPasswordServlet" method="post">
        <input type="hidden" name="token" value="<%= request.getParameter("token") %>">

        <div class="form-group">
            <input type="password" id="password" name="password" placeholder="New Password" required />
        </div>

        <div class="form-group">
            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm Password" required />
        </div>

        <button type="submit">Reset Password</button>
    </form>
</div>

</body>
</html>
