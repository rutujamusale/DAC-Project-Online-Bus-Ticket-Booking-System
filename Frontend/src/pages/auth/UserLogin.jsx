"use client"

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

import { useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import axios from "axios"
import { toast } from "react-toastify"

const UserLogin = () => {
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
      const response = await axios.post("http://localhost:8080/api/users/login", formData)

      // The backend returns token, userId, email, firstName, lastName, message
      if (response.data.token) {
        // Clear any existing vendor login data first
        localStorage.removeItem("vendorLoggedIn")
        localStorage.removeItem("vendorId")
        localStorage.removeItem("vendorName")
        localStorage.removeItem("vendorEmail")
        localStorage.removeItem("vendorToken")
        
        // Set user login data
        localStorage.setItem("userLoggedIn", "true")
        localStorage.setItem("token", response.data.token)
        localStorage.setItem("userId", response.data.userId)
        localStorage.setItem("userEmail", response.data.email)
        localStorage.setItem("userFirstName", response.data.firstName)
        localStorage.setItem("userLastName", response.data.lastName)
        toast.success(response.data.message || "Login successful!")
        navigate("/user-dashboard")
      }
    } catch (error) {
      toast.error(error.response?.data?.message || "Login failed. Please try again.")
    } finally {
      setLoading(false)
    }
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
              <i className="fas fa-user fa-3x text-white mb-3"></i>
              <h3 className="text-white mb-0">User Login</h3>
              <p className="text-white-50 mb-0">Welcome back! Please sign in to your account</p>
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

              <div className="text-center">
                <p className="text-muted mb-0">
                  Don't have an account?{" "}
                  <Link to="/user-signup" className="text-decoration-none fw-bold">
                    Sign Up Here
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

export default UserLogin
