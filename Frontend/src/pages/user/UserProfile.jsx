import { useState, useEffect } from "react"
import axios from "axios"
import { toast } from "react-toastify"

const UserProfile = () => {
  const [user, setUser] = useState({
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    address: "",
    city: "",
    state: "",
    pincode: ""
  })
  const [loading, setLoading] = useState(false)
  const [isEditing, setIsEditing] = useState(false)

  useEffect(() => {
    fetchUserProfile()
  }, [])

  const fetchUserProfile = async () => {
    try {
      const userId = localStorage.getItem("userId")
      console.log("Fetching user profile for userId:", userId)
      const response = await axios.get(`http://localhost:8080/api/users/id/${userId}`)
      console.log("User profile response:", response.data)
      setUser(response.data)
    } catch (error) {
      console.error("Error fetching user profile:", error)
      toast.error("Error fetching user profile: " + (error.response?.data?.message || error.message))
    }
  }

  const handleInputChange = (field, value) => {
    setUser(prev => ({
      ...prev,
      [field]: value
    }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)

    try {
      const userId = localStorage.getItem("userId")
      console.log("Updating user profile for userId:", userId)
      console.log("User data to update:", user)
      const response = await axios.put(`http://localhost:8080/api/users/${userId}`, user)
      console.log("Update response:", response.data)
      
      if (response.data) {
        toast.success("Profile updated successfully!")
        setIsEditing(false)
        // Update localStorage with new values
        localStorage.setItem("userFirstName", user.firstName)
        localStorage.setItem("userLastName", user.lastName)
        localStorage.setItem("userEmail", user.email)
      }
    } catch (error) {
      console.error("Error updating profile:", error)
      toast.error("Error updating profile: " + (error.response?.data?.message || error.message))
    } finally {
      setLoading(false)
    }
  }

  const handleCancel = () => {
    fetchUserProfile() // Reset to original values
    setIsEditing(false)
  }

  return (
    <div className="container py-4">
      <div className="row justify-content-center">
        <div className="col-lg-8">
          <div className="card">
            <div className="card-header">
              <div className="d-flex justify-content-between align-items-center">
                <h4 className="mb-0">
                  <i className="fas fa-user-edit me-2"></i>
                  User Profile
                </h4>
                {!isEditing && (
                  <button
                    className="btn btn-primary"
                    onClick={() => setIsEditing(true)}
                  >
                    <i className="fas fa-edit me-2"></i>
                    Edit Profile
                  </button>
                )}
              </div>
            </div>
            <div className="card-body">
              <form onSubmit={handleSubmit}>
                <div className="row">
                  <div className="col-md-6 mb-3">
                    <label className="form-label">First Name *</label>
                    <input
                      type="text"
                      className="form-control"
                      value={user.firstName}
                      onChange={(e) => handleInputChange("firstName", e.target.value)}
                      disabled={!isEditing}
                      required
                    />
                  </div>
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Last Name *</label>
                    <input
                      type="text"
                      className="form-control"
                      value={user.lastName}
                      onChange={(e) => handleInputChange("lastName", e.target.value)}
                      disabled={!isEditing}
                      required
                    />
                  </div>
                </div>

                <div className="row">
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Email *</label>
                    <input
                      type="email"
                      className="form-control"
                      value={user.email}
                      onChange={(e) => handleInputChange("email", e.target.value)}
                      disabled={!isEditing}
                      required
                    />
                  </div>
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Phone Number</label>
                    <input
                      type="tel"
                      className="form-control"
                      value={user.phone || ""}
                      onChange={(e) => handleInputChange("phone", e.target.value)}
                      disabled={!isEditing}
                      placeholder="Enter phone number"
                    />
                  </div>
                </div>

                <div className="mb-3">
                  <label className="form-label">Address</label>
                  <textarea
                    className="form-control"
                    rows="3"
                    value={user.address || ""}
                    onChange={(e) => handleInputChange("address", e.target.value)}
                    disabled={!isEditing}
                    placeholder="Enter your address"
                  />
                </div>

                <div className="row">
                  <div className="col-md-4 mb-3">
                    <label className="form-label">City</label>
                    <input
                      type="text"
                      className="form-control"
                      value={user.city || ""}
                      onChange={(e) => handleInputChange("city", e.target.value)}
                      disabled={!isEditing}
                      placeholder="Enter city"
                    />
                  </div>
                  <div className="col-md-4 mb-3">
                    <label className="form-label">State</label>
                    <input
                      type="text"
                      className="form-control"
                      value={user.state || ""}
                      onChange={(e) => handleInputChange("state", e.target.value)}
                      disabled={!isEditing}
                      placeholder="Enter state"
                    />
                  </div>
                  <div className="col-md-4 mb-3">
                    <label className="form-label">Pincode</label>
                    <input
                      type="text"
                      className="form-control"
                      value={user.pincode || ""}
                      onChange={(e) => handleInputChange("pincode", e.target.value)}
                      disabled={!isEditing}
                      placeholder="Enter pincode"
                    />
                  </div>
                </div>

                {isEditing && (
                  <div className="d-flex justify-content-end gap-2">
                    <button
                      type="button"
                      className="btn btn-outline-secondary"
                      onClick={handleCancel}
                      disabled={loading}
                    >
                      Cancel
                    </button>
                    <button
                      type="submit"
                      className="btn btn-primary"
                      disabled={loading}
                    >
                      {loading ? "Saving..." : "Save Changes"}
                    </button>
                  </div>
                )}
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default UserProfile 