<html>
<head><title>Patient Dashboard</title></head>
<body>
    <h2>Welcome, <%= session.getAttribute("userName") %>!</h2>
    <a href="appointment">Book Appointment</a><br/>
    <a href="prescriptions">View Prescriptions</a>
</body>
</html>
