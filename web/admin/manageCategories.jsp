<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Categories</title>
</head>
<style>
:root {
  --brown-dark: #5d4037;
  --brown-light: #8d6e63;
  --brown-gradient-start: #5d4037;
  --brown-gradient-end: #8d6e63;
  --cream-bg: #f9f6f1;
  --dark-bg: #252525;
  --text-dark: #212121;
  --text-light: #f0f0f0;
  --highlight: #ffd54f;
  --accent: #ffb300;
  --error: #e53935;
  --success: #43a047;
  --border-radius: 12px;
  --shadow-light: 0 8px 20px rgba(0, 0, 0, 0.12);
  --shadow-hover: 0 12px 28px rgba(0, 0, 0, 0.18);
  --font-main: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* Light Mode (default) */
body {
  font-family: var(--font-main);
  background: linear-gradient(135deg, var(--cream-bg) 0%, #f0ece8 100%);
  color: var(--text-dark);
  margin: 0;
  padding: 2rem 4%;
  transition: background-color 0.3s ease, color 0.3s ease;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

h2, h3 {
  color: var(--brown-dark);
  border-left: 6px solid var(--brown-gradient-start);
  padding-left: 1rem;
  margin-bottom: 1.5rem;
  font-weight: 700;
  letter-spacing: 0.04em;
  text-transform: uppercase;
  text-shadow: 1px 1px 1px rgba(93, 64, 55, 0.3);
}

h3 {
  margin-top: 3rem;
}

/* Error message styling */
p[style*="color: red"] {
  background: #ffebee;
  color: var(--error);
  border-left: 5px solid var(--error);
  padding: 1rem 1.5rem;
  border-radius: var(--border-radius);
  font-weight: 700;
  margin-bottom: 2.5rem;
  box-shadow: var(--shadow-light);
  letter-spacing: 0.03em;
}

/* Form styling */
form {
  background: white;
  padding: 2rem 3rem;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow-light);
  max-width: 720px;
  margin-bottom: 3rem;
  box-sizing: border-box;
  display: flex;
  flex-wrap: wrap;
  gap: 1.25rem;
  align-items: center;
  transition: box-shadow 0.3s ease;
}

form:hover {
  box-shadow: var(--shadow-hover);
}

form[action="ManageCategoriesServlet"][method="post"]:first-of-type input[type="text"] {
  flex: 1 1 260px;
  padding: 0.75rem 1.2rem;
  border: 2px solid #ccc;
  border-radius: var(--border-radius);
  font-size: 1.05rem;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
  box-shadow: inset 0 1px 3px #eee;
  letter-spacing: 0.02em;
  font-weight: 500;
}

form[action="ManageCategoriesServlet"][method="post"]:first-of-type input[type="text"]:focus {
  border-color: var(--brown-gradient-start);
  outline: none;
  box-shadow: 0 0 8px var(--brown-gradient-start);
}

form[action="ManageCategoriesServlet"][method="post"]:first-of-type button {
  background: linear-gradient(145deg, var(--brown-gradient-start), var(--brown-gradient-end));
  color: #fff;
  border: none;
  padding: 0.8rem 2.2rem;
  font-weight: 700;
  font-size: 1.1rem;
  border-radius: var(--border-radius);
  cursor: pointer;
  transition: background 0.4s ease, box-shadow 0.4s ease;
  flex-shrink: 0;
  box-shadow: 0 5px 12px rgba(93, 64, 55, 0.6);
  letter-spacing: 0.04em;
  text-transform: uppercase;
  user-select: none;
}

form[action="ManageCategoriesServlet"][method="post"]:first-of-type button:hover,
form[action="ManageCategoriesServlet"][method="post"]:first-of-type button:focus {
  background: linear-gradient(145deg, var(--brown-gradient-end), var(--brown-gradient-start));
  box-shadow: 0 8px 22px rgba(141, 110, 99, 0.9);
  outline: none;
}

/* Table styling */
table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0 14px;
  background: white;
  box-shadow: var(--shadow-light);
  border-radius: var(--border-radius);
  overflow: hidden;
  font-size: 1.05rem;
  transition: box-shadow 0.3s ease;
}

table:hover {
  box-shadow: var(--shadow-hover);
}

thead tr {
  background-color: var(--brown-gradient-start);
  color: white;
  font-weight: 700;
  text-align: left;
  border-radius: var(--border-radius) var(--border-radius) 0 0;
  letter-spacing: 0.05em;
  text-transform: uppercase;
}

th, td {
  padding: 1.2rem 1.5rem;
  vertical-align: middle;
  border-bottom: none;
}

tbody tr {
  background-color: var(--cream-bg);
  box-shadow: inset 0 0 6px #ddd;
  border-radius: var(--border-radius);
  transition: background-color 0.3s ease, transform 0.15s ease;
  cursor: default;
  user-select: none;
}

tbody tr:hover {
  background-color: var(--highlight);
  color: var(--text-dark);
  transform: scale(1.02);
  box-shadow: 0 0 12px var(--highlight);
}

td:nth-child(1), th:nth-child(1) {
  text-align: center;
  width: 60px;
}

td input[type="text"] {
  padding: 0.4rem 0.8rem;
  font-size: 1rem;
  border: 1.8px solid #ccc;
  border-radius: var(--border-radius);
  width: 140px;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
  letter-spacing: 0.02em;
}

td input[type="text"]:focus {
  border-color: var(--brown-gradient-start);
  outline: none;
  box-shadow: 0 0 8px var(--brown-gradient-start);
}

/* Buttons inside table */
td form {
  display: inline-block;
  margin: 0 6px;
}

td button {
  background: linear-gradient(145deg, var(--brown-gradient-start), var(--brown-gradient-end));
  border: none;
  color: #fff;
  padding: 0.45rem 1rem;
  border-radius: var(--border-radius);
  cursor: pointer;
  font-weight: 700;
  font-size: 0.95rem;
  transition: background 0.3s ease, box-shadow 0.3s ease, transform 0.15s ease;
  box-shadow: 0 3px 10px rgba(93, 64, 55, 0.6);
  user-select: none;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

td button:hover,
td button:focus {
  background: linear-gradient(145deg, var(--brown-gradient-end), var(--brown-gradient-start));
  box-shadow: 0 6px 18px rgba(141, 110, 99, 0.9);
  outline: none;
  transform: scale(1.05);
}

/* Responsive */
@media (max-width: 720px) {
  body {
    padding: 1rem;
  }
  form, table {
    width: 100%;
    overflow-x: auto;
  }
  form[action="ManageCategoriesServlet"][method="post"]:first-of-type {
    flex-direction: column;
    align-items: stretch;
  }
  form[action="ManageCategoriesServlet"][method="post"]:first-of-type input[type="text"] {
    width: 100%;
    flex-basis: auto;
  }
  td input[type="text"] {
    width: 100px;
  }
  td button {
    padding: 0.35rem 0.75rem;
    font-size: 0.85rem;
  }
}


  /* Dark mode styles */
    body.dark-mode {
      background: var(--dark-bg);
      color: var(--text-light);
    }
    body.dark-mode h2, body.dark-mode h3 {
      color: var(--accent);
      border-left-color: var(--accent);
      text-shadow: 1px 1px 3px #0008;
    }
    body.dark-mode form {
      background: #333;
      box-shadow: 0 8px 20px rgba(0,0,0,0.8);
    }
    body.dark-mode table {
      background: #2b2b2b;
      box-shadow: 0 8px 20px rgba(0,0,0,0.8);
      color: var(--text-light);
    }
    body.dark-mode thead tr {
      background-color: var(--accent);
      color: var(--dark-bg);
    }
    body.dark-mode tbody tr {
      background-color: #444;
      box-shadow: inset 0 0 6px #222;
    }
    body.dark-mode tbody tr:hover {
      background-color: var(--highlight);
      color: var(--text-dark);
      box-shadow: 0 0 12px var(--highlight);
    }
    body.dark-mode td input[type="text"] {
      background: #555;
      color: var(--text-light);
      border-color: var(--accent);
      box-shadow: inset 0 1px 3px #222;
    }
    body.dark-mode td input[type="text"]:focus {
      border-color: var(--highlight);
      box-shadow: 0 0 8px var(--highlight);
      outline: none;
    }
    body.dark-mode td button {
      background: var(--accent);
      color: var(--dark-bg);
      box-shadow: 0 3px 10px rgba(255 183 0, 0.6);
    }
    body.dark-mode td button:hover,
    body.dark-mode td button:focus {
      background: var(--highlight);
      box-shadow: 0 6px 18px rgba(255 183 0, 0.9);
      outline: none;
      transform: scale(1.05);
    }

    /* Dark mode error message */
    body.dark-mode p[style*="color: red"] {
      background: #6b2222;
      color: #ff6f6f;
      border-left-color: #ff6f6f;
      box-shadow: 0 0 10px #ff6f6f88;
    }

    /* Toggle switch container */
    .dark-mode-toggle {
      position: fixed;
      top: 1rem;
      right: 1rem;
      width: 60px;
      height: 30px;
      background: var(--brown-gradient-start);
      border-radius: 15px;
      box-shadow: 0 5px 12px rgba(93, 64, 55, 0.6);
      cursor: pointer;
      user-select: none;
      display: flex;
      align-items: center;
      padding: 0 5px;
      transition: background 0.3s ease;
      z-index: 1000;
    }

    body.dark-mode .dark-mode-toggle {
      background: var(--accent);
      box-shadow: 0 5px 12px rgba(255 183 0, 0.6);
    }

    /* The knob */
    .toggle-knob {
      width: 24px;
      height: 24px;
      background: white;
      border-radius: 50%;
      box-shadow: 0 1px 5px rgba(0,0,0,0.2);
      position: relative;
      transition: transform 0.3s ease;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 16px;
      color: var(--brown-dark);
      user-select: none;
    }

    /* Move knob when dark mode enabled */
    body.dark-mode .toggle-knob {
      transform: translateX(30px);
      color: var(--accent);
    }

    /* Sun and moon icons inside knob */
    .toggle-knob .sun {
      display: block;
    }
    body.dark-mode .toggle-knob .sun {
      display: none;
    }

    .toggle-knob .moon {
      display: none;
    }
    body.dark-mode .toggle-knob .moon {
      display: block;
    }

    /* Tooltip on hover */
    .dark-mode-toggle:hover::after {
      content: attr(data-tooltip);
      position: absolute;
      top: 40px;
      right: 0;
      background: rgba(0,0,0,0.7);
      color: white;
      padding: 4px 8px;
      border-radius: 5px;
      font-size: 0.75rem;
      white-space: nowrap;
      pointer-events: none;
      user-select: none;
      opacity: 1;
      transition: opacity 0.3s ease;
    }
    
    /* Error message styling */
p.error, p[style*="color: red"] {
  background: #ffebee; /* light red background */
  color: var(--error); /* #e53935 red from your variables */
  border-left: 5px solid var(--error);
  padding: 1rem 1.5rem;
  border-radius: var(--border-radius);
  font-weight: 700;
  margin-bottom: 2.5rem;
  box-shadow: var(--shadow-light);
  letter-spacing: 0.03em;
}

/* Success message styling */
p.success {
  background: #e8f5e9; /* light green background */
  color: var(--success); /* #43a047 green from your variables */
  border-left: 5px solid var(--success);
  padding: 1rem 1.5rem;
  border-radius: var(--border-radius);
  font-weight: 700;
  margin-bottom: 2.5rem;
  box-shadow: 0 8px 20px rgba(67, 160, 71, 0.2);
  letter-spacing: 0.03em;
}

    </style>
</head>
<body>
<!-- Dark mode toggle -->
<div class="dark-mode-toggle" id="darkModeToggle" data-tooltip="Toggle Dark Mode" role="button" tabindex="0" aria-pressed="false" aria-label="Toggle dark mode">
  <div class="toggle-knob">
    <span class="sun">🌞</span>
    <span class="moon">🌙</span>
  </div>
</div>

<c:if test="${not empty sessionScope.successMessage}">
    <p class="success">${sessionScope.successMessage}</p>
    <c:remove var="successMessage" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.errorMessage}">
    <p class="error">${sessionScope.errorMessage}</p>
    <c:remove var="errorMessage" scope="session"/>
</c:if>



<h2>Manage Categories</h2>

<form action="ManageCategoriesServlet" method="post">
    <input type="hidden" name="action" value="add" />
    <input type="text" name="name" placeholder="Category Name" required />
    <input type="text" name="description" placeholder="Description" required />
    <button type="submit">Add Category</button>
</form>

<h3>All Categories</h3>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Description</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="cat" items="${categories}">
        <tr>
            <td>${cat.id}</td>
            <td>${cat.name}</td>
            <td>${cat.description}</td>
            <td>
                <form action="ManageCategoriesServlet" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="edit" />
                    <input type="hidden" name="id" value="${cat.id}" />
                    <input type="text" name="name" value="${cat.name}" required />
                    <input type="text" name="description" value="${cat.description}" required />
                    <button type="submit">Update</button>
                </form>

                <form action="ManageCategoriesServlet" method="post" style="display:inline;" onsubmit="return confirm('Delete this category?');">
                    <input type="hidden" name="action" value="delete" />
                    <input type="hidden" name="id" value="${cat.id}" />
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

<script>
  const toggle = document.getElementById('darkModeToggle');
  const body = document.body;

  // Load saved mode
  if (localStorage.getItem('darkMode') === 'enabled') {
    body.classList.add('dark-mode');
    toggle.setAttribute('aria-pressed', 'true');
  }

  toggle.addEventListener('click', () => {
    body.classList.toggle('dark-mode');
    const enabled = body.classList.contains('dark-mode');
    toggle.setAttribute('aria-pressed', enabled ? 'true' : 'false');
    localStorage.setItem('darkMode', enabled ? 'enabled' : 'disabled');
  });

  // Support toggle by keyboard (space and enter)
  toggle.addEventListener('keydown', (e) => {
    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault();
      toggle.click();
    }
  });
</script>
</body>
</html>