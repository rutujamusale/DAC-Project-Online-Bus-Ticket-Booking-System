<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Vendor - Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .sidebar {
            min-height: 100vh;
            background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
        }
    </style>
</head>
<body class="bg-light">
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-3 col-lg-2 d-md-block sidebar collapse">
                <div class="position-sticky pt-3">
                    <div class="text-center text-white mb-4">
                        <i class="fas fa-tachometer-alt fa-2x mb-2"></i>
                        <h5>Admin Panel</h5>
                    </div>
                    
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/dashboard">
                                <i class="fas fa-home me-2"></i>
                                Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/vendors">
                                <i class="fas fa-building me-2"></i>
                                Vendors
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white active" href="${pageContext.request.contextPath}/admin/add-vendor">
                                <i class="fas fa-plus me-2"></i>
                                Add Vendor
                            </a>
                        </li>
                    </ul>
                    
                    <div class="position-absolute bottom-0 w-100 p-3">
                        <a href="${pageContext.request.contextPath}/admin/logout" class="btn btn-outline-light w-100">
                            <i class="fas fa-sign-out-alt me-2"></i>
                            Logout
                        </a>
                    </div>
                </div>
            </nav>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Add New Vendor</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <a href="${pageContext.request.contextPath}/admin/vendors" class="btn btn-outline-secondary">
                            <i class="fas fa-arrow-left me-1"></i>
                            Back to Vendors
                        </a>
                    </div>
                </div>

                <div class="row justify-content-center">
                    <div class="col-lg-8">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="mb-0">
                                    <i class="fas fa-building me-2"></i>
                                    Vendor Information
                                </h5>
                            </div>
                            <div class="card-body">
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

                                <form:form action="${pageContext.request.contextPath}/admin/add-vendor" 
                                          method="post" modelAttribute="vendorDto">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="vendorName" class="form-label">Vendor Name *</label>
                                                <form:input path="vendorName" class="form-control" id="vendorName" 
                                                           placeholder="Enter vendor name" required="true"/>
                                                <form:errors path="vendorName" cssClass="text-danger"/>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="email" class="form-label">Email *</label>
                                                <form:input path="email" type="email" class="form-control" id="email" 
                                                           placeholder="Enter email address" required="true"/>
                                                <form:errors path="email" cssClass="text-danger"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="password" class="form-label">Password *</label>
                                                <form:password path="password" class="form-control" id="password" 
                                                              placeholder="Enter password" required="true"/>
                                                <form:errors path="password" cssClass="text-danger"/>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="mb-3">
                                                <label for="phoneNumber" class="form-label">Phone Number</label>
                                                <form:input path="phoneNumber" class="form-control" id="phoneNumber" 
                                                           placeholder="Enter phone number"/>
                                                <form:errors path="phoneNumber" cssClass="text-danger"/>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="mb-3">
                                        <label for="address" class="form-label">Address</label>
                                        <form:textarea path="address" class="form-control" id="address" rows="3" 
                                                      placeholder="Enter complete address"/>
                                        <form:errors path="address" cssClass="text-danger"/>
                                    </div>

                                    <div class="mb-3">
                                        <label for="licenseNumber" class="form-label">License Number</label>
                                        <form:input path="licenseNumber" class="form-control" id="licenseNumber" 
                                                   placeholder="Enter license number"/>
                                        <form:errors path="licenseNumber" cssClass="text-danger"/>
                                    </div>

                                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                        <button type="reset" class="btn btn-outline-secondary me-md-2">
                                            <i class="fas fa-undo me-1"></i>
                                            Reset
                                        </button>
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-save me-1"></i>
                                            Add Vendor
                                        </button>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
