<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Registration</title>
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
    padding: 30px 15px; /* 🔥 Reduced top/bottom padding */
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

    .container h2 {
        text-align: center;
        background: linear-gradient(to right, #6d4c41, #8d6e63);
        color: #fff;
        padding: 12px;
        border-radius: 8px;
        font-size: 22px;
        margin-bottom: 30px;
        box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        letter-spacing: 0.5px;
    }

    .form-group {
        position: relative;
        margin-bottom: 20px;
    }

    .form-group input,
    .form-group textarea {
        width: 100%;
        padding: 12px;
        font-size: 14px;
        border: 1px solid #d7ccc8;
        border-radius: 8px;
        background: #fdfaf6;
        outline: none;
        transition: all 0.3s ease;
    }

    .form-group input:focus,
    .form-group textarea:focus {
        border-color: #8d6e63;
        background-color: #fff;
        box-shadow: 0 0 6px rgba(141, 110, 99, 0.3);
    }

    .form-group label {
        position: absolute;
        left: 12px;
        top: 12px;
        font-size: 13px;
        color: #8d6e63;
        background: #fffdf8;
        padding: 0 5px;
        pointer-events: none;
        transition: 0.2s ease;
    }

    .form-group input:focus + label,
    .form-group textarea:focus + label,
    .form-group input:not(:placeholder-shown) + label,
    .form-group textarea:not(:placeholder-shown) + label {
        top: -9px;
        left: 10px;
        font-size: 11px;
        color: #5d4037;
        background-color: #fffdf8;
    }

    textarea {
        resize: vertical;
        min-height: 70px;
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
    .login-link {
    text-align: center;
    margin-top: 20px;
    font-size: 14px;
    color: #5d4037;
}

.login-link a {
    color: #6d4c41;
    text-decoration: none;
    font-weight: 600;
    transition: color 0.3s ease;
}

.login-link a:hover {
    color: #8d6e63;
    text-decoration: underline;
}

</style>

    <script>
        function validateForm() {
            const form = document.forms["registerForm"];
            const email = form["email"].value.trim();
            const password = form["password_hash"].value.trim();
            const errorDiv = document.getElementById("error");
            errorDiv.innerHTML = "";

            const emailRegex = /^[^@]+@[^@]+\.[^@]+$/;
            const confirmPassword = form["confirm_password"].value.trim();

            if (!emailRegex.test(email)) {
                errorDiv.innerHTML = "Invalid email address.";
                return false;
            }

            if (password !== confirmPassword) {
                errorDiv.innerHTML = "Passwords do not match.";
                return false;
            }

            if (password.length < 6) {
                errorDiv.innerHTML = "Password must be at least 6 characters.";
                return false;
            }

            return true;
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Admin Registration</h2>

        <div id="error" class="error"></div>
        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) { %>
            <div class="error"><%= error %></div>
        <% } %>

        <form name="registerForm" method="post" action="RegisterServlet" onsubmit="return validateForm();">
            <input type="hidden" name="userType" value="admin" />

            <div class="form-group">
                <input type="text" name="username" required placeholder=" " />
                <label>Username</label>
            </div>

            <div class="form-group">
                <input type="text" name="first_name" required placeholder=" " />
                <label>First Name</label>
            </div>

            <div class="form-group">
                <input type="text" name="last_name" required placeholder=" " />
                <label>Last Name</label>
            </div>

            <div class="form-group">
                <input type="email" name="email" required placeholder=" " />
                <label>Email</label>
            </div>

            <div class="form-group">
                <input type="text" name="contact_number" required placeholder=" " />
                <label>Contact Number</label>
            </div>

            <div class="form-group">
                <input type="password" name="password_hash" required placeholder=" " />
                <label>Password</label>
            </div>

            <div class="form-group">
                <input type="password" name="confirm_password" required placeholder=" " />
                <label>Confirm Password</label>
            </div>

            <button type="submit">Register</button>
        </form>

        <div class="login-link">
            Already have an account?
            <a href="login.jsp">Login here</a>
        </div>
    </div>
</body>
</html>
