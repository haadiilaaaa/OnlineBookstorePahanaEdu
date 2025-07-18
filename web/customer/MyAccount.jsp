<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="dto.CustomerDashboardDTO" %>

<%
    CustomerDashboardDTO dashboard = (CustomerDashboardDTO) request.getAttribute("dashboardData");
    String customerName = dashboard != null ? dashboard.getCustomerName() : "Customer";
%>

<!DOCTYPE html>
<html>
<head>
    <title>My Account - Gallery Café</title>
    <link rel="stylesheet" href="styles/dashboard.css"> <!-- Link to your CSS -->
</head>
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

body {
  font-family: var(--font-sans);
  margin: 0;
  background: linear-gradient(135deg, var(--bg-light), #e8ded6);
  color: var(--text-light);
  transition: background-color var(--transition-speed), color var(--transition-speed);
}

body.dark-theme {
  background: linear-gradient(135deg, var(--bg-dark), #3a3a3a);
  color: var(--text-dark);
}

.dashboard-container {
  display: flex;
  min-height: 100vh;
}

/* Sidebar */
.sidebar {
  width: 260px;
  background-color: var(--card-bg-light);
  padding: 20px;
  box-shadow: 2px 0 10px var(--shadow-light);
  transition: background-color var(--transition-speed);
}

body.dark-theme .sidebar {
  background-color: var(--card-bg-dark);
  box-shadow: 2px 0 10px var(--shadow-dark);
}

/* Profile Info */
.profile {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.avatar {
  width: 55px;
  height: 55px;
  background-color: var(--brown-dark);
  color: white;
  font-weight: bold;
  font-size: 22px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  box-shadow: 0 3px 6px var(--shadow-light);
}

/* Username */
.username strong {
  font-size: 1.1rem;
  color: var(--text-light);
}

body.dark-theme .username strong {
  color: var(--text-dark);
}

/* Sidebar Links */
.nav-links {
  display: flex;
  flex-direction: column;
}

.nav-links a {
  padding: 10px 0;
  color: var(--brown-dark);
  text-decoration: none;
  font-weight: 600;
  transition: color 0.3s;
}

.nav-links a:hover {
  color: var(--brown-light);
}

body.dark-theme .nav-links a {
  color: var(--brown-light);
}

body.dark-theme .nav-links a:hover {
  color: #d7ccc8;
}

/* Main Content */
.main-content {
  flex: 1;
  padding: 40px;
  background-color: var(--card-bg-light);
  transition: background-color var(--transition-speed);
}

body.dark-theme .main-content {
  background-color: var(--card-bg-dark);
}

/* Main Headings */
.main-content h2 {
  font-size: 28px;
  margin-bottom: 12px;
  color: var(--brown-dark);
}

body.dark-theme .main-content h2 {
  color: var(--brown-light);
}

/* Dashboard Cards */
.dashboard-links {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 30px;
}

.card {
  flex: 1 1 calc(33% - 20px);
  background-color: var(--bg-light);
  padding: 22px;
  text-align: center;
  border-radius: var(--border-radius);
  text-decoration: none;
  color: var(--text-light);
  font-weight: 700;
  box-shadow: 0 6px 16px var(--shadow-light);
  transition: background-color 0.3s ease, transform 0.3s ease;
}

.card:hover {
  background-color: #e5d3c7;
  transform: translateY(-4px);
}

body.dark-theme .card {
  background-color: #484848;
  color: var(--text-dark);
  box-shadow: 0 6px 16px var(--shadow-dark);
}

body.dark-theme .card:hover {
  background-color: #5c5c5c;
}


</style>
<body>
    <button id="themeToggle" class="theme-toggle-btn" title="Toggle theme">
  <i class="fas fa-moon"></i>
</button>

    <div class="dashboard-container">
        <aside class="sidebar">
            <div class="profile">
                <div class="avatar">A</div>
                <div class="username">
                    <strong><%= customerName %></strong><br/>
                  
                </div>
            </div>
            <nav class="nav-links">
                <a href="#">Dashboard</a>
               
                <a href="#">Orders</a>
               
               
                <a href="#">Addresses</a>
                <a href="#">Account Details</a>
               
                <a href="logout">Logout</a>
            </nav>
        </aside>

        <main class="main-content">
            <h2>My Account</h2>
            <p>Hello <strong><%= customerName %></strong>!</p>
            <p>From your account dashboard you can view your <strong>recent orders</strong>, manage your <strong>shipping and billing addresses</strong>, and edit your <strong>password</strong> and <strong>account details</strong>.</p>

            <div class="dashboard-links">
                <a href="#" class="card">Pre-orders</a>
                <a href="#" class="card">Orders</a>
                <a href="#" class="card">Downloads</a>
                <a href="#" class="card">Stock Notifications</a>
                <a href="#" class="card">Addresses</a>
                <a href="#" class="card">Account details</a>
                <a href="#" class="card">Points</a>
                <a href="#" class="card">Wishlist</a>
            </div>
        </main>
    </div>
            
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
