<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Vendor - Bus Booking System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .sidebar {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            color: white;
        }
        .sidebar .nav-link {
            color: rgba(255, 255, 255, 0.8);
            padding: 15px 20px;
            border-radius: 10px;
            margin: 5px 10px;
            transition: all 0.3s;
        }
        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            background-color: rgba(255, 255, 255, 0.2);
            color: white;
        }
        .main-content {
            padding: 20px;
        }
        .form-card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
        }
        .form-control {
            border-radius: 10px;
            border: 2px solid #e9ecef;
            padding: 12px 15px;
        }
        .form-control:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            border-radius: 10px;
            padding: 12px 30px;
        }
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        .alert {
            border-radius: 10px;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <div class="col-md-3 col-lg-2 px-0">
                <div class="sidebar">
                    <div class="p-4">
                        <h4><i class="fas fa-bus me-2"></i>Bus Admin</h4>
                        <small>Welcome, ${sessionScope.adminUsername}</small>
                    </div>
                    <nav class="nav flex-column">
                        <a class="nav-link" href="/admin/dashboard">
                            <i class="fas fa-tachometer-alt me-2"></i>Dashboard
                        </a>
                        <a class="nav-link active" href="/admin/add-vendor">
                            <i class="fas fa-plus-circle me-2"></i>Add Vendor
                        </a>
                        <a class="nav-link" href="/admin/vendors">
                            <i class="fas fa-users me-2"></i>Manage Vendors
                        </a>
                        <a class="nav-link" href="/admin/logout">
                            <i class="fas fa-sign-out-alt me-2"></i>Logout
                        </a>
                    </nav>
                </div>
            </div>
            
            <!-- Main Content -->
            <div class="col-md-9 col-lg-10">
                <div class="main-content">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h2><i class="fas fa-plus-circle me-2"></i>Add New Vendor</h2>
                        <a href="/admin/vendors" class="btn btn-outline-secondary">
                            <i class="fas fa-arrow-left me-2"></i>Back to Vendors
                        </a>
                    </div>
                    
                    <div class="row justify-content-center">
                        <div class="col-lg-8">
                            <div class="form-card">
                                <c:if test="${not empty successMessage}">
                                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                                        <i class="fas fa-check-circle me-2"></i>
                                        ${successMessage}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                    </div>
                                </c:if>
                                
                                <c:if test="${not empty errorMessage}">
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        <i class="fas fa-exclamation-triangle me-2"></i>
                                        ${errorMessage}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                    </div>
                                </c:if>
                                
                                <form:form action="/admin/add-vendor" method="post" modelAttribute="vendorDto">
                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="vendorName" class="form-label">
                                                <i class="fas fa-building me-2"></i>Vendor Name *
                                            </label>
                                            <form:input path="vendorName" class="form-control" 
                                                       placeholder="Enter vendor name" required="true"/>
                                            <form:errors path="vendorName" cssClass="text-danger small"/>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="email" class="form-label">
                                                <i class="fas fa-envelope me-2"></i>Email Address *
                                            </label>
                                            <form:input path="email" type="email" class="form-control" 
                                                       placeholder="Enter email address" required="true"/>
                                            <form:errors path="email" cssClass="text-danger small"/>
                                        </div>
                                    </div>
                                    
                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label for="password" class="form-label">
                                                <i class="fas fa-lock me-2"></i>Password *
                                            </label>
                                            <form:password path="password" class="form-control" 
                                                          placeholder="Enter password" required="true"/>
                                            <form:errors path="password" cssClass="text-danger small"/>
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label for="phoneNumber" class="form-label">
                                                <i class="fas fa-phone me-2"></i>Phone Number
                                            </label>
                                            <form:input path="phoneNumber" class="form-control" 
                                                       placeholder="Enter phone number"/>
                                            <form:errors path="phoneNumber" cssClass="text-danger small"/>
                                        </div>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label for="address" class="form-label">
                                            <i class="fas fa-map-marker-alt me-2"></i>Address
                                        </label>
                                        <form:textarea path="address" class="form-control" rows="3" 
                                                      placeholder="Enter complete address"/>
                                        <form:errors path="address" cssClass="text-danger small"/>
                                    </div>
                                    
                                    <div class="mb-4">
                                        <label for="licenseNumber" class="form-label">
                                            <i class="fas fa-id-card me-2"></i>License Number
                                        </label>
                                        <form:input path="licenseNumber" class="form-control" 
                                                   placeholder="Enter license number"/>
                                        <form:errors path="licenseNumber" cssClass="text-danger small"/>
                                    </div>
                                    
                                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                        <button type="reset" class="btn btn-outline-secondary me-md-2">
                                            <i class="fas fa-undo me-2"></i>Reset
                                        </button>
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-save me-2"></i>Add Vendor
                                        </button>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
