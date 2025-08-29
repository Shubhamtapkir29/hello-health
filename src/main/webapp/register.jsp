<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register - Hello Health</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6f9;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }
        .container {
            background: #fff;
            padding: 25px 30px;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            width: 350px;
        }
        .container h2 {
            margin-bottom: 20px;
            text-align: center;
        }
        input, select, button {
            width: 100%;
            margin: 8px 0;
            padding: 10px;
            font-size: 14px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        button {
            background: #007bff;
            color: #fff;
            font-weight: bold;
            border: none;
            cursor: pointer;
        }
        button:hover {
            background: #0056b3;
        }
        .message {
            margin: 10px 0;
            padding: 10px;
            text-align: center;
            border-radius: 5px;
        }
        .error { background: #f8d7da; color: #721c24; }
        .success { background: #d4edda; color: #155724; }
    </style>
</head>
<body>
<div class="container">
    <h2>Create Account</h2>

    <!-- Show messages -->
    <%
        String error = request.getParameter("error");
        String registered = request.getParameter("registered");
        if (error != null) {
    %>
        <div class="message error">Registration failed. Try again.</div>
    <%
        } else if (registered != null) {
    %>
        <div class="message success">Registration successful! Please log in.</div>
    <%
        }
    %>

    <!-- Registration form -->
    <form action="<%=request.getContextPath()%>/register" method="post">
        <input type="text" name="fullname" placeholder="Full Name" required />
        <input type="email" name="email" placeholder="Email" required />
        <input type="password" name="password" placeholder="Password" required />
        
        <select name="role" required>
            <option value="">-- Select Role --</option>
            <option value="admin">Admin</option>
            <option value="doctor">Doctor</option>
            <option value="patient">Patient</option>
        </select>

        <button type="submit">Register</button>
    </form>

    <p style="text-align:center; margin-top:15px;">
        Already have an account? <a href="login.jsp">Login</a>
    </p>
</div>
</body>
</html>
