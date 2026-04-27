<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hello Health - Login</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f9;
            text-align: center;
            margin-top: 80px;
        }
        .login-box {
            background: white;
            padding: 25px;
            width: 300px;
            margin: auto;
            border-radius: 8px;
            box-shadow: 0px 0px 10px #ccc;
        }
        input {
            width: 90%;
            padding: 8px;
            margin: 8px 0;
        }
        button {
            padding: 10px 20px;
            background: #007bff;
            color: white;
            border: none;
            cursor: pointer;
        }
        .error {
            color: red;
            margin-bottom: 10px;
        }
    </style>
</head>

<body>

<div class="login-box">
    <h2>Hello Health Login</h2>

    <!-- ✅ Error Message -->
    <%
        String error = request.getParameter("error");
        if ("invalid".equals(error)) {
    %>
        <div class="error">Invalid email or password</div>
    <%
        } else if ("server".equals(error)) {
    %>
        <div class="error">Server error. Try again later</div>
    <%
        }
    %>

    <!-- ✅ Login Form -->
    <form action="login" method="post">
        <input type="email" name="email" placeholder="Enter Email" required /><br/>
        <input type="password" name="password" placeholder="Enter Password" required /><br/>
        <button type="submit">Login</button>
    </form>

</div>

</body>
</html>
