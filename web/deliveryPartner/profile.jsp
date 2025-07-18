<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>Delivery Partner Profile</h2>

<p><strong>Total Earnings:</strong> Rs. ${earnings}</p>

<form method="post" action="DeliveryPartnerProfileServlet">
    <label>First Name:</label>
    <input type="text" name="firstName" value="${profile.firstName}" required /><br/>

    <label>Last Name:</label>
    <input type="text" name="lastName" value="${profile.lastName}" required /><br/>

    <label>Email:</label>
    <input type="email" name="email" value="${profile.email}" required /><br/>

    <label>Contact Number:</label>
    <input type="text" name="contactNumber" value="${profile.contactNumber}" required /><br/>

    <label>Vehicle Number:</label>
    <input type="text" name="vehicleNumber" value="${profile.vehicleNumber}" required /><br/>

    <button type="submit">Update Profile</button>
</form>

<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>
