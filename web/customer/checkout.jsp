<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.CartItem" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="dto.UserSession" %>

<%
    // Get session data
    UserSession user = (UserSession) session.getAttribute("user");
    Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");

    BigDecimal total = BigDecimal.ZERO;
    if (cart != null && !cart.isEmpty()) {
        for (CartItem item : cart.values()) {
            total = total.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
    }

    if (user == null || cart == null || cart.isEmpty()) {
        response.sendRedirect("customerDashboard.jsp");
        return;
    }
%>

<html>
<head>
    <title>Checkout</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 30px;
        }
        .section {
            margin-bottom: 20px;
        }
        .section h2 {
            border-bottom: 1px solid #ccc;
            padding-bottom: 5px;
        }
        label {
            display: block;
            margin: 8px 0 4px;
        }
        input[type=text], textarea, select {
            width: 100%;
            padding: 8px;
            margin-bottom: 12px;
        }
        .cart-table {
            width: 100%;
            border-collapse: collapse;
        }
        .cart-table th, .cart-table td {
            border: 1px solid #ddd;
            padding: 8px;
        }
        .cart-table th {
            background-color: #f2f2f2;
        }
        .btn {
            background-color: green;
            color: white;
            padding: 10px 20px;
            border: none;
            text-decoration: none;
            cursor: pointer;
            font-size: 16px;
        }
    </style>
</head>
<body>

<form method="post" action="<%= request.getContextPath() %>/customer/PlaceOrderServlet">



    <!-- ✅ Shipping Information -->
    <div class="section">
        <h2>Shipping Information</h2>

        <label>Full Name:</label>
        <input type="text" name="fullName" value="<%= user.getFirstName() + " " + user.getLastName() %>" readonly />

        <label>Email:</label>
        <input type="text" name="email" value="<%= user.getEmail() %>" readonly />

        <label>Contact Number:</label>
        <input type="text" name="contactNumber" value="<%= user.getContactNumber() %>" readonly />

        <label>Address:</label>
        <textarea name="shippingAddress"><%= user.getAddress() %></textarea>

        <label>
            <input type="checkbox" name="changeAddress" value="yes" />
            Use different shipping address
        </label>
    </div>

    <!-- ✅ Cart Summary -->
   <!-- ✅ Cart Summary -->
<div class="section">
    <h2>Order Summary</h2>
    <table class="cart-table">
        <thead>
        <tr>
            <th>Image</th> <!-- 🆕 Add Image column -->
            <th>Title</th>
            <th>Price</th>
            <th>Qty</th>
            <th>Subtotal</th>
        </tr>
        </thead>
       <tbody>
<% for (CartItem item : cart.values()) {
    String imageUrl = item.getImageUrl();
    if (imageUrl == null || imageUrl.trim().isEmpty()) {
        imageUrl = "images/default-book.png";
    }
    String fullPath = request.getContextPath() + "/" + imageUrl;
%>
    <tr>
        <td>
            <img src="<%= fullPath %>" alt="Book Image" style="height: 60px; max-width: 80px;" />
        </td>
        <td><%= item.getItemTitle() %></td>
        <td>Rs. <%= item.getPrice() %></td>
        <td><%= item.getQuantity() %></td>
        <td>Rs. <%= item.getPrice().multiply(new BigDecimal(item.getQuantity())) %></td>
    </tr>
<% } %>
</tbody>

        <tfoot>
        <tr>
            <th colspan="4" style="text-align: right;">Total:</th>
            <th>Rs. <%= total %></th>
        </tr>
        </tfoot>
    </table>
</div>

    <!-- ✅ Payment Method -->
    <div class="section">
        <h2>Payment Method</h2>
        <select id="paymentMethod" name="paymentMethod" required>
  <option value="">-- Select --</option>
  <option value="Cash on Delivery">Cash on Delivery</option>
  <option value="Credit Card">Credit Card</option>
  <option value="Debit Card">Debit Card</option>
</select>

<div id="cardDetails" style="display:none; margin-top:10px;">
  <label>Card Number:</label>
  <input type="text" name="cardNumber" maxlength="16" />
  
  <label>Expiry Date:</label>
  <input type="month" name="expiryDate" />
  
  <label>CVV:</label>
  <input type="password" name="cvv" maxlength="4" />
</div>

<script>
  const paymentSelect = document.getElementById('paymentMethod');
  const cardDetails = document.getElementById('cardDetails');

  paymentSelect.addEventListener('change', () => {
    if (paymentSelect.value === 'Credit Card' || paymentSelect.value === 'Debit Card') {
      cardDetails.style.display = 'block';
    } else {
      cardDetails.style.display = 'none';
    }
  });
</script>

    <!-- ✅ Submit Button -->
    <div class="section">
        <button type="submit" class="btn">Place Order</button>
    </div>

</form>

</body>
</html>
