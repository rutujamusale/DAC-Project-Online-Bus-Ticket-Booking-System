<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Bus Booking System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .dashboard-card {
            transition: transform 0.2s;
        }
        .dashboard-card:hover {
            transform: translateY(-5px);
        }
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
                            <a class="nav-link text-white active" href="${pageContext.request.contextPath}/admin/dashboard">
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
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/add-vendor">
                                <i class="fas fa-plus me-2"></i>
                                Add Vendor
                            </a>
                        </li>
                    </ul>
                    
                    <div class="position-absolute bottom-0 w-100 p-5">
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
                    <h1 class="h2">Dashboard</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <div class="btn-group me-2">
                            <button type="button" class="btn btn-sm btn-outline-secondary">
                                <i class="fas fa-download me-1"></i>
                                Export
                            </button>
                        </div>
                    </div>
                </div>

                <!-- Dashboard Stats -->
                <div class="row mb-4">
                    <div class="col-md-4">
                        <div class="card dashboard-card text-white bg-primary">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h4 class="card-title">${dashboardData.totalUsers}</h4>
                                        <p class="card-text">Total Users</p>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-users fa-2x"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="card dashboard-card text-white bg-success">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h4 class="card-title">${dashboardData.totalVendors}</h4>
                                        <p class="card-text">Total Vendors</p>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-building fa-2x"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="card dashboard-card text-white bg-info">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <div>
                                        <h4 class="card-title">${dashboardData.dailyTraffic}</h4>
                                        <p class="card-text">Daily Traffic</p>
                                    </div>
                                    <div class="align-self-center">
                                        <i class="fas fa-chart-line fa-2x"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Recent Activity -->
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="mb-0">Recent Vendors</h5>
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${empty recentVendors}">
                                        <div class="text-center py-4">
                                            <i class="fas fa-building fa-3x text-muted mb-3"></i>
                                            <h6>No vendors found</h6>
                                            <p class="text-muted">Add your first vendor to get started</p>
                                            <a href="${pageContext.request.contextPath}/admin/add-vendor" class="btn btn-primary">
                                                <i class="fas fa-plus me-1"></i>
                                                Add Vendor
                                            </a>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="table-responsive">
                                            <table class="table table-hover">
                                                <thead>
                                                    <tr>
                                                        <th>ID</th>
                                                        <th>Vendor Name</th>
                                                        <th>Email</th>
                                                        <th>Phone</th>
                                                        <th>Status</th>
                                                        <th>Actions</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="vendor" items="${recentVendors}">
                                                        <tr>
                                                            <td>${vendor.id}</td>
                                                            <td>${vendor.vendorName}</td>
                                                            <td>${vendor.email}</td>
                                                            <td>${vendor.phoneNumber}</td>
                                                            <td>
                                                                <span class="badge bg-success">Active</span>
                                                            </td>
                                                            <td>
                                                                <a href="${pageContext.request.contextPath}/admin/vendors/${vendor.id}" 
                                                                   class="btn btn-sm btn-outline-primary">
                                                                    <i class="fas fa-eye"></i>
                                                                </a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                        <div class="text-center mt-3">
                                            <a href="${pageContext.request.contextPath}/admin/vendors" class="btn btn-outline-primary">
                                                View All Vendors
                                            </a>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
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
