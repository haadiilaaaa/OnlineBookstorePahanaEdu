<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, dto.OrderDTO, dto.OrderItemDTO" %>
<%@ page session="true" %>

<%
    List<OrderDTO> orders = (List<OrderDTO>) request.getAttribute("orders");
%>



<html>
<head>
    <title>Admin Order Management</title>
    <style>
    :root {
        --gradient-start: #6d4c41;
        --gradient-end: #8d6e63;
        --light-bg: #f3ede5;
        --card-bg: #fff9f4;
        --text-color: #3b2e2a;
        --font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        --radius: 12px;
        --shadow: 0 8px 18px rgba(109, 76, 65, 0.2);
        --transition: 0.3s ease;
        --success: #2ecc71;
        --danger: #e74c3c;
        --warning: #ffc107;
        --disabled: #c0b9b9;
    }

    body {
        font-family: var(--font-family);
        background-color: var(--light-bg);
        color: var(--text-color);
        margin: 0;
        padding: 40px 60px;
    }

    h1 {
        font-size: 2rem;
        color: var(--gradient-start);
        text-align: center;
        margin-bottom: 40px;
        text-shadow: 0 1px 3px rgba(109, 76, 65, 0.4);
    }

    .order {
        background: var(--card-bg);
        border-radius: var(--radius);
        padding: 24px 30px;
        margin-bottom: 30px;
        box-shadow: var(--shadow);
        transition: var(--transition);
    }

    .order:hover {
        transform: translateY(-4px);
        box-shadow: 0 12px 28px rgba(109, 76, 65, 0.2);
    }

    .order p {
        margin: 6px 0;
        font-size: 0.96rem;
    }

    .order strong {
        color: var(--gradient-start);
    }

    .item-row {
        margin-left: 20px;
        position: relative;
        padding-left: 14px;
    }

    .item-row::before {
        content: "•";
        position: absolute;
        left: 0;
        color: var(--gradient-end);
        font-weight: bold;
    }

    .status-badge {
        display: inline-block;
        padding: 4px 12px;
        font-size: 0.75rem;
        font-weight: bold;
        border-radius: 999px;
        margin-top: 6px;
        background-color: var(--warning);
        color: #000;
    }

    .delivered  { background-color: var(--success); color: white; }
    .cancelled  { background-color: var(--danger); }
    .disabled   { background-color: var(--disabled); color: #555; }

    form {
        display: inline-block;
        margin-top: 12px;
    }

    .btn {
        background: linear-gradient(135deg, var(--gradient-start), var(--gradient-end));
        color: #fff;
        font-weight: 600;
        padding: 8px 20px;
        border-radius: 30px;
        border: none;
        font-size: 0.85rem;
        cursor: pointer;
        transition: var(--transition);
        margin-right: 8px;
        box-shadow: 0 4px 12px rgba(109, 76, 65, 0.2);
    }

    .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 18px rgba(109, 76, 65, 0.25);
    }

    .btn-disabled {
        background-color: var(--disabled) !important;
        color: #555 !important;
        cursor: not-allowed;
        box-shadow: none;
    }

    .status-msg {
        font-weight: bold;
        margin-top: 10px;
        font-size: 0.9rem;
    }

    .status-msg.red { color: var(--danger); }
    .status-msg.green { color: var(--success); }

    @media (max-width: 768px) {
        body { padding: 20px; }
        h1 { font-size: 1.5rem; }
        .order { padding: 16px; }
        .btn { display: block; width: 100%; margin-bottom: 10px; }
        form { display: block; margin-right: 0; }
    }
</style>

</head>
<body>
    <h1>Admin Order Management</h1>

    <% if (orders == null || orders.isEmpty()) { %>
        <p>No orders found.</p>
    <% } else {
        for (OrderDTO order : orders) {
    %>
    <div class="order">
        <p><strong>Order ID:</strong> <%= order.getOrderId() %></p>
        <p><strong>Customer:</strong> <%= order.getCustomerName() %> (<%= order.getEmail() %>)</p>
        <p><strong>Placed On:</strong> <%= order.getOrderDate() %></p>
        <p><strong>Total Amount:</strong> Rs. <%= order.getTotalAmount() %></p>
        <p><strong>Payment Method:</strong> <%= order.getPaymentMethod() %></p>
        <p><strong>Status:</strong> <%= order.getStatus() %></p>

        <p><strong>Items:</strong></p>
        <% for (OrderItemDTO item : order.getItems()) { %>
            <div class="item-row">
                • <%= item.getItemTitle() %> - Quantity: <%= item.getQuantity() %>, Price: Rs. <%= item.getPrice() %>
            </div>
        <% } %>

        <br/>

        <% String status = order.getStatus();
           boolean canDeliver = "PENDING".equals(status);
           boolean isCancelledByCustomer = "CANCELLED_BY_CUSTOMER".equals(status);
           boolean isDelivered = "DELIVERED".equals(status);
           boolean isCancelledByAdmin = "CANCELLED_BY_ADMIN".equals(status);
        %>

        <form action="AdminUpdateOrderStatusServlet" method="post" style="display: inline-block;">
            <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">
            <input type="hidden" name="newStatus" value="DELIVERED">
            <button type="submit" class="btn-deliver" <%= (canDeliver ? "" : "disabled class='btn-disabled'") %> 
                <%= (isCancelledByCustomer ? "title='Customer cancelled order'" : "") %>>
                Mark Delivered
            </button>
        </form>

        <form action="AdminUpdateOrderStatusServlet" method="post" style="display: inline-block;">
            <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">
            <input type="hidden" name="newStatus" value="CANCELLED_BY_ADMIN">
            <button type="submit" class="btn-cancel" <%= (canDeliver ? "" : "disabled class='btn-disabled'") %>>
                Mark Cancelled
            </button>
        </form>

        <% if (isCancelledByCustomer) { %>
            <p style="color: red;"><strong>Cancelled by Customer</strong></p>
        <% } else if (isCancelledByAdmin) { %>
            <p style="color: red;"><strong>Cancelled by Admin</strong></p>
        <% } else if (isDelivered) { %>
            <p style="color: green;"><strong>Delivered</strong></p>
        <% } %>
    </div>
    <% }} %>

</body>
</html>
