<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Vendors - Bus Booking System</title>
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
        .vendors-card {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
        }
        .table {
            border-radius: 10px;
            overflow: hidden;
        }
        .table thead th {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            font-weight: 600;
        }
        .table tbody tr {
            transition: background-color 0.3s;
        }
        .table tbody tr:hover {
            background-color: #f8f9fa;
        }
        .btn-sm {
            border-radius: 8px;
        }
        .search-box {
            border-radius: 10px;
            border: 2px solid #e9ecef;
        }
        .search-box:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
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
                        <a class="nav-link" href="/admin/add-vendor">
                            <i class="fas fa-plus-circle me-2"></i>Add Vendor
                        </a>
                        <a class="nav-link active" href="/admin/vendors">
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
                        <h2><i class="fas fa-users me-2"></i>Manage Vendors</h2>
                        <a href="/admin/add-vendor" class="btn btn-primary">
                            <i class="fas fa-plus me-2"></i>Add New Vendor
                        </a>
                    </div>
                    
                    <div class="vendors-card">
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
                        
                        <!-- Search Bar -->
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="fas fa-search"></i>
                                    </span>
                                    <input type="text" class="form-control search-box" 
                                           placeholder="Search vendors..." id="searchInput">
                                </div>
                            </div>
                            <div class="col-md-6 text-end">
                                <span class="text-muted">
                                    Total Vendors: <strong>${vendors.size()}</strong>
                                </span>
                            </div>
                        </div>
                        
                        <c:choose>
                            <c:when test="${not empty vendors}">
                                <div class="table-responsive">
                                    <table class="table table-hover" id="vendorsTable">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Vendor Name</th>
                                                <th>Email</th>
                                                <th>Phone</th>
                                                <th>License No.</th>
                                                <th>Address</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="vendor" items="${vendors}">
                                                <tr>
                                                    <td><strong>#${vendor.id}</strong></td>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <div class="bg-primary rounded-circle d-flex align-items-center justify-content-center me-2" 
                                                                 style="width: 35px; height: 35px;">
                                                                <i class="fas fa-building text-white"></i>
                                                            </div>
                                                            <strong>${vendor.vendorName}</strong>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <i class="fas fa-envelope text-muted me-1"></i>
                                                        ${vendor.email}
                                                    </td>
                                                    <td>
                                                        <i class="fas fa-phone text-muted me-1"></i>
                                                        ${vendor.phoneNumber != null ? vendor.phoneNumber : 'N/A'}
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-info">
                                                            ${vendor.licenseNumber != null ? vendor.licenseNumber : 'N/A'}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <i class="fas fa-map-marker-alt text-muted me-1"></i>
                                                        <span class="text-truncate" style="max-width: 150px; display: inline-block;" 
                                                              title="${vendor.address}">
                                                            ${vendor.address != null ? vendor.address : 'N/A'}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group" role="group">
                                                            
                                                            <button type="button" class="btn btn-outline-danger btn-sm" 
                                                                    title="Delete Vendor" onclick="deleteVendor(${vendor.id}, '${vendor.vendorName}')">
                                                                <i class="fas fa-trash"></i>
                                                            </button>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-5">
                                    <i class="fas fa-inbox fa-4x text-muted mb-3"></i>
                                    <h4 class="text-muted">No Vendors Found</h4>
                                    <p class="text-muted">Start by adding your first vendor to the system.</p>
                                    <a href="/admin/add-vendor" class="btn btn-primary">
                                        <i class="fas fa-plus me-2"></i>Add First Vendor
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Delete Confirmation Modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">
                        <i class="fas fa-exclamation-triangle text-danger me-2"></i>
                        Confirm Delete
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to delete vendor <strong id="vendorNameToDelete"></strong>?</p>
                    <p class="text-muted small">This action cannot be undone.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <form id="deleteForm" method="post" style="display: inline;">
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-trash me-2"></i>Delete
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Search functionality
        document.getElementById('searchInput').addEventListener('keyup', function() {
            const searchTerm = this.value.toLowerCase();
            const tableRows = document.querySelectorAll('#vendorsTable tbody tr');
            
            tableRows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(searchTerm) ? '' : 'none';
            });
        });
        
        // Delete vendor function
        function deleteVendor(vendorId, vendorName) {
            document.getElementById('vendorNameToDelete').textContent = vendorName;
            document.getElementById('deleteForm').action = '/admin/vendors/' + vendorId + '/delete';
            new bootstrap.Modal(document.getElementById('deleteModal')).show();
        }
        
        
    </script>
</body>
</html>
