<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, dto.OrderDTO, dto.OrderItemDTO" %>
<%@ page session="true" %>
<%
    List<OrderDTO> orders = (List<OrderDTO>) request.getAttribute("orders");
%>

<html>
<head>
    <title>My Order History</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f9f9f9; padding: 20px; }
        h2 { color: #333; }
        .order { border: 1px solid #ccc; padding: 15px; background: #fff; margin-bottom: 20px; border-radius: 8px; }
        .items { margin-left: 20px; }
        .item-row { margin-bottom: 5px; }
        .status { font-weight: bold; }
        .cancel-btn {
            background-color: #ff4d4d;
            color: white;
            border: none;
            padding: 6px 12px;
            border-radius: 5px;
            cursor: pointer;
        }
        .cancel-btn[disabled] {
            background-color: #ccc;
            cursor: not-allowed;
        }
    </style>
</head>
<body>
    <h1>My Order History</h1>

    <%
        if (orders == null || orders.isEmpty()) {
    %>
        <p>You haven't placed any orders yet.</p>
    <%
        } else {
            for (OrderDTO order : orders) {
    %>
        <div class="order">
            <p><strong>Order ID:</strong> <%= order.getOrderId() %></p>
            <p><strong>Placed On:</strong> <%= order.getOrderDate() %></p>
            <p><strong>Total Amount:</strong> Rs. <%= order.getTotalAmount() %></p>
            <p><strong>Payment Method:</strong> <%= order.getPaymentMethod() %></p>
            <p><strong>Status:</strong> <%= order.getStatus() %></p>

            <p><strong>Items:</strong></p>
            <%
                for (OrderItemDTO item : order.getItems()) {
            %>
                <div class="item-row">
                    • <%= item.getItemTitle() %> - Quantity: <%= item.getQuantity() %>, Price: Rs. <%= item.getPrice() %>
                </div>
            <%
                }
            %>

            <br/>

           <% if ("PENDING".equals(order.getStatus())) { %>
    <form action="CancelOrderServlet" method="post">
        <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">
        <button type="submit" class="cancel-btn">Cancel Order</button>
    </form>
<% } else if ("CANCELLED_BY_CUSTOMER".equals(order.getStatus())) { %>
    <p style="color: red;"><strong>Cancelled</strong></p>
<% } else if ("DELIVERED".equals(order.getStatus())) { %>
    <p style="color: green;"><strong>Delivered</strong></p>
<% } %>


        </div>
    <%
            }
        }
    %>

</body>
</html>
