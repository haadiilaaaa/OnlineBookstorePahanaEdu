<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Delivery Partner Dashboard</title>
    <style>
        .stats-box {
            background-color: #f0f0f0;
            border-radius: 12px;
            padding: 15px;
            margin-bottom: 15px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .section-title {
            font-weight: bold;
            margin-top: 10px;
        }
        .link-bar a {
            margin-right: 15px;
        }
    </style>
</head>
<body>

<h2>Welcome, ${sessionScope.user.username}!</h2>

<div class="stats-box">
    <div class="section-title">Quick Stats</div>
    <p>Total Earnings: Rs. <strong><c:out value="${earnings}" default="0.00"/></strong></p>
    <p>Total Deliveries Completed: <strong><c:out value="${totalDeliveries}" default="0"/></strong></p>
    <p>Pending Deliveries: <strong><c:out value="${pendingDeliveries}" default="0"/></strong></p>
</div>

<div class="stats-box">
    <div class="section-title">Notifications</div>
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
    <a href="${pageContext.request.contextPath}/DeliveryPartnerOrderListServlet">View Assigned Orders</a>
    <a href="${pageContext.request.contextPath}/DeliveryPartnerProfileServlet">View/Edit Profile</a>
    <a href="${pageContext.request.contextPath}/DeliveryPartnerEarningsServlet">View Total Earnings</a>
    <a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a>
</div>

</body>
</html>
