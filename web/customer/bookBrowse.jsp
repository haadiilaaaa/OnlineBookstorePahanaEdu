<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.CartItem" %>

<%
    Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
    int cartCount = 0;
    if (cart != null) {
        for (CartItem it : cart.values()) cartCount += it.getQuantity();
    }
    java.math.BigDecimal cartTotal = java.math.BigDecimal.ZERO;
    if (cart != null) {
        for (CartItem it : cart.values())
            cartTotal = cartTotal.add(it.getPrice()
                                       .multiply(new java.math.BigDecimal(it.getQuantity())));
    }
    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Browse Books</title>
    <style>
        img.thumb {max-height:150px; display:block; margin-bottom:1rem;}
        .price-old   {text-decoration:line-through;color:#777;font-size:.9rem;}
        .price-new   {color:#2e7d32;font-weight:bold;}
        .badge-offer {background:#ffd54f;padding:3px 7px;border-radius:6px;font-size:.8rem;}
    </style>
</head>
<body>

<nav>
    <a href="<%= contextPath %>/BookBrowseServlet">Browse Books</a> |
    <a href="<%= contextPath %>/customer/cart.jsp">Cart (<span id="cartCount"><%= cartCount %></span>)</a> |
    <span>Total: Rs. <%= cartTotal %></span>
</nav>

<h2>Browse Books</h2>

<!-- Filter form -->
<form method="get" action="BookBrowseServlet">
    <label for="category">Category:</label>
    <select name="categoryId" id="category">
        <option value="">-- All Categories --</option>
        <c:forEach var="category" items="${categories}">
            <option value="${category.id}"
                    <c:if test="${category.id == selectedCategory}">selected</c:if>>
                ${category.name}
            </option>
        </c:forEach>
    </select>

    <label for="search">Search:</label>
    <input type="text" name="search" id="search" value="${searchKeyword}" />

    <label for="minPrice">Min Price:</label>
    <input type="number" step="0.01" name="minPrice" id="minPrice" value="${minPrice}" />

    <label for="maxPrice">Max Price:</label>
    <input type="number" step="0.01" name="maxPrice" id="maxPrice" value="${maxPrice}" />

    <button type="submit">Filter</button>
</form>

<hr/>

<c:if test="${empty items}">
    <p>No books found.</p>
</c:if>

<c:forEach var="item" items="${items}">
    <div style="border:1px solid #ccc;margin-bottom:1rem;padding:1rem;">
        <h3>${item.title}</h3>

        <c:if test="${not empty item.imageUrl}">
            <img class="thumb" src="${item.imageUrl}" alt="${item.title}" />
        </c:if>

        <p>Author: ${item.author}</p>

        <!-- 💰 PRICE BLOCK -->
        <p>
            <c:choose>
                <c:when test="${item.hasDiscount}">
                    <span class="price-old">Rs.&nbsp;${item.originalPrice}</span><br/>
                    <span class="price-new">Rs.&nbsp;${item.discountedPrice}</span><br/>
                    <c:if test="${not empty item.discountLabel}">
                        <span class="badge-offer">${item.discountLabel}</span>
                    </c:if>
                </c:when>
                <c:otherwise>
                    Rs.&nbsp;${item.price}
                </c:otherwise>
            </c:choose>
        </p>

        <p>Category: ${item.categoryName}</p>

        <form method="post" action="AddToCartServlet">
            <input type="hidden" name="itemId"  value="${item.id}" />
            <label>Quantity:
                <input type="number" name="quantity" value="1" min="1" />
            </label>
            <button type="submit">Add to Cart</button>
        </form>
    </div>
</c:forEach>

</body>
</html>
