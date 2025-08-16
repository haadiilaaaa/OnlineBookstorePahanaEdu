<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Delivery Partner Profile</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
          crossorigin="anonymous" referrerpolicy="no-referrer" />

    <style>
        :root {
            --gradient-start: #6d4c41;
            --gradient-end: #8d6e63;
            --light-bg: #f3ede5;
            --card-bg: #fff9f4;
            --text-color: #3b2e2a;
            --accent: #5b3e30;
            --radius: 12px;
            --font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            --shadow: 0 8px 20px rgba(141, 110, 99, 0.15);
        }

        body {
            background-color: var(--light-bg);
            font-family: var(--font-family);
            color: var(--text-color);
            margin: 0;
            padding: 20px;
        }

        .profile-container {
            max-width: 600px;
            margin: 40px auto;
            background-color: var(--card-bg);
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            padding: 30px;
        }

        h2 {
            text-align: center;
            color: var(--gradient-start);
            margin-bottom: 25px;
        }

        .earnings-box {
            background-color: #ebdbcf;
            padding: 12px 18px;
            border-left: 5px solid var(--gradient-start);
            margin-bottom: 20px;
            font-size: 16px;
            border-radius: 4px;
        }

        .form-group {
            margin-bottom: 18px;
        }

        label {
            display: block;
            margin-bottom: 6px;
            font-weight: 600;
            color: var(--accent);
        }

        input[type="text"],
        input[type="email"] {
            width: 100%;
            padding: 10px 12px;
            font-size: 15px;
            border: 1px solid #ccbfb6;
            border-radius: 6px;
            background-color: #fdfaf9;
            transition: border 0.3s ease;
        }

        input:focus {
            outline: none;
            border-color: var(--gradient-start);
            box-shadow: 0 0 5px rgba(109, 76, 65, 0.3);
        }

        .submit-button {
            background: linear-gradient(to right, var(--gradient-start), var(--gradient-end));
            color: white;
            font-size: 16px;
            padding: 12px 18px;
            border: none;
            border-radius: 25px;
            cursor: pointer;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            margin-top: 10px;
            transition: background 0.3s ease, transform 0.2s ease;
        }

        .submit-button:hover {
            background: linear-gradient(to right, #5b3e30, #7c5d47);
            transform: translateY(-2px);
        }

        .error-msg {
            color: red;
            font-weight: bold;
            margin-top: 15px;
            text-align: center;
        }

        @media (max-width: 600px) {
            .profile-container {
                margin: 20px;
                padding: 20px;
            }
        }
    </style>
</head>
<body>
    
    <% String successMessage = (String) request.getAttribute("successMessage"); %>
<% if (successMessage != null) { %>
    <div style="text-align:center; background:#e6ffee; border:1px solid #2e7d32; 
                color:#2e7d32; padding:10px; border-radius:8px; margin-bottom:20px; 
                font-weight:bold;">
        <%= successMessage %>
    </div>
<% } %>


<div class="profile-container">
    <h2>Delivery Partner Profile</h2>

    <div class="earnings-box">
        <p><strong>Total Earnings:</strong> Rs. ${earnings}</p>
    </div>

    <form method="post" action="DeliveryPartnerProfileServlet">
        <div class="form-group">
            <label>First Name:</label>
            <input type="text" name="firstName" value="${profile.firstName}" required />
        </div>

        <div class="form-group">
            <label>Last Name:</label>
            <input type="text" name="lastName" value="${profile.lastName}" required />
        </div>

        <div class="form-group">
            <label>Email:</label>
            <input type="email" name="email" value="${profile.email}" required />
        </div>

        <div class="form-group">
            <label>Contact Number:</label>
            <input type="text" name="contactNumber" value="${profile.contactNumber}" required />
        </div>

        <div class="form-group">
            <label>Vehicle Number:</label>
            <input type="text" name="vehicleNumber" value="${profile.vehicleNumber}" required />
        </div>

        <button type="submit" class="submit-button">
            <i class="fa-solid fa-floppy-disk"></i> Update Profile
        </button>
    </form>

    <c:if test="${not empty error}">
        <p class="error-msg">${error}</p>
    </c:if>
</div>

</body>
</html>
