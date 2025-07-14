<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.CartItem" %>
<%@ page import="java.math.BigDecimal" %>

<%
    Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
    BigDecimal total = BigDecimal.ZERO;
    int cartCount = 0;
    if (cart != null) {
        for (CartItem item : cart.values()) {
            cartCount += item.getQuantity();
            total = total.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
    }
    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Your Cart | Pahana Edu</title>

    <!-- Font Awesome for Icons -->
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

    .theme-toggle-btn {
      background: linear-gradient(135deg, var(--brown-light), var(--brown-dark));
      color: white;
      border: none;
      font-size: 18px;
      padding: 10px;
      border-radius: 50%;
      cursor: pointer;
      transition: all 0.3s ease;
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
      margin-left: 20px;
    }

    .theme-toggle-btn:hover {
      background: linear-gradient(135deg, var(--brown-dark), var(--brown-light));
      transform: rotate(15deg);
    }

    body.dark-theme .theme-toggle-btn {
      background: linear-gradient(135deg, var(--bg-dark), var(--brown-light));
      color: var(--text-dark);
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

.cart-dropdown btn {
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

.cart-dropdown btn:hover {
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
.cart-dropdown .btn {
  display: block;
  width: 100%;
  margin: 8px 0;
  padding: 10px 0;
  background: linear-gradient(45deg, var(--btn-gradient-start), var(--btn-gradient-end));
  color: var(--card-bg-light);
  border: none;
  border-radius: var(--border-radius);
  font-weight: bold;
  text-align: center;
  text-decoration: none;
  cursor: pointer;
  transition: background 0.3s ease, transform 0.2s;
}

.cart-dropdown .btn:hover {
  background: linear-gradient(45deg, var(--btn-gradient-end), var(--btn-gradient-start));
  transform: translateY(-1px);
}

/* Shopping Cart Table & Content */
table {
  width: 90%;
  margin: 40px auto;
  border-collapse: collapse;
  background-color: var(--card-bg-light);
  box-shadow: 0 4px 15px var(--shadow-light);
  border-radius: var(--border-radius);
  overflow: hidden;
  font-size: 18px;
}

body.dark-theme table {
  background-color: var(--card-bg-dark);
  box-shadow: 0 4px 15px var(--shadow-dark);
}

thead {
  background-color: var(--brown-light);
  color: white;
  text-transform: uppercase;
}

thead th {
  padding: 16px;
}

tbody td {
  padding: 16px;
  text-align: center;
  vertical-align: middle;
  border-bottom: 1px solid #ddd;
}

tbody img {
  width: 60px;
  height: auto;
  border-radius: 6px;
}

.price-old {
  text-decoration: line-through;
  color: #a1887f;
}

.price-new {
  font-weight: bold;
  color: var(--brown-dark);
}

body.dark-theme .price-new {
  color: var(--brown-light);
}

.savings {
  font-size: 14px;
  color: green;
  font-weight: 600;
}

form.cart-action button {
  background: linear-gradient(45deg, var(--btn-gradient-start), var(--btn-gradient-end));
  border: none;
  color: white;
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
  transition: background 0.3s ease;
}

form.cart-action button:hover {
  background: linear-gradient(45deg, var(--btn-gradient-end), var(--btn-gradient-start));
  transform: scale(1.05);
}

tfoot td {
  padding: 16px;
  font-size: 20px;
  font-weight: bold;
  color: var(--brown-dark);
  background-color: var(--bg-light);
}

body.dark-theme tfoot td {
  background-color: #333;
  color: var(--brown-light);
}

.buttons {
  width: 90%;
  margin: 30px auto;
  display: flex;
  justify-content: space-between;
  gap: 20px;
}

.button {
  background: linear-gradient(45deg, var(--btn-gradient-start), var(--btn-gradient-end));
  color: white;
  padding: 14px 26px;
  text-decoration: none;
  font-weight: bold;
  border-radius: var(--border-radius);
  box-shadow: 0 3px 10px var(--shadow-light);
  transition: background 0.3s, transform 0.2s;
}

.button:hover {
  background: linear-gradient(45deg, var(--btn-gradient-end), var(--btn-gradient-start));
  transform: translateY(-2px);
}

.button.secondary {
  background: none;
  border: 2px solid var(--brown-dark);
  color: var(--brown-dark);
}

.button.secondary:hover {
  background: var(--brown-dark);
  color: white;
}

body.dark-theme .button.secondary {
  border-color: var(--brown-light);
  color: var(--brown-light);
}

body.dark-theme .button.secondary:hover {
  background: var(--brown-light);
  color: black;
}
.cart-thumb {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 6px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.12);
}
.popup-box {
  position: fixed;
  top: 20%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: var(--card-bg-light);
  color: var(--text-light);
  padding: 20px 30px;
  border: 2px solid var(--brown-dark);
  border-radius: 10px;
  box-shadow: 0 8px 20px rgba(0,0,0,0.2);
  z-index: 9999;
  max-width: 400px;
  text-align: center;
  animation: popupFadeIn 0.4s ease-out;
}

body.dark-theme .popup-box {
  background-color: var(--card-bg-dark);
  color: var(--text-dark);
  border-color: var(--brown-light);
}

.popup-close {
  position: absolute;
  top: 8px;
  right: 14px;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
  color: var(--brown-dark);
}

body.dark-theme .popup-close {
  color: var(--brown-light);
}

@keyframes popupFadeIn {
  from { opacity: 0; transform: translate(-50%, -60%); }
  to { opacity: 1; transform: translate(-50%, -50%); }
}
/* Overlay for dialog */
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.4); /* semi-transparent dark */
  display: none; /* hidden by default */
  z-index: 9998;
}

/* Dialog box */
.dialog-box {
  position: fixed;
  top: 50%;
  left: 50%;
  width: 360px;
  max-width: 90%;
  background-color: var(--card-bg-light);
  color: var(--text-light);
  border-radius: var(--border-radius);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
  padding: 20px 25px;
  transform: translate(-50%, -50%);
  z-index: 9999;
  display: none; /* hidden by default */
  animation: dialogFadeIn 0.3s ease forwards;
  font-family: var(--font-sans);
  text-align: center;
}

/* Dark theme adjustments */
body.dark-theme .dialog-box {
  background-color: var(--card-bg-dark);
  color: var(--text-dark);
  box-shadow: 0 8px 24px rgba(255, 255, 255, 0.15);
}

/* Close button for dialog */
.dialog-close {
  position: absolute;
  top: 8px;
  right: 14px;
  font-size: 22px;
  font-weight: bold;
  cursor: pointer;
  color: var(--brown-dark);
  transition: color 0.3s ease;
}

body.dark-theme .dialog-close {
  color: var(--brown-light);
}

.dialog-close:hover {
  color: #ff5555;
}

/* Dialog fade-in animation */
@keyframes dialogFadeIn {
  from {
    opacity: 0;
    transform: translate(-50%, -60%);
  }
  to {
    opacity: 1;
    transform: translate(-50%, -50%);
  }
}

/* Dialog buttons container */
.dialog-buttons {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  gap: 15px;
}

.dialog-buttons button {
  padding: 10px 18px;
  border-radius: var(--border-radius);
  font-weight: 700;
  border: none;
  cursor: pointer;
  background: linear-gradient(45deg, var(--btn-gradient-start), var(--btn-gradient-end));
  color: var(--card-bg-light);
  transition: background 0.3s ease, transform 0.2s ease;
}

.dialog-buttons button:hover {
  background: linear-gradient(45deg, var(--btn-gradient-end), var(--btn-gradient-start));
  transform: translateY(-2px);
}

/* Dark theme button hover */
body.dark-theme .dialog-buttons button:hover {
  background: linear-gradient(45deg, var(--btn-gradient-end), var(--btn-gradient-start));
}


  </style>

   
</head>
<body>
    <div id="popupBox" class="popup-box" style="display:none;">
    <span id="popupClose" class="popup-close">&times;</span>
    <p id="popupMessage"></p>
</div>

    


<!-- Top Banner -->
<div class="top-banner">
  Free shipping on orders above Rs. 6,500! Delivery in 2–5 working days in Sri Lanka.
  <button id="themeToggle" class="theme-toggle-btn" title="Toggle theme">
    <i class="fas fa-moon"></i>
  </button>
</div>

<!-- Navbar -->
<div class="navbar">
  <div class="logo"><i class="fas fa-book-open"></i> Pahana Edu</div>

  <form class="search-container" method="get" action="BookBrowseServlet">
    <select name="category" class="category-select">
      <option value="">-- All Categories --</option>
      <c:forEach var="category" items="${categories}">
        <option value="${category.id}" <c:if test="${category.id == selectedCategory}">selected</c:if>>${category.name}</option>
      </c:forEach>
    </select>
    <input type="text" class="search-box" name="keyword" placeholder="Search books by title/author" value="${searchKeyword}" />
    <input type="number" step="0.01" name="minPrice" class="search-box" placeholder="Min Price" value="${minPrice}" style="max-width:100px;" />
    <input type="number" step="0.01" name="maxPrice" class="search-box" placeholder="Max Price" value="${maxPrice}" style="max-width:100px;" />
    <button type="submit" class="search-button"><i class="fas fa-search"></i></button>
  </form>

  <nav class="nav-links" style="display: flex; font-weight: 700; align-items:end; font-size: 16px; color: var(--brown-dark);">
    <!-- Profile Dropdown -->
    <div class="profile-wrapper dropdown">
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
    <div class="account-cart dropdown">
      <span class="price">Rs. <%= total != null ? total.toPlainString() : "0.00" %></span>
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
            <a href="cart.jsp" class="btn" style="font-size: 0.85rem; padding: 6px 12px;">View Cart</a>
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
    <a href="CustomerOrderHistoryServlet">My Orders ▾</a>
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
    <a href="browseBooks.jsp?filter=discounts">See Offers ▾</a>
    <div class="dropdown-content">
      <p>No active discounts.</p>
      <a href="browseBooks.jsp?filter=discounts" style="display: block; margin-top: 8px; text-align: center; font-weight: bold;">Browse Offers</a>
    </div>
  </div>
</div>
    


  

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
                    <form method="post" class="cart-action" action="<%= contextPath %>/UpdatecartServlet">
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
                   <form method="post" action="<%= contextPath %>/RemoveCartItemServlet" class="cart-action" onsubmit="return confirm('Are you sure?');">
              
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
  
    function showPopup(message) {
    const popup = document.getElementById('popupBox');
    const popupMessage = document.getElementById('popupMessage');
    popupMessage.textContent = message;
    popup.style.display = 'block';

    // Auto-hide after 4 seconds
    setTimeout(() => {
      popup.style.display = 'none';
    }, 4000);
  }

  document.getElementById('popupClose').onclick = function() {
    document.getElementById('popupBox').style.display = 'none';
  };

  // Show popup based on success or error param
  const urlParams = new URLSearchParams(window.location.search);
  if (urlParams.get("success") === "cartUpdated") {
    showPopup("Cart updated successfully!");
  } else if (urlParams.get("success") === "itemRemoved") {
    showPopup("Item removed from your cart.");
  } else if (urlParams.get("error") === "cartUpdateFailed") {
    showPopup("Failed to update the cart. Please try again.");
  }
</script>


</body>
</html>
