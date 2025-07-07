<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>OTP Verification</title>
    <style>
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
            max-width: 440px;
            border-left: 6px solid #6d4c41;
            transition: all 0.3s ease-in-out;
        }

        h2 {
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

        form {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }

        label {
            font-size: 14px;
            color: #5d4037;
            margin-bottom: 5px;
        }

        input[type="text"] {
            padding: 12px;
            font-size: 14px;
            border: 1px solid #d7ccc8;
            border-radius: 8px;
            background: #fdfaf6;
            outline: none;
            transition: all 0.3s ease;
        }

        input[type="text"]:focus {
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

        .error {
            background-color: #ffe5e0;
            color: #b00020;
            padding: 10px;
            border-radius: 6px;
            font-size: 13px;
            margin-top: 15px;
            text-align: center;
        }

        .success {
            background-color: #e8f5e9;
            color: #2e7d32;
            padding: 10px;
            border-radius: 6px;
            font-size: 13px;
            margin-top: 15px;
            text-align: center;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>OTP Verification</h2>

    <%
        String userId = request.getParameter("userId");
        String userType = request.getParameter("userType");

        if (userId == null || userType == null) {
            userId = (String) request.getAttribute("userId");
            userType = (String) request.getAttribute("userType");
        }
    %>

    <!-- Existing OTP verification form -->
<form action="OtpVerificationServlet" method="post">
    <input type="hidden" name="userId" value="<%= userId %>" />
    <input type="hidden" name="userType" value="<%= userType %>" />
    <label for="otp">Enter OTP:</label>
    <input type="text" name="otp" required />
    <button type="submit">Verify</button>
</form>

<form action="ResendOtpServlet" method="post" style="margin-top: 10px; text-align: center;">
    <input type="hidden" name="userId" value="<%= userId %>" />
    <input type="hidden" name="userType" value="<%= userType %>" />
    <button type="submit" style="background: none; border: none; color: #6d4c41; font-weight: bold; cursor: pointer;">
        Resend OTP
    </button>
</form>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error"><%= request.getAttribute("error") %></div>
    <% } else if (request.getAttribute("success") != null) { %>
        <div class="success"><%= request.getAttribute("success") %></div>
    <% } %>
</div>

</body>
</html>
