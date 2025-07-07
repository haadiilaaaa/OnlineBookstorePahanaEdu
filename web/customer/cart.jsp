<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.CartItem" %>
<%@ page import="java.math.BigDecimal" %>

<%
    Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
    BigDecimal total = BigDecimal.ZERO;
    String contextPath = request.getContextPath();
%>

<html>
<head>
    <title>Your Cart</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        table {
            width: 90%;
            border-collapse: collapse;
            margin: 20px auto;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ccc;
            text-align: center;
        }
        img {
            height: 80px;
            max-width: 100px;
        }
        .actions button {
            margin: 0 5px;
        }
        .summary {
            text-align: right;
            margin-right: 10%;
            font-size: 18px;
        }
        .buttons {
            text-align: center;
            margin-top: 20px;
        }
        a.button {
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin: 0 10px;
            display: inline-block;
        }
        a.button.secondary {
            background-color: #2196F3;
        }
        .price-old {
            text-decoration: line-through;
            color: #888;
            font-size: 0.9em;
        }
        .price-new {
            color: green;
            font-weight: bold;
            font-size: 1em;
        }
        .savings {
            color: #d32f2f;
            font-size: 0.9em;
        }
    </style>
</head>
<body>

<h1 style="text-align: center;">Your Shopping Cart</h1>

<% if (cart == null || cart.isEmpty()) { %>
    <p style="text-align: center;">Your cart is empty.</p>
<% } else { %>
    <table>
        <thead>
            <tr>
                <th>Image</th>
                <th>Title</th>
                <th>Price (Rs.)</th>
                <th>Quantity</th>
                <th>Subtotal (Rs.)</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <% for (CartItem item : cart.values()) {
               BigDecimal originalPrice = item.getOriginalPrice() != null ? item.getOriginalPrice() : item.getPrice();
               BigDecimal price = item.getPrice();
               BigDecimal subtotal = price.multiply(new BigDecimal(item.getQuantity()));
               total = total.add(subtotal);
               BigDecimal savings = BigDecimal.ZERO;
               if (originalPrice.compareTo(price) > 0) {
                   savings = originalPrice.subtract(price).multiply(new BigDecimal(item.getQuantity()));
               }
               String imageUrl = item.getImageUrl();
               if (imageUrl == null || imageUrl.trim().isEmpty()) {
                   imageUrl = "uploads/default-book.png";
               }
        %>
            <tr>
                <td><img src="<%= contextPath + "/" + imageUrl %>" alt="<%= item.getItemTitle() %>" /></td>
                <td><%= item.getItemTitle() %></td>
                <td>
                    <% if (originalPrice.compareTo(price) > 0) { %>
                        <div class="price-old">Rs. <%= String.format("%.2f", originalPrice) %></div>
                        <div class="price-new">Rs. <%= String.format("%.2f", price) %></div>
                    <% } else { %>
                        Rs. <%= String.format("%.2f", price) %>
                    <% } %>
                </td>
                <td>
                    <form method="post" action="<%= contextPath %>/UpdatecartServlet">
                        <input type="hidden" name="itemId" value="<%= item.getItemId() %>" />
                        <input type="number" name="quantity" value="<%= item.getQuantity() %>" min="1"/>
                        <button type="submit">Update</button>
                    </form>
                </td>
                <td>
                    Rs. <%= String.format("%.2f", subtotal) %><br/>
                    <% if (savings.compareTo(BigDecimal.ZERO) > 0) { %>
                        <div class="savings">You saved Rs. <%= String.format("%.2f", savings) %>!</div>
                    <% } %>
                </td>
                <td class="actions">
                    <form method="post" action="<%= contextPath %>/RemoveCartItemServlet" onsubmit="return confirm('Are you sure?');">
                        <input type="hidden" name="itemId" value="<%= item.getItemId() %>" />
                        <button type="submit">Remove</button>
                    </form>
                </td>
            </tr>
        <% } %>
        </tbody>
        <tfoot>
            <tr>
                <td colspan="4" class="summary"><strong>Total:</strong></td>
                <td colspan="2"><strong>Rs. <%= String.format("%.2f", total) %></strong></td>
            </tr>
        </tfoot>
    </table>

    <div class="buttons">
        <a href="checkout.jsp" class="button">Proceed to Checkout</a>
        <a href="BookBrowseServlet" class="button secondary">Continue Shopping</a>
    </div>
<% } %>

</body>
</html>
