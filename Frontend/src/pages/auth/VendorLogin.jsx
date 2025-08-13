import { useState } from "react"
import { useNavigate } from "react-router-dom"
import axios from "axios"
import { toast } from "react-toastify"

const VendorLogin = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  })
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const handleInputChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)

    try {
      const response = await axios.post("http://localhost:8080/api/vendor/login", formData)

      if (response.data.vendorId) {
        // Clear any existing user login data first
        localStorage.removeItem("userLoggedIn")
        localStorage.removeItem("userToken")
        localStorage.removeItem("userId")
        localStorage.removeItem("userEmail")
        localStorage.removeItem("userFirstName")
        localStorage.removeItem("userLastName")
        
        // Set vendor login data
        localStorage.setItem("vendorLoggedIn", "true")
        localStorage.setItem("vendorId", response.data.vendorId)
        localStorage.setItem("vendorName", response.data.vendorName)
        localStorage.setItem("vendorEmail", response.data.email)
        localStorage.setItem("vendorToken", response.data.token)
        toast.success("Login successful!")
        navigate("/vendor-dashboard")
      }
    } catch (error) {
      toast.error(error.response?.data?.message || "Login failed. Please try again.")
    } finally {
      setLoading(false)
    }
  }

  const handleRegisterClick = () => {
    navigate("/vendor-register")
  }

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-md-6 col-lg-5">
          <div className="card shadow-lg border-0" style={{ borderRadius: "15px" }}>
            <div
              className="card-header text-center py-4"
              style={{ background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)", borderRadius: "15px 15px 0 0" }}
            >
              <i className="fas fa-building fa-3x text-white mb-3"></i>
              <h3 className="text-white mb-0">Vendor Login</h3>
              <p className="text-white-50 mb-0">Access your vendor dashboard</p>
            </div>
            <div className="card-body p-5">
              <form onSubmit={handleSubmit}>
                <div className="mb-4">
                  <label htmlFor="email" className="form-label">
                    <i className="fas fa-envelope me-2 text-primary"></i>
                    Email Address
                  </label>
                  <input
                    type="email"
                    className="form-control form-control-lg"
                    id="email"
                    name="email"
                    value={formData.email}
                    onChange={handleInputChange}
                    placeholder="Enter your email"
                    required
                    style={{ borderRadius: "10px" }}
                  />
                </div>

                <div className="mb-4">
                  <label htmlFor="password" className="form-label">
                    <i className="fas fa-lock me-2 text-primary"></i>
                    Password
                  </label>
                  <input
                    type="password"
                    className="form-control form-control-lg"
                    id="password"
                    name="password"
                    value={formData.password}
                    onChange={handleInputChange}
                    placeholder="Enter your password"
                    required
                    style={{ borderRadius: "10px" }}
                  />
                </div>

                <button
                  type="submit"
                  className="btn btn-primary btn-lg w-100 mb-3"
                  disabled={loading}
                  style={{
                    background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                    border: "none",
                    borderRadius: "10px",
                  }}
                >
                  {loading ? (
                    <>
                      <span className="spinner-border spinner-border-sm me-2"></span>
                      Signing In...
                    </>
                  ) : (
                    <>
                      <i className="fas fa-sign-in-alt me-2"></i>
                      Sign In
                    </>
                  )}
                </button>
              </form>

              {/* Register New Vendor Button */}
              <div className="text-center mb-3">
                <button
                  type="button"
                  onClick={handleRegisterClick}
                  className="btn btn-outline-primary btn-lg w-100"
                  style={{ borderRadius: "10px" }}
                >
                  <i className="fas fa-user-plus me-2"></i>
                  Register New Vendor
                </button>
              </div>

              <div className="text-center">
                <div className="alert alert-info">
                  <i className="fas fa-info-circle me-2"></i>
                  <strong>Note:</strong> New vendor registrations require admin approval before login access.
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default VendorLogin
