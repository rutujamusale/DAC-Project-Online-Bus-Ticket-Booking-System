import { useState, useEffect } from "react"
import { useParams, useNavigate } from "react-router-dom"
import axios from "axios"
import { toast } from "react-toastify"

const Feedback = () => {
  const [booking, setBooking] = useState(null)
  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)
  const { bookingId } = useParams()
  const navigate = useNavigate()

  const [feedback, setFeedback] = useState({
    overallRating: 0,
    cleanliness: 0,
    punctuality: 0,
    staffBehavior: 0,
    comfort: 0,
    comments: ""
  })

  useEffect(() => {
    fetchBookingDetails()
    checkExistingFeedback()
  }, [bookingId])

  const fetchBookingDetails = async () => {
    try {
      const userId = localStorage.getItem("userId")
      const response = await axios.get(`http://localhost:8080/api/bookings/user/${userId}`)
      const bookingData = response.data.find(b => b.id == bookingId)
      
      if (bookingData) {
        setBooking(bookingData)
      } else {
        toast.error("Booking not found")
        navigate("/user-dashboard")
      }
    } catch (error) {
      toast.error("Error fetching booking details: " + (error.response?.data?.message || error.message))
      navigate("/user-dashboard")
    } finally {
      setLoading(false)
    }
  }

  const checkExistingFeedback = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/feedback/enhanced/booking/${bookingId}`)
      if (response.data && response.data.length > 0) {
        toast.error("Feedback already exists for this booking")
        navigate("/user-dashboard")
      }
    } catch (error) {
      // If error is 404, it means no feedback exists, which is fine
      if (error.response?.status !== 404) {
        console.error("Error checking existing feedback:", error)
        // Don't show error to user for this check
      }
    }
  }

  const handleInputChange = (field, value) => {
    setFeedback(prev => ({
      ...prev,
      [field]: value
    }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    // Validate that overall rating is selected
    if (feedback.overallRating === 0) {
      toast.error("Please provide an overall rating before submitting feedback")
      return
    }
    
    // Validate comments length
    if (feedback.comments.length < 10) {
      toast.error("Comments must be at least 10 characters long")
      return
    }
    
    // Validate that comments are not too long
    if (feedback.comments.length > 1000) {
      toast.error("Comments cannot exceed 1000 characters")
      return
    }
    
    setSubmitting(true)

    try {
      const feedbackData = {
        userId: parseInt(localStorage.getItem("userId")),
        bookingId: parseInt(bookingId),
        overallRating: feedback.overallRating,
        cleanliness: feedback.cleanliness > 0 ? feedback.cleanliness : null,
        punctuality: feedback.punctuality > 0 ? feedback.punctuality : null,
        staffBehavior: feedback.staffBehavior > 0 ? feedback.staffBehavior : null,
        comfort: feedback.comfort > 0 ? feedback.comfort : null,
        busName: booking?.busName || "Unknown Bus",
        journeyDate: booking?.scheduleDate || new Date().toISOString().split('T')[0],
        comments: feedback.comments.trim()
      }

      const response = await axios.post("http://localhost:8080/api/feedback/enhanced", feedbackData)
      
      if (response.data) {
        toast.success("Thank you for your feedback!")
        navigate("/user-dashboard")
      }
    } catch (error) {
      let errorMessage = "Error submitting feedback"
      if (error.response?.data) {
        if (typeof error.response.data === 'string') {
          errorMessage = error.response.data
        } else if (error.response.data.message) {
          errorMessage = error.response.data.message
        }
      } else if (error.message) {
        errorMessage = error.message
      }
      toast.error(errorMessage)
    } finally {
      setSubmitting(false)
    }
  }

  const renderRadioRating = (field, label, required = false) => (
    <div className="mb-4">
      <label className="form-label fw-bold">
        {label} {required && <span className="text-danger">*</span>}
      </label>
      <div className="d-flex align-items-center flex-wrap">
        {[1, 2, 3, 4, 5].map((rating) => (
          <div key={rating} className="form-check me-4 mb-2">
            <input
              className="form-check-input"
              type="radio"
              name={field}
              id={`${field}-${rating}`}
              value={rating}
              checked={feedback[field] === rating}
              onChange={(e) => handleInputChange(field, parseInt(e.target.value))}
              style={{ 
                width: '18px', 
                height: '18px',
                cursor: 'pointer'
              }}
            />
            <label 
              className="form-check-label fw-medium" 
              htmlFor={`${field}-${rating}`}
              style={{ 
                cursor: 'pointer',
                fontSize: '16px',
                marginLeft: '8px'
              }}
            >
              {rating}
            </label>
          </div>
        ))}
        <span className="ms-3 text-muted fst-italic">
          {feedback[field] > 0 ? `(${feedback[field]}/5)` : "(Not rated)"}
        </span>
      </div>
      <div className="form-text">
        {feedback[field] > 0 ? (
          <span className="text-success">
            ✓ Rated: {feedback[field]} out of 5
          </span>
        ) : (
          <span className="text-muted">
            Please select a rating from 1 to 5
          </span>
        )}
      </div>
    </div>
  )

  if (loading) {
    return (
      <div className="container py-5">
        <div className="text-center">
          <div className="spinner-border" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="mt-2">Loading booking details...</p>
        </div>
      </div>
    )
  }

  if (!booking) {
    return (
      <div className="container py-5">
        <div className="text-center">
          <i className="fas fa-exclamation-triangle fa-3x text-warning mb-3"></i>
          <h5>Booking Not Found</h5>
          <p>The requested booking could not be found.</p>
          <button className="btn btn-primary" onClick={() => navigate("/user-dashboard")}>
            Go to Dashboard
          </button>
        </div>
      </div>
    )
  }

  return (
    <div className="container py-4">
      <div className="row justify-content-center">
        <div className="col-lg-8">
          <div className="card shadow">
            <div className="card-header bg-primary text-white">
              <div className="d-flex justify-content-between align-items-center">
                <h4 className="mb-0">
                  <i className="fas fa-comment-alt me-2"></i>
                  Journey Feedback
                </h4>
                <button 
                  className="btn btn-outline-light" 
                  onClick={() => navigate("/user-dashboard")}
                >
                  <i className="fas fa-arrow-left me-2"></i>
                  Back to Dashboard
                </button>
              </div>
            </div>
            <div className="card-body">
              {/* Journey Summary */}
              <div className="alert alert-info mb-4">
                <h6 className="fw-bold">Journey Summary</h6>
                <p className="mb-1">
                  <strong>Bus:</strong> {booking.busName} | 
                  <strong> Route:</strong> {booking.source} → {booking.destination} | 
                  <strong> Date:</strong> {booking.scheduleDate}
                </p>
                <p className="mb-0">
                  <strong>Booking ID:</strong> #{booking.id} | 
                  <strong> Amount:</strong> ₹{booking.totalAmount}
                </p>
              </div>

              <form onSubmit={handleSubmit}>
                {/* Overall Experience - Required */}
                <div className="mb-4 p-3 border rounded bg-light">
                  <h6 className="text-primary mb-3">Overall Experience (Required)</h6>
                  {renderRadioRating("overallRating", "Overall Experience", true)}
                </div>

                {/* Individual Rating Categories */}
                <div className="row">
                  <div className="col-md-6">
                    <div className="p-3 border rounded bg-light">
                      {renderRadioRating("cleanliness", "Cleanliness")}
                    </div>
                  </div>
                  <div className="col-md-6">
                    <div className="p-3 border rounded bg-light">
                      {renderRadioRating("punctuality", "Punctuality")}
                    </div>
                  </div>
                </div>

                <div className="row mt-3">
                  <div className="col-md-6">
                    <div className="p-3 border rounded bg-light">
                      {renderRadioRating("staffBehavior", "Staff Behavior")}
                    </div>
                  </div>
                  <div className="col-md-6">
                    <div className="p-3 border rounded bg-light">
                      {renderRadioRating("comfort", "Comfort")}
                    </div>
                  </div>
                </div>

                {/* Comments */}
                <div className="mb-4 mt-4">
                  <label className="form-label fw-bold">Additional Comments <span className="text-danger">*</span></label>
                  <textarea
                    className="form-control"
                    rows="4"
                    value={feedback.comments}
                    onChange={(e) => handleInputChange("comments", e.target.value)}
                    placeholder="Please share your experience, suggestions, or any issues you encountered... (minimum 10 characters)"
                    required
                    style={{ fontSize: '14px' }}
                  ></textarea>
                  <div className="form-text">
                    <span className={feedback.comments.length >= 10 ? 'text-success' : 'text-muted'}>
                      {feedback.comments.length}/1000 characters (minimum 10 required)
                    </span>
                  </div>
                </div>

                {/* Submit Button */}
                <div className="d-flex justify-content-end">
                  <button
                    type="submit"
                    className="btn btn-primary btn-lg"
                    disabled={submitting}
                  >
                    {submitting ? (
                      <>
                        <span className="spinner-border spinner-border-sm me-2" role="status"></span>
                        Submitting...
                      </>
                    ) : (
                      <>
                        <i className="fas fa-paper-plane me-2"></i>
                        Submit Feedback
                      </>
                    )}
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

export default Feedback 