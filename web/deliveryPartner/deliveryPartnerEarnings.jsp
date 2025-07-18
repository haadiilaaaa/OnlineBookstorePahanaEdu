<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>Your Delivery Stats</h2>

<c:choose>
    <c:when test="${not empty earnings}">
        <p><strong>Total Earnings:</strong> Rs. ${earnings}</p>
        <p><strong>Total Deliveries:</strong> ${totalDeliveries}</p>
    </c:when>
    <c:otherwise>
        <p style="color:red">${error}</p>
    </c:otherwise>
</c:choose>

<a href="DeliveryPartnerDashboardServlet">Back to Dashboard</a>
