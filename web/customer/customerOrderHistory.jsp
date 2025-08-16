

<%@ page import="model.CartItem" %>
<%@ page import="java.util.*" %>
<%@ page import="dto.OrderDTO, dto.OrderItemDTO" %>

<%
    List<OrderDTO> orders = (List<OrderDTO>) request.getAttribute("orders");

    Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
    int cartCount = 0;
    java.math.BigDecimal cartTotal = java.math.BigDecimal.ZERO;
    if (cart != null) {
        for (CartItem it : cart.values()) {
            cartCount += it.getQuantity();
            cartTotal = cartTotal.add(it.getPrice().multiply(new java.math.BigDecimal(it.getQuantity())));
        }
    }
    String contextPath = request.getContextPath();
%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>My Order History | Pahana Edu</title>
    <!-- Include your CSS styles here or link your CSS file -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" crossorigin="anonymous" />
    <style>
       :root {
  --brown-dark: #6d4c41;
  --brown-light: #8d6e63;
  --bg-light: #f3ede5;
  --bg-dark: #2c2c2c;
  --text-light: #5d4037;
  --text-dark: #d7ccc8;
  --card-bg-light: #fffdf8;
  --card-bg-dark: #3a3a3a;
  --shadow-light: rgba(109, 76, 65, 0.15);
  --shadow-dark: rgba(0, 0, 0, 0.6);
  --border-radius: 12px;
  --transition-speed: 0.35s;
  --font-sans: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  --btn-gradient-start: #6d4c41;
  --btn-gradient-end: #a1887f;
}

body, .navbar, .card, .links-bar, .search-container, .account-cart, .category-select, .search-box, .search-button, .theme-toggle-btn {
  transition: background-color var(--transition-speed), color var(--transition-speed), box-shadow 0.4s ease, transform 0.3s ease;
}

body {
  font-family: var(--font-sans);
  background: linear-gradient(135deg, #f3ede5 0%, #e8ded6 100%);
  color: var(--text-light);
}

body.dark-theme {
  background: linear-gradient(135deg, #2c2c2c 0%, #3a3a3a 100%);
  color: var(--text-dark);
}

.top-banner {
  background: linear-gradient(90deg, var(--brown-dark), var(--brown-light));
  color: var(--card-bg-light);
  text-align: center;
  padding: 10px 0;
  font-size: 14px;
  font-weight: 700;
  letter-spacing: 1.1px;
  box-shadow: 0 3px 10px var(--shadow-light);
  user-select: none;
  text-transform: uppercase;
}

.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 15px 25px;
  background-color: var(--card-bg-light);
  box-shadow: 0 5px 12px var(--shadow-light);
  position: sticky;
  top: 0;
  z-index: 1000;
}

body.dark-theme .navbar {
  background-color: var(--card-bg-dark);
  box-shadow: 0 5px 12px var(--shadow-dark);
}

.navbar .logo {
  font-weight: 900;
  font-size: 1.8rem;
  color: var(--text-light);
  display: flex;
  align-items: center;
  gap: 8px;
  letter-spacing: 2px;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.2);
}

body.dark-theme .navbar .logo {
  color: var(--text-dark);
  text-shadow: 1px 1px 4px rgba(255, 255, 255, 0.15);
}

.navbar .logo i {
  color: var(--brown-dark);
  font-size: 30px;
}

.search-container {
  display: flex;
  align-items: center;
  flex-grow: 1;
  margin: 0 30px;
  max-width: 700px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 3px 8px var(--shadow-light);
  overflow: hidden;
  height: 42px;
}

body.dark-theme .search-container {
  background-color: #484848;
  box-shadow: 0 3px 8px rgba(255, 255, 255, 0.1);
}

.category-select {
  width: 160px;
  border: none;
  background: var(--brown-light);
  color: var(--card-bg-light);
  font-weight: 600;
  padding: 0 15px;
  cursor: pointer;
  border-radius: 8px 0 0 8px;
}

.category-select:hover {
  background: var(--brown-dark);
}

.search-box {
  flex-grow: 1;
  border: none;
  padding: 0 15px;
  font-size: 16px;
  color: var(--brown-dark);
  border-left: 2px solid var(--brown-light);
}

.search-box::placeholder {
  color: #a1887f;
}

.search-button {
  width: 60px;
  border: none;
  background: linear-gradient(45deg, var(--btn-gradient-start), var(--btn-gradient-end));
  color: var(--card-bg-light);
  font-weight: 700;
  cursor: pointer;
  border-radius: 0 8px 8px 0;
  font-size: 18px;
}

.search-button:hover {
  background: linear-gradient(45deg, var(--btn-gradient-end), var(--btn-gradient-start));
}

.account-cart {
  display: flex;
  align-items: center;
  gap: 20px;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-light);
}

.account-cart .price {
  font-size: 1.3rem;
  font-weight: 700;
  color: var(--brown-dark);
}

body.dark-theme .account-cart .price {
  color: var(--brown-light);
}

.account-cart .cart {
  font-size: 28px;
  cursor: pointer;
  color: var(--brown-dark);
}

.account-cart .cart:hover {
  transform: scale(1.15);
}

.links-bar {
  background-color: var(--bg-light);
  padding: 12px 20px;
  text-align: center;
  color: var(--text-light);
}

.links-bar a {
  color: var(--brown-dark);
  text-decoration: none;
  margin: 0 12px;
  font-weight: 700;
}

.dropdown {
  display: inline-block;
  position: relative;
}

.dropdown-content {
  display: none;
  position: absolute;
  top: 100%;
  left: 0;
  background-color: var(--card-bg-light);
  min-width: 250px;
  box-shadow: 0 4px 10px var(--shadow-light);
  z-index: 1000;
  padding: 10px;
  border-radius: 10px;
  text-align: left;
}

body.dark-theme .dropdown-content {
  background-color: var(--card-bg-dark);
  box-shadow: 0 4px 10px var(--shadow-dark);
  color: var(--text-dark);
}

.dropdown:hover .dropdown-content {
  display: block;
}

.dropdown-content li {
  padding: 6px 10px;
}

.dropdown-content li:hover {
  background-color: var(--bg-light);
}

body.dark-theme .dropdown-content li:hover {
  background-color: #444;
}

.profile-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 700;
  font-size: 16px;
  color: var(--brown-dark);
}

.profile-link {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: var(--brown-dark);
  font-size: 22px;
  margin-right: 20px;
  text-decoration: none;
  padding: 8px;
  border: 2px solid var(--brown-dark);
  border-radius: 50%;
  transition: background-color 0.3s, color 0.3s;
}

.profile-link:hover {
  background-color: var(--brown-dark);
  color: var(--card-bg-light);
}

body.dark-theme .profile-link {
  color: var(--brown-light);
  border-color: var(--brown-light);
}

body.dark-theme .profile-link:hover {
  background-color: var(--brown-light);
  color: var(--card-bg-dark);
}

.profile-text {
  text-decoration: none;
  color: inherit;
}

.profile-text:hover {
  text-decoration: underline;
}
.cart-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  background-color: var(--card-bg-light);
  min-width: 220px;
  box-shadow: 0 4px 12px var(--shadow-light);
  padding: 15px;
  border-radius: 12px;
  z-index: 1000;
  display: none;
  text-align: center;
}

body.dark-theme .cart-dropdown {
  background-color: var(--card-bg-dark);
  box-shadow: 0 4px 12px var(--shadow-dark);
  color: var(--text-dark);
}

.account-cart .cart:hover + .cart-dropdown,
.cart-dropdown:hover {
  display: block;
}

.cart-dropdown button {
  display: block;
  width: 100%;
  margin: 8px 0;
  padding: 10px 0;
  background: linear-gradient(45deg, var(--btn-gradient-start), var(--btn-gradient-end));
  color: var(--card-bg-light);
  border: none;
  border-radius: var(--border-radius);
  font-weight: bold;
  cursor: pointer;
  transition: background 0.3s ease, transform 0.2s;
}

.cart-dropdown button:hover {
  background: linear-gradient(45deg, var(--btn-gradient-end), var(--btn-gradient-start));
  transform: translateY(-1px);
}

.cart-dropdown .empty-message {
  color: var(--text-light);
  font-size: 14px;
  padding: 8px 0;
}

body.dark-theme .cart-dropdown .empty-message {
  color: var(--text-dark);
}
/* Add this to your existing CSS in the Book Browse page */
.btn {
  padding: 0.85rem 2rem;
  border-radius: 50px;
  background: linear-gradient(135deg, var(--btn-gradient-end), var(--btn-gradient-start));
  color: var(--card-bg-light);
  font-weight: 800;
  font-size: 1.15rem;
  text-decoration: none;
  cursor: pointer;
  border: none;
  display: inline-block;
  text-align: center;
  transition: background 0.3s ease, transform 0.2s;
}

.btn:hover {
  background: linear-gradient(135deg, var(--btn-gradient-start), var(--btn-gradient-end));
  transform: translateY(-1px);
}
.theme-toggle-btn {
  background: linear-gradient(135deg, var(--light-blue-2), var(--medium-blue));
  color: var(--font-color);
  border: none;
  font-size: 18px;
  padding: 10px;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
}

.theme-toggle-btn:hover {
  background: linear-gradient(135deg, var(--medium-blue), var(--darker-blue));
  color: white;
  transform: rotate(15deg);
}
.cart-thumb {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 6px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.12);
}
main {
  max-width: 960px;
  margin: 40px auto;
  padding: 30px;
  background-color: var(--card-bg-light);
  border-radius: 20px;
  box-shadow: 0 10px 30px var(--shadow-light);
  font-size: 16px;
}

body.dark-theme main {
  background-color: var(--card-bg-dark);
  box-shadow: 0 10px 30px var(--shadow-dark);
}

main h1 {
  text-align: center;
  font-size: 2.2rem;
  margin-bottom: 40px;
  color: var(--brown-dark);
  text-transform: uppercase;
  letter-spacing: 1.2px;
  position: relative;
}

main h1::after {
  content: '';
  display: block;
  width: 80px;
  height: 4px;
  background: linear-gradient(to right, var(--brown-dark), var(--brown-light));
  margin: 10px auto 0;
  border-radius: 2px;
}

body.dark-theme main h1 {
  color: var(--brown-light);
}

.order {
  background: linear-gradient(145deg, #fffdf8, #f3ede5);
  border-left: 6px solid var(--brown-dark);
  border-radius: 18px;
  padding: 20px 25px;
  margin-bottom: 30px;
  box-shadow: 0 6px 20px var(--shadow-light);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  position: relative;
}

body.dark-theme .order {
  background: linear-gradient(145deg, #3a3a3a, #2c2c2c);
  border-left-color: var(--brown-light);
  box-shadow: 0 6px 20px var(--shadow-dark);
}

.order:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 28px var(--shadow-light);
}

body.dark-theme .order:hover {
  box-shadow: 0 12px 28px var(--shadow-dark);
}

.order p {
  margin: 10px 0;
  line-height: 1.6;
  color: var(--text-light);
  font-weight: 500;
}

body.dark-theme .order p {
  color: var(--text-dark);
}

.status-badge {
  display: inline-block;
  padding: 6px 14px;
  border-radius: 50px;
  font-weight: bold;
  font-size: 0.85rem;
  text-transform: uppercase;
  margin-top: 5px;
}

.status-pending {
  background: #fff3cd;
  color: #856404;
  border: 1px solid #ffeeba;
}

.status-cancelled {
  background: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}

.status-delivered {
  background: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.item-row {
  margin-left: 20px;
  padding: 6px 0;
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 15px;
  color: var(--text-light);
}

body.dark-theme .item-row {
  color: var(--text-dark);
}

.item-row::before {
  content: "?";
  display: inline-block;
  font-size: 18px;
}

.cancel-btn {
  background: linear-gradient(to right, #d32f2f, #f44336);
  color: white;
  border: none;
  padding: 10px 24px;
  border-radius: 50px;
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  margin-top: 20px;
  transition: transform 0.3s ease, box-shadow 0.3s ease, background 0.3s ease;
}

.cancel-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 14px rgba(211, 47, 47, 0.4);
  background: linear-gradient(to right, #c62828, #e53935);
}

    </style>
</head>
<body>

<!-- BEGIN HEADER / NAVBAR copied from your previous Browse Books page -->
<div class="top-banner">
  Free shipping on orders above Rs. 6,500! Delivery in 2?5 working days in Sri Lanka.
  <button id="themeToggle" class="theme-toggle-btn" title="Toggle theme">
    <i class="fas fa-moon"></i>
  </button>
</div>

<div class="navbar">
  <div class="logo"><i class="fas fa-book-open"></i> Pahana Edu</div>

  <form class="search-container" method="get" action="BookBrowseServlet">
    <select name="categoryId" class="category-select">
      <option value="">-- All Categories --</option>
      <c:forEach var="category" items="${categories}">
        <option value="${category.id}" <c:if test="${category.id == selectedCategory}">selected</c:if>>${category.name}</option>
      </c:forEach>
    </select>
    <input type="text" class="search-box" name="keyword" placeholder="Search books by title/author" />

    <input type="number" step="0.01" name="minPrice" class="search-box" placeholder="Min Price" style="max-width:100px;" />
    <input type="number" step="0.01" name="maxPrice" class="search-box" placeholder="Max Price" style="max-width:100px;" />

    <button type="submit" class="search-button"><i class="fas fa-search"></i></button>
  </form>

  <nav class="nav-links" style="display: flex; font-weight: 700; align-items: center; font-size: 16px; color: var(--brown-dark); gap: 12px;">

    <!-- Profile Dropdown -->
    <div class="profile-wrapper dropdown" style="display:flex; align-items:center; gap:6px;">
      <a href="editCustomerProfile.jsp" class="profile-text">Profile</a>
      <a href="editCustomerProfile.jsp" class="profile-link" title="My Profile">
        <i class="fas fa-user"></i>
      </a>
      <div class="dropdown-content">
        <ul style="list-style:none; padding:0; margin:0;">
          <li><a href="orders.jsp" style="color:inherit; text-decoration:none;">Orders</a></li>
          <li><a href="editCustomerProfile.jsp" style="color:inherit; text-decoration:none;">Account Details</a></li>
          <li><a href="addresses.jsp" style="color:inherit; text-decoration:none;">Addresses</a></li>
          <li><a href="logout" style="color:inherit; text-decoration:none;">Logout</a></li>
        </ul>
      </div>
    </div>

    <!-- Cart Dropdown -->
    <div class="account-cart dropdown" style="display:flex; align-items:center; gap:8px;">
      <span class="price">Rs. <%= cartTotal != null ? cartTotal.toPlainString() : "0.00" %></span>

      <span class="cart" style="cursor:pointer;">
        <i class="fas fa-shopping-cart"></i> (<%= cartCount %>)
      </span>

      <div class="dropdown-content cart-dropdown">
        <% if (cart != null && !cart.isEmpty()) { %>
          <ul style="list-style:none; padding:0; margin:0; max-height: 300px; overflow-y: auto;">
            <% for (CartItem item : cart.values()) { %>

              <li style="display:flex; align-items:center; gap:10px; padding: 8px 5px; border-bottom: 1px solid #ccc;">
                <img src="<%= contextPath + "/" + ((item.getImageUrl() == null || item.getImageUrl().trim().isEmpty()) ? "images/default-book.png" : item.getImageUrl()) %>" 
     alt="<%= item.getItemTitle() %>" 
     class="cart-thumb" />

                <div style="flex-grow:1;">
                  <strong><%= item.getItemTitle() %></strong><br/>
                  Qty: <%= item.getQuantity() %> | Rs. <%= item.getPrice() %>
                </div>
              </li>
            <% } %>
          </ul>
          <div style="padding: 10px; display:flex; justify-content: space-between;">
            <a href="customer/cart.jsp" class="btn" style="font-size: 0.85rem; padding: 6px 12px;">View Cart</a>
            <a href="checkout.jsp" class="btn" style="font-size: 0.85rem; padding: 6px 12px;">Checkout</a>
          </div>
        <% } else { %>
          <p style="padding: 10px; margin: 0;">Your cart is empty.</p>
        <% } %>
      </div>
    </div>
  </nav>
</div>
<!-- Links Bar -->
<div class="links-bar">
  <a href="BookBrowseServlet">Browse Books</a> |
  <a href="about.jsp">About Us</a> |
  <a href="guidelines.jsp">Guidelines</a> |
  <div class="dropdown">
    <a href="CustomerOrderHistoryServlet">My Orders ?</a>
    <div class="dropdown-content">
      <ul style="list-style: none; padding: 0; margin: 0;">
        <li>#12345 - Delivered</li>
        <li>#12344 - Shipped</li>
        <li>#12343 - Processing</li>
      </ul>
      <a href="CustomerOrderHistoryServlet" style="display: block; margin-top: 8px; text-align: center; font-weight: bold;">View All Orders</a>
    </div>
  </div> |
  <a href="cart.jsp">Cart</a> |
  <a href="checkout.jsp">Checkout</a> |
  <div class="dropdown">
    <a href="browseBooks.jsp?filter=discounts">See Offers ?</a>
    <div class="dropdown-content">
      <p>No active discounts.</p>
      <a href="browseBooks.jsp?filter=discounts" style="display: block; margin-top: 8px; text-align: center; font-weight: bold;">Browse Offers</a>
    </div>
  </div>
</div>


<main>
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
    <p><strong>Subtotal:</strong> Rs. <%= order.getTotalAmount().subtract(order.getDeliveryFare()) %></p>
    <p><strong>Delivery Fare:</strong> Rs. <%= order.getDeliveryFare() %></p>
    <p><strong>Total Amount:</strong> Rs. <%= order.getTotalAmount() %></p>
    <p><strong>Payment Method:</strong> <%= order.getPaymentMethod() %></p>
    <p><strong>Status:</strong> <%= order.getStatus() %></p>

    <p><strong>Items:</strong></p>
    <%
        for (OrderItemDTO item : order.getItems()) {
    %>
        <div class="item-row">
            ? <%= item.getItemTitle() %> - Quantity: <%= item.getQuantity() %>, Price: Rs. <%= item.getPrice() %>
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
</main>

<script>
  const themeToggle = document.getElementById('themeToggle');
  const icon = themeToggle.querySelector('i');
  const savedTheme = localStorage.getItem('theme');
  if (savedTheme === 'dark') {
    document.body.classList.add('dark-theme');
    icon.classList.remove('fa-moon');
    icon.classList.add('fa-sun');
  }

  themeToggle.addEventListener('click', () => {
    document.body.classList.toggle('dark-theme');
    const isDark = document.body.classList.contains('dark-theme');
    icon.classList.toggle('fa-moon', !isDark);
    icon.classList.toggle('fa-sun', isDark);
    localStorage.setItem('theme', isDark ? 'dark' : 'light');
  });
</script>

</body>
</html>
