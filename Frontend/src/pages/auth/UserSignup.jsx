import { useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import axios from "axios"
import { toast } from "react-toastify"

const UserSignup = () => {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    confirmPassword: "",
    phoneNumber: "",
    address: "",
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

    if (formData.password !== formData.confirmPassword) {
      toast.error("Passwords do not match!")
      return
    }

    setLoading(true)

    try {
      const { confirmPassword, ...submitData } = formData
      const response = await axios.post("http://localhost:8080/api/users/register", submitData)

      if (response.data.success) {
        toast.success("Registration successful! Please login to continue.")
        navigate("/user-login")
      }
    } catch (error) {
      toast.error(error.response?.data?.message || "Registration failed. Please try again.")
    } finally {
      setLoading(false)
    }
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
              <i className="fas fa-user-plus fa-3x text-white mb-3"></i>
              <h3 className="text-white mb-0">Create Account</h3>
              <p className="text-white-50 mb-0">Join us for seamless bus booking experience</p>
            </div>
            <div className="card-body p-5">
              <form onSubmit={handleSubmit}>
                <div className="row">
                  <div className="col-md-6 mb-3">
                    <label htmlFor="firstName" className="form-label">
                      <i className="fas fa-user me-2 text-primary"></i>
                      First Name *
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      id="firstName"
                      name="firstName"
                      value={formData.firstName}
                      onChange={handleInputChange}
                      placeholder="Enter first name"
                      required
                      style={{ borderRadius: "10px" }}
                    />
                  </div>
                  <div className="col-md-6 mb-3">
                    <label htmlFor="lastName" className="form-label">
                      <i className="fas fa-user me-2 text-primary"></i>
                      Last Name *
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      id="lastName"
                      name="lastName"
                      value={formData.lastName}
                      onChange={handleInputChange}
                      placeholder="Enter last name"
                      required
                      style={{ borderRadius: "10px" }}
                    />
                  </div>
                </div>

                <div className="mb-3">
                  <label htmlFor="email" className="form-label">
                    <i className="fas fa-envelope me-2 text-primary"></i>
                    Email Address *
                  </label>
                  <input
                    type="email"
                    className="form-control"
                    id="email"
                    name="email"
                    value={formData.email}
                    onChange={handleInputChange}
                    placeholder="Enter your email"
                    required
                    style={{ borderRadius: "10px" }}
                  />
                </div>

                <div className="row">
                  <div className="col-md-6 mb-3">
                    <label htmlFor="password" className="form-label">
                      <i className="fas fa-lock me-2 text-primary"></i>
                      Password *
                    </label>
                    <input
                      type="password"
                      className="form-control"
                      id="password"
                      name="password"
                      value={formData.password}
                      onChange={handleInputChange}
                      placeholder="Enter password"
                      required
                      style={{ borderRadius: "10px" }}
                    />
                  </div>
                  <div className="col-md-6 mb-3">
                    <label htmlFor="confirmPassword" className="form-label">
                      <i className="fas fa-lock me-2 text-primary"></i>
                      Confirm Password *
                    </label>
                    <input
                      type="password"
                      className="form-control"
                      id="confirmPassword"
                      name="confirmPassword"
                      value={formData.confirmPassword}
                      onChange={handleInputChange}
                      placeholder="Confirm password"
                      required
                      style={{ borderRadius: "10px" }}
                    />
                  </div>
                </div>

                <div className="mb-3">
                  <label htmlFor="phoneNumber" className="form-label">
                    <i className="fas fa-phone me-2 text-primary"></i>
                    Phone Number
                  </label>
                  <input
                    type="tel"
                    className="form-control"
                    id="phoneNumber"
                    name="phoneNumber"
                    value={formData.phoneNumber}
                    onChange={handleInputChange}
                    placeholder="Enter phone number"
                    style={{ borderRadius: "10px" }}
                  />
                </div>

                <div className="mb-4">
                  <label htmlFor="address" className="form-label">
                    <i className="fas fa-map-marker-alt me-2 text-primary"></i>
                    Address
                  </label>
                  <textarea
                    className="form-control"
                    id="address"
                    name="address"
                    value={formData.address}
                    onChange={handleInputChange}
                    placeholder="Enter your address"
                    rows="3"
                    style={{ borderRadius: "10px" }}
                  ></textarea>
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
                      Creating Account...
                    </>
                  ) : (
                    <>
                      <i className="fas fa-user-plus me-2"></i>
                      Create Account
                    </>
                  )}
                </button>
              </form>

              <div className="text-center">
                <p className="text-muted mb-0">
                  Already have an account?{" "}
                  <Link to="/user-login" className="text-decoration-none fw-bold">
                    Sign In Here
                  </Link>
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default UserSignup
