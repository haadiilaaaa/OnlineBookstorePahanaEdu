<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Profile | The Gallery Café</title>

    <!-- Font Awesome for Icons (optional) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" crossorigin="anonymous" />

    <style>
        :root {
            --brown-dark: #6d4c41;
            --brown-light: #8d6e63;
            --bg-light: #f3ede5;   
            --bg-dark: #2c2c2c;
            --text-light: #5d4037;
            --text-dark: #d7ccc8;
            --card-bg-light: #fffdf8;
            --card-bg-dark: #3a3a3a;
            --shadow-light: rgba(109, 76, 65, 0.15);
            --shadow-dark: rgba(0, 0, 0, 0.6);
            --border-radius: 12px;
            --transition-speed: 0.35s;
            --font-sans: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            --btn-gradient-start: #6d4c41;
            --btn-gradient-end: #a1887f;
        }

        body {
            font-family: var(--font-sans);
            background: linear-gradient(135deg, var(--bg-light), #e8ded6);
            color: var(--text-light);
            transition: background 0.3s, color 0.3s;
        }

        body.dark-theme {
            background: linear-gradient(135deg, var(--bg-dark), #3a3a3a);
            color: var(--text-dark);
        }

        .edit-profile-container {
    max-width: 700px;
    margin: 50px auto;
    padding: 30px 40px; /* increased side padding */
    background-color: var(--card-bg-light);
    border-radius: var(--border-radius);
    box-shadow: 0 5px 15px var(--shadow-light);
    transition: background 0.3s;
}


        body.dark-theme .edit-profile-container {
            background-color: var(--card-bg-dark);
            color: var(--text-dark);
            box-shadow: 0 5px 15px var(--shadow-dark);
        }

        h2 {
            text-align: center;
            color: var(--brown-dark);
        }

        body.dark-theme h2 {
            color: var(--brown-light);
        }

        label {
            font-weight: 600;
            margin-top: 15px;
            display: block;
        }

         input[type="text"],
input[type="email"],
input[type="password"],
textarea {
    width: 100%;
    max-width: 100%;
    padding: 10px 14px;
    border: 1px solid var(--brown-light);
    border-radius: 6px;
    font-size: 15px;
    margin-top: 5px;
    background-color: #fff;
    color: var(--text-light);
    box-sizing: border-box; /* ensures padding is included in width */
}


        body.dark-theme input,
        body.dark-theme textarea {
            background-color: #484848;
            color: var(--text-dark);
            border: 1px solid var(--brown-light);
        }

        input:focus,
        textarea:focus {
            border-color: var(--brown-dark);
            outline: none;
        }

        .btn-submit {
            background: linear-gradient(45deg, var(--btn-gradient-start), var(--btn-gradient-end));
            color: #fff;
            border: none;
            border-radius: 6px;
            padding: 12px;
            width: 100%;
            font-size: 16px;
            font-weight: 700;
            margin-top: 20px;
            cursor: pointer;
        }

        .btn-submit:hover {
            background: linear-gradient(45deg, var(--btn-gradient-end), var(--btn-gradient-start));
        }

        .btn-back {
            background: #ccc;
            border: none;
            padding: 8px 14px;
            border-radius: 6px;
            color: #333;
            cursor: pointer;
            font-weight: bold;
            margin-bottom: 15px;
        }

        .message.success {
            background-color: #d4edda;
            color: #155724;
            padding: 10px;
            border-radius: 6px;
            margin-bottom: 20px;
        }

        .message.error {
            background-color: #f8d7da;
            color: #721c24;
            padding: 10px;
            border-radius: 6px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>

<div class="edit-profile-container">

    <button onclick="history.back()" class="btn-back"><i class="fas fa-arrow-left"></i> Back</button>

    <c:if test="${not empty message}">
        <div class="message success">${message}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="message error">${error}</div>
    </c:if>

    <h2>Edit Your Profile</h2>

    <form method="post" action="${pageContext.request.contextPath}/customer/edit-profile">

        <label for="firstName">First Name</label>
        <input type="text" id="firstName" name="firstName" value="${sessionScope.user.firstName}" />

        <label for="lastName">Last Name</label>
        <input type="text" id="lastName" name="lastName" value="${sessionScope.user.lastName}" />

        <label for="email">Email</label>
        <input type="email" id="email" name="email" value="${sessionScope.user.email}" />

        <label for="contactNumber">Contact Number</label>
        <input type="text" id="contactNumber" name="contactNumber" value="${sessionScope.user.contactNumber}" />

        <label for="address">Address</label>
        <textarea id="address" name="address">${sessionScope.user.address}</textarea>

        <label for="password">New Password (leave blank if not changing)</label>
        <input type="password" id="password" name="password" />

        <label for="confirmPassword">Confirm New Password</label>
        <input type="password" id="confirmPassword" name="confirmPassword" />

        <button type="submit" class="btn-submit">Update Profile</button>
    </form>

</div>

<script>
  const savedTheme = localStorage.getItem('theme');
  if (savedTheme === 'dark') {
    document.body.classList.add('dark-theme');
  }
</script>

</body>
</html>
