import { useState, useEffect } from "react"
import axios from "axios"
import { toast } from "react-toastify"

const UserFeedback = () => {
  const [feedback, setFeedback] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchUserFeedback()
  }, [])

  const fetchUserFeedback = async () => {
    try {
      const userId = localStorage.getItem("userId")
      const response = await axios.get(`http://localhost:8080/api/feedback/enhanced/user/${userId}`)
      setFeedback(response.data)
    } catch (error) {
      let errorMessage = "Error fetching feedback"
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

  const renderStars = (rating) => {
    const safeRating = rating || 0;
    return (
      <div className="d-flex">
        {[1, 2, 3, 4, 5].map((star) => (
          <i 
            key={star}
            className={`fas fa-star ${star <= safeRating ? 'text-warning' : 'text-muted'}`}
            style={{ fontSize: '0.9rem' }}
          ></i>
        ))}
        <span className="ms-1 text-muted small">({safeRating}/5)</span>
      </div>
    )
  }

  if (loading) {
    return (
      <div className="container py-5">
        <div className="text-center">
          <div className="spinner-border" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="mt-2">Loading your feedback...</p>
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
              <h4 className="mb-0">
                <i className="fas fa-comment-alt me-2"></i>
                My Feedback History
              </h4>
            </div>
            <div className="card-body">
              {feedback.length === 0 ? (
                <div className="text-center py-4">
                  <i className="fas fa-comment-alt fa-3x text-muted mb-3"></i>
                  <h5 className="text-muted">No feedback found</h5>
                  <p className="text-muted">You haven't submitted any feedback yet.</p>
                  <a href="/user-dashboard" className="btn btn-primary">
                    <i className="fas fa-ticket-alt me-2"></i>
                    View My Bookings
                  </a>
                </div>
              ) : (
                <div className="row">
                  {feedback.map((item) => (
                    <div key={item.id} className="col-12 mb-4">
                      <div className="card border">
                        <div className="card-header d-flex justify-content-between align-items-center">
                          <div>
                            <h6 className="mb-1">
                              <i className="fas fa-bus me-2"></i>
                              {item.busName}
                            </h6>
                            <p className="mb-0 text-muted">
                              Journey Date: {item.journeyDate} | Booking ID: #{item.bookingId}
                            </p>
                          </div>
                          <div className="text-end">
                            {getCategoryBadge(item.category)}
                            <p className="mb-0 text-muted small">
                              {new Date(item.createdAt || new Date()).toLocaleDateString()}
                            </p>
                          </div>
                        </div>
                        <div className="card-body">
                          <div className="row">
                            <div className="col-md-6">
                              <h6>Ratings:</h6>
                              <div className="mb-2">
                                <small className="text-muted">Overall Experience:</small>
                                {renderStars(item.overallRating || item.overallExperience || 0)}
                              </div>
                                                              {item.cleanliness && item.cleanliness > 0 && (
                                  <div className="mb-2">
                                    <small className="text-muted">Cleanliness:</small>
                                    {renderStars(item.cleanliness)}
                                  </div>
                                )}
                                {item.punctuality && item.punctuality > 0 && (
                                  <div className="mb-2">
                                    <small className="text-muted">Punctuality:</small>
                                    {renderStars(item.punctuality)}
                                  </div>
                                )}
                                {item.staffBehavior && item.staffBehavior > 0 && (
                                  <div className="mb-2">
                                    <small className="text-muted">Staff Behavior:</small>
                                    {renderStars(item.staffBehavior)}
                                  </div>
                                )}
                                {item.comfort && item.comfort > 0 && (
                                  <div className="mb-2">
                                    <small className="text-muted">Comfort:</small>
                                    {renderStars(item.comfort)}
                                  </div>
                                )}
                            </div>
                            <div className="col-md-6">
                              {item.comments && (
                                <div>
                                  <h6>Comments:</h6>
                                  <p className="text-muted mb-0">{item.comments}</p>
                                </div>
                              )}
                            </div>
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

export default UserFeedback 