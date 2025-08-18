<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Edit Customer</title>
</head>   
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
        line-height: 1.4;
    }
    body.dark {
        background: #1e1e1e;
        color: var(--text-color-light);
    }

    .wrapper {
        max-width: 900px;
        margin: 0 auto;
        padding: 40px 24px;
    }

    .card {
        background: var(--card-bg);
        border-radius: var(--radius);
        padding: 32px 36px;
        box-shadow: var(--shadow-light);
        transition: background var(--transition), box-shadow var(--transition);
    }
    body.dark .card {
        background: var(--card-bg-dark);
        box-shadow: var(--shadow-dark);
    }

    h2 {
        font-size: 1.8rem;
        margin: 0 0 16px;
        font-weight: 800;
        background: linear-gradient(135deg, var(--gradient-start), var(--gradient-end));
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
    }

    form {
        display: grid;
        gap: 18px;
        margin-top: 8px;
    }

    .field-group {
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
    textarea {
        padding: 12px 14px;
        font-size: 0.95rem;
        border: 1px solid rgba(109,76,65,0.3);
        border-radius: 8px;
        background: #fff;
        transition: border var(--transition), background var(--transition);
        font-family: inherit;
        resize: vertical;
    }
    body.dark input[type="text"],
    body.dark input[type="email"],
    body.dark textarea {
        background: #3b2f2a;
        border: 1px solid rgba(255,255,255,0.08);
        color: var(--text-color-light);
    }

    textarea {
        min-height: 100px;
    }

    .actions {
        display: flex;
        gap: 14px;
        flex-wrap: wrap;
        align-items: center;
        margin-top: 8px;
    }

    button {
        padding: 12px 24px;
        font-size: 0.9rem;
        font-weight: 700;
        border: none;
        border-radius: var(--btn-radius);
        cursor: pointer;
        background: linear-gradient(135deg, var(--gradient-start), var(--gradient-end));
        color: white;
        transition: all var(--transition);
        display: inline-flex;
        align-items: center;
        gap: 6px;
    }
    button:hover, button:focus {
        filter: brightness(1.05);
        transform: translateY(-1px);
        outline: none;
    }

    a {
        text-decoration: none;
        font-weight: 600;
        font-size: 0.9rem;
        padding: 12px 20px;
        border-radius: var(--btn-radius);
        border: 2px solid var(--gradient-start);
        transition: all var(--transition);
        color: var(--gradient-start);
        display: inline-block;
    }
    a:hover {
        background: var(--gradient-end);
        color: white;
        border-color: var(--gradient-end);
    }

    .error {
        background: rgba(255, 235, 235, 0.9);
        border: 1px solid #e07a7a;
        padding: 12px 16px;
        border-radius: 8px;
        color: #a82f2f;
        font-weight: 600;
        margin-bottom: 6px;
    }

    .note {
        font-size: 0.8rem;
        color: rgba(0,0,0,0.6);
    }
</style>

<body>
    <div class="wrapper">
  <div class="card">
    <h2>Edit Customer</h2>

    <c:if test="${not empty error}">
      <div class="error">${error}</div>
    </c:if>

    <form method="post" action="EditCustomerServlet">
      <input type="hidden" name="id" value="${customer.id}" />

      <div class="field-group">
        <label>Username:</label>
        <input type="text" name="username" value="${customer.username}" readonly />
      </div>

      <div class="field-group">
        <label>First Name:</label>
        <input type="text" name="firstName" value="${customer.firstName}" required />
      </div>

      <div class="field-group">
        <label>Last Name:</label>
        <input type="text" name="lastName" value="${customer.lastName}" required />
      </div>

      <div class="field-group">
        <label>Email:</label>
        <input type="email" name="email" value="${customer.email}" required />
      </div>

      <div class="field-group">
        <label>Contact Number:</label>
        <input type="text" name="contactNumber" value="${customer.contactNumber}" />
      </div>

      <div class="field-group">
        <label>Address:</label>
        <textarea name="address">${customer.address}</textarea>
      </div>

      <div class="actions">
        <button type="submit">Update Customer</button>
        <a href="AdminManageUsersServlet">Cancel</a>
      </div>
    </form>
  </div>
</div>

</body>
</html>
