<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Items</title>
</head>
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
          --border-radius: 12px;
          --shadow-light: 0 10px 30px rgba(0, 0, 0, 0.1);
          --shadow-dark: 0 10px 30px rgba(0, 0, 0, 0.6);
          --font-main: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
          --transition-fast: 0.25s ease;
          --transition-normal: 0.4s ease;
        }

        /* Base Reset & Body */
        * {
          box-sizing: border-box;
        }

        body {
          font-family: var(--font-main);
          background: linear-gradient(135deg, #fdf6e3, #f9f9f9);
          color: var(--text-dark);
          margin: 0;
          padding: 2.5rem 6%;
          transition: background-color 0.3s ease, color 0.3s ease;
          -webkit-font-smoothing: antialiased;
          -moz-osx-font-smoothing: grayscale;
        }

        /* Dark Mode Support */
        body.dark-theme {
          background: linear-gradient(135deg, #1c1c1c, #252525);
          color: var(--text-light);
        }

        body.dark-theme input,
        body.dark-theme select,
        body.dark-theme textarea {
          background-color: #333;
          color: var(--text-light);
          border-color: var(--brown-light);
          box-shadow: inset 0 2px 5px rgba(0,0,0,0.5);
        }

        body.dark-theme table th,
        body.dark-theme table td {
          background: #3a3a3a;
          color: var(--text-light);
          box-shadow: none;
        }

        body.dark-theme form[action="AddItemServlet"][method="get"] {
          background: #2b2b2b;
          box-shadow: var(--shadow-dark);
        }

        body.dark-theme input[type="submit"],
        body.dark-theme form[action$="AddItemServlet"] input[type="submit"] {
          background: linear-gradient(135deg, var(--brown-light), var(--brown-dark));
          box-shadow: 0 8px 24px rgba(141, 110, 99, 0.7);
          color: var(--text-light);
        }

        /* Toggle button styles */
        nav {
          display: flex;
          justify-content: flex-end;
          margin-bottom: 20px;
          padding-right: 6%;
        }
        button#themeToggle {
          background: transparent;
          border: 2.5px solid var(--brown-dark);
          border-radius: 50%;
          width: 44px;
          height: 44px;
          font-size: 22px;
          cursor: pointer;
          color: var(--brown-dark);
          transition:
              color 0.35s ease,
              border-color 0.35s ease,
              transform 0.3s ease;
          display: flex;
          align-items: center;
          justify-content: center;
          user-select: none;
        }
        button#themeToggle:hover {
          color: var(--brown-light);
          border-color: var(--brown-light);
          transform: scale(1.15) rotate(20deg);
        }
        body.dark-theme button#themeToggle {
          color: var(--brown-light);
          border-color: var(--brown-light);
        }
/* Heading */
h2 {
  font-size: 2.4rem;
  margin-bottom: 1.8rem;
  padding-left: 1rem;
  border-left: 8px solid var(--brown-dark);
  color: var(--brown-dark);
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 1.2px;
  filter: drop-shadow(1px 1px 1px rgba(0,0,0,0.05));
  user-select: none;
}

body.dark-theme h2 {
  color: var(--highlight);
  border-left-color: var(--highlight);
}

/* Flash Messages */
p[style*="color: red"],
p[style*="color: green"] {
  font-weight: 600;
  border-radius: var(--border-radius);
  margin: 1.2rem 0;
  padding: 1rem 1.3rem;
  box-shadow: var(--shadow-light);
  font-size: 1.1rem;
  letter-spacing: 0.03em;
  max-width: 600px;
  user-select: none;
  transition: background-color var(--transition-fast);
}

p[style*="color: red"] {
  background: #fdecea;
  color: var(--error);
  border-left: 6px solid var(--error);
}

p[style*="color: green"] {
  background: #e6f4ea;
  color: var(--success);
  border-left: 6px solid var(--success);
}

/* --- Search Form --- */
form[action="AddItemServlet"][method="get"] {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(170px, 1fr)) auto;
  gap: 1.3rem 1.6rem;
  background: #fff;
  padding: 1.8rem 2.5rem;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow-light);
  max-width: 100%;
  align-items: end;
  transition: box-shadow var(--transition-normal);
}

form[action="AddItemServlet"][method="get"]:focus-within {
  box-shadow: 0 0 15px var(--brown-light);
}

form[action="AddItemServlet"][method="get"] input[type="text"],
form[action="AddItemServlet"][method="get"] input[type="number"],
form[action="AddItemServlet"][method="get"] select {
  height: 44px;
  padding: 0 1rem;
  font-size: 1rem;
  border: 2px solid #ddd;
  border-radius: var(--border-radius);
  background-color: #fafafa;
  transition: border-color var(--transition-fast), background-color var(--transition-fast);
  box-shadow: inset 0 2px 4px rgba(0,0,0,0.05);
  font-weight: 500;
}

form[action="AddItemServlet"][method="get"] input[type="text"]:focus,
form[action="AddItemServlet"][method="get"] input[type="number"]:focus,
form[action="AddItemServlet"][method="get"] select:focus {
  border-color: var(--brown-dark);
  background-color: #fff;
  outline: none;
  box-shadow: 0 0 8px var(--brown-light);
  font-weight: 600;
}

/* Search Button */
form[action="AddItemServlet"][method="get"] input[type="submit"] {
  height: 44px;
  padding: 0 2.2rem;
  font-size: 1.1rem;
  font-weight: 700;
  background: linear-gradient(45deg, var(--brown-dark), var(--brown-light));
  border: none;
  border-radius: var(--border-radius);
  color: white;
  cursor: pointer;
  box-shadow: 0 5px 15px rgba(93, 64, 55, 0.4);
  transition: background 0.3s ease, box-shadow 0.3s ease, transform 0.15s ease;
  user-select: none;
}

form[action="AddItemServlet"][method="get"] input[type="submit"]:hover {
  background: linear-gradient(45deg, var(--brown-light), var(--brown-dark));
  box-shadow: 0 8px 25px rgba(141, 110, 99, 0.65);
  transform: translateY(-2px);
}

form[action="AddItemServlet"][method="get"] input[type="submit"]:active {
  transform: translateY(0);
  box-shadow: 0 5px 15px rgba(93, 64, 55, 0.4);
}

/* --- Add/Edit Form --- */
form[action="AddItemServlet"][method="post"] {
  background: linear-gradient(135deg, #fff, #fcfcfc);
  padding: 2.5rem 3rem;
  max-width: 820px;
  margin-bottom: 3.5rem;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow-light);
  transition: box-shadow var(--transition-normal);
}

form[action="AddItemServlet"][method="post"]:focus-within {
  box-shadow: 0 0 25px var(--brown-light);
}

label {
  display: block;
  font-weight: 700;
  font-size: 1.05rem;
  margin: 1.2rem 0 0.6rem 0;
  color: var(--brown-dark);
  user-select: none;
}

/* Inputs and textareas */
input[type="text"],
input[type="number"],
textarea,
select,
input[type="file"] {
  width: 100%;
  padding: 0.85rem 1.2rem;
  font-size: 1.05rem;
  font-weight: 500;
  color: var(--text-dark);
  border: 2px solid #ddd;
  border-radius: var(--border-radius);
  background-color: #fafafa;
  transition: border-color 0.3s ease, background-color 0.3s ease, box-shadow 0.3s ease;
  box-shadow: inset 0 2px 5px rgba(0,0,0,0.05);
  resize: vertical;
  min-height: 40px;
}

input[type="file"] {
  padding: 0.5rem 1rem;
  cursor: pointer;
  background-color: #fff;
  box-shadow: none;
}

input[type="text"]:focus,
input[type="number"]:focus,
textarea:focus,
select:focus,
input[type="file"]:focus {
  border-color: var(--brown-dark);
  background-color: #fff;
  box-shadow: 0 0 10px var(--brown-light);
  outline: none;
  font-weight: 600;
}

/* Submit Button */
input[type="submit"] {
  margin-top: 2rem;
  padding: 0.75rem 2.4rem;
  font-weight: 700;
  font-size: 1.2rem;
  background: linear-gradient(135deg, var(--brown-dark), var(--brown-light));
  border: none;
  border-radius: var(--border-radius);
  color: white;
  cursor: pointer;
  box-shadow: 0 8px 24px rgba(93, 64, 55, 0.45);
  transition: background 0.35s ease, box-shadow 0.35s ease, transform 0.2s ease;
  user-select: none;
}

input[type="submit"]:hover {
  background: linear-gradient(135deg, var(--brown-light), var(--brown-dark));
  box-shadow: 0 12px 35px rgba(141, 110, 99, 0.7);
  transform: translateY(-3px);
}

input[type="submit"]:active {
  transform: translateY(0);
  box-shadow: 0 8px 24px rgba(93, 64, 55, 0.45);
}

/* Image Preview */
img {
  margin-top: 0.8rem;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow-light);
  max-height: 130px;
  transition: transform 0.3s ease;
  cursor: zoom-in;
  object-fit: cover;
}

img:hover {
  transform: scale(1.05);
}

/* Table */
table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0 12px;
  background: transparent;
  font-size: 1rem;
  user-select: none;
  margin-top: 3rem;
}

th, td {
  background: #fff;
  padding: 1rem 1.2rem;
  text-align: center;
  vertical-align: middle;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow-light);
  transition: background-color 0.3s ease;
  border: none;
}

th {
  background: linear-gradient(135deg, var(--brown-light), var(--brown-dark));
  color: #fff;
  font-weight: 700;
  letter-spacing: 0.05em;
  user-select: none;
}

tbody tr:hover td {
  background-color: #f0e6de;
  box-shadow: 0 8px 20px rgba(93, 64, 55, 0.2);
}

td img {
  width: 65px;
  height: 65px;
  border-radius: 12px;
  object-fit: cover;
  box-shadow: 0 2px 8px rgba(0,0,0,0.12);
  transition: transform 0.3s ease;
}

td img:hover {
  transform: scale(1.1);
}

td span[style*="line-through"] {
  display: block;
  color: #999;
  font-size: 0.9rem;
  font-style: italic;
  letter-spacing: 0.01em;
}

td span[style*="font-weight: bold"] {
  font-weight: 700;
  color: var(--success);
  font-size: 1.1rem;
}

td span[style*="background-color: yellow"] {
  background-color: var(--highlight);
  padding: 4px 10px;
  border-radius: var(--border-radius);
  font-weight: 700;
  font-size: 0.85rem;
  color: #111;
  box-shadow: 0 2px 5px rgba(0,0,0,0.12);
  display: inline-block;
  margin-top: 6px;
}

/* Action Buttons */
form[action$="AddItemServlet"][method="post"] {
  display: inline-block;
  margin: 0 0.3rem;
}

form[action$="AddItemServlet"] input[type="submit"] {
  font-size: 0.9rem;
  padding: 0.5rem 1.2rem;
  background-color: #546e7a;
  border-radius: var(--border-radius);
  box-shadow: 0 3px 12px rgba(84, 110, 122, 0.5);
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
  font-weight: 600;
  user-select: none;
}

form[action$="AddItemServlet"] input[type="submit"]:hover {
  background-color: #78909c;
  box-shadow: 0 5px 20px rgba(120, 144, 156, 0.75);
}

/* Horizontal Rule */
hr {
  border: none;
  height: 1px;
  background: linear-gradient(to right, transparent, #c8b5a1, transparent);
  margin: 3rem 0;
  user-select: none;
}

/* Responsive */
@media (max-width: 900px) {
  form[action="AddItemServlet"][method="get"] {
    grid-template-columns: 1fr;
    gap: 1rem;
    padding: 1.4rem 1.6rem;
  }

  form[action="AddItemServlet"][method="post"] {
    padding: 2rem 1.5rem;
  }

  h2 {
    font-size: 1.8rem;
    margin-bottom: 1.2rem;
  }

  table, th, td {
    font-size: 0.9rem;
  }

  input[type="submit"] {
    width: 100%;
    margin-top: 1rem;
  }
}
/* Styled dropdown (select) */
form[action="AddItemServlet"][method="get"] select,
form[action="AddItemServlet"][method="post"] select {
  appearance: none;           /* Remove default arrow */
  -webkit-appearance: none;
  -moz-appearance: none;
  background-color: #fafafa;
  border: 2px solid #ddd;
  border-radius: var(--border-radius);
  padding: 0 1rem;
  padding-right: 2.8rem;      /* Space for custom arrow */
  font-size: 1rem;
  font-weight: 500;
  color: var(--text-dark);
  cursor: pointer;
  box-shadow: inset 0 2px 5px rgba(0,0,0,0.05);
  transition: border-color 0.3s ease, background-color 0.3s ease, box-shadow 0.3s ease;
  background-image:
    linear-gradient(45deg, transparent 50%, var(--brown-dark) 50%),
    linear-gradient(135deg, var(--brown-dark) 50%, transparent 50%),
    linear-gradient(to right, #ddd, #ddd);
  background-position:
    calc(100% - 20px) calc(50% - 6px),
    calc(100% - 15px) calc(50% - 6px),
    calc(100% - 25px) 50%;
  background-size: 5px 5px, 5px 5px, 1px 20px;
  background-repeat: no-repeat;
}

form[action="AddItemServlet"][method="get"] select:hover,
form[action="AddItemServlet"][method="post"] select:hover {
  border-color: var(--brown-dark);
  background-color: #fff;
  box-shadow: 0 0 8px var(--brown-light);
}

form[action="AddItemServlet"][method="get"] select:focus,
form[action="AddItemServlet"][method="post"] select:focus {
  border-color: var(--brown-dark);
  background-color: #fff;
  outline: none;
  box-shadow: 0 0 12px var(--brown-light);
  font-weight: 600;
}
/* Dark Mode input, select, textarea backgrounds and text colors */
body.dark-theme input[type="text"],
body.dark-theme input[type="number"],
body.dark-theme textarea,
body.dark-theme select,
body.dark-theme input[type="file"] {
    background-color: #3a3a3a;   /* medium dark gray */
    color: var(--text-light);    /* light text */
    border-color: var(--brown-light);
    box-shadow: inset 0 2px 5px rgba(0, 0, 0, 0.7);
}

/* On focus */
body.dark-theme input[type="text"]:focus,
body.dark-theme input[type="number"]:focus,
body.dark-theme textarea:focus,
body.dark-theme select:focus,
body.dark-theme input[type="file"]:focus {
    background-color: #4b4b4b; /* slightly lighter */
    color: var(--text-light);
    border-color: var(--highlight);
    box-shadow: 0 0 8px var(--highlight);
}

/* Table cells (td, th) */
body.dark-theme table th,
body.dark-theme table td {
    background-color: #3a3a3a;  /* dark gray */
    color: var(--text-light);   /* light text */
    box-shadow: none;
    border: none;
}

/* Table header with gradient */
body.dark-theme table th {
    background: linear-gradient(135deg, var(--brown-light), var(--brown-dark));
    color: var(--text-light);
}

/* Hover row */
body.dark-theme tbody tr:hover td {
    background-color: #555555;
    box-shadow: none;
}

/* Paragraph flash messages */
body.dark-theme p[style*="color: red"] {
    background: #592424;
    color: var(--error);
    border-left-color: var(--error);
}

body.dark-theme p[style*="color: green"] {
    background: #27482f;
    color: var(--success);
    border-left-color: var(--success);
}

/* Buttons */
body.dark-theme input[type="submit"],
body.dark-theme form[action$="AddItemServlet"] input[type="submit"] {
    background: linear-gradient(135deg, var(--brown-light), var(--brown-dark));
    color: var(--text-light);
    box-shadow: 0 8px 24px rgba(141, 110, 99, 0.7);
}

body.dark-theme input[type="submit"]:hover,
body.dark-theme form[action$="AddItemServlet"] input[type="submit"]:hover {
    background: linear-gradient(135deg, var(--brown-dark), var(--brown-light));
    box-shadow: 0 12px 35px rgba(141, 110, 99, 0.9);
}
/* Dark mode select and option styling */
body.dark-theme select {
  background-color: #3a3a3a; /* dark background */
  color: var(--text-light);  /* light text */
  border-color: var(--brown-light);
}

body.dark-theme select option {
  background-color: #3a3a3a; /* dark background */
  color: var(--text-light);  /* light text */
}

/* On hover / focus */
body.dark-theme select option:hover,
body.dark-theme select option:focus {
  background-color: var(--highlight); /* highlight background */
  color: #111; /* dark text on highlight */
}

</style>

<body>
    <nav>
    <button id="themeToggle" aria-label="Toggle Dark Mode" title="Toggle Dark Mode">🌙</button>
</nav>


<c:choose>
    <c:when test="${not empty editItem}">
        <h2>Edit Item</h2>
    </c:when>
    <c:otherwise>
        <h2>Add New Item</h2>
    </c:otherwise>
</c:choose>


<%-- Messages --%>
<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>
<c:if test="${not empty success}">
    <p style="color: green;">${success}</p>
</c:if>

<%-- Search Form --%>
<form action="AddItemServlet" method="get">
    <input type="text" name="keyword" placeholder="Title or Author" />
    <select name="category">
        <option value="">-- All Categories --</option>
        <c:forEach var="cat" items="${categories}">
            <option value="${cat.id}">${cat.name}</option>
        </c:forEach>
    </select>
    <input type="number" step="0.01" name="minPrice" placeholder="Min Price" />
    <input type="number" step="0.01" name="maxPrice" placeholder="Max Price" />
    <input type="submit" value="Search" />
</form>

<hr>

<%-- Main Form (Add/Edit) --%>
<form action="AddItemServlet" method="post" enctype="multipart/form-data">
    <c:choose>
        <c:when test="${not empty editItem}">
            <input type="hidden" name="action" value="update" /> <!-- ✅ Fixed here -->
            <input type="hidden" name="id" value="${editItem.id}" />
        </c:when>
        <c:otherwise>
            <input type="hidden" name="action" value="add" />
        </c:otherwise>
    </c:choose>

    <label>Title:</label><br>
    <input type="text" name="title" value="${editItem.title}" required><br>

    <label>Author:</label><br>
    <input type="text" name="author" value="${editItem.author}" required><br>

    <label>Description:</label><br>
    <textarea name="description" rows="3" cols="30">${editItem.description}</textarea><br>

    <label>Price:</label><br>
    <input type="number" name="price" step="0.01" value="${editItem.price}" required><br>

    <label>Stock Quantity:</label><br>
    <input type="number" name="stock" value="${editItem.stockQuantity}" required><br>

    <label>Upload Image:</label><br>
    <input type="file" name="imageFile" accept="image/*" <c:if test="${empty editItem}">required</c:if>><br>

    <%-- Show image preview only in edit mode --%>
    <c:if test="${not empty editItem.imageUrl}">
        <p>Current Image:</p>
        <img src="${editItem.imageUrl}?t=${now.time}" width="100"/>
    </c:if>

    <label>Category:</label><br>
    <select name="category" required>
        <option value="">-- Select --</option>
        <c:forEach var="cat" items="${categories}">
            <option value="${cat.id}" <c:if test="${editItem != null && cat.id == editItem.categoryId}">selected</c:if>>
                ${cat.name}
            </option>
        </c:forEach>
    </select>

    <br><br>
    <input type="submit" value="<c:out value='${not empty editItem ? "Update Item" : "Add Item"}'/>">
</form>

<hr>

<h2>All Items</h2>
<table border="1" cellpadding="10">
    <tr>
        <th>Image</th>
        <th>Title</th>
        <th>Author</th>
        <th>Price</th>
        <th>Stock</th>
        <th>Category</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="item" items="${items}">
        <tr>
            
            <td>
                <c:choose>
                    <c:when test="${not empty item.imageUrl}">
                        <img src="${item.imageUrl}" width="60" height="60" alt="Image" />
                    </c:when>
                    <c:otherwise>
                        No image
                    </c:otherwise>
                </c:choose>
            </td>
            <td>${item.title}</td>
            <td>${item.author}</td>
           <td>
    <c:choose>
        <c:when test="${item.hasDiscount}">
            <span style="text-decoration: line-through; color: gray;">
                Rs. ${item.originalPrice}
            </span><br>
            <span style="color: green; font-weight: bold;">
                Rs. ${item.discountedPrice}
            </span><br>
            <span style="background-color: yellow; padding: 2px 5px; border-radius: 3px;">
                ${item.discountLabel}
            </span>
        </c:when>
        <c:otherwise>
            Rs. ${item.price}
        </c:otherwise>
    </c:choose>
</td>

            <td>${item.stockQuantity}</td>
            <td>${item.categoryName}</td>
            <td>
                <%-- ✅ FIXED: Added enctype="multipart/form-data" to the Edit form --%>
                <form action="AddItemServlet" method="post" style="display:inline;" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="edit" />
                    <input type="hidden" name="id" value="${item.id}" />
                    <input type="submit" value="Edit" />
                </form>

                <form action="AddItemServlet" method="post" style="display:inline;" onsubmit="return confirm('Are you sure?');" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="delete" />
                    <input type="hidden" name="id" value="${item.id}" />
                    <input type="submit" value="Delete" />
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<script>
    const toggleBtn = document.getElementById('themeToggle');
    toggleBtn.addEventListener('click', () => {
        document.body.classList.toggle('dark-theme');
        toggleBtn.textContent = document.body.classList.contains('dark-theme') ? '☀️' : '🌙';
    });
</script>

</body>
</html>    