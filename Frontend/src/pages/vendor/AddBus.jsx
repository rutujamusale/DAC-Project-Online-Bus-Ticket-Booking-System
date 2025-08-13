
import { useState, useEffect } from "react"
import { useNavigate } from "react-router-dom"
import axios from "axios"
import { toast } from "react-toastify"

const AddBus = () => {
  const [formData, setFormData] = useState({
    busNumber: "",
    busName: "",
    totalSeats: "",
    price: "",
    busType: "AC",
  })
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    // Check if vendor is logged in
    const vendorLoggedIn = localStorage.getItem("vendorLoggedIn")
    if (!vendorLoggedIn) {
      toast.error("Please login as vendor to access this page")
      navigate("/vendor-login")
    }
  }, [navigate])

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
      const vendorId = localStorage.getItem("vendorId")
      const token = localStorage.getItem("vendorToken")

      const busData = {
        ...formData,
        vendorId: Number.parseInt(vendorId),
        totalSeats: Number.parseInt(formData.totalSeats),
        price: Number.parseFloat(formData.price),
      }

      const response = await axios.post("http://localhost:8080/api/buses", busData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      })

      if (response.data.message) {
        toast.success(response.data.message)
        navigate("/vendor-dashboard")
      }
    } catch (error) {
      toast.error(error.response?.data?.message || "Failed to add bus. Please try again.")
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
              <i className="fas fa-bus fa-3x text-white mb-3"></i>
              <h3 className="text-white mb-0">Add New Bus</h3>
              <p className="text-white-50 mb-0">Register your bus for scheduling</p>
            </div>
            <div className="card-body p-5">
              <form onSubmit={handleSubmit}>
                <div className="row">
                  <div className="col-md-6 mb-3">
                    <label htmlFor="busNumber" className="form-label">
                      <i className="fas fa-hashtag me-2 text-primary"></i>
                      Bus Number *
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      id="busNumber"
                      name="busNumber"
                      value={formData.busNumber}
                      onChange={handleInputChange}
                      placeholder="e.g., MH12AB1234"
                      required
                      style={{ borderRadius: "10px" }}
                    />
                  </div>
                  <div className="col-md-6 mb-3">
                    <label htmlFor="busName" className="form-label">
                      <i className="fas fa-bus me-2 text-primary"></i>
                      Bus Name *
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      id="busName"
                      name="busName"
                      value={formData.busName}
                      onChange={handleInputChange}
                      placeholder="e.g., Shivneri Express"
                      required
                      style={{ borderRadius: "10px" }}
                    />
                  </div>
                </div>

                <div className="row">
                  <div className="col-md-6 mb-3">
                    <label htmlFor="totalSeats" className="form-label">
                      <i className="fas fa-chair me-2 text-primary"></i>
                      Total Seats *
                    </label>
                    <input
                      type="number"
                      className="form-control"
                      id="totalSeats"
                      name="totalSeats"
                      value={formData.totalSeats}
                      onChange={handleInputChange}
                      placeholder="e.g., 40"
                      min="1"
                      max="100"
                      required
                      style={{ borderRadius: "10px" }}
                    />
                  </div>
                  <div className="col-md-6 mb-3">
                    <label htmlFor="price" className="form-label">
                      <i className="fas fa-rupee-sign me-2 text-success"></i>
                      Price per Seat *
                    </label>
                    <input
                      type="number"
                      className="form-control"
                      id="price"
                      name="price"
                      value={formData.price}
                      onChange={handleInputChange}
                      placeholder="e.g., 500"
                      min="1"
                      step="0.01"
                      required
                      style={{ borderRadius: "10px" }}
                    />
                  </div>
                </div>

                <div className="mb-4">
                  <label htmlFor="busType" className="form-label">
                    <i className="fas fa-snowflake me-2 text-info"></i>
                    Bus Type *
                  </label>
                  <select
                    className="form-select"
                    id="busType"
                    name="busType"
                    value={formData.busType}
                    onChange={handleInputChange}
                    required
                    style={{ borderRadius: "10px" }}
                  >
                    <option value="AC">AC</option>
                    <option value="Non-AC">Non-AC</option>
                    <option value="Sleeper">Sleeper</option>
                    <option value="Semi-Sleeper">Semi-Sleeper</option>
                    <option value="Luxury">Luxury</option>
                  </select>
                </div>

                <div className="alert alert-info">
                  <i className="fas fa-info-circle me-2"></i>
                  <strong>Note:</strong> After adding the bus, you can create schedules with specific routes, dates, and
                  timings from your dashboard.
                </div>

                <div className="d-grid gap-2">
                  <button
                    type="submit"
                    className="btn btn-primary btn-lg"
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
                        Adding Bus...
                      </>
                    ) : (
                      <>
                        <i className="fas fa-plus me-2"></i>
                        Add Bus
                      </>
                    )}
                  </button>
                  <button
                    type="button"
                    className="btn btn-outline-secondary"
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

export default AddBus
