<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Bookshop Navbar</title>
  <link rel="stylesheet" href="styles.css">
</head>
<style>
    /* Root variables (already in your code) reused here */
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
}

/* Top banner */
.top-banner {
  background-color: var(--brown-dark);
  color: var(--card-bg-light);
  text-align: center;
  padding: 8px;
  font-size: 14px;
  font-weight: bold;
}

/* Navbar container */
.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 15px 25px;
  background-color: var(--card-bg-light);
  box-shadow: 0 2px 6px var(--shadow-light);
  transition: background-color var(--transition-speed), color var(--transition-speed);
}

/* Dark mode for navbar */
body.dark-theme .navbar {
  background-color: var(--card-bg-dark);
  box-shadow: 0 2px 6px var(--shadow-dark);
}

/* Logo */
.navbar .logo {
  font-weight: bold;
  font-size: 1.6rem;
  color: var(--text-light);
  display: flex;
  align-items: center;
  gap: 5px;
  letter-spacing: 1px;
}
body.dark-theme .navbar .logo {
  color: var(--text-dark);
}

/* Search container */
.search-container {
  display: flex;
  flex-grow: 1;
  margin: 0 30px;
}

.category-select {
  padding: 8px;
  border: 1px solid var(--brown-light);
  border-radius: 6px 0 0 6px;
  background-color: var(--card-bg-light);
  color: var(--text-light);
}
body.dark-theme .category-select {
  background-color: var(--card-bg-dark);
  color: var(--text-dark);
  border-color: var(--brown-dark);
}

.search-box {
  flex-grow: 1;
  padding: 8px;
  border: 1px solid var(--brown-light);
  border-left: none;
  background-color: var(--card-bg-light);
  color: var(--text-light);
}
body.dark-theme .search-box {
  background-color: var(--card-bg-dark);
  color: var(--text-dark);
  border-color: var(--brown-dark);
}

.search-button {
  background-color: var(--brown-dark);
  color: var(--card-bg-light);
  border: none;
  padding: 0 16px;
  font-size: 18px;
  border-radius: 0 6px 6px 0;
  cursor: pointer;
  transition: background-color 0.3s ease;
}
.search-button:hover {
  background-color: var(--brown-light);
}

/* Cart and Login */
.account-cart {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: var(--text-light);
}
body.dark-theme .account-cart {
  color: var(--text-dark);
}

.account-cart .cart {
  font-size: 20px;
}

.account-cart .login {
  text-decoration: none;
  color: var(--brown-dark);
  font-weight: 600;
}
body.dark-theme .account-cart .login {
  color: var(--brown-light);
}

/* Links bar */
.links-bar {
  background-color: var(--bg-light);
  padding: 10px 20px;
  font-size: 14px;
  text-align: center;
  color: var(--text-light);
}
body.dark-theme .links-bar {
  background-color: var(--bg-dark);
  color: var(--text-dark);
}

.links-bar a {
  color: inherit;
  text-decoration: none;
  margin: 0 8px;
  font-weight: bold;
  transition: color 0.3s ease;
}
.links-bar a:hover {
  color: var(--brown-dark);
}
body.dark-theme .links-bar a:hover {
  color: var(--brown-light);
}

</style>
<body>

 <div class="top-banner">
  Free shipping on orders above Rs. 6,500! Delivery in 2?5 working days in Sri Lanka.
</div>

<div class="navbar">
  <div class="logo">? Pahana Edu</div>

  <div class="search-container">
    <select class="category-select">
      <option>All</option>
      <option>Fiction</option>
      <option>Kids</option>
    </select>
    <input type="text" class="search-box" placeholder="Search books by title/author">
    <button class="search-button">?</button>
  </div>

  <div class="account-cart">
    <span class="price">Rs. 0.00</span>
    <span class="cart">?</span>
    <a href="login.jsp" class="login">LOGIN / REGISTER</a>
  </div>
</div>

<div class="links-bar">
  <a href="#">NEW ARRIVALS</a> |
  <a href="#">BESTSELLERS</a> |
  <a href="#">SALE</a> |
  <a href="#">BOOK BUNDLES</a> |
  <a href="#">IN STOCK</a> |
  <a href="#">GIFT CARDS</a> |
  <a href="#">ALL PRODUCTS</a>
</div>
</body>
</html>
