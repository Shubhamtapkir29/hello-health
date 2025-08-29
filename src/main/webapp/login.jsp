<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Hello Health</title>
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
        input, button {
            width: 100%;
            margin: 8px 0;
            padding: 10px;
            font-size: 14px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        button {
            background: #28a745;
            color: #fff;
            font-weight: bold;
            border: none;
            cursor: pointer;
        }
        button:hover {
            background: #218838;
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
    <h2>Login</h2>

    <!-- Show messages -->
    <%
        String error = request.getParameter("error");
        String registered = request.getParameter("registered");
        if (error != null) {
    %>
        <div class="message error">Invalid email or password. Please try again.</div>
    <%
        } else if (registered != null) {
    %>
        <div class="message success">Registration successful! Please log in.</div>
    <%
        }
    %>

    <!-- Login form -->
    <form action="<%=request.getContextPath()%>/login" method="post">
        <input type="email" name="email" placeholder="Email" required />
        <input type="password" name="password" placeholder="Password" required />
        <button type="submit">Login</button>
    </form>

    <p style="text-align:center; margin-top:15px;">
        Don’t have an account? <a href="register.jsp">Register</a>
    </p>
</div>
</body>
</html>
