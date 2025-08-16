<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Waiting for Approval</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #4e342e, #3e2723);
            color: #f5f5dc;
            margin: 0;
            padding: 0;
            display: flex;
            height: 100vh;
            align-items: center;
            justify-content: center;
        }

        .card {
            background-color: #5d4037;
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.4);
            text-align: center;
            max-width: 500px;
            animation: fadeIn 1.2s ease-in-out;
        }

        h2 {
            font-size: 28px;
            margin-bottom: 15px;
            color: #ffe0b2;
        }

        p {
            font-size: 18px;
            color: #fff3e0;
        }

        .coffee-icon {
            font-size: 60px;
            margin-bottom: 15px;
            color: #ffcc80;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to   { opacity: 1; transform: translateY(0); }
        }
    </style>
</head>
<body>
    <div class="card">
        <div class="coffee-icon">☕</div>
        <h2>Your account is under review</h2>
        <p>Thank you for registering. An administrator will review your information shortly.</p>
    </div>
</body>
</html>
