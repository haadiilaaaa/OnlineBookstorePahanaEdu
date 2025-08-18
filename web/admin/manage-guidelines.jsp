<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Guidelines</title>
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

    .page-wrapper {
        max-width: 1100px;
        margin: 0 auto;
        padding: 36px 24px 60px;
    }

    .header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 12px;
        margin-bottom: 32px;
    }
    .header h2 {
        font-size: 1.9rem;
        font-weight: 800;
        margin: 0;
        position: relative;
        color: var(--gradient-start);
        background: linear-gradient(135deg, var(--gradient-start), var(--gradient-end));
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
    }
    .theme-toggle {
        background: none;
        border: 2px solid var(--gradient-start);
        padding: 10px 16px;
        border-radius: 999px;
        cursor: pointer;
        font-weight: 600;
        transition: all var(--transition);
        display: flex;
        align-items: center;
        gap: 6px;
    }
    .theme-toggle:hover {
        background: var(--gradient-end);
        color: white;
        border-color: var(--gradient-end);
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
    }
    thead th {
        background: rgba(109,76,65,0.08);
        position: sticky;
        top: 0;
        padding: 14px 16px;
        text-align: left;
        font-weight: 700;
        letter-spacing: 0.05em;
        font-size: 0.9rem;
        border-bottom: 2px solid rgba(109,76,65,0.15);
    }
    tbody tr {
        background: white;
        transition: transform var(--transition), box-shadow var(--transition);
        border-radius: 8px;
        overflow: hidden;
        position: relative;
        margin-bottom: 8px;
        display: table;
        width: 100%;
    }
    body.dark tbody tr {
        background: #2f2723;
    }
    tbody tr:hover {
        transform: translateY(-2px);
        box-shadow: 0 12px 24px rgba(109,76,65,0.25);
    }
    td {
        padding: 14px 16px;
        vertical-align: top;
        border-bottom: 1px solid rgba(109,76,65,0.08);
        font-size: 0.9rem;
    }

    label {
        font-weight: 600;
        display: block;
        margin-bottom: 6px;
        font-size: 0.95rem;
    }

    input[type="text"],
    textarea {
        width: 100%;
        padding: 12px 14px;
        font-size: 0.95rem;
        border: 1px solid rgba(109,76,65,0.3);
        border-radius: 8px;
        background: #fff;
        transition: border var(--transition), background var(--transition);
        resize: vertical;
        font-family: inherit;
    }
    body.dark input[type="text"],
    body.dark textarea {
        background: #3b2f2a;
        border: 1px solid rgba(255,255,255,0.08);
        color: var(--text-color-light);
    }

    button {
        padding: 12px 22px;
        font-size: 0.95rem;
        font-weight: 700;
        border: none;
        border-radius: var(--btn-radius);
        cursor: pointer;
        transition: all var(--transition);
        background: linear-gradient(135deg, var(--gradient-start), var(--gradient-end));
        color: white;
        display: inline-flex;
        align-items: center;
        gap: 6px;
        letter-spacing: 0.05em;
    }
    button:hover, button:focus {
        filter: brightness(1.05);
        transform: translateY(-1px);
        outline: none;
    }

    .small-btn {
        padding: 8px 16px;
        font-size: 0.8rem;
        border-radius: 999px;
    }

    .form-section {
        display: grid;
        gap: 18px;
    }

    .flex-row {
        display: flex;
        gap: 16px;
        flex-wrap: wrap;
    }

    .actions-row {
        display: flex;
        gap: 12px;
        flex-wrap: wrap;
        margin-top: 6px;
    }

    .divider {
        height: 1px;
        background: rgba(109,76,65,0.1);
        margin: 32px 0;
        border-radius: 2px;
    }

    .note {
        font-size: 0.8rem;
        color: rgba(0,0,0,0.6);
        margin-top: 4px;
    }
</style>

</head>
<body>
    
    <!-- Display success/error messages -->
<c:if test="${not empty successMessage}">
    <div style="background-color: #d4edda; color: #155724; padding: 12px 16px; border-radius: 8px; margin-bottom: 16px; border: 1px solid #c3e6cb;">
        ${successMessage}  
    </div>
</c:if>   

<c:if test="${not empty errorMessage}">
    <div style="background-color: #f8d7da; color: #721c24; padding: 12px 16px; border-radius: 8px; margin-bottom: 16px; border: 1px solid #f5c6cb;">
        ${errorMessage}
    </div>
</c:if>


<div class="header">
    <h2>Manage Guidelines</h2>
    <button class="theme-toggle" id="themeBtn" aria-label="Toggle theme">🌙 Dark Mode</button>
</div>


<!-- Guidelines Table -->
<table>
    <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Content</th>
        <th>Action</th>
    </tr>
    <c:forEach var="g" items="${guidelines}">
        <tr>
            <td>${g.id}</td>
            <td>${g.title}</td>
            <td>${g.content}</td>
           <td>
    <form method="get" action="EditGuidelinesServlet" style="display:inline;">
        <input type="hidden" name="id" value="${g.id}" />
        <button type="submit">Edit</button>
    </form>

    <form method="post" action="DeleteGuidelineServlet" style="display:inline;">
        <input type="hidden" name="id" value="${g.id}" />
        <button type="submit" onclick="return confirm('Are you sure you want to delete this guideline?');">Delete</button>
    </form>
</td>
        </tr>
    </c:forEach>
</table>

<!-- Add New Guideline Form -->
<h3>Add New Guideline</h3>
<form method="post" action="AddGuidelinesServlet">
    <label for="title">Title:</label><br>
    <input type="text" id="title" name="title" required placeholder="Enter guideline title here..." /><br><br>
    <label for="content">Content:</label><br>
    <textarea id="content" name="content" rows="4" required placeholder="Enter guideline content here..."></textarea><br>
    <button type="submit">Add Guideline</button>
</form>


<script>
    const themeBtn = document.getElementById('themeBtn');
    const body = document.body;
    function updateBtn() {
        themeBtn.textContent = body.classList.contains('dark') ? '☀️ Light Mode' : '🌙 Dark Mode';
    }
    themeBtn.addEventListener('click', () => {
        body.classList.toggle('dark');
        updateBtn();
    });
    updateBtn();
</script>

</body>
</html>
