<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Manage Customers</title>
  <style>
    :root {
        --gradient-start: #6d4c41;
        --gradient-end: #8d6e63;
        --light-bg: #f3ede5;
        --card-bg: #fff9f4;
        --card-bg-dark: #372f2a;
        --text-color: #3b2e2a;
        --text-color-light: #d9cdc5;
        --radius: 12px;
        --shadow-light: 0 12px 30px rgba(141,110,99,0.2);
        --shadow-dark: 0 12px 30px rgba(0,0,0,0.6);
        --transition: 0.35s cubic-bezier(0.4,0,0.2,1);
        --font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        --btn-radius: 30px;
    }

    * { box-sizing: border-box; }
    body {
        margin: 0;
        padding: 0;
        font-family: var(--font-family);
        background: var(--light-bg);
        color: var(--text-color);
        min-height: 100vh;
        transition: background var(--transition), color var(--transition);
    }
    body.dark {
        background: #1e1e1e;
        color: var(--text-color-light);
    }

    .wrapper {
        max-width: 1100px;
        margin: 0 auto;
        padding: 40px 24px;
    }

    h2 {
        font-size: 1.9rem;
        margin: 0 0 24px;
        font-weight: 800;
        background: linear-gradient(135deg, var(--gradient-start), var(--gradient-end));
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
    }

    .card {
        background: var(--card-bg);
        border-radius: var(--radius);
        padding: 28px 32px;
        box-shadow: var(--shadow-light);
        margin-bottom: 32px;
        transition: background var(--transition), box-shadow var(--transition);
    }
    body.dark .card {
        background: var(--card-bg-dark);
        box-shadow: var(--shadow-dark);
    }

    table {
        width: 100%;
        border-collapse: separate;
        border-spacing: 0;
        font-size: 0.95rem;
        background: transparent;
    }

    thead th {
        position: sticky;
        top: 0;
        background: rgba(109,76,65,0.08);
        padding: 14px 16px;
        text-align: left;
        font-weight: 700;
        letter-spacing: 0.05em;
        font-size: 0.85rem;
    }

    tbody tr {
        background: #fff;
        transition: transform var(--transition), box-shadow var(--transition);
    }
    body.dark tbody tr {
        background: #2f2723;
    }
    tbody tr:hover {
        transform: translateY(-2px);
        box-shadow: 0 10px 22px rgba(109,76,65,0.25);
    }

    td {
        padding: 14px 16px;
        vertical-align: middle;
        border-bottom: 1px solid rgba(109,76,65,0.08);
        font-size: 0.9rem;
    }

    a {
        text-decoration: none;
        color: var(--gradient-start);
        font-weight: 600;
        margin-left: 8px;
        transition: color var(--transition);
    }
    a:hover {
        color: var(--gradient-end);
    }

    button {
        padding: 10px 18px;
        font-size: 0.85rem;
        font-weight: 700;
        border: none;
        border-radius: var(--btn-radius);
        cursor: pointer;
        background: linear-gradient(135deg, var(--gradient-start), var(--gradient-end));
        color: white;
        transition: all var(--transition);
    }
    button:hover, button:focus {
        filter: brightness(1.05);
        transform: translateY(-1px);
        outline: none;
    }

    .action-group {
        display: flex;
        gap: 8px;
        flex-wrap: wrap;
    }

    .small {
        padding: 6px 14px;
        font-size: 0.75rem;
        border-radius: 999px;
    }

    /* Optional theme toggle pill (if you want to add) */
    .theme-toggle {
        position: fixed;
        top: 16px;
        right: 16px;
        background: none;
        border: 2px solid var(--gradient-start);
        padding: 10px 14px;
        border-radius: 999px;
        cursor: pointer;
        font-weight: 600;
        transition: all var(--transition);
    }
    .theme-toggle:hover {
        background: var(--gradient-end);
        color: white;
        border-color: var(--gradient-end);
    }
    
    .status-msg {
    padding: 12px 20px;
    border-radius: 8px;
    font-weight: 600;
    margin-bottom: 20px;
}

.status-msg.green { background-color: #2ecc71; color: white; }
.status-msg.red   { background-color: #e74c3c; color: white; }
</style>

</head>

<c:if test="${not empty sessionScope.successMessage}">
    <div class="status-msg green">${sessionScope.successMessage}</div>
    <c:remove var="successMessage" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.errorMessage}">
    <div class="status-msg red">${sessionScope.errorMessage}</div>
    <c:remove var="errorMessage" scope="session"/>
</c:if>

<body>
    <button class="theme-toggle" id="themeBtn">🌙</button>
   <div class="wrapper">
  <div class="card">
    <h2>Customer Management</h2>
    <table> ... </table>
  </div>
</div>

    <table>
        <tr>
            <th>ID</th><th>Username</th><th>Name</th><th>Email</th><th>Actions</th>
        </tr>
        <c:forEach var="c" items="${customers}">
            <tr>
                <td>${c.id}</td>
                <td>${c.username}</td>
                <td>${c.firstName} ${c.lastName}</td>
                <td>${c.email}</td>
                <td>
                    <form method="post" action="DeleteCustomerServlet" style="display:inline;">
                        <input type="hidden" name="customerId" value="${c.id}" />
                        <button type="submit">Delete</button>
                    </form>
                    <a href="EditCustomerServlet?customerId=${c.id}">Edit</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    
    
    <script>
  const btn = document.getElementById('themeBtn');
  btn.addEventListener('click', () => {
    document.body.classList.toggle('dark');
    btn.textContent = document.body.classList.contains('dark') ? '☀️' : '🌙';
  });
</script>
</body>


</html>
