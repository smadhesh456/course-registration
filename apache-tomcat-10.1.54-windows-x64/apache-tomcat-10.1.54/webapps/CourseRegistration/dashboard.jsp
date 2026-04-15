<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%
    // Check if user is logged in
    HttpSession session_obj = request.getSession(false);
    if (session_obj == null || session_obj.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
        }
        .navbar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .navbar h1 {
            font-size: 28px;
        }
        .navbar .logout-btn {
            background: #e74c3c;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            color: white;
            cursor: pointer;
            font-weight: bold;
        }
        .navbar .logout-btn:hover {
            background: #c0392b;
        }
        .container {
            max-width: 1000px;
            margin: 40px auto;
            padding: 20px;
        }
        .welcome-card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            margin-bottom: 30px;
        }
        .welcome-card h2 {
            color: #333;
            margin-bottom: 20px;
        }
        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }
        .info-card {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            border-left: 4px solid #667eea;
        }
        .info-card h3 {
            color: #667eea;
            font-size: 14px;
            margin-bottom: 10px;
            text-transform: uppercase;
        }
        .info-card p {
            color: #333;
            font-size: 18px;
            font-weight: bold;
        }
        .action-cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-top: 30px;
        }
        .action-card {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            text-align: center;
            cursor: pointer;
            transition: transform 0.3s;
        }
        .action-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
        }
        .action-card h3 {
            color: #667eea;
            margin-bottom: 10px;
        }
        .action-card p {
            color: #666;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="navbar">
        <h1>Course Registration System</h1>
        <form action="LogoutServlet" method="POST" style="margin: 0;">
            <button type="submit" class="logout-btn">Logout</button>
        </form>
    </div>
    
    <div class="container">
        <div class="welcome-card">
            <h2>Welcome, <%= session_obj.getAttribute("fullname") %>!</h2>
            <p>You are successfully logged in to the Course Registration System.</p>
            
            <div class="info-grid">
                <div class="info-card">
                    <h3>Username</h3>
                    <p><%= session_obj.getAttribute("username") %></p>
                </div>
                <div class="info-card">
                    <h3>Email</h3>
                    <p><%= session_obj.getAttribute("email") %></p>
                </div>
                <div class="info-card">
                    <h3>Phone</h3>
                    <p><%= session_obj.getAttribute("phone") %></p>
                </div>
                <div class="info-card">
                    <h3>Degree</h3>
                    <p><%= session_obj.getAttribute("degree") %></p>
                </div>
            </div>
        </div>
        
        <div class="action-cards">
            <div class="action-card">
                <h3>📚 View Courses</h3>
                <p>Browse available courses</p>
            </div>
            <div class="action-card">
                <h3>📝 Register Courses</h3>
                <p>Register for your courses</p>
            </div>
            <div class="action-card">
                <h3>📊 View Grades</h3>
                <p>Check your grades</p>
            </div>
            <div class="action-card">
                <h3>⚙️ Settings</h3>
                <p>Update your profile</p>
            </div>
        </div>
    </div>
</body>
</html>
