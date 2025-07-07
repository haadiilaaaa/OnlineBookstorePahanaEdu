<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Thank You for Your Order</title>
    <style>
        /* Add some styling for invoice */
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
<h2>Thank You for Your Order!</h2>

<div>
    <!-- Invoice HTML injected here -->
    <%= request.getAttribute("invoice") %>
</div>

<button onclick="window.print()">Print Invoice</button>


<script>
    window.onload = function() {
        const link = document.createElement('a');
        link.href = '<%= request.getContextPath() %>/<%= request.getAttribute("invoiceDownloadPath") %>';
        link.download = '';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    };
</script>

</body>
</html>
