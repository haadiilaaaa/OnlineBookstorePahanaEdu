<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Delivery Partner Registration</title>
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
        max-width: 600px;
        margin: 0 auto;
        padding: 48px 24px;
    }

    .card {
        background: var(--card-bg);
        border-radius: var(--radius);
        padding: 36px 32px;
        box-shadow: var(--shadow-light);
        transition: background var(--transition), box-shadow var(--transition);
    }
    body.dark .card {
        background: var(--card-bg-dark);
        box-shadow: var(--shadow-dark);
    }

    h2 {
        margin: 0 0 18px;
        font-size: 1.9rem;
        font-weight: 800;
        background: linear-gradient(135deg, var(--gradient-start), var(--gradient-end));
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
    }

    form {
        display: grid;
        gap: 16px;
        margin-top: 4px;
    }

    .field {
        display: flex;
        flex-direction: column;
        gap: 6px;
    }

    label {
        font-weight: 600;
        font-size: 0.95rem;
    }

    input[type="text"],
    input[type="email"],
    input[type="password"],
    input[type="tel"] {
        padding: 12px 14px;
        font-size: 0.95rem;
        border: 1px solid rgba(109,76,65,0.3);
        border-radius: 8px;
        background: #fff;
        transition: border var(--transition), background var(--transition);
        font-family: inherit;
    }
    body.dark input[type="text"],
    body.dark input[type="email"],
    body.dark input[type="password"],
    body.dark input[type="tel"] {
        background: #3b2f2a;
        border: 1px solid rgba(255,255,255,0.08);
        color: var(--text-color-light);
    }

    button {
        padding: 14px 24px;
        font-size: 0.95rem;
        font-weight: 700;
        border: none;
        border-radius: var(--btn-radius);
        cursor: pointer;
        background: linear-gradient(135deg, var(--gradient-start), var(--gradient-end));
        color: white;
        transition: all var(--transition);
        width: 100%;
        letter-spacing: 0.05em;
    }
    button:hover, button:focus {
        filter: brightness(1.05);
        transform: translateY(-1px);
        outline: none;
    }

    .messages {
        margin-top: 12px;
        display: flex;
        flex-direction: column;
        gap: 6px;
    }
    .error-message, .error {
        background: rgba(255, 235, 235, 0.9);
        border: 1px solid #e07a7a;
        padding: 12px 16px;
        border-radius: 8px;
        color: #a82f2f;
        font-weight: 600;
    }
    .success-message, .success {
        background: rgba(220, 255, 220, 0.9);
        border: 1px solid #55a654;
        padding: 12px 16px;
        border-radius: 8px;
        color: #2f6f32;
        font-weight: 600;
    }

    .small-note {
        font-size: 0.8rem;
        color: rgba(0,0,0,0.6);
    }
</style>

</head>
<body>

<div class="wrapper">
  <div class="card">
    <h2>Register as Delivery Partner</h2>

    <form action="${pageContext.request.contextPath}/RegisterServlet" method="post">
      <input type="hidden" name="userType" value="delivery" />

      <div class="field">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required maxlength="50" />
      </div>

      <div class="field">
        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" name="firstName" required maxlength="50" />
      </div>

      <div class="field">
        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" name="lastName" required maxlength="50" />
      </div>

      <div class="field">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required maxlength="100" />
      </div>

      <div class="field">
        <label for="contactNumber">Contact Number:</label>
        <input type="tel" id="contactNumber" name="contactNumber" required maxlength="15" pattern="[0-9]+"
               title="Only digits allowed" />
      </div>

      <div class="field">
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required minlength="6" />
      </div>

      <div class="field">
        <label for="confirmPassword">Confirm Password:</label>
        <input type="password" id="confirmPassword" name="confirmPassword" required minlength="6" />
      </div>

      <div class="field">
        <label for="vehicleNumber">Vehicle Number:</label>
        <input type="text" id="vehicleNumber" name="vehicleNumber" required maxlength="20" />
      </div>

      <button type="submit">Register</button>
    </form>

    <div class="messages">
      <c:if test="${not empty error}">
        <div class="error">${error}</div>
      </c:if>
      <c:if test="${not empty success}">
        <div class="success">${success}</div>
      </c:if>
    </div>
  </div>
</div>


</body>
</html>
