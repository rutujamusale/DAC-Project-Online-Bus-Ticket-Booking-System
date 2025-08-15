import { useState, useEffect } from "react"
import axios from "axios"
import { toast } from "react-toastify"

const VendorFeedback = () => {
  const [feedback, setFeedback] = useState([])
  const [loading, setLoading] = useState(true)
  const [vendorBuses, setVendorBuses] = useState([])
  const [selectedBus, setSelectedBus] = useState("all")

  useEffect(() => {
    // Check if user is logged in as vendor
    const vendorLoggedIn = localStorage.getItem("vendorLoggedIn")
    const vendorId = localStorage.getItem("vendorId")
    
    console.log("Vendor login check:", { vendorLoggedIn, vendorId })
    
    if (!vendorLoggedIn || !vendorId) {
      toast.error("Please login as vendor to view feedback")
      return
    }
    
    fetchVendorBuses()
  }, [])

  useEffect(() => {
    if (vendorBuses.length > 0) {
      fetchVendorFeedback()
    }
  }, [vendorBuses, selectedBus])

  const fetchVendorBuses = async () => {
    try {
      const vendorId = localStorage.getItem("vendorId")
      const token = localStorage.getItem("vendorToken")
      
      if (!vendorId) {
        toast.error("Vendor ID not found. Please login again.")
        return
      }
      
      if (!token) {
        toast.error("Authentication token not found. Please login again.")
        return
      }
      
      const response = await axios.get(`http://localhost:8080/api/buses/vendor/${vendorId}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      })
      console.log("Vendor buses:", response.data)
      setVendorBuses(response.data)
      
      if (response.data.length === 0) {
        toast.info("No buses found for this vendor. Add some buses first to see feedback.")
      }
    } catch (error) {
      console.error("Error fetching vendor buses:", error)
      if (error.response?.status === 403) {
        toast.error("Access denied. Please login again.")
      } else if (error.response?.status === 404) {
        toast.error("No buses found for this vendor")
      } else {
        toast.error("Error fetching vendor buses: " + (error.response?.data?.message || error.message))
      }
    }
  }

  const fetchVendorFeedback = async () => {
    try {
      setLoading(true)
      
      if (vendorBuses.length === 0) {
        setFeedback([])
        return
      }

      if (selectedBus === "all") {
        // Try to get all feedback first, then filter by vendor's buses
        try {
          const allFeedbackResponse = await axios.get("http://localhost:8080/api/feedback/enhanced")
          const allFeedback = allFeedbackResponse.data || []
          const busNames = vendorBuses.map(bus => bus.busName)
          
          // Filter feedback for vendor's buses
          const vendorFeedback = allFeedback.filter(feedback => 
            feedback.busName && busNames.includes(feedback.busName)
          )
          
          console.log("Total feedback collected:", vendorFeedback.length)
          setFeedback(vendorFeedback)
        } catch (error) {
          console.error("Error fetching all feedback:", error)
          // Fallback to individual bus requests
          const busNames = vendorBuses.map(bus => bus.busName)
          const allFeedback = []
          
          for (const busName of busNames) {
            try {
              console.log(`Fetching feedback for bus: ${busName}`)
              const busFeedback = await axios.get(`http://localhost:8080/api/feedback/enhanced/bus/${encodeURIComponent(busName)}`)
              console.log(`Feedback for ${busName}:`, busFeedback.data)
              if (busFeedback.data && Array.isArray(busFeedback.data)) {
                allFeedback.push(...busFeedback.data)
              }
            } catch (error) {
              console.error(`Error fetching feedback for bus ${busName}:`, error)
              // Don't show error for individual bus, just log it
            }
          }
          
          console.log("Total feedback collected:", allFeedback.length)
          setFeedback(allFeedback)
        }
      } else {
        // Get feedback for specific bus
        const selectedBusObj = vendorBuses.find(bus => bus.id.toString() === selectedBus)
        if (selectedBusObj) {
          console.log(`Fetching feedback for specific bus: ${selectedBusObj.busName}`)
          try {
            const response = await axios.get(`http://localhost:8080/api/feedback/enhanced/bus/${encodeURIComponent(selectedBusObj.busName)}`)
            console.log("Specific bus feedback:", response.data)
            setFeedback(response.data || [])
          } catch (error) {
            console.error(`Error fetching feedback for specific bus ${selectedBusObj.busName}:`, error)
            // Try to get all feedback and filter
            try {
              const allFeedbackResponse = await axios.get("http://localhost:8080/api/feedback/enhanced")
              const allFeedback = allFeedbackResponse.data || []
              const filteredFeedback = allFeedback.filter(feedback => 
                feedback.busName === selectedBusObj.busName
              )
              setFeedback(filteredFeedback)
            } catch (fallbackError) {
              console.error("Fallback also failed:", fallbackError)
              setFeedback([])
              toast.error(`No feedback found for bus: ${selectedBusObj.busName}`)
            }
          }
        }
      }
    } catch (error) {
      console.error("Error fetching vendor feedback:", error)
      toast.error("Error fetching feedback data: " + (error.response?.data?.message || error.message))
    } finally {
      setLoading(false)
    }
  }

  const getCategoryBadge = (category) => {
    const categoryColors = {
      'GENERAL': 'bg-primary',
      'CLEANLINESS': 'bg-success',
      'PUNCTUALITY': 'bg-info',
      'STAFF_BEHAVIOR': 'bg-warning',
      'COMFORT': 'bg-secondary',
      'SAFETY': 'bg-danger',
      'COMPLAINT': 'bg-danger',
      'SUGGESTION': 'bg-info'
    }
    
    return (
      <span className={`badge ${categoryColors[category] || 'bg-secondary'}`}>
        {category.replace('_', ' ')}
      </span>
    )
  }

  const getRatingStars = (rating) => {
    const stars = []
    for (let i = 1; i <= 5; i++) {
      stars.push(
        <i
          key={i}
          className={`fas fa-star ${i <= rating ? 'text-warning' : 'text-muted'}`}
        ></i>
      )
    }
    return stars
  }

  if (loading) {
    return (
      <div className="container py-5">
        <div className="text-center">
          <div className="spinner-border" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="mt-2">Loading feedback...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="container py-4">
      <div className="row">
        <div className="col-12">
          <div className="card">
            <div className="card-header">
              <div className="d-flex justify-content-between align-items-center">
                <h4 className="mb-0">
                  <i className="fas fa-comments me-2"></i>
                  Vendor Feedback
                </h4>
                                 <div className="d-flex align-items-center gap-2">
                   <label className="form-label mb-0 fw-semibold">Filter by Bus:</label>
                   <select
                     className="form-select form-select-sm"
                     value={selectedBus}
                     onChange={(e) => setSelectedBus(e.target.value)}
                     style={{ width: '200px', minWidth: '200px' }}
                   >
                     <option value="all">All Buses</option>
                     {vendorBuses.map((bus) => (
                       <option key={bus.id} value={bus.id}>
                         {bus.busName}
                       </option>
                     ))}
                   </select>
                 </div>
              </div>
            </div>
            <div className="card-body">
              {feedback.length === 0 ? (
                <div className="text-center py-4">
                  <i className="fas fa-comment-slash fa-3x text-muted mb-3"></i>
                  <h5 className="text-muted">No feedback available</h5>
                  <p className="text-muted">
                    {selectedBus === "all" 
                      ? "No feedback has been submitted for your buses yet."
                      : "No feedback has been submitted for this bus yet."
                    }
                  </p>
                </div>
              ) : (
                <div className="row">
                  {feedback.map((item) => (
                    <div key={item.id} className="col-md-6 col-lg-4 mb-3">
                      <div className="card h-100">
                        <div className="card-header">
                          <div className="d-flex justify-content-between align-items-center">
                            <h6 className="mb-0">Bus: {item.busName}</h6>
                            {getCategoryBadge(item.category)}
                          </div>
                        </div>
                        <div className="card-body">
                          <div className="mb-2">
                            <div className="d-flex justify-content-between align-items-center mb-1">
                              <span className="fw-bold">Overall Rating:</span>
                              <div>{getRatingStars(item.rating || item.overallExperience)}</div>
                            </div>
                          </div>
                          
                          {item.cleanliness && (
                            <div className="mb-1">
                              <small className="text-muted">Cleanliness: {item.cleanliness}/5</small>
                            </div>
                          )}
                          
                          {item.punctuality && (
                            <div className="mb-1">
                              <small className="text-muted">Punctuality: {item.punctuality}/5</small>
                            </div>
                          )}
                          
                          {item.staffBehavior && (
                            <div className="mb-1">
                              <small className="text-muted">Staff Behavior: {item.staffBehavior}/5</small>
                            </div>
                          )}
                          
                          {item.comfort && (
                            <div className="mb-1">
                              <small className="text-muted">Comfort: {item.comfort}/5</small>
                            </div>
                          )}
                          
                          {item.comments && (
                            <div className="mt-2">
                              <small className="text-muted">Comments:</small>
                              <p className="mb-0 small">{item.comments}</p>
                            </div>
                          )}
                          
                          <div className="mt-2">
                            <small className="text-muted">
                              Journey Date: {item.journeyDate}
                            </small>
                          </div>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default VendorFeedback
