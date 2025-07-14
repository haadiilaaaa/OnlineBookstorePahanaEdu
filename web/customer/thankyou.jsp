<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page import="static util.contannts.PagePaths.LOGIN_PAGE" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + LOGIN_PAGE);
        return;
    }
%>
<html>
<head>
    <title>Thank You for Your Order</title>
    <style>
       :root {
  --primary-dark: #5a3e36;
  --primary-light: #a07f72;
  --primary-gradient-start: #7a5c51;
  --primary-gradient-end: #b5988c;
  --background-gradient: linear-gradient(135deg, #fefefe, #e6d6ca);
  --glass-bg: rgba(255, 255, 255, 0.85);
  --shadow-light: rgba(90, 62, 54, 0.15);
  --shadow-strong: rgba(90, 62, 54, 0.3);
  --text-dark: #3f2f2a;
  --text-muted: #6e5a54;
  --border-radius: 20px;
  --transition-speed: 0.35s;
  --font-primary: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

body {
  font-family: var(--font-primary);
  background: var(--background-gradient);
  margin: 0;
  padding: 40px 20px;
  color: var(--text-dark);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  display: flex;
  justify-content: center;
  min-height: 100vh;
  align-items: flex-start;
}

.container {
  background: var(--glass-bg);
  padding: 30px 40px;
  border-radius: var(--border-radius);
  box-shadow: 0 10px 25px var(--shadow-strong);
  max-width: 700px;
  width: 100%;
  user-select: none;
  text-align: center;
  animation: fadeInUp 0.6s ease forwards;
}

h2 {
  font-weight: 700;
  font-size: 2.2rem;
  margin-bottom: 20px;
  color: var(--primary-dark);
  letter-spacing: 0.06em;
  text-transform: uppercase;
  user-select: text;
}

.invoice-wrapper {
  background: #fff;
  border-radius: 18px;
  padding: 25px 30px;
  box-shadow: 0 6px 18px var(--shadow-light);
  margin-bottom: 30px;
  overflow-x: auto;
  user-select: text;
  font-size: 1rem;
  color: var(--primary-dark);
}

.invoice-wrapper table {
  border-collapse: separate;
  border-spacing: 0 12px;
  width: 100%;
  table-layout: fixed;
}

.invoice-wrapper th {
  background: var(--primary-gradient-start);
  color: white;
  font-weight: 600;
  padding: 14px 12px;
  text-transform: uppercase;
  font-size: 0.9rem;
  letter-spacing: 0.05em;
  user-select: text;
  border-radius: 12px 12px 0 0;
}

.invoice-wrapper td {
  background: #f9f4ef;
  padding: 14px 12px;
  font-weight: 500;
  border-radius: 12px;
  color: var(--primary-dark);
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

.invoice-wrapper tbody tr:hover td {
  background: var(--primary-light);
  color: white;
  font-weight: 700;
  transition: background 0.3s ease;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 25px;
  flex-wrap: wrap;
}

.btn {
  background: linear-gradient(135deg, var(--primary-gradient-end), var(--primary-gradient-start));
  color: white;
  font-weight: 700;
  border: none;
  padding: 14px 28px;
  border-radius: 50px;
  cursor: pointer;
  box-shadow: 0 6px 15px rgba(181, 150, 135, 0.6);
  font-size: 1.1rem;
  display: inline-flex;
  align-items: center;
  gap: 10px;
  transition: background var(--transition-speed), transform 0.2s ease;
  text-decoration: none;
  user-select: none;
}

.btn:hover {
  background: linear-gradient(135deg, var(--primary-gradient-start), var(--primary-gradient-end));
  transform: translateY(-3px);
  box-shadow: 0 12px 30px rgba(181, 150, 135, 0.8);
}

.btn i {
  font-size: 1.3rem;
  line-height: 1;
}

@keyframes fadeInUp {
  0% {
    opacity: 0;
    transform: translateY(20px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 480px) {
  .container {
    padding: 25px 20px;
  }

  h2 {
    font-size: 1.8rem;
  }

  .btn {
    width: 100%;
    justify-content: center;
  }

  .button-group {
    gap: 15px;
  }
}

    </style>
</head>
<body>
    
  <div class="container">
    <h2>Thank You for Your Order!</h2>

    <div class="invoice-wrapper">
      <c:choose>
  <c:when test="${not empty invoice}">
    <c:out value="${invoice}" escapeXml="false" />
  </c:when>
  <c:otherwise>
    <p>No invoice available.</p>
  </c:otherwise>
</c:choose>

    </div>

    <div class="button-group">
      <button class="btn" onclick="window.print()"><i class="fas fa-print"></i> Print Invoice</button>
      <a class="btn" href="<%= request.getContextPath() %>/Customer_DashboardServlet"><i class="fas fa-home"></i> Home</a>
      <a class="btn" href="<%= request.getContextPath() %>/BrowseBooksServlet"><i class="fas fa-book"></i> Keep Shopping</a>
    </div>
  </div>

<script>
window.onload = function() {
    var path = '<%= request.getAttribute("invoiceDownloadPath") %>';
    if (path) {
        const link = document.createElement('a');
        link.href = '<%= request.getContextPath() %>/' + path;
        link.download = 'Invoice_<%= ((dto.OrderDTO)request.getAttribute("order")) != null ? ((dto.OrderDTO)request.getAttribute("order")).getOrderId() : "" %>.pdf';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }
};
</script>

</body>
</html>
