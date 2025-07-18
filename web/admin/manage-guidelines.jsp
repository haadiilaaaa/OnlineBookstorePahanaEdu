<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Guidelines</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 30px;
        }
        h2 {
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
        }
        th, td {
            padding: 12px;
            border: 1px solid #ccc;
            text-align: left;
        }
        th {
            background-color: #f4f4f4;
        }
        input[type="text"], textarea {
            width: 100%;
            padding: 10px;
            font-size: 14px;
            margin-top: 5px;
            box-sizing: border-box;
        }
        button {
            padding: 8px 15px;
            font-size: 14px;
            margin-top: 10px;
            cursor: pointer;
        }
    </style>
</head>
<body>

<h2>Manage Guidelines</h2>

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

</body>
</html>
