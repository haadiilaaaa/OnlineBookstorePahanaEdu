<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dto.CustomerDashboardDTO" %>
<%@ page import="model.CartItem" %>
<%@ page import="model.Discount" %>
<%@ page import="dto.UserSession" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Category" %>

<%
    // All page-level imports and object retrievals are now done in scriptlets
    HttpSession sessionObj = session;
    UserSession user = (UserSession) sessionObj.getAttribute("user");
    CustomerDashboardDTO dashboard = (CustomerDashboardDTO) request.getAttribute("dashboardData");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
    String selectedCategory = (String) request.getAttribute("selectedCategory");
    String searchKeyword = (String) request.getAttribute("searchKeyword");
    String minPrice = (String) request.getAttribute("minPrice");
    String maxPrice = (String) request.getAttribute("maxPrice");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Customer Dashboard</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" crossorigin="anonymous" />
    <link rel="stylesheet" href="styles.css">

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
      --shadow-dark: rgba(0,0,0,0.6);
      --border-radius: 12px;
      --transition-speed: 0.35s;
      --font-sans: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      --btn-gradient-start: #6d4c41;
      --btn-gradient-end: #a1887f;
      --light-blue-2: #80d4f4;
      --medium-blue: #4aa7d9;
      --font-color: #333;
      --darker-blue: #2c6c9a;
      --dark-bg: #1e1e1e;
      --dark-text: #eee;
    }

    body, .navbar, .card, .links-bar, .top-banner, .search-container, .account-cart, .category-select, .search-box, .search-button, .theme-toggle-btn {
      transition: background-color var(--transition-speed), color var(--transition-speed), box-shadow 0.4s ease, transform 0.3s ease;
    }

    body {
      font-family: var(--font-sans);
      background: linear-gradient(135deg, #f3ede5 0%, #e8ded6 100%);
      color: var(--text-light);
      margin: 0;
      padding: 0;
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      opacity: 0;
      animation: fadeIn 0.6s ease forwards;
    }

    @keyframes fadeIn {
      to {
        opacity: 1;
      }
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
      text-transform: uppercase;
      user-select: none;
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
      box-shadow: 0 3px 8px rgba(109, 76, 65, 0.15);
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

    .account-cart .login {
      text-decoration: none;
      color: var(--brown-dark);
      font-weight: 700;
      border: 2px solid var(--brown-dark);
      padding: 5px 15px;
      border-radius: 25px;
    }

    .account-cart .login:hover {
      background-color: var(--brown-dark);
      color: var(--card-bg-light);
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

    .cards {
      display: flex;
      flex-wrap: wrap;
      justify-content: center;
      gap: 2.5rem;
      padding: 3rem 2rem;
    }

    .card {
      flex: 1 1 280px;
      background: linear-gradient(145deg, #fffefb, #f9f7f0);
      border-radius: var(--border-radius);
      padding: 2rem;
      box-shadow: 6px 6px 16px var(--shadow-light), -6px -6px 16px #fff;
      transition: transform 0.4s ease, box-shadow 0.4s ease;
    }

    body.dark-theme .card {
      background: linear-gradient(145deg, #3e3e3e, #424242);
      box-shadow: 6px 6px 16px var(--shadow-dark), -6px -6px 16px #4a4a4a;
    }

    .card:hover {
      transform: translateY(-12px) scale(1.05);
    }

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
    }

    .theme-toggle-btn {
      background: none;
      border: none;
      font-size: 20px;
      color: var(--card-bg-light);
      margin-left: 15px;
      cursor: pointer;
    }

    footer {
      text-align: center;
      margin: 4rem 0 2rem;
      font-size: 0.95rem;
    }
    
    .dropdown {
      display: inline-block;
      position: relative;
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

    @media (max-width: 1024px) {
      .navbar {
        flex-direction: column;
        align-items: flex-start;
        gap: 10px;
      }

      .search-container {
        width: 100%;
        margin: 10px 0;
        flex-wrap: wrap;
        height: auto;
      }

      .search-box,
      .category-select,
      .search-button {
        flex: 1 1 100%;
        max-width: 100%;
        margin-bottom: 8px;
        border-radius: 8px;
      }

      .nav-links {
        flex-direction: column;
        gap: 10px;
        width: 100%;
      }

      .account-cart {
        justify-content: space-between;
        width: 100%;
      }
    }

    @media (max-width: 768px) {
      .cards {
        flex-direction: column;
        align-items: center;
        padding: 2rem 1rem;
      }

      .card {
        width: 90%;
        flex: none;
      }

      .navbar {
        padding: 10px 15px;
      }

      .top-banner {
        font-size: 12px;
        padding: 8px;
      }

      .search-box {
        font-size: 14px;
      }

      .search-button {
        font-size: 16px;
      }

      .dropdown-content {
        min-width: 200px;
      }
    }

    @media (max-width: 480px) {
      .logo {
        font-size: 1.4rem;
      }

      .price {
        font-size: 1rem;
      }

      .cart {
        font-size: 22px;
      }

      .login {
        font-size: 14px;
        padding: 4px 12px;
      }

      .btn {
        font-size: 1rem;
        padding: 0.6rem 1.4rem;
      }

      footer {
        font-size: 0.8rem;
      }
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
    .profile-wrapper {
      display: flex;
      align-items: center;
      gap: 8px;
      font-weight: 700;
      font-size: 16px;
      color: var(--brown-dark);
    }

    .profile-text {
      text-decoration: none;
      color: inherit;
    }

    .profile-text:hover {
      text-decoration: underline;
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

    body.dark-theme .theme-toggle-btn {
      background: linear-gradient(135deg, var(--darker-blue), var(--dark-bg));
      color: var(--dark-text);
    }
</style>
    
</head>
<body>

<div class="top-banner">
    Free shipping on orders above Rs. 6,500! Delivery in 2–5 working days in Sri Lanka.
    <button id="themeToggle" class="theme-toggle-btn" title="Toggle theme">
        <i class="fas fa-moon"></i>
    </button>
</div>

<div class="navbar">
    <div class="logo"><i class="fas fa-book-open"></i> Pahana Edu</div>

    <form class="search-container" method="get" action="BookBrowseServlet">
        <select name="categoryId" class="category-select">
            <option value="">-- All Categories --</option>
            <%
                if (categories != null) {
                    for (Category category : categories) {
            %>
            <option value="<%= category.getId() %>" <%= (category.getId().equals(selectedCategory) ? "selected" : "") %>><%= category.getName() %></option>
            <%
                    }
                }
            %>
        </select>
        <input type="text" class="search-box" name="search" placeholder="Search books by title/author" value="<%= searchKeyword != null ? searchKeyword : "" %>" />
        <input type="number" step="0.01" name="minPrice" class="search-box" placeholder="Min Price" value="<%= minPrice != null ? minPrice : "" %>" style="max-width:100px;" />
        <input type="number" step="0.01" name="maxPrice" class="search-box" placeholder="Max Price" value="<%= maxPrice != null ? maxPrice : "" %>" style="max-width:100px;" />
        <button type="submit" class="search-button"><i class="fas fa-search"></i></button>
    </form>

    <nav class="nav-links" style="display: flex; font-weight: 700; align-items:end; font-size: 16px; color: var(--brown-dark);">

        <div class="profile-wrapper dropdown">
            <a href="customer/MyAccount.jsp" class="profile-text">Profile</a>
            <a href="customer/MyAccount.jsp" class="profile-link" title="My Profile">
                <i class="fas fa-user"></i>
            </a>

            <div class="dropdown-content">
                <ul style="list-style:none; padding:0; margin:0;">
                    <li><a href="orders.jsp" style="color:inherit; text-decoration:none;">Orders</a></li>
                    <li><a href="customer/customerEditProfile.jsp" style="color:inherit; text-decoration:none;">Account Details</a></li>
                    <li><a href="addresses.jsp" style="color:inherit; text-decoration:none;">Addresses</a></li>
                    <li><a href="LogoutServlet" style="color:inherit; text-decoration:none;">Logout</a></li>
                </ul>
            </div>
        </div>

        <div class="account-cart dropdown">
            <span class="price">Rs. <%= dashboard != null && dashboard.getCartTotal() != null ? dashboard.getCartTotal().toPlainString() : "0.00" %></span>

            <span class="cart" style="cursor:pointer;">
                <i class="fas fa-shopping-cart"></i> (<%= dashboard != null ? dashboard.getCartItemCount() : 0 %>)
            </span>

            <div class="dropdown-content cart-dropdown">
                <%
                    if (dashboard != null && dashboard.getCartItems() != null && !dashboard.getCartItems().isEmpty()) {
                %>
                <ul style="list-style:none; padding:0; margin:0; max-height: 300px; overflow-y: auto;">
                    <%
                        for (model.CartItem item : dashboard.getCartItems()) {
                    %>
                    <li style="display:flex; align-items:center; gap:10px; padding: 8px 5px; border-bottom: 1px solid #ccc;">
                        <img src="<%= (item.getImageUrl() == null || item.getImageUrl().trim().isEmpty()) ? "images/default-book.png" : item.getImageUrl() %>" alt="<%= item.getItemTitle() %>" style="height:50px; width:auto; border-radius: 4px;" />
                        <div style="flex-grow:1;">
                            <strong><%= item.getItemTitle() %></strong><br/>
                            Qty: <%= item.getQuantity() %> | Rs. <%= item.getPrice() %>
                        </div>
                    </li>
                    <%
                        }
                    %>
                </ul>
                <div style="padding: 10px; display:flex; justify-content: space-between;">
                    <a href="customer/cart.jsp" class="btn" style="font-size: 0.85rem; padding: 6px 12px;">View Cart</a>
                    <a href="checkout.jsp" class="btn" style="font-size: 0.85rem; padding: 6px 12px;">Proceed to Checkout</a>
                </div>
                <%
                } else {
                %>
                <p style="padding: 10px; margin: 0;">Your cart is empty.</p>
                <%
                    }
                %>
            </div>
        </div>
    </nav>
</div>

<div class="links-bar">
    <a href="BookBrowseServlet">Browse Books</a> |
    <a href="about.jsp">About Us</a> |
    <a href="CustomerGuidelinesServlet">Guidelines</a> |
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
            <%
                if (dashboard != null && dashboard.getActiveDiscounts() != null && !dashboard.getActiveDiscounts().isEmpty()) {
            %>
            <ul style="list-style: none; padding: 0; margin: 0;">
                <%
                    for (Discount d : dashboard.getActiveDiscounts()) {
                %>
                <li><strong><%= d.getName() %></strong> - <%= d.getDiscountPercent() %>% off</li>
                <%
                    }
                %>
            </ul>
            <% } else { %>
            <p style="margin: 0 15px;">No active discounts.</p>
            <% } %>
            <a href="browseBooks.jsp?filter=discounts" style="display: block; margin-top: 8px; text-align: center; font-weight: bold;">Browse Offers</a>
        </div>
    </div>
</div>

<main>
    <h1>Welcome, <%= dashboard != null ? dashboard.getCustomerName() : "Guest" %>!</h1>

    <div class="cards">
        <section class="card">
            <h3>Your Recent Orders</h3>
            <ul>
                <li>#12345 - Delivered</li>
                <li>#12344 - Shipped</li>
                <li>#12343 - Processing</li>
            </ul>
            <a href="orders.jsp" class="btn">View All Orders</a>
        </section>

        <section class="card">
            <h3>Recommended For You</h3>
            <p>Based on your Browse history</p>
            <a href="browseBooks.jsp" class="btn">Browse Recommendations</a>
        </section>

        <section class="card">
            <h3>Exclusive Offers</h3>
            <p>10% off all textbooks this week!</p>
            <a href="browseBooks.jsp?filter=discounts" class="btn">See Offers</a>
        </section>
    </div>

    <div style="text-align: center; margin-top: 2rem;">
        <button id="toggleSummaryBtn" class="btn">Show My Summary</button>
    </div>

    <div id="summarySection" style="display: none; margin-top: 2rem;">
        <div class="cards">
            <section class="card">
                <h3>Cart Items</h3>
                <p id="cartCountDisplay">0</p>
                <a href="cart.jsp" class="btn">View Cart</a>
            </section>

            <section class="card">
                <h3>Cart Total</h3>
                <p>Rs. <%= dashboard != null ? dashboard.getCartTotal() : "0.00" %></p>
                <a href="checkout.jsp" class="btn">Checkout</a>
            </section>

            <section class="card">
                <h3>Active Discounts</h3>
                <%
                    if (dashboard != null && dashboard.getActiveDiscounts() != null && !dashboard.getActiveDiscounts().isEmpty()) {
                %>
                <ul>
                    <%
                        for (Discount d : dashboard.getActiveDiscounts()) {
                    %>
                    <li><strong><%= d.getName() %></strong> - <%= d.getDiscountPercent() %>% off</li>
                    <%
                        }
                    %>
                </ul>
                <% } else { %>
                <p>No active discounts.</p>
                <% } %>
                <a href="browseBooks.jsp" class="btn">Browse Offers</a>
            </section>
        </div>
    </div>
</main>

<footer>© 2025 Pahana Edu. All rights reserved.</footer>
<pre>
Dashboard: <%= dashboard %>
Cart items count: <%= (dashboard != null && dashboard.getCartItems() != null) ? dashboard.getCartItems().size() : 0 %>
</pre>

<script>
    const cartCountDisplay = document.getElementById('cartCountDisplay');
    const targetCount = <%= dashboard != null && dashboard.getCartItems() != null ? dashboard.getCartItems().size() : 0 %>;
    let count = 0;
    function animateCount() {
        if (count < targetCount) {
            count++;
            cartCountDisplay.textContent = count;
            setTimeout(animateCount, 30);
        }
    }
    animateCount();

    document.getElementById("toggleSummaryBtn").addEventListener("click", (event) => {
        const summary = document.getElementById("summarySection");
        const isVisible = summary.style.display === "block";
        summary.style.display = isVisible ? "none" : "block";
        event.target.textContent = isVisible ? "Show My Summary" : "Hide My Summary";
    });

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