<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pending Vendor Requests - Admin Dashboard</title>
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
        .vendor-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
            margin-bottom: 20px;
            border-left: 4px solid #ffc107;
        }
        .vendor-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.12);
        }
        .status-badge {
            font-size: 0.8rem;
            padding: 5px 10px;
        }
        .btn-action {
            margin: 0 5px;
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
                        <a class="nav-link" href="/admin/vendors">
                            <i class="fas fa-users me-2"></i>Manage Vendors
                        </a>
                        <a class="nav-link active" href="/admin/pending-vendors">
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
                         <h2><i class="fas fa-clock me-2 text-warning"></i>Pending Vendor Requests</h2>
                         <div>
                             <button class="btn btn-outline-primary" onclick="refreshPendingVendors()">
                                 <i class="fas fa-sync-alt me-2"></i>Refresh
                             </button>
                         </div>
                     </div>
                    
                    <!-- Loading State -->
                    <div id="loadingState" class="text-center py-5">
                        <i class="fas fa-spinner fa-spin fa-3x text-primary mb-3"></i>
                        <h5>Loading pending requests...</h5>
                    </div>
                    
                    <!-- Empty State -->
                    <div id="emptyState" class="text-center py-5" style="display: none;">
                        <i class="fas fa-check-circle fa-3x text-success mb-3"></i>
                        <h5>No Pending Requests</h5>
                        <p class="text-muted">All vendor registration requests have been processed.</p>
                        <a href="/admin/dashboard" class="btn btn-primary">
                            <i class="fas fa-arrow-left me-2"></i>Back to Dashboard
                        </a>
                    </div>
                    
                    <!-- Pending Vendors List -->
                    <div id="pendingVendorsList" style="display: none;">
                        <!-- Vendors will be loaded here dynamically -->
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Load pending vendors
        function loadPendingVendors() {
            const loadingState = document.getElementById('loadingState');
            const emptyState = document.getElementById('emptyState');
            const pendingVendorsList = document.getElementById('pendingVendorsList');
            const pendingCount = document.getElementById('pendingCount');
            
            loadingState.style.display = 'block';
            emptyState.style.display = 'none';
            pendingVendorsList.style.display = 'none';
            
            fetch('/api/admin/vendors/pending')
                .then(response => response.json())
                .then(data => {
                    // Handle different response formats
                    let vendors = [];
                    if (Array.isArray(data)) {
                        vendors = data;
                    } else if (data && data.data && Array.isArray(data.data)) {
                        vendors = data.data;
                    } else if (data && data.vendors && Array.isArray(data.vendors)) {
                        vendors = data.vendors;
                    } else {
                        console.error('Unexpected data format:', data);
                        vendors = [];
                    }
                    
                    loadingState.style.display = 'none';
                    
                    if (vendors && vendors.length > 0) {
                        displayVendors(vendors);
                    } else {
                        pendingCount.style.display = 'none';
                        emptyState.style.display = 'block';
                    }
                })
                .catch(error => {
                    console.error('Error loading pending vendors:', error);
                    showErrorState();
                });
        }
        
        // Helper function to display vendors
        function displayVendors(vendors) {
            const pendingCount = document.getElementById('pendingCount');
            const pendingVendorsList = document.getElementById('pendingVendorsList');
            const loadingState = document.getElementById('loadingState');
            const emptyState = document.getElementById('emptyState');
            
            loadingState.style.display = 'none';
            emptyState.style.display = 'none';
            
            if (vendors && vendors.length > 0) {
                pendingCount.textContent = vendors.length;
                pendingCount.style.display = 'inline';
                
                let html = '';
                vendors.forEach((vendor, index) => {
                    // Check if fields are null or undefined
                    const vendorName = vendor.vendorName || vendor.vendor_name || vendor.name || 'N/A';
                    const email = vendor.email || 'N/A';
                    const phoneNumber = vendor.phoneNumber || vendor.phone_number || vendor.phone || 'N/A';
                    const licenseNumber = vendor.licenseNumber || vendor.license_number || vendor.license || 'N/A';
                    const address = vendor.address || 'N/A';
                    const id = vendor.id || 'N/A';
                    
                    // Create HTML using string concatenation
                    html += '<div class="vendor-card">' +
                        '<div class="row">' +
                            '<div class="col-md-8">' +
                                '<div class="d-flex justify-content-between align-items-start mb-3">' +
                                    '<div>' +
                                        '<h5 class="mb-1">' + vendorName + '</h5>' +
                                        '<span class="badge bg-warning status-badge">' +
                                            '<i class="fas fa-clock me-1"></i>Pending Approval' +
                                        '</span>' +
                                    '</div>' +
                                '</div>' +
                                '<div class="row">' +
                                    '<div class="col-md-6">' +
                                        '<p class="mb-1">' +
                                            '<i class="fas fa-envelope me-2 text-primary"></i>' +
                                            '<strong>Email:</strong> ' + email +
                                        '</p>' +
                                        '<p class="mb-1">' +
                                            '<i class="fas fa-phone me-2 text-primary"></i>' +
                                            '<strong>Phone:</strong> ' + phoneNumber +
                                        '</p>' +
                                    '</div>' +
                                    '<div class="col-md-6">' +
                                        '<p class="mb-1">' +
                                            '<i class="fas fa-id-card me-2 text-primary"></i>' +
                                            '<strong>License:</strong> ' + licenseNumber +
                                        '</p>' +
                                        '<p class="mb-1">' +
                                            '<i class="fas fa-map-marker-alt me-2 text-primary"></i>' +
                                            '<strong>Address:</strong> ' + address +
                                        '</p>' +
                                    '</div>' +
                                '</div>' +
                            '</div>' +
                            '<div class="col-md-4 text-end">' +
                                '<div class="d-grid gap-2">' +
                                    '<button class="btn btn-success btn-action" onclick="approveVendor(' + id + ')" data-vendor-id="' + id + '">' +
                                        '<i class="fas fa-check me-2"></i>Approve' +
                                    '</button>' +
                                    '<button class="btn btn-danger btn-action" onclick="rejectVendor(' + id + ')" data-vendor-id="' + id + '">' +
                                        '<i class="fas fa-times me-2"></i>Reject' +
                                    '</button>' +
                                '</div>' +
                            '</div>' +
                        '</div>' +
                    '</div>';
                });
                
                pendingVendorsList.innerHTML = html;
                pendingVendorsList.style.display = 'block';
                pendingVendorsList.style.visibility = 'visible';
            } else {
                pendingCount.style.display = 'none';
                emptyState.style.display = 'block';
            }
        }
        
        // Helper function to show error state
        function showErrorState() {
            const loadingState = document.getElementById('loadingState');
            const emptyState = document.getElementById('emptyState');
            
            loadingState.style.display = 'none';
            emptyState.innerHTML = `
                <i class="fas fa-exclamation-triangle fa-3x text-danger mb-3"></i>
                <h5>Error Loading Requests</h5>
                <p class="text-muted">Failed to load pending vendor requests.</p>
                <button class="btn btn-primary" onclick="loadPendingVendors()">
                    <i class="fas fa-sync-alt me-2"></i>Try Again
                </button>
            `;
            emptyState.style.display = 'block';
        }
        
                 // Approve vendor
         function approveVendor(vendorId) {
             // Validate vendor ID
             if (!vendorId || vendorId === 'N/A' || vendorId === 'undefined') {
                 showAlert('Error: Invalid vendor ID', 'danger');
                 return;
             }
             
             if (confirm('Are you sure you want to approve this vendor? They will be able to login after approval.')) {
                 const url = '/api/admin/vendors/' + vendorId + '/approve';
                 
                 fetch(url, {
                     method: 'POST',
                     headers: {
                         'Content-Type': 'application/json'
                     }
                 })
                 .then(response => {
                     if (response.status === 403) {
                         throw new Error('Access denied (403 Forbidden). Please check if you are logged in as admin.');
                     }
                     
                     if (!response.ok) {
                         throw new Error(`HTTP error! status: ${response.status}`);
                     }
                     
                     return response.json();
                 })
                 .then(data => {
                     if (data.success) {
                         showAlert('Vendor approved successfully!', 'success');
                         loadPendingVendors();
                     } else {
                         showAlert('Error: ' + data.message, 'danger');
                     }
                 })
                 .catch(error => {
                     console.error('Error approving vendor:', error);
                     showAlert('Error approving vendor: ' + error.message, 'danger');
                 });
             }
         }
        
                 // Reject vendor
         function rejectVendor(vendorId) {
             // Validate vendor ID
             if (!vendorId || vendorId === 'N/A' || vendorId === 'undefined') {
                 showAlert('Error: Invalid vendor ID', 'danger');
                 return;
             }
             
             if (confirm('Are you sure you want to reject this vendor? This action cannot be undone.')) {
                 const url = '/api/admin/vendors/' + vendorId + '/reject';
                 
                 fetch(url, {
                     method: 'POST',
                     headers: {
                         'Content-Type': 'application/json'
                     }
                 })
                 .then(response => {
                     if (response.status === 403) {
                         throw new Error('Access denied (403 Forbidden). Please check if you are logged in as admin.');
                     }
                     
                     if (!response.ok) {
                         throw new Error(`HTTP error! status: ${response.status}`);
                     }
                     
                     return response.json();
                 })
                 .then(data => {
                     if (data.success) {
                         showAlert('Vendor rejected successfully!', 'success');
                         loadPendingVendors();
                     } else {
                         showAlert('Error: ' + data.message, 'danger');
                     }
                 })
                 .catch(error => {
                     console.error('Error rejecting vendor:', error);
                     showAlert('Error rejecting vendor: ' + error.message, 'danger');
                 });
             }
         }
         
         // Refresh pending vendors
         function refreshPendingVendors() {
             loadPendingVendors();
         }
         
         // Show alert
         function showAlert(message, type) {
             const alertDiv = document.createElement('div');
             alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
             alertDiv.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
             alertDiv.innerHTML = `
                 ${message}
                 <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
             `;
             document.body.appendChild(alertDiv);
             
             // Auto remove after 3 seconds
             setTimeout(() => {
                 if (alertDiv.parentNode) {
                     alertDiv.parentNode.removeChild(alertDiv);
                 }
             }, 3000);
         }
         
         // Load pending vendors when page loads
         document.addEventListener('DOMContentLoaded', function() {
             loadPendingVendors();
         });
    </script>
</body>
</html>
