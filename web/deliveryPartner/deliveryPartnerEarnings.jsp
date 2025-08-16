<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Delivery Stats</title>

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

        .stats-container {
            max-width: 600px;
            margin: 50px auto;
            background-color: var(--card-bg);
            border-radius: var(--radius);
            padding: 30px;
            box-shadow: var(--shadow);
        }

        h2 {
            text-align: center;
            color: var(--gradient-start);
            margin-bottom: 30px;
        }

        .stat-box {
            background-color: #ebdbcf;
            padding: 15px 20px;
            border-left: 5px solid var(--gradient-start);
            border-radius: 4px;
            margin-bottom: 15px;
            font-size: 16px;
        }

        .error-msg {
            color: red;
            font-weight: bold;
            text-align: center;
            margin-top: 20px;
        }

        a {
            display: inline-block;
            margin-top: 30px;
            padding: 10px 18px;
            background: linear-gradient(to right, var(--gradient-start), var(--gradient-end));
            color: white;
            text-decoration: none;
            border-radius: 25px;
            font-weight: bold;
            transition: background 0.3s ease, transform 0.2s ease;
        }

        a:hover {
            background: linear-gradient(to right, #5b3e30, #7c5d47);
            transform: translateY(-2px);
        }

        @media (max-width: 600px) {
            .stats-container {
                margin: 20px;
                padding: 20px;
            }
        }
    </style>
</head>
<body>

<div class="stats-container">
    <h2>Your Delivery Stats</h2>

    <c:choose>
        <c:when test="${not empty earnings}">
            <div class="stat-box">
                <p><strong>Total Earnings:</strong> Rs. ${earnings}</p>
            </div>
            <div class="stat-box">
                <p><strong>Total Deliveries:</strong> ${totalDeliveries}</p>
            </div>
        </c:when>
        <c:otherwise>
            <p class="error-msg">${error}</p>
        </c:otherwise>
    </c:choose>

    <a href="DeliveryPartnerDashboardServlet"><i class="fa-solid fa-arrow-left"></i> Back to Dashboard</a>
</div>

</body>
</html>
