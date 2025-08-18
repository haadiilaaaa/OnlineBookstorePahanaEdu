<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Delivery Partner Dashboard</title>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
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
            --shadow: 0 8px 20px rgba(141, 110, 99, 0.2);
        }

        body {
            margin: 0;
            padding: 0;
            background: var(--light-bg);
            font-family: var(--font-family);
            color: var(--text-color);
        }

        header {
            background: var(--card-bg);
            box-shadow: var(--shadow);
            padding: 20px 40px;
            text-align: center;
        }

        header h1 {
            margin: 0;
            font-size: 2rem;
            color: var(--gradient-start);
        }

        main {
            max-width: 1100px;
            margin: 30px auto;
            padding: 0 20px;
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 24px;
            margin-bottom: 40px;
        }

        .card {
            background: var(--card-bg);
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            padding: 24px;
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            gap: 16px;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .card:hover {
            transform: translateY(-6px);
            box-shadow: 0 12px 28px rgba(109, 76, 65, 0.35);
        }

        .card i {
            font-size: 2.2rem;
            color: var(--gradient-start);
        }

        .card h3 {
            margin: 0;
            font-size: 1.2rem;
            color: var(--accent);
        }

        .card p {
            margin: 0;
            font-size: 1.6rem;
            font-weight: bold;
            color: var(--gradient-start);
        }

        .notifications {
            background: var(--card-bg);
            border-radius: var(--radius);
            box-shadow: var(--shadow);
            padding: 24px;
            margin-bottom: 40px;
        }

        .notifications h3 {
            margin-bottom: 16px;
            font-size: 1.2rem;
            color: var(--accent);
        }

        .notifications ul {
            padding-left: 20px;
        }

        .notifications li {
            margin-bottom: 10px;
            font-size: 1rem;
        }

        .link-bar {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 16px;
        }

        .link-bar a {
            background: linear-gradient(135deg, var(--gradient-start), var(--gradient-end));
            color: white;
            font-weight: 600;
            text-decoration: none;
            padding: 14px 28px;
            border-radius: 30px;
            display: flex;
            align-items: center;
            gap: 10px;
            box-shadow: 0 6px 14px rgba(141, 110, 99, 0.3);
            transition: all 0.3s ease;
        }

        .link-bar a:hover {
            background: linear-gradient(135deg, #5b3e30, #7c5d47);
            transform: translateY(-3px);
        }

        @media (max-width: 600px) {
            header h1 {
                font-size: 1.5rem;
            }

            .card p {
                font-size: 1.2rem;
            }

            .link-bar a {
                width: 100%;
                justify-content: center;
            }
        }
    </style>
</head>

<body>

<header>
    <h1>Welcome, ${sessionScope.user.username}!</h1>
</header>

<main>
    <div class="grid">
        <div class="card">
            <i class="fa-solid fa-money-bill-wave"></i>
            <h3>Total Earnings</h3>
            <p>Rs. <c:out value="${earnings}" default="0.00"/></p>
        </div>
        <div class="card">
            <i class="fa-solid fa-check-circle"></i>
            <h3>Deliveries Completed</h3>
            <p><c:out value="${totalDeliveries}" default="0"/></p>
        </div>
        <div class="card">
            <i class="fa-solid fa-truck-loading"></i>
            <h3>Pending Deliveries</h3>
            <p><c:out value="${pendingDeliveries}" default="0"/></p>
        </div>
    </div>

    <div class="notifications">
        <h3>Notifications</h3>
        <c:choose>
            <c:when test="${not empty notifications}">
                <ul>
                    <c:forEach var="note" items="${notifications}">
                        <li><c:out value="${note}"/></li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <p>No new notifications.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="link-bar">
        <a href="${pageContext.request.contextPath}/DeliveryPartnerOrderListServlet">
            <i class="fa-solid fa-list"></i> View Assigned Orders
        </a>
        <a href="${pageContext.request.contextPath}/DeliveryPartnerProfileServlet">
            <i class="fa-solid fa-user"></i> View/Edit Profile
        </a>
        <a href="${pageContext.request.contextPath}/DeliveryPartnerEarningsServlet">
            <i class="fa-solid fa-wallet"></i> View Total Earnings
        </a>
        <a href="${pageContext.request.contextPath}/LogoutServlet">
            <i class="fa-solid fa-sign-out-alt"></i> Logout
        </a>
    </div>
</main>

</body>
</html>
