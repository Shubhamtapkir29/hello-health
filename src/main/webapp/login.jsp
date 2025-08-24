<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Health Hello - Login</title>
</head>
<body>
    <h2>Health Hello - Login</h2>
    <form action="login" method="post">
        <label>Email:</label>
        <input type="text" name="email" required/><br/>
        <label>Password:</label>
        <input type="password" name="password" required/><br/>
        <input type="submit" value="Login"/>
    </form>

    <br/>

    <!-- Register button -->
    <form action="register.jsp" method="get">
        <input type="submit" value="Register Now"/>
    </form>
</body>
</html>
