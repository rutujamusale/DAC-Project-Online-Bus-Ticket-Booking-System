<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Login - Bus Booking System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        .login-card {
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
            border-radius: 15px;
        }
    </style>
</head>
<body>
    <div class="container-fluid d-flex align-items-center justify-content-center min-vh-100">
        <div class="row w-100 justify-content-center">
            <div class="col-md-6 col-lg-4">
                <div class="card login-card">
                    <div class="card-body p-5">
                        <div class="text-center mb-4">
                            <i class="fas fa-user-shield fa-3x text-primary mb-3"></i>
                            <h3 class="fw-bold">Admin Login</h3>
                            <p class="text-muted">Access the admin dashboard</p>
                        </div>

                        <c:if test="${param.error != null}">
                            <div class="alert alert-danger" role="alert">
                                <i class="fas fa-exclamation-triangle me-2"></i>
                                Invalid username or password!
                            </div>
                        </c:if>

                        <c:if test="${param.logout != null}">
                            <div class="alert alert-success" role="alert">
                                <i class="fas fa-check-circle me-2"></i>
                                You have been logged out successfully!
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/admin/authenticate" method="post">
                            <div class="mb-3">
                                <label for="username" class="form-label">Username</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                                    <input type="text" class="form-control form-control-lg" id="username" 
                                           name="username" placeholder="Enter username" required>
                                </div>
                            </div>

                            <div class="mb-4">
                                <label for="password" class="form-label">Password</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-lock"></i></span>
                                    <input type="password" class="form-control form-control-lg" id="password" 
                                           name="password" placeholder="Enter password" required>
                                </div>
                            </div>

                            <button type="submit" class="btn btn-primary btn-lg w-100">
                                <i class="fas fa-sign-in-alt me-2"></i>
                                Login
                            </button>
                        </form>

                        <div class="text-center mt-4">
                            <small class="text-muted">Demo credentials: admin / 1234</small>
                        </div>

                        <div class="text-center mt-3">
                            <a href="${pageContext.request.contextPath}/" class="text-decoration-none">
                                <i class="fas fa-arrow-left me-1"></i>
                                Back to Home
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
