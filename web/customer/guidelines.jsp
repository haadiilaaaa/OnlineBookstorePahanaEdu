<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Customer Guidelines</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 30px; }
        h2 { margin-bottom: 20px; }
        .guideline {
            margin-bottom: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #fafafa;
        }
        .guideline h3 {
            margin-top: 0;
            color: #333;
        }
        .guideline p {
            margin-bottom: 0;
            color: #555;
            line-height: 1.5;
        }
    </style>
</head>
<body>

<h2>Customer Guidelines</h2>

<c:forEach var="g" items="${guidelines}">
    <div class="guideline">
        <h3>${g.title}</h3>  
        <p>${g.content}</p>
    </div>
</c:forEach>

</body>
</html>
