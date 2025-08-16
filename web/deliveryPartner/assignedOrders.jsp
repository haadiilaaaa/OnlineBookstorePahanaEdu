<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, dto.OrderDTO, dto.OrderItemDTO" %>
<%@ page session="true" %>
<%@ page import="dto.UserSession" %>

<%
    List<OrderDTO> orders = (List<OrderDTO>) request.getAttribute("orders");
    dto.UserSession userSession = (dto.UserSession) session.getAttribute("user");
    String partnerId = userSession.getId();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Assigned Orders</title>

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

        h2 {
            text-align: center;
            color: var(--gradient-start);
            margin-bottom: 30px;
        }

        .order-card {
            background-color: var(--card-bg);
            border-radius: var(--radius);
            padding: 20px 24px;
            margin: 20px auto;
            max-width: 700px;
            box-shadow: var(--shadow);
            transition: box-shadow 0.3s ease, transform 0.3s ease;
        }

        .order-card:hover {
            box-shadow: 0 12px 30px rgba(109, 76, 65, 0.3);
            transform: translateY(-4px);
        }

        .order-card p {
            margin: 8px 0;
            font-size: 1rem;
        }

        .order-card strong {
            color: var(--accent);
        }

        form {
            display: inline-block;
            margin-top: 10px;
        }

        button {
            padding: 10px 16px;
            border: none;
            border-radius: 20px;
            font-weight: bold;
            cursor: pointer;
            font-family: inherit;
            transition: background 0.3s ease, transform 0.2s ease;
        }

        button:disabled {
            background-color: #ccc;
            color: #666;
            cursor: not-allowed;
        }

        .deliver-btn {
            background: linear-gradient(135deg, var(--gradient-start), var(--gradient-end));
            color: white;
            margin-right: 10px;
        }

        .deliver-btn:hover {
            background: linear-gradient(135deg, #5b3e30, #7c5d47);
            transform: translateY(-2px);
        }

        .cancel-btn {
            background-color: red;
            color: white;
        }

        .cancel-btn:hover {
            background-color: #b30000;
            transform: translateY(-2px);
        }

        .no-orders {
            text-align: center;
            margin-top: 50px;
            font-size: 1.2rem;
        }

        @media (max-width: 600px) {
            .order-card {
                margin: 16px;
                padding: 18px;
            }

            button {
                width: 100%;
                margin-bottom: 10px;
            }

            form {
                display: block;
                width: 100%;
            }
        }
    </style>
</head>
<body>
    
    

<h2>Orders Assigned to You</h2>

<% if (orders == null || orders.isEmpty()) { %>
    <div class="no-orders">No orders assigned yet.</div>
<% } else {
    for (OrderDTO order : orders) {
%>
    <div class="order-card">
        <p><strong>Order ID:</strong> <%= order.getOrderId() %></p>
        <p><strong>Customer:</strong> <%= order.getCustomerName() %></p>
        <p><strong>Address:</strong> <%= order.getShippingAddress() %></p>
        <p><strong>Total:</strong> Rs. <%= order.getTotalAmount() %></p>
        <p><strong>Status:</strong> <%= order.getStatus() %></p>

        <form action="DeliveryMarkDeliveredServlet" method="post">
            <input type="hidden" name="orderId" value="<%= order.getOrderId() %>" />
            <button type="submit" class="deliver-btn" <%= order.getStatus().equals("DELIVERED") ? "disabled" : "" %>>
                <i class="fa-solid fa-check"></i> Mark as Delivered
            </button>
        </form>

        <%
            String status = order.getStatus();
            if (!status.equals("DELIVERED") && !status.equals("CANCELLED")) {
        %>
        <form action="<%= request.getContextPath() %>/DeliveryCancelOrderServlet" method="post">
            <input type="hidden" name="orderId" value="<%= order.getOrderId() %>" />
            <button type="submit" class="cancel-btn">
                <i class="fa-solid fa-times"></i> Cancel Order
            </button>
        </form>
        <% } %>
    </div>
<% }} %>

</body>
</html>
