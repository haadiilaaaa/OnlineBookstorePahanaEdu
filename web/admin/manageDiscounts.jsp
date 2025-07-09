<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Manage Discounts</title>
    <style>
      :root {
  --brown-dark: #5d4037;
  --brown-light: #8d6e63;
  --cream-bg: #f9f6f1;
  --dark-bg: #252525;
  --text-dark: #212121;
  --text-light: #f0f0f0;
  --highlight: #ffd54f;
  --accent: #ffb300;
  --error: #e53935;
  --success: #43a047;
  --border-radius: 14px;
  --shadow-light: 0 6px 12px rgba(0,0,0,0.12);
  --shadow-hover: 0 10px 22px rgba(0,0,0,0.2);
  --font-main: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  --transition-fast: 0.25s ease;
  --input-bg: #fefefe;
  --input-shadow: inset 6px 6px 12px #d1cfcf, inset -6px -6px 12px #ffffff;
  --input-shadow-focus: inset 2px 2px 5px #b0a9a9, inset -2px -2px 5px #ffffff;
}

/* Dark mode overrides */
body.dark-mode {
  background: var(--dark-bg);
  color: var(--text-light);
  user-select: none;
}
body.dark-mode h1, body.dark-mode h2 {
  color: var(--accent);
  border-left-color: var(--accent);
  text-shadow: 1px 1px 2px #0008;
}
body.dark-mode .section {
  background: #2a2a2a;
  box-shadow:
    8px 8px 16px #1f1f1f,
    -8px -8px 16px #363636;
}
body.dark-mode .section:hover {
  box-shadow:
    12px 12px 24px #141414,
    -12px -12px 24px #484848;
}
body.dark-mode input[type="text"],
body.dark-mode input[type="number"],
body.dark-mode input[type="date"],
body.dark-mode select,
body.dark-mode textarea {
  background: #444;
  color: var(--text-light);
  box-shadow: inset 6px 6px 12px #3a3a3a, inset -6px -6px 12px #4d4d4d;
}
body.dark-mode input[type="text"]:focus,
body.dark-mode input[type="number"]:focus,
body.dark-mode input[type="date"]:focus,
body.dark-mode select:focus,
body.dark-mode textarea:focus {
  background-color: #554d26;
  box-shadow: inset 2px 2px 5px #6a6938, inset -2px -2px 5px #8c853e;
  outline: none;
}
body.dark-mode input[disabled] {
  background-color: #555;
  color: #bbb;
  box-shadow: inset 4px 4px 8px #444, inset -4px -4px 8px #666;
}
body.dark-mode input[type="submit"],
body.dark-mode button {
  background: linear-gradient(145deg, var(--accent), var(--brown-dark));
  box-shadow:
    4px 4px 8px #886701,
    -4px -4px 8px #ffcd3c;
  color: #1f1f1f;
}
body.dark-mode input[type="submit"]:hover,
body.dark-mode button:hover,
body.dark-mode input[type="submit"]:focus,
body.dark-mode button:focus {
  background: linear-gradient(145deg, var(--brown-dark), var(--accent));
  box-shadow:
    2px 2px 5px #664f00,
    -2px -2px 5px #ffef7d;
  outline: none;
  transform: scale(1.07);
}
body.dark-mode table {
  background: #1e1e1e;
  box-shadow:
    6px 6px 14px #121212,
    -6px -6px 14px #2b2b2b;
  color: var(--text-light);
}
body.dark-mode thead tr {
  background-color: var(--accent);
  color: var(--dark-bg);
}
body.dark-mode tbody tr {
  background-color: #2b2b2b;
  box-shadow:
    inset 4px 4px 8px #1b1b1b,
    inset -4px -4px 8px #393939;
}
body.dark-mode tbody tr:nth-child(odd) {
  background-color: #222222;
}
body.dark-mode tbody tr:hover {
  background-color: var(--highlight);
  color: var(--text-dark);
  box-shadow:
    0 4px 20px var(--highlight);
}
body.dark-mode ul li:hover {
  background-color: #5e5400;
}
body.dark-mode .success {
  background: #2e462e;
  border-left-color: var(--success);
  box-shadow: 4px 4px 12px #2f552f;
  color: var(--success);
}
body.dark-mode .error {
  background: #4a2222;
  border-left-color: var(--error);
  box-shadow: 4px 4px 12px #6b2d2d;
  color: var(--error);
}

/* Global Reset */
* {
  box-sizing: border-box;
}

body {
  font-family: var(--font-main);
  background: var(--cream-bg);
  color: var(--text-dark);
  margin: 0;
  padding: 2rem 3rem;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  user-select: none;
}

/* Headings */
h1, h2 {
  color: var(--brown-dark);
  border-left: 6px solid var(--brown-dark);
  padding-left: 1rem;
  margin-bottom: 2rem;
  font-weight: 900;
  letter-spacing: 0.07em;
  text-transform: uppercase;
  text-shadow: 1px 1px 2px rgba(93,64,55,0.25);
  user-select: text;
}

/* Section containers */
.section {
  background: var(--cream-bg);
  border-radius: var(--border-radius);
  padding: 2rem 2.5rem;
  box-shadow:
    8px 8px 16px #c6c3c3,
    -8px -8px 16px #ffffff;
  margin-bottom: 3rem;
  transition: box-shadow var(--transition-fast);
}

.section:hover {
  box-shadow:
    12px 12px 24px #b2adad,
    -12px -12px 24px #ffffff;
}

/* Form elements: neumorphic style */
input[type="text"],
input[type="number"],
input[type="date"],
select,
textarea {
  width: 100%;
  padding: 0.75rem 1.25rem;
  font-size: 1.1rem;
  border: none;
  border-radius: var(--border-radius);
  background: var(--input-bg);
  box-shadow: var(--input-shadow);
  margin: 8px 0 1.5rem 0;
  transition: box-shadow var(--transition-fast), background-color var(--transition-fast);
  font-family: var(--font-main);
  color: var(--text-dark);
  resize: vertical;
  user-select: text;
}

input[type="text"]:focus,
input[type="number"]:focus,
input[type="date"]:focus,
select:focus,
textarea:focus {
  box-shadow: var(--input-shadow-focus);
  outline: none;
  background-color: #fff9e6;
}

/* Disabled inputs for item/category ID */
input[disabled] {
  background-color: #f0f0f0;
  color: #999;
  cursor: not-allowed;
  box-shadow: inset 4px 4px 8px #d0cdcd, inset -4px -4px 8px #ffffff;
}

/* Buttons: neumorphic gradient */
input[type="submit"], button {
  background: linear-gradient(145deg, var(--brown-light), var(--brown-dark));
  color: white;
  font-weight: 700;
  font-size: 1.1rem;
  padding: 0.85rem 2.5rem;
  border-radius: var(--border-radius);
  border: none;
  cursor: pointer;
  box-shadow:
    4px 4px 8px #b0a1a1,
    -4px -4px 8px #fffaf8;
  transition: background-color var(--transition-fast), box-shadow var(--transition-fast), transform 0.2s ease;
  user-select: none;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  margin-top: 0.3rem;
}

input[type="submit"]:hover,
button:hover,
input[type="submit"]:focus,
button:focus {
  background: linear-gradient(145deg, var(--brown-dark), var(--brown-light));
  box-shadow:
    2px 2px 5px #8a6c6c,
    -2px -2px 5px #fff7f2;
  outline: none;
  transform: scale(1.07);
}

/* Tables with zebra stripes and smooth shadows */
table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0 12px;
  font-size: 1rem;
  box-shadow:
    6px 6px 14px #cfcaca,
    -6px -6px 14px #ffffff;
  border-radius: var(--border-radius);
  overflow: hidden;
  background: var(--cream-bg);
  user-select: none;
  margin-top: 2rem;
}

thead tr {
  background-color: var(--brown-dark);
  color: white;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

th, td {
  padding: 1.2rem 1.7rem;
  text-align: left;
  vertical-align: middle;
  border: none;
}

tbody tr {
  background-color: white;
  border-radius: var(--border-radius);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: default;
  box-shadow:
    inset 4px 4px 8px #e0dcdc,
    inset -4px -4px 8px #ffffff;
}

tbody tr:nth-child(odd) {
  background-color: #faf8f7;
}

tbody tr:hover {
  background-color: var(--highlight);
  transform: translateX(8px);
  box-shadow:
    0 4px 20px var(--highlight);
  color: var(--text-dark);
}

/* Lists inside discount assignments */
ul {
  list-style-type: none;
  padding-left: 0;
  margin-top: 0.5rem;
}

ul li {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.4rem 0.3rem;
  font-weight: 600;
  border-radius: 8px;
  transition: background-color 0.3s;
  user-select: text;
}

ul li:hover {
  background-color: #fffbdb;
}

/* Inline form buttons in list */
ul li form {
  margin-left: 1rem;
  display: inline-block;
}

ul li form input[type="submit"] {
  padding: 0.3rem 0.7rem;
  font-size: 0.85rem;
  border-radius: 14px;
  box-shadow: none;
  background: var(--error);
  border: none;
  color: white;
  cursor: pointer;
  transition: background-color var(--transition-fast);
  letter-spacing: normal;
  text-transform: none;
}

ul li form input[type="submit"]:hover {
  background-color: #a52727;
}

/* Success & Error messages */
.success {
  color: var(--success);
  font-weight: 700;
  padding: 1.2rem 1.8rem;
  border-left: 6px solid var(--success);
  background: #e6f4ea;
  margin-bottom: 2rem;
  border-radius: var(--border-radius);
  box-shadow:
    4px 4px 12px #b7d8b2;
  user-select: text;
}

.error {
  color: var(--error);
  font-weight: 700;
  padding: 1.2rem 1.8rem;
  border-left: 6px solid var(--error);
  background: #fdecea;
  margin-bottom: 2rem;
  border-radius: var(--border-radius);
  box-shadow:
    4px 4px 12px #f1a6a3;
  user-select: text;
}

/* Toast notification with bounce */
@keyframes bounceInUp {
  0% {
    transform: translateY(100%) scale(0.9);
    opacity: 0;
  }
  60% {
    transform: translateY(-15px) scale(1.05);
    opacity: 1;
  }
  80% {
    transform: translateY(10px) scale(0.95);
  }
  100% {
    transform: translateY(0) scale(1);
  }
}

.toast {
  visibility: hidden;
  min-width: 260px;
  max-width: 90vw;
  margin-left: -130px;
  background-color: var(--brown-dark);
  color: #fff;
  text-align: center;
  border-radius: var(--border-radius);
  padding: 18px 28px;
  position: fixed;
  left: 50%;
  bottom: 30px;
  font-size: 1.1rem;
  z-index: 9999;
  opacity: 0;
  box-shadow: 0 8px 22px rgba(0, 0, 0, 0.28);
  font-weight: 700;
  letter-spacing: 0.04em;
  user-select: none;
  transition: opacity 0.5s ease-in-out, visibility 0.5s;
}

.toast.show {
  visibility: visible;
  opacity: 1;
  animation: bounceInUp 0.6s ease forwards;
}

/* Responsive */
@media (max-width: 720px) {
  body {
    padding: 1.5rem 1rem;
  }

  .section {
    padding: 1.5rem 1.8rem;
  }

  input[type="text"],
  input[type="number"],
  input[type="date"],
  select,
  textarea,
  input[type="submit"],
  button {
    width: 100% !important;
    max-width: 100% !important;
    margin-bottom: 1.3rem !important;
  }

  ul li {
    flex-direction: column;
    align-items: flex-start;
  }

  ul li form {
    margin-left: 0;
    margin-top: 0.6rem;
  }

  table {
    font-size: 0.9rem;
  }
}

/* Dark/Light mode toggle button */
.dark-mode-toggle {
  position: fixed;
  top: 1rem;
  right: 1rem;
  width: 60px;
  height: 30px;
  background: var(--brown-dark);
  border-radius: 15px;
  box-shadow: 0 5px 12px rgba(93, 64, 55, 0.6);
  cursor: pointer;
  user-select: none;
  display: flex;
  align-items: center;
  padding: 0 5px;
  transition: background 0.3s ease;
  z-index: 10000;
}
body.dark-mode .dark-mode-toggle {
  background: var(--accent);
  box-shadow: 0 5px 12px rgba(255 183 0, 0.6);
}
.toggle-knob {
  width: 24px;
  height: 24px;
  background: white;
  border-radius: 50%;
  box-shadow: 0 1px 5px rgba(0,0,0,0.2);
  position: relative;
  transition: transform 0.3s ease;
}
.toggle-knob.sun {
  transform: translateX(30px);
}
.toggle-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  pointer-events: none;
  font-size: 16px;
  color: #ffb300;
  user-select: none;
  line-height: 1;
}
.toggle-icon.moon {
  color: #fff;
}
    </style>
</head>
<body>
<!-- Dark/Light mode toggle button -->
<div class="dark-mode-toggle" role="button" aria-label="Toggle dark mode" tabindex="0" onclick="toggleDarkMode()" onkeydown="if(event.key==='Enter') toggleDarkMode();">
  <div class="toggle-knob" id="toggleKnob"></div>
  <div class="toggle-icon moon" id="iconMoon" style="left: 12px;">🌙</div>
  <div class="toggle-icon sun" id="iconSun" style="left: 48px;">☀️</div>
</div>

<c:if test="${not empty error}">
    <p class="error">${error}</p>
</c:if>

<c:if test="${not empty sessionScope.successMessage}">
    <div id="toast" class="toast">${sessionScope.successMessage}</div>
    <script>
        const toast = document.getElementById('toast');
        toast.classList.add('show');
        setTimeout(() => {
            toast.classList.remove('show');
        }, 3000);
    </script>
    <c:remove var="successMessage" scope="session" />
</c:if>



<h1>Admin - Manage Discounts</h1>

<div class="section">
    <h2> Create a New Discount</h2>
    <form action="ManageDiscountServlet" method="post">
        <input type="hidden" name="action" value="create" />

        <label>Name</label>
        <input type="text" name="name" required />

        <label>Description</label>
        <textarea name="description" placeholder="E.g. 10% off all snacks..."></textarea>

        <div style="display: flex; gap: 1rem;">
            <div style="flex: 1;">
                <label>Percent (%)</label>
                <input type="number" name="percent" step="0.01" required />
            </div>
            <div style="flex: 1;">
                <label>Start Date</label>
                <input type="date" name="start" required />
            </div>
            <div style="flex: 1;">
                <label>End Date</label>
                <input type="date" name="end" required />
            </div>
        </div>

        <label>Status</label>
        <select name="active">
            <option value="true">✅ Active</option>
            <option value="false">🚫 Inactive</option>
        </select>

        <input type="submit" value=" Create Discount" />
    </form>
</div>

<div class="section">
    <h2> Assign Discount</h2>
    <form action="ManageDiscountServlet" method="post">
        <input type="hidden" name="action" value="assign" />

        <label>Discount</label>
        <select name="discountId">
            <c:forEach var="d" items="${discounts}">
                <option value="${d.id}">${d.name}</option>
            </c:forEach>
        </select>

        <label>Assignment Type</label>
        <select name="type" onchange="toggleFields(this.value)">
            <option value="ITEM">🛒 Item</option>
            <option value="CATEGORY">📚 Category</option>
            <option value="ALL">🌐 All Items</option>
        </select>

        <label>Item ID</label>
        <input type="text" name="itemId" id="itemField" />

        <label>Category ID</label>
        <input type="text" name="categoryId" id="catField" />

        <input type="submit" value="✅ Assign Discount" />
    </form>
</div>


<script>
    function toggleFields(value) {
        document.getElementById("itemField").disabled = (value !== "ITEM");
        document.getElementById("catField").disabled = (value !== "CATEGORY");
    }
    window.onload = function() {
        toggleFields(document.querySelector("select[name='type']").value);

        // Initialize dark mode based on localStorage
        if(localStorage.getItem('darkMode') === 'true') {
            document.body.classList.add('dark-mode');
            document.getElementById('toggleKnob').classList.add('sun');
        }
    };

    function toggleDarkMode() {
        const body = document.body;
        const knob = document.getElementById('toggleKnob');
        if(body.classList.contains('dark-mode')) {
            body.classList.remove('dark-mode');
            knob.classList.remove('sun');
            localStorage.setItem('darkMode', 'false');
        } else {
            body.classList.add('dark-mode');
            knob.classList.add('sun');
            localStorage.setItem('darkMode', 'true');
        }
    }
</script>

<h2>? Existing Discounts</h2>
<c:forEach var="d" items="${discounts}">
    <div class="section" style="border-left: 6px solid var(--brown-dark);">
        <h3 style="margin-bottom: 0.5rem;">
            <span style="font-size: 1.4rem;">? ${d.name}</span>
            <span style="float: right; background: var(--highlight); padding: 4px 10px; border-radius: 12px; font-size: 0.9rem;">
                ${d.discountPercent}% OFF
            </span>
        </h3>

        <p><strong>🗓 ${d.startDate}</strong> → <strong>${d.endDate}</strong></p>
        <p><em>${d.description}</em></p>
        <p>Status: 
            <c:choose>
                <c:when test="${d.active}">✅ <span style="color: green;">Active</span></c:when>
                <c:otherwise>❌ <span style="color: red;">Inactive</span></c:otherwise>
            </c:choose>
        </p>

        <div>
            <strong>?Assignments:</strong>
            <ul>
                <c:forEach var="a" items="${discountAssignmentsMap[d.id]}">
                    <li>
                        <c:choose>
                            <c:when test="${a.type == 'ITEM'}">🛍️ Item: ${a.displayName} (${a.itemId})</c:when>
                            <c:when test="${a.type == 'CATEGORY'}">📁 Category: ${a.displayName} (${a.categoryId})</c:when>
                            <c:when test="${a.type == 'ALL'}">🌐 All Items</c:when>
                        </c:choose>

                        <form action="ManageDiscountServlet" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to remove this assignment?');">
                            <input type="hidden" name="action" value="removeAssignment" />
                            <input type="hidden" name="assignmentId" value="${a.id}" />
                            <input type="submit" value="❌ Remove" />
                        </form>
                    </li>
                </c:forEach>
                <c:if test="${empty discountAssignmentsMap[d.id]}">
                    <li>No assignments yet</li>
                </c:if>
            </ul>
        </div>
    </div>
</c:forEach>

<script>
function confirmDelete() {
    return confirm("Are you sure you want to delete this discount?");
}
</script>

</body>
</html>
