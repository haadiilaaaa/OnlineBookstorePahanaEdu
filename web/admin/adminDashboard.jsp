<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="dto.AdminDashboardDTO" %>
<%@ page import="dto.UserSession" %>

<%
    AdminDashboardDTO dashboardData = (AdminDashboardDTO) request.getAttribute("dashboardData");
    UserSession user           = (UserSession) session.getAttribute("user");

    if (dashboardData == null || user == null || !"admin".equals(user.getUserType())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Admin Dashboard</title>

    <!-- Font Awesome icons (tiny CDN include) -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
          crossorigin="anonymous" referrerpolicy="no-referrer" />

    <style>
        :root {
            /* color palette */
            --gradient-start: #6d4c41;
            --gradient-end: #8d6e63;
            --light-bg: #f3ede5;
            --dark-bg: #1e1e1e;
            --card-bg: #fff9f4;
            --card-bg-dark: #372f2a;
            --text-color: #3b2e2a;
            --text-color-light: #d9cdc5;
            --radius: 12px;
            --shadow-light: 0 8px 20px rgba(141, 110, 99, 0.3);
            --shadow-dark: 0 8px 30px rgba(0, 0, 0, 0.6);
            --transition: 0.4s cubic-bezier(0.4, 0, 0.2, 1);
            --font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        /* Base and reset */
        * {
            box-sizing: border-box;
        }
        html, body {
            margin: 0; padding: 0; height: 100%;
            font-family: var(--font-family);
            background: var(--light-bg);
            color: var(--text-color);
            display: flex;
            flex-direction: column;
            transition: background var(--transition), color var(--transition);
            user-select: none;
        }
        body.dark {
            background: var(--dark-bg);
            color: var(--text-color-light);
        }

        /* Sidebar */
        .sidebar {
            position: fixed;
            top: 0; left: 0;
            height: 100vh;
            width: 260px;
            background: linear-gradient(180deg, var(--gradient-start), var(--gradient-end));
            color: white;
            display: flex;
            flex-direction: column;
            padding-top: 32px;
            box-shadow: 4px 0 20px rgba(0,0,0,0.15);
            transition: width var(--transition);
            z-index: 20;
            border-top-right-radius: var(--radius);
            border-bottom-right-radius: var(--radius);
        }
        body.dark .sidebar {
            background: linear-gradient(180deg, #4a3a35, #6b5047);
            box-shadow: 4px 0 24px rgba(0,0,0,0.5);
        }
        .sidebar .brand {
            font-size: 1.5rem;
            font-weight: 900;
            text-align: center;
            letter-spacing: 0.1em;
            margin-bottom: 36px;
            user-select: none;
            text-shadow: 0 2px 8px rgba(0,0,0,0.3);
        }
        .nav-link {
            display: flex;
            align-items: center;
            gap: 16px;
            padding: 16px 28px;
            font-weight: 700;
            font-size: 1.1rem;
            text-decoration: none;
            color: white;
            border-radius: var(--radius);
            margin: 6px 12px;
            transition:
                background-color var(--transition),
                transform var(--transition),
                box-shadow var(--transition);
            box-shadow: inset 0 0 0 0 transparent;
        }
        .nav-link i {
            font-size: 1.4rem;
            width: 28px;
            text-align: center;
        }
        .nav-link:hover, .nav-link:focus {
            background-color: rgba(255,255,255,0.15);
            box-shadow: 0 8px 12px rgba(141,110,99,0.3);
            transform: translateX(6px);
            outline: none;
        }
        .sidebar .spacer {
            flex-grow: 1;
        }

        /* Header */
        header {
            position: sticky;
            top: 0;
            background: var(--card-bg);
            padding: 0 40px;
            height: 64px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 6px 15px rgba(141,110,99,0.2);
            z-index: 10;
            transition: background var(--transition), box-shadow var(--transition);
        }
        body.dark header {
            background: var(--card-bg-dark);
            box-shadow: 0 6px 20px rgba(0,0,0,0.6);
        }
        .header-title {
            font-size: 1.6rem;
            font-weight: 900;
            color: var(--gradient-start);
            text-shadow: 0 2px 4px rgba(109,76,65,0.8);
            user-select: none;
        }
        body.dark .header-title {
            color: var(--gradient-end);
            text-shadow: 0 2px 6px rgba(141,110,99,0.7);
        }
        .header-subtitle {
            font-size: 0.95rem;
            font-weight: 500;
            color: var(--brown-300);
            margin-top: 4px;
            user-select: none;
        }
        body.dark .header-subtitle {
            color: #bda79a;
        }
        .header-actions {
            display: flex;
            gap: 20px;
            align-items: center;
        }
        .icon-btn {
            background: none;
            border: 3px solid var(--gradient-start);
            width: 44px;
            height: 44px;
            border-radius: 50%;
            font-size: 20px;
            color: var(--gradient-start);
            cursor: pointer;
            transition: all var(--transition);
            display: flex;
            justify-content: center;
            align-items: center;
            box-shadow: 0 0 8px rgba(141,110,99,0.4);
            user-select: none;
        }
        .icon-btn:hover, .icon-btn:focus {
            background: var(--gradient-end);
            color: #fff;
            outline: none;
            box-shadow: 0 0 18px rgba(141,110,99,0.8);
            transform: scale(1.12);
        }
        body.dark .icon-btn {
            border-color: var(--gradient-end);
            color: var(--gradient-end);
            box-shadow: 0 0 8px rgba(109,76,65,0.6);
        }
        body.dark .icon-btn:hover, body.dark .icon-btn:focus {
            background: var(--gradient-start);
            color: #fff;
            box-shadow: 0 0 20px rgba(109,76,65,1);
        }

        /* Main content */
        .main {
            margin-left: 260px;
            padding: 36px 48px;
            background: var(--light-bg);
            flex-grow: 1;
            min-height: calc(100vh - 64px);
            transition: background var(--transition);
        }
        body.dark .main {
            background: var(--dark-bg);
        }

        /* Stats grid */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit,minmax(280px,1fr));
            gap: 28px;
            margin-bottom: 64px;
        }
        .stat-card {
            background: var(--card-bg);
            box-shadow: var(--shadow-light);
            border-radius: var(--radius);
            padding: 32px 36px;
            display: flex;
            align-items: center;
            gap: 24px;
            transition: box-shadow var(--transition), transform var(--transition);
            cursor: default;
            user-select: none;
        }
        body.dark .stat-card {
            background: var(--card-bg-dark);
            box-shadow: var(--shadow-dark);
        }
        .stat-card:hover {
            box-shadow: 0 12px 28px rgba(141,110,99,0.6);
            transform: translateY(-4px);
        }
        .stat-icon {
            font-size: 3rem;
            color: var(--gradient-start);
            width: 64px;
            height: 64px;
            border-radius: 50%;
            background: linear-gradient(145deg, #a17d6a, #7a5b48);
            display: flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 4px 12px rgba(109,76,65,0.7);
            transition: background var(--transition), color var(--transition);
        }
        body.dark .stat-icon {
            background: linear-gradient(145deg, #7a5b48, #a17d6a);
            color: #f3ede5;
            box-shadow: 0 6px 18px rgba(109,76,65,0.9);
        }
        .stat-info .value {
            font-size: 2rem;
            font-weight: 900;
            line-height: 1.1;
            color: var(--gradient-start);
            user-select: text;
            text-shadow: 0 1px 2px rgba(141,110,99,0.8);
        }
        body.dark .stat-info .value {
            color: #f0d9c8;
            text-shadow: 0 1px 4px rgba(141,110,99,1);
        }
        .stat-info .label {
            font-size: 1rem;
            font-weight: 600;
            color: var(--brown-300);
            user-select: none;
            letter-spacing: 0.06em;
        }
        body.dark .stat-info .label {
            color: #c9b5aa;
        }

        /* Quick actions */
        .actions {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            user-select: none;
        }
        .actions a {
            background: linear-gradient(135deg, var(--gradient-start), var(--gradient-end));
            color: white;
            font-weight: 700;
            text-decoration: none;
            padding: 18px 36px;
            border-radius: 30px;
            font-size: 1.125rem;
            box-shadow: 0 10px 18px rgba(141,110,99,0.35);
            display: flex;
            align-items: center;
            gap: 12px;
            transition: background-color var(--transition), transform var(--transition), box-shadow var(--transition);
            user-select: none;
        }
        .actions a i {
            font-size: 1.4rem;
        }
        .actions a:hover, .actions a:focus {
            background: linear-gradient(135deg, #5b3e30, #7c5d47);
            box-shadow: 0 14px 26px rgba(109,76,65,0.55);
            transform: translateY(-4px);
            outline: none;
        }

        /* Mobile sidebar toggle */
        @media (max-width: 900px) {
            .sidebar {
                left: -260px;
                width: 260px;
                box-shadow: 4px 0 22px rgba(0,0,0,0.3);
            }
            .sidebar.open {
                left: 0;
            }
            .overlay {
                display: none;
            }
            .overlay.show {
                display: block;
                position: fixed;
                inset: 0;
                background: rgba(0,0,0,0.5);
                z-index: 15;
            }
            .icon-btn.hide-on-desktop {
                display: flex;
            }
            .main {
                margin-left: 0;
                padding: 24px 20px;
                min-height: auto;
            }
        }
        @media (min-width: 901px) {
            .icon-btn.hide-on-desktop {
                display: none;
            }
        }

        /* Footer */
        footer {
            text-align: center;
            padding: 14px 0;
            font-size: 0.9rem;
            color: var(--brown-300);
            user-select: none;
            margin-top: auto;
        }
        body.dark footer {
            color: #bca49f;
        }
    </style>
</head>

<body>
    <!-- ===== SIDEBAR ===== -->
    <aside class="sidebar" id="sidebar" tabindex="0" aria-label="Sidebar Navigation">
        <div class="brand" tabindex="0">Admin</div>

        <a href="AddItemServlet"          class="nav-link" tabindex="0"><i class="fa-solid fa-box"></i>Manage Items</a>
        <a href="ManageCategoriesServlet" class="nav-link" tabindex="0"><i class="fa-solid fa-tags"></i>Manage Categories</a>
        <a href="ManageDiscountServlet"   class="nav-link" tabindex="0"><i class="fa-solid fa-percent"></i>Manage Discounts</a>
        <a href="ViewAuditLogsServlet"    class="nav-link" tabindex="0"><i class="fa-solid fa-file-waveform"></i>Audit Logs</a>

        <div class="spacer"></div>
        <a href="logout.jsp" class="nav-link" tabindex="0"><i class="fa-solid fa-right-from-bracket"></i>Logout</a>
    </aside>

    <div class="overlay" id="overlay"></div>

    <!-- ===== MAIN ===== -->
    <div class="main">
        <header>
            <div>
                <div class="header-title" tabindex="0">Welcome, Admin <%= dashboardData.getAdminName() %></div>
                <div class="header-subtitle" tabindex="0">Manage your dashboard and site settings</div>
            </div>

            <div class="header-actions">
                <button class="icon-btn hide-on-desktop" id="menuBtn" title="Menu" aria-label="Open sidebar menu">
                    <i class="fa-solid fa-bars"></i>
                </button>
                <button class="icon-btn" id="themeBtn" title="Toggle Theme" aria-label="Toggle light/dark mode">
                    ☀️
                </button>
            </div>
        </header>

        <section class="content" tabindex="0">
            <div class="stats-grid">
                <div class="stat-card" tabindex="0" aria-label="Total items">
                    <div class="stat-icon"><i class="fa-solid fa-box"></i></div>
                    <div class="stat-info">
                        <div class="value"><%= dashboardData.getTotalItems() %></div>
                        <div class="label">Total Items</div>
                    </div>
                </div>

                <div class="stat-card" tabindex="0" aria-label="Total categories">
                    <div class="stat-icon"><i class="fa-solid fa-tags"></i></div>
                    <div class="stat-info">
                        <div class="value"><%= dashboardData.getTotalCategories() %></div>
                        <div class="label">Categories</div>
                    </div>
                </div>

                <div class="stat-card" tabindex="0" aria-label="Total customers">
                    <div class="stat-icon"><i class="fa-solid fa-users"></i></div>
                    <div class="stat-info">
                        <div class="value"><%= dashboardData.getTotalCustomers() %></div>
                        <div class="label">Customers</div>
                    </div>
                </div>

                <div class="stat-card" tabindex="0" aria-label="Total staff members">
                    <div class="stat-icon"><i class="fa-solid fa-user-tie"></i></div>
                    <div class="stat-info">
                        <div class="value"><%= dashboardData.getTotalStaff() %></div>
                        <div class="label">Staff Members</div>
                    </div>
                </div>

                <div class="stat-card" tabindex="0" aria-label="Total active discounts">
                    <div class="stat-icon"><i class="fa-solid fa-percent"></i></div>
                    <div class="stat-info">
                        <div class="value"><%= dashboardData.getTotalActiveDiscounts() %></div>
                        <div class="label">Active Discounts</div>
                    </div>
                </div>
            </div>

            <div class="actions" role="list">
                <a href="AddItemServlet" tabindex="0"><i class="fa-solid fa-box"></i>Manage Items</a>
                <a href="ManageCategoriesServlet" tabindex="0"><i class="fa-solid fa-tags"></i>Manage Categories</a>
                <a href="ManageDiscountServlet" tabindex="0"><i class="fa-solid fa-percent"></i>Manage Discounts</a>
                <a href="ViewAuditLogsServlet" tabindex="0"><i class="fa-solid fa-file-waveform"></i>Audit Logs</a>
                    <a href="AdminOrderHistoryServlet" tabindex="0"><i class="fa-solid fa-clipboard-list"></i>Manage Orders</a> <!-- ✅ New link -->
            </div>
        </section>

        <footer>
            &copy; <%= java.time.Year.now() %> Your Company. All rights reserved.
        </footer>
    </div>

    <script>
        const sidebar = document.getElementById('sidebar');
        const overlay = document.getElementById('overlay');
        const menuBtn = document.getElementById('menuBtn');
        const themeBtn = document.getElementById('themeBtn');
        const body = document.body;

        menuBtn.addEventListener('click', () => {
            sidebar.classList.toggle('open');
            overlay.classList.toggle('show');
        });
        overlay.addEventListener('click', () => {
            sidebar.classList.remove('open');
            overlay.classList.remove('show');
        });
        document.querySelectorAll('.nav-link').forEach(link => {
            link.addEventListener('click', () => {
                sidebar.classList.remove('open');
                overlay.classList.remove('show');
            });
        });

        themeBtn.addEventListener('click', () => {
            body.classList.toggle('dark');
            themeBtn.textContent = body.classList.contains('dark') ? '🌙' : '☀️';
        });

        const header = document.querySelector('header');
        window.addEventListener('scroll', () => {
            if(window.scrollY > 5){
                header.style.boxShadow = '0 10px 25px rgba(141,110,99,0.35)';
            } else {
                header.style.boxShadow = '';
            }
        });
    </script>
</body>
</html>
