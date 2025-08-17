<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Guideline</title>
</head>
<body>
    
    <c:if test="${not empty sessionScope.successMessage}">
    <div style="padding:12px; background:#4CAF50; color:white; border-radius:6px; margin-bottom:16px;">
        ${sessionScope.successMessage} 
    </div>
    <c:remove var="successMessage" scope="session"/>
</c:if>

<c:if test="${not empty sessionScope.errorMessage}">
    <div style="padding:12px; background:#f44336; color:white; border-radius:6px; margin-bottom:16px;">
        ${sessionScope.errorMessage}
    </div>
    <c:remove var="errorMessage" scope="session"/>
</c:if>

<h2>Edit Guideline</h2>
<<form method="post" action="UpdateGuidelineServlet">
    <input type="hidden" name="id" value="${guideline.id}" />
    <label>Title:</label>
    <input type="text" name="title" value="${guideline.title}" required/>
    <label>Content:</label>
    <textarea name="content" required>${guideline.content}</textarea>
    <button type="submit">Update Guideline</button>
</form>

</body>
</html>
