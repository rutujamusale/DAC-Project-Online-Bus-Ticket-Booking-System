import { useState, useEffect } from "react"
import { useNavigate, Link } from "react-router-dom"
import axios from "axios"
import { toast } from "react-toastify"

const VendorDashboard = () => {
  const [buses, setBuses] = useState([])
  const [schedules, setSchedules] = useState([])
  const [loading, setLoading] = useState(true)
  const [showScheduleModal, setShowScheduleModal] = useState(false)
  const [selectedBus, setSelectedBus] = useState(null)
  const [scheduleForm, setScheduleForm] = useState({
    source: "",
    destination: "",
    scheduleDate: "",
    departureTime: "",
    arrivalTime: "",
    availableSeats: "",
  })
  const navigate = useNavigate()

  useEffect(() => {
    const vendorLoggedIn = localStorage.getItem("vendorLoggedIn")
    if (!vendorLoggedIn) {
      toast.error("Please login as vendor to access this page")
      navigate("/vendor-login")
      return
    }
    fetchDashboardData()
  }, [navigate])

  const fetchDashboardData = async () => {
    try {
      const vendorId = localStorage.getItem("vendorId")
      const token = localStorage.getItem("vendorToken")

      const [busesResponse, schedulesResponse] = await Promise.all([
        axios.get(`http://localhost:8080/api/buses/vendor/${vendorId}`, {
          headers: { Authorization: `Bearer ${token}` },
        }),
        axios.get(`http://localhost:8080/api/schedules/vendor/${vendorId}`, {
          headers: { Authorization: `Bearer ${token}` },
        }),
      ])

      setBuses(busesResponse.data)
      setSchedules(schedulesResponse.data)
    } catch (error) {
      console.error("Error fetching dashboard data:", error)
      toast.error("Error loading dashboard data")
    } finally {
      setLoading(false)
    }
  }

  const handleDeleteBus = async (busId) => {
    if (window.confirm("Are you sure you want to delete this bus? This will also delete all its schedules.")) {
      try {
        const token = localStorage.getItem("vendorToken")
        await axios.delete(`http://localhost:8080/api/buses/${busId}`, {
          headers: { Authorization: `Bearer ${token}` },
        })
        toast.success("Bus deleted successfully")
        fetchDashboardData()
      } catch (error) {
        toast.error("Error deleting bus")
      }
    }
  }

  const handleScheduleSubmit = async (e) => {
    e.preventDefault()

    if (scheduleForm.source.toLowerCase() === scheduleForm.destination.toLowerCase()) {
      toast.error("Source and destination cannot be the same")
      return
    }

    try {
      const token = localStorage.getItem("vendorToken")
      const scheduleData = {
        busId: selectedBus.id,
        source: scheduleForm.source,
        destination: scheduleForm.destination,
        scheduleDate: scheduleForm.scheduleDate,
        departureTime: scheduleForm.departureTime,
        arrivalTime: scheduleForm.arrivalTime,
        availableSeats: scheduleForm.availableSeats || selectedBus.totalSeats,
      }

      await axios.post("http://localhost:8080/api/schedules", scheduleData, {
        headers: { Authorization: `Bearer ${token}` },
      })

      toast.success("Schedule added successfully")
      setShowScheduleModal(false)
      setScheduleForm({
        source: "",
        destination: "",
        scheduleDate: "",
        departureTime: "",
        arrivalTime: "",
        availableSeats: "",
      })
      fetchDashboardData()
    } catch (error) {
      toast.error(error.response?.data?.message || "Error adding schedule")
    }
  }

  const handleDeleteSchedule = async (scheduleId) => {
    if (window.confirm("Are you sure you want to delete this schedule?")) {
      try {
        const token = localStorage.getItem("vendorToken")
        await axios.delete(`http://localhost:8080/api/schedules/${scheduleId}`, {
          headers: { Authorization: `Bearer ${token}` },
        })
        toast.success("Schedule deleted successfully")
        fetchDashboardData()
      } catch (error) {
        toast.error("Error deleting schedule")
      }
    }
  }

  const openScheduleModal = (bus) => {
    setSelectedBus(bus)
    setScheduleForm({
      source: "",
      destination: "",
      scheduleDate: "",
      departureTime: "",
      arrivalTime: "",
      availableSeats: bus.totalSeats,
    })
    setShowScheduleModal(true)
  }

  const handleLogout = () => {
    localStorage.clear()
    navigate("/vendor-login")
  }

  if (loading) {
    return (
      <div className="container py-5">
        <div className="text-center">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="mt-3">Loading dashboard...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="container py-5">
      {/* Header */}
      <div className="row mb-4">
        <div className="col-12">
          <div className="d-flex justify-content-between align-items-center">
            <div>
              <h2 className="mb-1">
                <i className="fas fa-tachometer-alt me-2 text-primary"></i>
                Vendor Dashboard
              </h2>
              <p className="text-muted mb-0">Welcome back, {localStorage.getItem("vendorCompanyName") || "Vendor"}!</p>
            </div>
            <div>
              <Link to="/add-bus" className="btn btn-primary me-2">
                <i className="fas fa-plus me-2"></i>
                Add New Bus
              </Link>
              <button className="btn btn-outline-danger" onClick={handleLogout}>
                <i className="fas fa-sign-out-alt me-2"></i>
                Logout
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="row mb-4">
        <div className="col-md-6">
          <div className="card border-0 shadow-sm">
            <div className="card-body">
              <div className="d-flex align-items-center">
                <div className="flex-shrink-0">
                  <i className="fas fa-bus fa-2x text-primary"></i>
                </div>
                <div className="flex-grow-1 ms-3">
                  <h5 className="card-title mb-0">Total Buses</h5>
                  <h3 className="text-primary mb-0">{buses.length}</h3>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div className="col-md-6">
          <div className="card border-0 shadow-sm">
            <div className="card-body">
              <div className="d-flex align-items-center">
                <div className="flex-shrink-0">
                  <i className="fas fa-calendar-alt fa-2x text-success"></i>
                </div>
                <div className="flex-grow-1 ms-3">
                  <h5 className="card-title mb-0">Total Schedules</h5>
                  <h3 className="text-success mb-0">{schedules.length}</h3>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Buses Section */}
      <div className="row mb-4">
        <div className="col-12">
          <div className="card border-0 shadow-sm">
            <div className="card-header bg-white">
              <h5 className="mb-0">
                <i className="fas fa-bus me-2 text-primary"></i>
                My Buses
              </h5>
            </div>
            <div className="card-body">
              {buses.length === 0 ? (
                <div className="text-center py-4">
                  <i className="fas fa-bus fa-3x text-muted mb-3"></i>
                  <p className="text-muted">No buses added yet. Add your first bus to get started!</p>
                  <Link to="/add-bus" className="btn btn-primary">
                    <i className="fas fa-plus me-2"></i>
                    Add Bus
                  </Link>
                </div>
              ) : (
                <div className="table-responsive">
                  <table className="table table-hover">
                    <thead>
                      <tr>
                        <th>Bus Number</th>
                        <th>Bus Name</th>
                        <th>Seats</th>
                        <th>Price</th>
                        <th>Type</th>
                        <th>Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      {buses.map((bus) => (
                        <tr key={bus.id}>
                          <td>
                            <strong>{bus.busNumber}</strong>
                          </td>
                          <td>{bus.busName}</td>
                          <td>
                            <span className="badge bg-info">{bus.totalSeats} seats</span>
                          </td>
                          <td>
                            <strong className="text-success">₹{bus.price}</strong>
                          </td>
                          <td>
                            <span className="badge bg-secondary">{bus.busType}</span>
                          </td>
                          <td>
                            <button
                              className="btn btn-sm btn-outline-primary me-2"
                              onClick={() => openScheduleModal(bus)}
                            >
                              <i className="fas fa-calendar-plus me-1"></i>
                              Schedule
                            </button>
                            <button className="btn btn-sm btn-outline-danger" onClick={() => handleDeleteBus(bus.id)}>
                              <i className="fas fa-trash me-1"></i>
                              Delete
                            </button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Schedules Section */}
      <div className="row">
        <div className="col-12">
          <div className="card border-0 shadow-sm">
            <div className="card-header bg-white">
              <h5 className="mb-0">
                <i className="fas fa-calendar-alt me-2 text-success"></i>
                Bus Schedules
              </h5>
            </div>
            <div className="card-body">
              {schedules.length === 0 ? (
                <div className="text-center py-4">
                  <i className="fas fa-calendar-alt fa-3x text-muted mb-3"></i>
                  <p className="text-muted">
                    No schedules created yet. Schedule your buses to make them available for booking!
                  </p>
                </div>
              ) : (
                <div className="table-responsive">
                  <table className="table table-hover">
                    <thead>
                      <tr>
                        <th>Bus</th>
                        <th>Route</th>
                        <th>Date</th>
                        <th>Departure</th>
                        <th>Arrival</th>
                        <th>Available Seats</th>
                        <th>Price</th>
                        <th>Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      {schedules.map((schedule) => (
                        <tr key={schedule.id}>
                          <td>
                            <div>
                              <strong>{schedule.busNumber}</strong>
                              <br />
                              <small className="text-muted">{schedule.busName}</small>
                            </div>
                          </td>
                          <td>
                            <span className="badge bg-success me-1">{schedule.source}</span>
                            <i className="fas fa-arrow-right mx-1"></i>
                            <span className="badge bg-danger">{schedule.destination}</span>
                          </td>
                          <td>{new Date(schedule.scheduleDate).toLocaleDateString()}</td>
                          <td>
                            <strong>{schedule.departureTime}</strong>
                          </td>
                          <td>
                            <strong>{schedule.arrivalTime}</strong>
                          </td>
                          <td>
                            <span className="badge bg-warning">{schedule.availableSeats}</span>
                          </td>
                          <td>
                            <strong className="text-success">₹{schedule.price}</strong>
                          </td>
                          <td>
                            <button
                              className="btn btn-sm btn-outline-danger"
                              onClick={() => handleDeleteSchedule(schedule.id)}
                            >
                              <i className="fas fa-trash me-1"></i>
                              Delete
                            </button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Schedule Modal */}
      {showScheduleModal && (
        <div className="modal show d-block" tabIndex="-1" style={{ backgroundColor: "rgba(0,0,0,0.5)" }}>
          <div className="modal-dialog modal-lg">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">
                  <i className="fas fa-calendar-plus me-2"></i>
                  Add Schedule for {selectedBus?.busNumber} - {selectedBus?.busName}
                </h5>
                <button type="button" className="btn-close" onClick={() => setShowScheduleModal(false)}></button>
              </div>
              <form onSubmit={handleScheduleSubmit}>
                <div className="modal-body">
                  <div className="row">
                    <div className="col-md-6 mb-3">
                      <label className="form-label">
                        <i className="fas fa-map-marker-alt me-2 text-success"></i>
                        Source City *
                      </label>
                      <input
                        type="text"
                        className="form-control"
                        value={scheduleForm.source}
                        onChange={(e) => setScheduleForm({ ...scheduleForm, source: e.target.value })}
                        placeholder="e.g., Mumbai"
                        required
                      />
                    </div>
                    <div className="col-md-6 mb-3">
                      <label className="form-label">
                        <i className="fas fa-map-marker-alt me-2 text-danger"></i>
                        Destination City *
                      </label>
                      <input
                        type="text"
                        className="form-control"
                        value={scheduleForm.destination}
                        onChange={(e) => setScheduleForm({ ...scheduleForm, destination: e.target.value })}
                        placeholder="e.g., Pune"
                        required
                      />
                    </div>
                  </div>
                  <div className="mb-3">
                    <label className="form-label">
                      <i className="fas fa-calendar me-2"></i>
                      Schedule Date *
                    </label>
                    <input
                      type="date"
                      className="form-control"
                      value={scheduleForm.scheduleDate}
                      onChange={(e) => setScheduleForm({ ...scheduleForm, scheduleDate: e.target.value })}
                      min={new Date().toISOString().split("T")[0]}
                      required
                    />
                  </div>
                  <div className="row">
                    <div className="col-md-6 mb-3">
                      <label className="form-label">
                        <i className="fas fa-clock me-2"></i>
                        Departure Time *
                      </label>
                      <input
                        type="time"
                        className="form-control"
                        value={scheduleForm.departureTime}
                        onChange={(e) => setScheduleForm({ ...scheduleForm, departureTime: e.target.value })}
                        required
                      />
                    </div>
                    <div className="col-md-6 mb-3">
                      <label className="form-label">
                        <i className="fas fa-clock me-2"></i>
                        Arrival Time *
                      </label>
                      <input
                        type="time"
                        className="form-control"
                        value={scheduleForm.arrivalTime}
                        onChange={(e) => setScheduleForm({ ...scheduleForm, arrivalTime: e.target.value })}
                        required
                      />
                    </div>
                  </div>
                  <div className="mb-3">
                    <label className="form-label">
                      <i className="fas fa-chair me-2"></i>
                      Available Seats
                    </label>
                    <input
                      type="number"
                      className="form-control"
                      value={scheduleForm.availableSeats}
                      onChange={(e) => setScheduleForm({ ...scheduleForm, availableSeats: e.target.value })}
                      placeholder={`Default: ${selectedBus?.totalSeats} seats`}
                      min="1"
                      max={selectedBus?.totalSeats}
                    />
                    <small className="text-muted">Maximum: {selectedBus?.totalSeats} seats</small>
                  </div>
                  <div className="alert alert-info">
                    <i className="fas fa-info-circle me-2"></i>
                    <strong>Bus Details:</strong> {selectedBus?.busName} ({selectedBus?.busNumber}) |
                    {selectedBus?.totalSeats} seats | ₹{selectedBus?.price} per seat | {selectedBus?.busType}
                  </div>
                </div>
                <div className="modal-footer">
                  <button type="button" className="btn btn-secondary" onClick={() => setShowScheduleModal(false)}>
                    Cancel
                  </button>
                  <button type="submit" className="btn btn-primary">
                    <i className="fas fa-plus me-2"></i>
                    Add Schedule
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default VendorDashboard
