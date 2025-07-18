<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.DeliveryPartnerDTO" %>

<%
    List<DeliveryPartnerDTO> pendingPartners = (List<DeliveryPartnerDTO>) request.getAttribute("pendingPartners");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Delivery Partners</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f9f4f0;
            padding: 40px;
            color: #333;
        }
        h1 {
            text-align: center;
            color: #5c4033;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 30px;
            background: #fff;
            box-shadow: 0 6px 15px rgba(0,0,0,0.1);
            border-radius: 8px;
            overflow: hidden;
        }
        th, td {
            padding: 16px;
            text-align: center;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #8d6e63;
            color: white;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        form {
            display: inline-block;
        }
        button {
            padding: 8px 16px;
            margin: 0 4px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-weight: bold;
            transition: 0.3s;
        }
        .approve {
            background-color: #4CAF50;
            color: white;
        }
        .approve:hover {
            background-color: #388e3c;
        }
        .reject {
            background-color: #f44336;
            color: white;
        }
        .reject:hover {
            background-color: #c62828;
        }
        .no-results {
            text-align: center;
            margin-top: 50px;
            font-size: 1.2rem;
            color: #777;
        }
        a.back-link {
            display: inline-block;
            margin-top: 20px;
            color: #5c4033;
            text-decoration: none;
            font-weight: bold;
        }
        a.back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

    <h1>Pending Delivery Partner Approvals</h1>

    <% if (pendingPartners == null || pendingPartners.isEmpty()) { %>
        <div class="no-results">No pending delivery partners to review.</div>
    <% } else { %>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Contact</th>
                    <th>Vehicle</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% for (DeliveryPartnerDTO partner : pendingPartners) { %>
                    <tr>
                        <td><%= partner.getId() %></td>
                        <td><%= partner.getUsername() %></td>
                        <td><%= partner.getFirstName() %> <%= partner.getLastName() %></td>
                        <td><%= partner.getEmail() %></td>
                        <td><%= partner.getContactNumber() %></td>
                        <td><%= partner.getVehicleNumber() %></td>
                        <td>
                  <form method="post" action="UpdateDeliveryPartnerStatusServlet">
    <input type="hidden" name="partnerId" value="<%= partner.getId() %>"/>
    <input type="hidden" name="action" value="approve"/>
    <button type="submit" class="approve">Approve</button>
</form>

<form method="post" action="UpdateDeliveryPartnerStatusServlet">
    <input type="hidden" name="partnerId" value="<%= partner.getId() %>"/>
    <input type="hidden" name="action" value="reject"/>
    <button type="submit" class="reject">Reject</button>
</form>

                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>

    <div style="text-align:center;">
        <a href="AdminDashboardServlet" class="back-link">&laquo; Back to Dashboard</a>
    </div>

</body>
</html>
