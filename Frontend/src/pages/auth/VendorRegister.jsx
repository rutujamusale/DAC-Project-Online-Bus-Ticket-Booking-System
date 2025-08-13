import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const VendorRegister = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        vendorName: '',
        email: '',
        phoneNumber: '',
        password: '',
        confirmPassword: '',
        address: '',
        licenseNumber: ''
    });
    const [errors, setErrors] = useState({});
    const [isLoading, setIsLoading] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
        
        // Clear error when user starts typing
        if (errors[name]) {
            setErrors(prev => ({
                ...prev,
                [name]: ''
            }));
        }
    };

    const validateForm = () => {
        const newErrors = {};
        
        // Vendor Name validation
        if (!formData.vendorName.trim()) {
            newErrors.vendorName = 'Vendor name is required';
        }
        
        // Email validation
        if (!formData.email.trim()) {
            newErrors.email = 'Email is required';
        } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
            newErrors.email = 'Please enter a valid email address';
        }
        
        // Phone Number validation
        if (!formData.phoneNumber.trim()) {
            newErrors.phoneNumber = 'Phone number is required';
        } else if (!/^[6-9]\d{9}$|^\+91[6-9]\d{9}$|^91[6-9]\d{9}$/.test(formData.phoneNumber)) {
            newErrors.phoneNumber = 'Please enter a valid Indian phone number';
        }
        
        // Password validation
        if (!formData.password.trim()) {
            newErrors.password = 'Password is required';
        } else if (formData.password.length < 6) {
            newErrors.password = 'Password must be at least 6 characters long';
        }
        
        // Confirm Password validation
        if (!formData.confirmPassword.trim()) {
            newErrors.confirmPassword = 'Please confirm your password';
        } else if (formData.password !== formData.confirmPassword) {
            newErrors.confirmPassword = 'Passwords do not match';
        }
        
        // Address validation
        if (!formData.address.trim()) {
            newErrors.address = 'Address is required';
        }
        
        // License Number validation
        if (!formData.licenseNumber.trim()) {
            newErrors.licenseNumber = 'License number is required';
        }
        
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        if (!validateForm()) {
            toast.error('Please fix the errors in the form');
            return;
        }
        
        setIsLoading(true);
        
        try {
            const response = await axios.post(
                'http://localhost:8080/api/vendor/register',
                {
                    vendorName: formData.vendorName,
                    email: formData.email,
                    phoneNumber: formData.phoneNumber,
                    password: formData.password,
                    address: formData.address,
                    licenseNumber: formData.licenseNumber
                },
                {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }
            );
            
            if (response.data.success) {
                toast.success('Registration submitted successfully! Please wait for admin approval.');
                navigate('/vendor-login');
            } else {
                toast.error(response.data.message || 'Registration failed');
            }
            
        } catch (error) {
            console.error('Error registering vendor:', error);
            
            if (error.response) {
                const errorMessage = error.response.data.message || 'Registration failed';
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

    const handleCancel = () => {
        navigate('/vendor-login');
    };

    return (
        <div className="container py-5">
            <div className="row justify-content-center">
                <div className="col-md-8 col-lg-6">
                    <div className="card shadow-lg border-0" style={{ borderRadius: "15px" }}>
                        <div
                            className="card-header text-center py-4"
                            style={{ background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)", borderRadius: "15px 15px 0 0" }}
                        >
                            <i className="fas fa-user-plus fa-3x text-white mb-3"></i>
                            <h3 className="text-white mb-0">Vendor Registration</h3>
                            <p className="text-white-50 mb-0">Register for a new vendor account</p>
                        </div>
                        <div className="card-body p-5">
                            <form onSubmit={handleSubmit}>
                                {/* Vendor Name */}
                                <div className="mb-4">
                                    <label htmlFor="vendorName" className="form-label">
                                        <i className="fas fa-building me-2 text-primary"></i>
                                        Vendor Name
                                    </label>
                                    <input
                                        type="text"
                                        className={`form-control form-control-lg ${errors.vendorName ? 'is-invalid' : ''}`}
                                        id="vendorName"
                                        name="vendorName"
                                        value={formData.vendorName}
                                        onChange={handleInputChange}
                                        placeholder="Enter vendor name"
                                        required
                                        style={{ borderRadius: "10px" }}
                                    />
                                    {errors.vendorName && (
                                        <div className="invalid-feedback">{errors.vendorName}</div>
                                    )}
                                </div>

                                {/* Email */}
                                <div className="mb-4">
                                    <label htmlFor="email" className="form-label">
                                        <i className="fas fa-envelope me-2 text-primary"></i>
                                        Email Address
                                    </label>
                                    <input
                                        type="email"
                                        className={`form-control form-control-lg ${errors.email ? 'is-invalid' : ''}`}
                                        id="email"
                                        name="email"
                                        value={formData.email}
                                        onChange={handleInputChange}
                                        placeholder="Enter email address"
                                        required
                                        style={{ borderRadius: "10px" }}
                                    />
                                    {errors.email && (
                                        <div className="invalid-feedback">{errors.email}</div>
                                    )}
                                </div>

                                {/* Phone Number */}
                                <div className="mb-4">
                                    <label htmlFor="phoneNumber" className="form-label">
                                        <i className="fas fa-phone me-2 text-primary"></i>
                                        Phone Number
                                    </label>
                                    <input
                                        type="tel"
                                        className={`form-control form-control-lg ${errors.phoneNumber ? 'is-invalid' : ''}`}
                                        id="phoneNumber"
                                        name="phoneNumber"
                                        value={formData.phoneNumber}
                                        onChange={handleInputChange}
                                        placeholder="Enter phone number (e.g., 9876543210)"
                                        required
                                        style={{ borderRadius: "10px" }}
                                    />
                                    {errors.phoneNumber && (
                                        <div className="invalid-feedback">{errors.phoneNumber}</div>
                                    )}
                                </div>

                                {/* Password */}
                                <div className="mb-4">
                                    <label htmlFor="password" className="form-label">
                                        <i className="fas fa-lock me-2 text-primary"></i>
                                        Password
                                    </label>
                                    <div className="position-relative">
                                        <input
                                            type={showPassword ? "text" : "password"}
                                            className={`form-control form-control-lg ${errors.password ? 'is-invalid' : ''}`}
                                            id="password"
                                            name="password"
                                            value={formData.password}
                                            onChange={handleInputChange}
                                            placeholder="Enter password (min 6 characters)"
                                            required
                                            style={{ borderRadius: "10px" }}
                                        />
                                        <button
                                            type="button"
                                            className="btn btn-link position-absolute end-0 top-0 h-100 d-flex align-items-center pe-3"
                                            onClick={() => setShowPassword(!showPassword)}
                                            style={{ zIndex: 10 }}
                                        >
                                            <i className={`fas ${showPassword ? 'fa-eye-slash' : 'fa-eye'} text-muted`}></i>
                                        </button>
                                    </div>
                                    {errors.password && (
                                        <div className="invalid-feedback">{errors.password}</div>
                                    )}
                                </div>

                                {/* Confirm Password */}
                                <div className="mb-4">
                                    <label htmlFor="confirmPassword" className="form-label">
                                        <i className="fas fa-check-circle me-2 text-primary"></i>
                                        Confirm Password
                                    </label>
                                    <div className="position-relative">
                                        <input
                                            type={showConfirmPassword ? "text" : "password"}
                                            className={`form-control form-control-lg ${errors.confirmPassword ? 'is-invalid' : ''}`}
                                            id="confirmPassword"
                                            name="confirmPassword"
                                            value={formData.confirmPassword}
                                            onChange={handleInputChange}
                                            placeholder="Confirm password"
                                            required
                                            style={{ borderRadius: "10px" }}
                                        />
                                        <button
                                            type="button"
                                            className="btn btn-link position-absolute end-0 top-0 h-100 d-flex align-items-center pe-3"
                                            onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                                            style={{ zIndex: 10 }}
                                        >
                                            <i className={`fas ${showConfirmPassword ? 'fa-eye-slash' : 'fa-eye'} text-muted`}></i>
                                        </button>
                                    </div>
                                    {errors.confirmPassword && (
                                        <div className="invalid-feedback">{errors.confirmPassword}</div>
                                    )}
                                </div>

                                {/* Address */}
                                <div className="mb-4">
                                    <label htmlFor="address" className="form-label">
                                        <i className="fas fa-map-marker-alt me-2 text-primary"></i>
                                        Address
                                    </label>
                                    <textarea
                                        className={`form-control form-control-lg ${errors.address ? 'is-invalid' : ''}`}
                                        id="address"
                                        name="address"
                                        value={formData.address}
                                        onChange={handleInputChange}
                                        placeholder="Enter complete address"
                                        rows="3"
                                        required
                                        style={{ borderRadius: "10px" }}
                                    ></textarea>
                                    {errors.address && (
                                        <div className="invalid-feedback">{errors.address}</div>
                                    )}
                                </div>

                                {/* License Number */}
                                <div className="mb-4">
                                    <label htmlFor="licenseNumber" className="form-label">
                                        <i className="fas fa-id-card me-2 text-primary"></i>
                                        License Number
                                    </label>
                                    <input
                                        type="text"
                                        className={`form-control form-control-lg ${errors.licenseNumber ? 'is-invalid' : ''}`}
                                        id="licenseNumber"
                                        name="licenseNumber"
                                        value={formData.licenseNumber}
                                        onChange={handleInputChange}
                                        placeholder="Enter license number"
                                        required
                                        style={{ borderRadius: "10px" }}
                                    />
                                    {errors.licenseNumber && (
                                        <div className="invalid-feedback">{errors.licenseNumber}</div>
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
                                                Registering...
                                            </>
                                        ) : (
                                            <>
                                                <i className="fas fa-user-plus me-2"></i>
                                                Register Vendor
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
                                        Back to Login
                                    </button>
                                </div>
                            </form>

                            {/* Info Alert */}
                            <div className="mt-4">
                                <div className="alert alert-info">
                                    <i className="fas fa-info-circle me-2"></i>
                                    <strong>Important:</strong> Your registration will be reviewed by an administrator. 
                                    You will be notified via email once your account is approved and you can then login.
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default VendorRegister;
