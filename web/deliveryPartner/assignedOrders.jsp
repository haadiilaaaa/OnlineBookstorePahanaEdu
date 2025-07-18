<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, dto.OrderDTO, dto.OrderItemDTO" %>
<%@ page session="true" %>
<%@page import="dto.UserSession" %>

<%
    List<OrderDTO> orders = (List<OrderDTO>) request.getAttribute("orders");
    


%>

<%
    dto.UserSession userSession = (dto.UserSession) session.getAttribute("user");
    String partnerId = userSession.getId();
    out.println("Partner ID from session: " + partnerId);
%>


<html>
<head>
    
    
    
      

    <title>Assigned Orders</title>
</head>
<body>
<h2>Orders Assigned to You</h2>

<% if (orders == null || orders.isEmpty()) { %>
    <p>No orders assigned yet.</p>
<% } else { 
    for (OrderDTO order : orders) {
%>
    <div style="border:1px solid gray;padding:10px;margin:10px;">
        <p><strong>Order ID:</strong> <%= order.getOrderId() %></p>
        <p><strong>Customer:</strong> <%= order.getCustomerName() %></p>
        <p><strong>Address:</strong> <%= order.getShippingAddress() %></p>
        <p><strong>Total:</strong> Rs. <%= order.getTotalAmount() %></p>
        <p><strong>Status:</strong> <%= order.getStatus() %></p>

        <form action="DeliveryMarkDeliveredServlet" method="post">
            <input type="hidden" name="orderId" value="<%= order.getOrderId() %>" />
            <button type="submit" <%= order.getStatus().equals("DELIVERED") ? "disabled" : "" %>>Mark as Delivered</button>
        </form>
        
        <form action="${pageContext.request.contextPath}/delivery/cancel-order" method="post" style="display:inline;">
    <input type="hidden" name="orderId" value="${order.orderId}" />
    <c:if test="${order.status != 'DELIVERED' && order.status != 'CANCELLED'}">
        <button type="submit" style="background-color:red; color:white;">Cancel Order</button>
    </c:if>
</form>

        
     

    </div>
<% }} %>
</body>
</html>
