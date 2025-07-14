<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.CartItem" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="dto.UserSession" %>
<%@ page import="java.util.Map" %>

<%
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

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Checkout | Pahana Edu</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <style>
  :root {
  --primary-dark: #5a3e36;
  --primary-light: #a07f72;
  --primary-gradient-start: #7a5c51;
  --primary-gradient-end: #b5988c;
  --background-gradient: linear-gradient(135deg, #fefefe, #e6d6ca);
  --glass-bg: rgba(255, 255, 255, 0.65);
  --shadow-light: rgba(90, 62, 54, 0.2);
  --shadow-strong: rgba(90, 62, 54, 0.35);
  --text-dark: #3f2f2a;
  --text-muted: #6e5a54;
  --border-radius: 18px;
  --transition-speed: 0.35s;
  --font-primary: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

body {
  font-family: var(--font-primary);
  background: var(--background-gradient);
  margin: 0;
  padding: 50px 25px;
  color: var(--text-dark);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.checkout-wrapper {
  max-width: 1100px;
  margin: auto;
  display: flex;
  gap: 40px;
  flex-wrap: wrap;
  background: var(--glass-bg);
  box-shadow: 0 8px 32px var(--shadow-light);
  border-radius: var(--border-radius);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  padding: 40px 50px;
  transition: box-shadow var(--transition-speed);
}

.checkout-wrapper:hover {
  box-shadow: 0 12px 40px var(--shadow-strong);
}

.left-column, .right-column {
  flex: 1;
  min-width: 320px;
}

h2 {
  font-weight: 700;
  font-size: 1.7rem;
  margin-bottom: 25px;
  color: var(--primary-dark);
  border-bottom: 3px solid var(--primary-gradient-start);
  padding-bottom: 8px;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  user-select: none;
}

label {
  display: block;
  font-weight: 600;
  margin-top: 20px;
  color: var(--text-muted);
  letter-spacing: 0.04em;
  font-size: 0.95rem;
}

input[type="text"],
input[type="password"],
input[type="month"],
textarea,
select {
  width: 100%;
  padding: 14px 18px;
  margin-top: 7px;
  font-size: 1rem;
  color: var(--primary-dark);
  border-radius: 12px;
  border: 2px solid transparent;
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 3px 8px rgba(160, 128, 114, 0.15);
  transition: border-color var(--transition-speed), box-shadow var(--transition-speed);
  font-weight: 500;
  resize: vertical;
  user-select: text;
}

input[type="text"]:focus,
input[type="password"]:focus,
input[type="month"]:focus,
textarea:focus,
select:focus {
  outline: none;
  border-color: var(--primary-gradient-start);
  box-shadow: 0 6px 16px rgba(122, 92, 81, 0.4);
  background: #fff;
}

textarea {
  min-height: 80px;
  font-family: var(--font-primary);
}

input[type="checkbox"] {
  margin-right: 8px;
  accent-color: var(--primary-gradient-start);
  cursor: pointer;
}

.btn {
  margin-top: 30px;
  background: linear-gradient(135deg, var(--primary-gradient-start), var(--primary-gradient-end));
  color: #fff;
  padding: 15px 32px;
  font-size: 1.15rem;
  font-weight: 700;
  border: none;
  border-radius: 50px;
  cursor: pointer;
  box-shadow: 0 6px 15px rgba(122, 92, 81, 0.4);
  transition: background var(--transition-speed), transform 0.2s ease;
  user-select: none;
  display: inline-flex;
  align-items: center;
  gap: 10px;
}

.btn i {
  font-size: 1.25rem;
  line-height: 1;
}

.btn:hover {
  background: linear-gradient(135deg, var(--primary-gradient-end), var(--primary-gradient-start));
  transform: translateY(-3px);
  box-shadow: 0 10px 25px rgba(122, 92, 81, 0.6);
}

.back-home-btns {
  max-width: 1100px;
  margin: 0 auto 35px auto;
  display: flex;
  justify-content: space-between;
  gap: 15px;
  flex-wrap: wrap;
}

.back-home-btns .btn {
  flex: 1 1 120px;
  justify-content: center;
}

.order-summary-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0 12px;
  font-size: 1rem;
  color: var(--primary-dark);
}

.order-summary-table thead tr th {
  font-weight: 700;
  padding-bottom: 12px;
  border-bottom: 3px solid var(--primary-gradient-start);
  user-select: none;
  text-transform: uppercase;
}

.order-summary-table tbody tr {
  background: rgba(255, 255, 255, 0.85);
  border-radius: 14px;
  box-shadow: 0 3px 8px rgba(160, 128, 114, 0.1);
  transition: background 0.25s ease;
}

.order-summary-table tbody tr:hover {
  background: #f2e6dd;
}

.order-summary-table td {
  padding: 15px 18px;
  vertical-align: middle;
  border: none;
  font-weight: 600;
}

.order-summary-table img {
  width: 60px;
  height: 80px;
  object-fit: cover;
  border-radius: 10px;
  box-shadow: 0 3px 10px rgba(122, 92, 81, 0.3);
}

.order-summary-table tfoot tr td {
  font-size: 1.15rem;
  font-weight: 700;
  color: var(--primary-dark);
  padding-top: 18px;
  border-top: 3px solid var(--primary-gradient-start);
  text-align: right;
}

#cardDetails {
  margin-top: 20px;
  padding: 20px;
  background: #fff4ec;
  border-radius: 16px;
  border: 2px solid var(--primary-gradient-start);
  box-shadow: 0 5px 15px rgba(160, 128, 114, 0.2);
  transition: opacity 0.4s ease, max-height 0.4s ease;
  overflow: hidden;
}

@media (max-width: 900px) {
  .checkout-wrapper {
    flex-direction: column;
    padding: 30px 25px;
  }

  .back-home-btns {
    justify-content: center;
  }

  .btn {
    width: 100%;
    justify-content: center;
  }
}

  </style>
</head>
<div class="back-home-btns">
  <button class="btn" onclick="history.back();"><i class="fas fa-arrow-left"></i> Back</button>
  <a href="customerDashboard.jsp" class="btn"><i class="fas fa-home"></i> Home</a>
</div>

<div class="checkout-wrapper">

  <!-- Left Column -->
  <div class="left-column">
    <form method="post" action="<%= request.getContextPath() %>/customer/PlaceOrderServlet">

      <h2>Shipping Information</h2>
      <label>Full Name:</label>
      <input type="text" name="fullName" value="<%= user.getFirstName() + " " + user.getLastName() %>" readonly>

      <label>Email:</label>
      <input type="text" name="email" value="<%= user.getEmail() %>" readonly>

      <label>Contact Number:</label>
      <input type="text" name="contactNumber" value="<%= user.getContactNumber() %>" readonly>

      <label>Address:</label>
      <textarea name="shippingAddress" rows="3"><%= user.getAddress() %></textarea>

      <label><input type="checkbox" name="changeAddress" value="yes"> Use different shipping address</label>

      <h2>Payment Method</h2>
      <select id="paymentMethod" name="paymentMethod" required>
        <option value="">-- Select Payment Method --</option>
        <option value="Cash on Delivery">Cash on Delivery</option>
        <option value="Credit Card">Credit Card</option>
        <option value="Debit Card">Debit Card</option>
      </select>

      <div id="cardDetails">
        <label>Card Number:</label>
        <input type="text" name="cardNumber" maxlength="16" placeholder="1234 5678 9012 3456">

        <label>Expiry Date:</label>
        <input type="month" name="expiryDate">

        <label>CVV:</label>
        <input type="password" name="cvv" maxlength="4" placeholder="•••">
      </div>

      <button type="submit" class="btn"><i class="fas fa-check-circle"></i> Place Order</button>
    </form>
  </div>

  <!-- Right Column -->
  <div class="right-column">
    <h2>Your Order</h2>
    <table class="order-summary-table">
      <thead>
        <tr>
          <th>Image</th>
          <th>Title</th>
          <th>Price</th>
          <th>Qty</th>
          <th>Subtotal</th>
        </tr>
      </thead>
      <tbody>
        <% for (CartItem item : cart.values()) {
            String imageUrl = (item.getImageUrl() == null || item.getImageUrl().trim().isEmpty()) 
                            ? "images/default-book.png" 
                            : item.getImageUrl();
            String fullPath = request.getContextPath() + "/" + imageUrl;
        %>
        <tr>
          <td><img src="<%= fullPath %>" alt="Book"></td>
          <td><%= item.getItemTitle() %></td>
          <td>Rs. <%= item.getPrice() %></td>
          <td><%= item.getQuantity() %></td>
          <td>Rs. <%= item.getPrice().multiply(new java.math.BigDecimal(item.getQuantity())) %></td>
        </tr>
        <% } %>
      </tbody>
      <tfoot>
        <tr>
          <td colspan="4" style="text-align:right;"><strong>Total:</strong></td>
          <td><strong>Rs. <%= total %></strong></td>
        </tr>
      </tfoot>
    </table>
  </div>
</div>

<script>
  const paymentSelect = document.getElementById('paymentMethod');
  const cardDetails = document.getElementById('cardDetails');

  paymentSelect.addEventListener('change', () => {
    cardDetails.style.display = paymentSelect.value.includes("Card") ? 'block' : 'none';
  });
</script>




</body>
</html>
