import { useState, useEffect } from "react"
import { useNavigate } from "react-router-dom"
import axios from "axios"
import { toast } from "react-toastify"

const VendorProfile = () => {
  const [formData, setFormData] = useState({
    vendorName: "",
    email: "",
    phoneNumber: "",
    address: "",
    licenseNumber: ""
  })
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    const vendorLoggedIn = localStorage.getItem("vendorLoggedIn")
    if (!vendorLoggedIn) {
      toast.error("Please login as vendor to access this page")
      navigate("/vendor-login")
      return
    }
    fetchVendorProfile()
  }, [navigate])

  const fetchVendorProfile = async () => {
    try {
      const vendorEmail = localStorage.getItem("vendorEmail")
      const token = localStorage.getItem("vendorToken")
      
      if (!vendorEmail || !token) {
        toast.error("Vendor information not found. Please login again.")
        navigate("/vendor-login")
        return
      }

      const response = await axios.get(`http://localhost:8080/api/vendor/profile/${vendorEmail}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      })

      const vendorData = response.data
      setFormData({
        vendorName: vendorData.vendorName || "",
        email: vendorData.email || "",
        phoneNumber: vendorData.phoneNumber || "",
        address: vendorData.address || "",
        licenseNumber: vendorData.licenseNumber || ""
      })
    } catch (error) {
      console.error("Error fetching vendor profile:", error)
      if (error.response?.status === 403) {
        toast.error("Access denied. Please login again.")
        navigate("/vendor-login")
      } else {
        toast.error("Error loading profile: " + (error.response?.data?.message || error.message))
      }
    } finally {
      setLoading(false)
    }
  }

  const handleInputChange = (e) => {
    const { name, value } = e.target
    
    // Special handling for phone number
    if (name === 'phoneNumber') {
      let cleanedValue = value.replace(/[^\d+\s]/g, '')
      
      // Handle +91 prefix
      if (cleanedValue.startsWith('+91')) {
        const digitsAfterPrefix = cleanedValue.substring(3).replace(/\D/g, '').slice(0, 10)
        setFormData(prev => ({ ...prev, [name]: '+91' + digitsAfterPrefix }))
      } else if (cleanedValue.startsWith('91')) {
        const digitsAfterPrefix = cleanedValue.substring(2).replace(/\D/g, '').slice(0, 10)
        setFormData(prev => ({ ...prev, [name]: '91' + digitsAfterPrefix }))
      } else {
        const numericValue = cleanedValue.replace(/\D/g, '').slice(0, 10)
        setFormData(prev => ({ ...prev, [name]: numericValue }))
      }
    } else {
      setFormData(prev => ({ ...prev, [name]: value }))
    }
  }

  const validatePhoneNumber = (phone) => {
    const phoneRegex = /^[6-9]\d{9}$|^\+91[6-9]\d{9}$|^91[6-9]\d{9}$/
    return phoneRegex.test(phone.trim())
  }

  const validateForm = () => {
    if (!formData.vendorName.trim()) {
      toast.error("Please enter vendor name")
      return false
    }
    
    if (!formData.email.trim()) {
      toast.error("Please enter email address")
      return false
    }
    
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(formData.email)) {
      toast.error("Please enter a valid email address")
      return false
    }
    
    if (formData.phoneNumber && !validatePhoneNumber(formData.phoneNumber)) {
      toast.error("Please enter a valid Indian mobile number starting with 6, 7, 8, or 9")
      return false
    }
    
    return true
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    if (!validateForm()) {
      return
    }

    setSaving(true)
    
    try {
      const vendorId = localStorage.getItem("vendorId")
      const token = localStorage.getItem("vendorToken")
      
      if (!vendorId || !token) {
        toast.error("Vendor information not found. Please login again.")
        navigate("/vendor-login")
        return
      }

      const updateData = {
        vendorName: formData.vendorName.trim(),
        email: formData.email.trim(),
        phoneNumber: formData.phoneNumber.trim(),
        address: formData.address.trim(),
        licenseNumber: formData.licenseNumber.trim()
      }

      const response = await axios.put(`http://localhost:8080/api/vendor/${vendorId}/profile`, updateData, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      })

      if (response.data.success) {
        toast.success("Profile updated successfully!")
        // Update localStorage with new vendor name if it changed
        if (formData.vendorName !== localStorage.getItem("vendorName")) {
          localStorage.setItem("vendorName", formData.vendorName)
        }
        if (formData.email !== localStorage.getItem("vendorEmail")) {
          localStorage.setItem("vendorEmail", formData.email)
        }
      } else {
        toast.error(response.data.message || "Failed to update profile")
      }
    } catch (error) {
      console.error("Error updating vendor profile:", error)
      if (error.response?.status === 400) {
        toast.error("❌ " + (error.response?.data?.message || "Invalid data provided"))
      } else if (error.response?.status === 403) {
        toast.error("❌ Access denied. Please login again.")
        navigate("/vendor-login")
      } else if (error.response?.status === 500) {
        toast.error("❌ Server error. Please try again later.")
      } else {
        toast.error("❌ Error updating profile: " + (error.response?.data?.message || error.message))
      }
    } finally {
      setSaving(false)
    }
  }

  if (loading) {
    return (
      <div className="container py-5">
        <div className="text-center">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="mt-3">Loading profile...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-md-8 col-lg-6">
          <div className="card shadow-lg border-0" style={{ borderRadius: "15px" }}>
            <div
              className="card-header text-center py-4"
              style={{ background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)", borderRadius: "15px 15px 0 0" }}
            >
              <i className="fas fa-user-edit fa-3x text-white mb-3"></i>
              <h3 className="text-white mb-0">Edit Profile</h3>
              <p className="text-white-50 mb-0">Update your vendor information</p>
            </div>
            
            <div className="card-body p-5">
              <form onSubmit={handleSubmit} className="vendor-profile-form">
                <div className="mb-4">
                  <label htmlFor="vendorName" className="form-label">
                    <i className="fas fa-building me-2 text-primary"></i>
                    Vendor Name *
                  </label>
                  <input
                    type="text"
                    className="form-control form-control-lg"
                    id="vendorName"
                    name="vendorName"
                    value={formData.vendorName}
                    onChange={handleInputChange}
                    placeholder="Enter vendor name"
                    required
                    style={{ borderRadius: "10px" }}
                  />
                </div>

                <div className="mb-4">
                  <label htmlFor="email" className="form-label">
                    <i className="fas fa-envelope me-2 text-primary"></i>
                    Email Address *
                  </label>
                  <input
                    type="email"
                    className="form-control form-control-lg"
                    id="email"
                    name="email"
                    value={formData.email}
                    onChange={handleInputChange}
                    placeholder="Enter email address"
                    required
                    style={{ borderRadius: "10px" }}
                  />
                </div>

                <div className="mb-4">
                  <label htmlFor="phoneNumber" className="form-label">
                    <i className="fas fa-phone me-2 text-primary"></i>
                    Phone Number
                  </label>
                  <input
                    type="tel"
                    className={`form-control form-control-lg ${formData.phoneNumber && !validatePhoneNumber(formData.phoneNumber) ? 'is-invalid' : formData.phoneNumber && validatePhoneNumber(formData.phoneNumber) ? 'is-valid' : ''}`}
                    id="phoneNumber"
                    name="phoneNumber"
                    value={formData.phoneNumber}
                    onChange={handleInputChange}
                    placeholder="e.g., 9876543210 or +919876543210"
                    style={{ borderRadius: "10px" }}
                  />
                  <small className="text-muted">
                    Enter 10-digit Indian mobile number starting with 6, 7, 8, or 9
                  </small>
                  {formData.phoneNumber && !validatePhoneNumber(formData.phoneNumber) && (
                    <div className="invalid-feedback">
                      Please enter a valid Indian mobile number
                    </div>
                  )}
                  {formData.phoneNumber && validatePhoneNumber(formData.phoneNumber) && (
                    <div className="valid-feedback">
                      ✓ Valid phone number
                    </div>
                  )}
                </div>

                <div className="mb-4">
                  <label htmlFor="address" className="form-label">
                    <i className="fas fa-map-marker-alt me-2 text-primary"></i>
                    Address
                  </label>
                  <textarea
                    className="form-control form-control-lg"
                    id="address"
                    name="address"
                    value={formData.address}
                    onChange={handleInputChange}
                    placeholder="Enter your address"
                    rows="3"
                    style={{ borderRadius: "10px" }}
                  ></textarea>
                </div>

                <div className="mb-4">
                  <label htmlFor="licenseNumber" className="form-label">
                    <i className="fas fa-id-card me-2 text-primary"></i>
                    License Number
                  </label>
                  <input
                    type="text"
                    className="form-control form-control-lg"
                    id="licenseNumber"
                    name="licenseNumber"
                    value={formData.licenseNumber}
                    onChange={handleInputChange}
                    placeholder="Enter license number"
                    style={{ borderRadius: "10px" }}
                  />
                </div>

                <div className="d-grid gap-2">
                  <button
                    type="submit"
                    className="btn btn-primary btn-lg"
                    disabled={saving}
                    style={{ borderRadius: "10px" }}
                  >
                    {saving ? (
                      <>
                        <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                        Updating...
                      </>
                    ) : (
                      <>
                        <i className="fas fa-save me-2"></i>
                        Update Profile
                      </>
                    )}
                  </button>
                  
                  <button
                    type="button"
                    className="btn btn-outline-secondary btn-lg"
                    onClick={() => navigate("/vendor-dashboard")}
                    style={{ borderRadius: "10px" }}
                  >
                    <i className="fas fa-arrow-left me-2"></i>
                    Back to Dashboard
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default VendorProfile
