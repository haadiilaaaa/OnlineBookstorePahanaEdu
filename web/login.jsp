<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Customer Login</title>
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

        .form-group label {
            display: block;
            margin-bottom: 6px;
            color: #5d4037;
            font-weight: 500;
            font-size: 14px;
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
        .recover-link {
    text-align: right;
    margin-top: -10px;
    margin-bottom: 20px;
}

.recover-link a {
    font-size: 13px;
    color: #6d4c41;
    text-decoration: none;
    transition: color 0.3s ease;
}

.recover-link a:hover {
    color: #8d6e63;
    text-decoration: underline;
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

    </style>
</head>
<body>
<% System.out.println("🟡 login.jsp reloaded again!"); %>

<div class="container">
    <h2>Login</h2>

   <%
    String error = (String) request.getAttribute("error");
    int remaining = -1;

    if (error != null && error.contains("Try again in")) {
        try {
            int start = error.indexOf("Try again in") + "Try again in".length();
            int end = error.indexOf("second", start);
            String secondsStr = error.substring(start, end).replaceAll("[^0-9]", "").trim();
            remaining = Integer.parseInt(secondsStr);
        } catch (Exception e) {
            remaining = -1; // fail silently
        }
    }
%>

<% if (remaining > 0) { %>
    <p style="color:red; text-align: center; margin-bottom: 20px;">
        Message A: Too many failed attempts. Please wait <span id="countdown"></span> before trying again.
    </p>

    <script>
        let remaining = <%= remaining %>;

        document.addEventListener('DOMContentLoaded', () => {
            const usernameInput = document.querySelector('input[name="username"]');
            const passwordInput = document.querySelector('input[name="password"]');
            const submitButton = document.querySelector('button[type="submit"]');

            usernameInput.disabled = true;
            passwordInput.disabled = true;
            submitButton.disabled = true;

            function formatTime(seconds) {
                const minutes = Math.floor(seconds / 60);
                const secs = seconds % 60;
                let timeStr = '';
                if (minutes > 0) {
                    timeStr += minutes + " minute" + (minutes > 1 ? "s" : "");
                    if (secs > 0) {
                        timeStr += " and " + secs + " second" + (secs > 1 ? "s" : "");
                    }
                } else {
                    timeStr += secs + " second" + (secs > 1 ? "s" : "");
                }
                return timeStr;
            }

            const countdownElem = document.getElementById('countdown');
            countdownElem.innerText = formatTime(remaining);

            let countdownInterval = setInterval(() => {
                remaining--;
                countdownElem.innerText = formatTime(remaining);

                if (remaining <= 0) {
                    clearInterval(countdownInterval);
                    usernameInput.disabled = false;
                    passwordInput.disabled = false;
                    submitButton.disabled = false;
                    countdownElem.innerText = "";
                }
            }, 1000);
        });
    </script>
<% } else if (error != null) { %>
    <div class="error-message" style="color: red; text-align: center; margin-bottom: 15px;">
        <%= error %>
    </div>
<% } %>
    <%
    String successMessage = (String) session.getAttribute("successMessage");
    if (successMessage != null) {
%>
    <div class="success"><%= successMessage %></div>
<%
        session.removeAttribute("successMessage"); // Remove after showing once
    }
%>


    <form action="LoginServlet" method="post">
        <div class="form-group">
            <label>Username or Email</label>
            <input type="text" name="username" required />
        </div>

        <div class="form-group">
            <label>Password</label>
            <input type="password" name="password" required />
        </div>

        <div class="recover-link">
            <a href="forgotPassword.jsp">Forgot password?</a>
        </div>

        <button type="submit">Login</button>
    </form>

    <div class="login-link" style="text-align: center; margin-top: 20px; font-size: 14px;">
        Don't have an account? <a href="customerRegister.jsp" style="color: #6d4c41; font-weight: 600; text-decoration: none;">Register here</a>
    </div>
</div>

</body>
</html>
