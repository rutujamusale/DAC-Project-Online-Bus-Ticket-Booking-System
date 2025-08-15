<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Bus Booking System</title>
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
        .stat-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
            border-left: 4px solid;
            transition: transform 0.3s;
        }
        .stat-card:hover {
            transform: translateY(-5px);
        }
        .stat-card.users {
            border-left-color: #28a745;
        }
        .stat-card.vendors {
            border-left-color: #007bff;
        }
        .stat-card.traffic {
            border-left-color: #ffc107;
        }
        .stat-number {
            font-size: 2.5rem;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .recent-vendors {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
        }
        .vendor-item {
            padding: 15px;
            border-bottom: 1px solid #eee;
            transition: background-color 0.3s;
        }
        .vendor-item:hover {
            background-color: #f8f9fa;
        }
        .vendor-item:last-child {
            border-bottom: none;
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
                        <a class="nav-link active" href="/admin/dashboard">
                            <i class="fas fa-tachometer-alt me-2"></i>Dashboard
                        </a>
                        <a class="nav-link" href="/admin/add-vendor">
                            <i class="fas fa-plus-circle me-2"></i>Add Vendor
                        </a>
                        <a class="nav-link" href="/admin/vendors">
                            <i class="fas fa-users me-2"></i>Manage Vendors
                        </a>
                        <a class="nav-link" href="/admin/pending-vendors" id="pendingVendorsLink">
                            <i class="fas fa-clock me-2"></i>Pending Requests
                            <span class="badge bg-warning text-dark ms-2" id="pendingCount">0</span>
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
                        <h2>Dashboard</h2>
                        <div class="text-muted">
                            <i class="fas fa-calendar-alt me-2"></i>
                            <span id="currentDate"></span>
                        </div>
                    </div>
                    
                    <!-- Statistics Cards -->
                    <div class="row mb-4">
                        <div class="col-md-4 mb-3">
                            <div class="stat-card users">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <div class="stat-number text-success">${dashboardData.totalUsers}</div>
                                        <h6 class="text-muted">Total Users</h6>
                                    </div>
                                    <i class="fas fa-users fa-3x text-success opacity-25"></i>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 mb-3">
                            <div class="stat-card vendors">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <div class="stat-number text-primary">${dashboardData.totalVendors}</div>
                                        <h6 class="text-muted">Total Vendors</h6>
                                    </div>
                                    <i class="fas fa-building fa-3x text-primary opacity-25"></i>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 mb-3">
                            <div class="stat-card traffic">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <div class="stat-number text-warning">${dashboardData.dailyTraffic}</div>
                                        <h6 class="text-muted">Daily Traffic</h6>
                                    </div>
                                    <i class="fas fa-chart-line fa-3x text-warning opacity-25"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Recent Vendors -->
                    <div class="row">
                        <div class="col-12">
                            <div class="recent-vendors">
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <h5><i class="fas fa-clock me-2"></i>Recent Vendors</h5>
                                    <a href="/admin/vendors" class="btn btn-outline-primary btn-sm">
                                        View All <i class="fas fa-arrow-right ms-1"></i>
                                    </a>
                                </div>
                                <c:choose>
                                    <c:when test="${not empty recentVendors}">
                                        <c:forEach var="vendor" items="${recentVendors}">
                                            <div class="vendor-item">
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <div>
                                                        <h6 class="mb-1">${vendor.vendorName}</h6>
                                                        <small class="text-muted">
                                                            <i class="fas fa-envelope me-1"></i>${vendor.email}
                                                        </small>
                                                    </div>
                                                    <div class="text-end">
                                                        <small class="text-muted">
                                                            <i class="fas fa-phone me-1"></i>${vendor.phoneNumber}
                                                        </small>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="text-center py-4">
                                            <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                            <p class="text-muted">No vendors found</p>
                                            <a href="/admin/add-vendor" class="btn btn-primary">
                                                <i class="fas fa-plus me-2"></i>Add First Vendor
                                            </a>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Pending Vendor Requests -->
                    <div class="row mt-4">
                        <div class="col-12">
                            <div class="recent-vendors">
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <h5><i class="fas fa-clock me-2 text-warning"></i>Pending Vendor Requests</h5>
                                    <a href="/admin/pending-vendors" class="btn btn-outline-warning btn-sm">
                                        View All <i class="fas fa-arrow-right ms-1"></i>
                                    </a>
                                </div>
                                <div id="pendingVendorsList">
                                    <div class="text-center py-4">
                                        <i class="fas fa-spinner fa-spin fa-2x text-muted mb-3"></i>
                                        <p class="text-muted">Loading pending requests...</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Display current date
        document.getElementById('currentDate').textContent = new Date().toLocaleDateString('en-US', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
        
        // Load pending vendors
        function loadPendingVendors() {
            console.log('Loading pending vendors...');
            fetch('/api/admin/vendors/pending')
                .then(response => {
                    console.log('Dashboard - Response status:', response.status);
                    return response.json();
                })
                .then(data => {
                    console.log('Dashboard - Pending vendors data:', data);
                    const pendingList = document.getElementById('pendingVendorsList');
                    const pendingCount = document.getElementById('pendingCount');
                    
                    if (data && data.length > 0) {
                        pendingCount.textContent = data.length;
                        pendingCount.style.display = 'inline';
                        
                        let html = '';
                        data.slice(0, 3).forEach(vendor => {
                            html += `
                                <div class="vendor-item">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <h6 class="mb-1">${vendor.vendorName}</h6>
                                            <small class="text-muted">
                                                <i class="fas fa-envelope me-1"></i>${vendor.email}
                                            </small>
                                            <br>
                                            <small class="text-muted">
                                                <i class="fas fa-phone me-1"></i>${vendor.phoneNumber}
                                            </small>
                                        </div>
                                        <div class="text-end">
                                            <button class="btn btn-success btn-sm me-1" onclick="approveVendor(${vendor.id})">
                                                <i class="fas fa-check"></i> Approve
                                            </button>
                                            <button class="btn btn-danger btn-sm" onclick="rejectVendor(${vendor.id})">
                                                <i class="fas fa-times"></i> Reject
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            `;
                        });
                        
                        if (data.length > 3) {
                            html += `
                                <div class="text-center py-2">
                                    <small class="text-muted">And ${data.length - 3} more pending requests...</small>
                                </div>
                            `;
                        }
                        
                        pendingList.innerHTML = html;
                    } else {
                        pendingCount.style.display = 'none';
                        pendingList.innerHTML = `
                            <div class="text-center py-4">
                                <i class="fas fa-check-circle fa-3x text-success mb-3"></i>
                                <p class="text-muted">No pending vendor requests</p>
                            </div>
                        `;
                    }
                })
                .catch(error => {
                    console.error('Error loading pending vendors:', error);
                    document.getElementById('pendingVendorsList').innerHTML = `
                        <div class="text-center py-4">
                            <i class="fas fa-exclamation-triangle fa-3x text-danger mb-3"></i>
                            <p class="text-muted">Error loading pending requests</p>
                        </div>
                    `;
                });
        }
        
        // Approve vendor
        function approveVendor(vendorId) {
            if (confirm('Are you sure you want to approve this vendor?')) {
                fetch(`/api/admin/vendors/${vendorId}/approve`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('Vendor approved successfully!');
                        loadPendingVendors();
                    } else {
                        alert('Error: ' + data.message);
                    }
                })
                .catch(error => {
                    console.error('Error approving vendor:', error);
                    alert('Error approving vendor');
                });
            }
        }
        
        // Reject vendor
        function rejectVendor(vendorId) {
            if (confirm('Are you sure you want to reject this vendor?')) {
                fetch(`/api/admin/vendors/${vendorId}/reject`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('Vendor rejected successfully!');
                        loadPendingVendors();
                    } else {
                        alert('Error: ' + data.message);
                    }
                })
                .catch(error => {
                    console.error('Error rejecting vendor:', error);
                    alert('Error rejecting vendor');
                });
            }
        }
        
        // Load pending vendors when page loads
        document.addEventListener('DOMContentLoaded', function() {
            loadPendingVendors();
        });
    </script>
</body>
</html>
