import { useState, useEffect } from "react"
import { useNavigate, useLocation } from "react-router-dom"
import axios from "axios"
import { toast } from "react-toastify"

const AccountDeactivation = () => {
  const [loading, setLoading] = useState(false)
  const [accountType, setAccountType] = useState("")
  const [accountName, setAccountName] = useState("")
  const [accountId, setAccountId] = useState(null)
  const navigate = useNavigate()
  const location = useLocation()

  useEffect(() => {
    // Get account type from localStorage or URL params
    const userLoggedIn = localStorage.getItem("userLoggedIn")
    const vendorLoggedIn = localStorage.getItem("vendorLoggedIn")
    
    console.log("AccountDeactivation: userLoggedIn =", userLoggedIn)
    console.log("AccountDeactivation: vendorLoggedIn =", vendorLoggedIn)
    
    if (userLoggedIn) {
      setAccountType("user")
      setAccountName(`${localStorage.getItem("userFirstName")} ${localStorage.getItem("userLastName")}`)
      setAccountId(localStorage.getItem("userId"))
      console.log("AccountDeactivation: Set user account, ID =", localStorage.getItem("userId"))
    } else if (vendorLoggedIn) {
      setAccountType("vendor")
      setAccountName(localStorage.getItem("vendorName"))
      setAccountId(localStorage.getItem("vendorId"))
      console.log("AccountDeactivation: Set vendor account, ID =", localStorage.getItem("vendorId"))
    } else {
      console.log("AccountDeactivation: No user logged in, redirecting to home")
      // Redirect to home if not logged in
      navigate("/")
    }
  }, [navigate])

  const handleConfirmDeactivation = async () => {
    setLoading(true)
    
    try {
      if (!accountId) {
        console.error("AccountDeactivation: No account ID found")
        toast.error("Account ID not found. Please login again.")
        return
      }

      console.log("AccountDeactivation: Starting deactivation for", accountType, "with ID:", accountId)

      let response
      const baseUrl = "http://localhost:8080"
      
      if (accountType === "user") {
        console.log("AccountDeactivation: Calling user deactivation API")
        response = await axios.put(`${baseUrl}/api/users/${accountId}/deactivate`, {}, {
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          }
        })
      } else if (accountType === "vendor") {
        console.log("AccountDeactivation: Calling vendor deactivation API")
        response = await axios.put(`${baseUrl}/api/vendor/${accountId}/deactivate`, {}, {
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          }
        })
      } else {
        console.error("AccountDeactivation: Invalid account type:", accountType)
        toast.error("Invalid account type")
        return
      }

      console.log("AccountDeactivation: API response:", response.data)

      if (response.data.success) {
        toast.success("Account deactivated successfully")
        
        // Clear all localStorage data
        localStorage.clear()
        
        // Redirect to home page
        setTimeout(() => {
          navigate("/")
        }, 2000)
      } else {
        console.error("AccountDeactivation: API returned success=false:", response.data.message)
        toast.error(response.data.message || "Failed to deactivate account")
      }
    } catch (error) {
      console.error("AccountDeactivation: Error during deactivation:", error)
      console.error("AccountDeactivation: Error response:", error.response?.data)
      console.error("AccountDeactivation: Error status:", error.response?.status)
      console.error("AccountDeactivation: Error config:", error.config)
      
      if (error.response?.status === 404) {
        toast.error("Account not found. Please check your login status.")
      } else if (error.response?.status === 400) {
        toast.error(error.response?.data?.message || "Invalid request")
      } else if (error.response?.status === 403) {
        toast.error("Access denied. Please check your permissions.")
      } else if (error.response?.status === 500) {
        toast.error("Server error. Please try again later.")
      } else if (error.code === 'ERR_NETWORK') {
        toast.error("Network error. Please check your connection and try again.")
      } else {
        toast.error(error.response?.data?.message || "Error deactivating account")
      }
    } finally {
      setLoading(false)
    }
  }

  const handleCancel = () => {
    // Go back to previous page or dashboard
    if (accountType === "user") {
      navigate("/user-dashboard")
    } else if (accountType === "vendor") {
      navigate("/vendor-dashboard")
    } else {
      navigate("/")
    }
  }

  if (!accountType) {
    return (
      <div className="container py-5">
        <div className="text-center">
          <div className="spinner-border" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-md-8 col-lg-6">
          <div className="card shadow">
            <div className="card-header bg-danger text-white text-center">
              <h4 className="mb-0">
                <i className="fas fa-exclamation-triangle me-2"></i>
                Account Deactivation
              </h4>
            </div>
            <div className="card-body text-center">
              <div className="mb-4">
                <i className="fas fa-user-times fa-4x text-danger mb-3"></i>
                <h5>Are you sure you want to deactivate your account?</h5>
                <p className="text-muted">
                  Account: <strong>{accountName}</strong> ({accountType})
                </p>
                {accountId && (
                  <p className="text-muted small">
                    ID: {accountId}
                  </p>
                )}
              </div>

              <div className="alert alert-warning">
                <h6><i className="fas fa-info-circle me-2"></i>Important Information:</h6>
                <ul className="text-start mb-0">
                  <li>Your account will be deactivated but not permanently deleted</li>
                  <li>You can contact support to reactivate your account</li>
                  <li>All your data will be preserved</li>
                  <li>You will be logged out immediately</li>
                  {accountType === "vendor" && (
                    <li>All your buses and schedules will also be deactivated</li>
                  )}
                </ul>
              </div>

              <div className="d-grid gap-2 d-md-flex justify-content-md-center">
                <button
                  className="btn btn-danger me-md-2"
                  onClick={handleConfirmDeactivation}
                  disabled={loading}
                >
                  {loading ? (
                    <>
                      <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                      Deactivating...
                    </>
                  ) : (
                    <>
                      <i className="fas fa-user-times me-2"></i>
                      Deactivate Account
                    </>
                  )}
                </button>
                <button
                  className="btn btn-secondary"
                  onClick={handleCancel}
                  disabled={loading}
                >
                  <i className="fas fa-times me-2"></i>
                  Cancel
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default AccountDeactivation
