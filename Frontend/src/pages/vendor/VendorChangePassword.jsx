import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const VendorChangePassword = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        currentPassword: '',
        newPassword: '',
        confirmPassword: ''
    });
    const [errors, setErrors] = useState({});
    const [touched, setTouched] = useState({
        currentPassword: false,
        newPassword: false,
        confirmPassword: false
    });
    const [isLoading, setIsLoading] = useState(false);
    const [showPasswords, setShowPasswords] = useState({
        current: false,
        new: false,
        confirm: false
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
        
        // Mark field as touched
        setTouched(prev => ({
            ...prev,
            [name]: true
        }));
        
        // Clear error when user starts typing
        if (errors[name]) {
            setErrors(prev => ({
                ...prev,
                [name]: ''
            }));
        }
    };

    const handleBlur = (e) => {
        const { name } = e.target;
        setTouched(prev => ({
            ...prev,
            [name]: true
        }));
        
        // Validate individual field on blur
        validateField(name, formData[name]);
    };

    const validateField = (fieldName, value) => {
        let error = '';
        
        switch (fieldName) {
            case 'currentPassword':
                if (!value.trim()) {
                    error = 'Current password is required';
                }
                break;
            case 'newPassword':
                if (!value.trim()) {
                    error = 'New password is required';
                } else if (value.length < 6) {
                    error = 'Password must be at least 6 characters long';
                } else if (!/^[A-Za-z\d@$!%*?&]{6,}$/.test(value)) {
                    error = 'Password can only contain letters, numbers, and special characters (@$!%*?&)';
                } else if (formData.currentPassword && value === formData.currentPassword) {
                    error = 'New password must be different from current password';
                }
                break;
            case 'confirmPassword':
                if (!value.trim()) {
                    error = 'Please confirm your new password';
                } else if (formData.newPassword && value !== formData.newPassword) {
                    error = 'Passwords do not match';
                }
                break;
            default:
                break;
        }
        
        setErrors(prev => ({
            ...prev,
            [fieldName]: error
        }));
        
        return !error;
    };

    const validateForm = () => {
        const newErrors = {};
        let isValid = true;
        
        // Validate all fields
        Object.keys(formData).forEach(fieldName => {
            const fieldValid = validateField(fieldName, formData[fieldName]);
            if (!fieldValid) {
                isValid = false;
            }
        });
        
        return isValid;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        // Mark all fields as touched
        setTouched({
            currentPassword: true,
            newPassword: true,
            confirmPassword: true
        });
        
        if (!validateForm()) {
            toast.error('Please fix the errors in the form');
            return;
        }
        
        setIsLoading(true);
        
        try {
            const vendorId = localStorage.getItem('vendorId');
            const token = localStorage.getItem('vendorToken');
            
            if (!vendorId || !token) {
                toast.error('Please login again');
                navigate('/vendor-login');
                return;
            }
            
            const response = await axios.put(
                `http://localhost:8080/api/vendor/${vendorId}/change-password`,
                formData,
                {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    }
                }
            );
            
            if (response.data.success) {
                toast.success('Password changed successfully!');
                setFormData({
                    currentPassword: '',
                    newPassword: '',
                    confirmPassword: ''
                });
                setTouched({
                    currentPassword: false,
                    newPassword: false,
                    confirmPassword: false
                });
                setShowPasswords({
                    current: false,
                    new: false,
                    confirm: false
                });
            } else {
                toast.error(response.data.message || 'Failed to change password');
            }
            
        } catch (error) {
            console.error('Error changing password:', error);
            
            if (error.response) {
                const errorMessage = error.response.data.message || 'Failed to change password';
                toast.error(errorMessage);
            } else if (error.request) {
                toast.error('Network error. Please check your connection.');
            } else {
                toast.error('An unexpected error occurred');
            }
        } finally {
            setIsLoading(false);
        }
    };

    const togglePasswordVisibility = (field) => {
        setShowPasswords(prev => ({
            ...prev,
            [field]: !prev[field]
        }));
    };

    const handleCancel = () => {
        navigate('/vendor-dashboard');
    };

    return (
        <div className="container py-5">
            <div className="row justify-content-center">
                <div className="col-md-6 col-lg-5">
                    <div className="card shadow-lg border-0" style={{ borderRadius: "15px" }}>
                        <div
                            className="card-header text-center py-4"
                            style={{ background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)", borderRadius: "15px 15px 0 0" }}
                        >
                            <i className="fas fa-key fa-3x text-white mb-3"></i>
                            <h3 className="text-white mb-0">Change Password</h3>
                            <p className="text-white-50 mb-0">Update your account password securely</p>
                        </div>
                        <div className="card-body p-5">
                            <form onSubmit={handleSubmit}>
                                {/* Current Password */}
                                <div className="mb-4">
                                    <label htmlFor="currentPassword" className="form-label">
                                        <i className="fas fa-lock me-2 text-primary"></i>
                                        Current Password
                                    </label>
                                    <div className="position-relative">
                                        <input
                                            type={showPasswords.current ? "text" : "password"}
                                            className={`form-control form-control-lg ${errors.currentPassword && touched.currentPassword ? 'is-invalid' : ''}`}
                                            id="currentPassword"
                                            name="currentPassword"
                                            value={formData.currentPassword}
                                            onChange={handleInputChange}
                                            onBlur={handleBlur}
                                            placeholder="Enter your current password"
                                            required
                                            style={{ borderRadius: "10px" }}
                                        />
                                        <button
                                            type="button"
                                            className="btn btn-link position-absolute end-0 top-0 h-100 d-flex align-items-center pe-3"
                                            onClick={() => togglePasswordVisibility('current')}
                                            style={{ zIndex: 10 }}
                                        >
                                            <i className={`fas ${showPasswords.current ? 'fa-eye-slash' : 'fa-eye'} text-muted`}></i>
                                        </button>
                                    </div>
                                    {errors.currentPassword && touched.currentPassword && (
                                        <div className="invalid-feedback d-block">
                                            {errors.currentPassword}
                                        </div>
                                    )}
                                </div>

                                {/* New Password */}
                                <div className="mb-4">
                                    <label htmlFor="newPassword" className="form-label">
                                        <i className="fas fa-lock me-2 text-primary"></i>
                                        New Password
                                    </label>
                                    <div className="position-relative">
                                        <input
                                            type={showPasswords.new ? "text" : "password"}
                                            className={`form-control form-control-lg ${errors.newPassword && touched.newPassword ? 'is-invalid' : ''}`}
                                            id="newPassword"
                                            name="newPassword"
                                            value={formData.newPassword}
                                            onChange={handleInputChange}
                                            onBlur={handleBlur}
                                            placeholder="Enter your new password"
                                            required
                                            style={{ borderRadius: "10px" }}
                                        />
                                        <button
                                            type="button"
                                            className="btn btn-link position-absolute end-0 top-0 h-100 d-flex align-items-center pe-3"
                                            onClick={() => togglePasswordVisibility('new')}
                                            style={{ zIndex: 10 }}
                                        >
                                            <i className={`fas ${showPasswords.new ? 'fa-eye-slash' : 'fa-eye'} text-muted`}></i>
                                        </button>
                                    </div>
                                    {errors.newPassword && touched.newPassword && (
                                        <div className="invalid-feedback d-block">
                                            {errors.newPassword}
                                        </div>
                                    )}
                                    
                                    {/* Password Requirements */}
                                    {formData.newPassword && (
                                        <div className="mt-2">
                                            <small className="text-muted">Password Requirements:</small>
                                            <ul className="list-unstyled small text-muted mt-1">
                                                <li className={`${formData.newPassword.length >= 6 ? 'text-success' : ''}`}>
                                                    <i className={`fas ${formData.newPassword.length >= 6 ? 'fa-check' : 'fa-circle'} me-1`}></i>
                                                    At least 6 characters
                                                </li>
                                            </ul>
                                        </div>
                                    )}
                                </div>

                                {/* Confirm Password */}
                                <div className="mb-4">
                                    <label htmlFor="confirmPassword" className="form-label">
                                        <i className="fas fa-check-circle me-2 text-primary"></i>
                                        Confirm New Password
                                    </label>
                                    <div className="position-relative">
                                        <input
                                            type={showPasswords.confirm ? "text" : "password"}
                                            className={`form-control form-control-lg ${errors.confirmPassword && touched.confirmPassword ? 'is-invalid' : formData.confirmPassword && formData.newPassword === formData.confirmPassword ? 'is-valid' : ''}`}
                                            id="confirmPassword"
                                            name="confirmPassword"
                                            value={formData.confirmPassword}
                                            onChange={handleInputChange}
                                            onBlur={handleBlur}
                                            placeholder="Confirm your new password"
                                            required
                                            style={{ borderRadius: "10px" }}
                                        />
                                        <button
                                            type="button"
                                            className="btn btn-link position-absolute end-0 top-0 h-100 d-flex align-items-center pe-3"
                                            onClick={() => togglePasswordVisibility('confirm')}
                                            style={{ zIndex: 10 }}
                                        >
                                            <i className={`fas ${showPasswords.confirm ? 'fa-eye-slash' : 'fa-eye'} text-muted`}></i>
                                        </button>
                                    </div>
                                    {errors.confirmPassword && touched.confirmPassword && (
                                        <div className="invalid-feedback d-block">
                                            {errors.confirmPassword}
                                        </div>
                                    )}
                                    {formData.confirmPassword && formData.newPassword === formData.confirmPassword && !errors.confirmPassword && (
                                        <div className="valid-feedback d-block">
                                            Passwords match
                                        </div>
                                    )}
                                </div>

                                {/* Buttons */}
                                <div className="d-grid gap-2">
                                    <button
                                        type="submit"
                                        className="btn btn-primary btn-lg"
                                        disabled={isLoading}
                                        style={{
                                            background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                                            border: "none",
                                            borderRadius: "10px",
                                        }}
                                    >
                                        {isLoading ? (
                                            <>
                                                <span className="spinner-border spinner-border-sm me-2"></span>
                                                Changing Password...
                                            </>
                                        ) : (
                                            <>
                                                <i className="fas fa-save me-2"></i>
                                                Change Password
                                            </>
                                        )}
                                    </button>
                                    
                                    <button
                                        type="button"
                                        onClick={handleCancel}
                                        className="btn btn-outline-secondary btn-lg"
                                        style={{ borderRadius: "10px" }}
                                    >
                                        <i className="fas fa-arrow-left me-2"></i>
                                        Cancel
                                    </button>
                                </div>
                            </form>

                            {/* Security Tips */}
                            <div className="mt-4">
                                <div className="alert alert-info">
                                    <i className="fas fa-shield-alt me-2"></i>
                                    <strong>Security Tips:</strong>
                                    <ul className="mb-0 mt-2">
                                        <li>Use a unique password that you don't use elsewhere</li>
                                        <li>Include a mix of letters, numbers, and symbols</li>
                                        <li>Avoid using personal information like birthdays</li>
                                        <li>Consider using a password manager for better security</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default VendorChangePassword;
