import { useState, useEffect } from "react"
import axios from "axios"
import { toast } from "react-toastify"

const FeedbackStats = () => {
  const [stats, setStats] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchFeedbackStats()
  }, [])

  const fetchFeedbackStats = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/feedback/enhanced/statistics")
      setStats(response.data)
    } catch (error) {
      let errorMessage = "Error fetching feedback statistics"
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

  const renderRatingDistribution = (ratingDist) => {
    if (!ratingDist) return null
    
    return (
      <div className="mb-3">
        <h6>Rating Distribution:</h6>
        <div className="row">
          {[5, 4, 3, 2, 1].map(rating => (
            <div key={rating} className="col">
              <div className="text-center">
                <div className="fw-bold text-primary">{rating}★</div>
                <div className="small text-muted">{ratingDist[rating] || 0}</div>
              </div>
            </div>
          ))}
        </div>
      </div>
    )
  }

  const renderCategoryDistribution = (categoryDist) => {
    if (!categoryDist) return null
    
    return (
      <div className="mb-3">
        <h6>Category Distribution:</h6>
        <div className="row">
          {Object.entries(categoryDist).map(([category, count]) => (
            <div key={category} className="col-md-6 mb-2">
              <div className="d-flex justify-content-between">
                <span className="badge bg-secondary">{category.replace('_', ' ')}</span>
                <span className="fw-bold">{count}</span>
              </div>
            </div>
          ))}
        </div>
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
          <p className="mt-2">Loading feedback statistics...</p>
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
                <i className="fas fa-chart-bar me-2"></i>
                Feedback Statistics
              </h4>
            </div>
            <div className="card-body">
              {!stats ? (
                <div className="text-center py-4">
                  <i className="fas fa-exclamation-triangle fa-3x text-warning mb-3"></i>
                  <h5 className="text-muted">No statistics available</h5>
                  <p className="text-muted">No feedback data found to generate statistics.</p>
                </div>
              ) : (
                <>
                  <div className="row">
                    <div className="col-md-4">
                      <div className="card bg-primary text-white">
                        <div className="card-body text-center">
                          <h3 className="mb-0">{stats.totalFeedback || 0}</h3>
                          <p className="mb-0">Total Feedback</p>
                        </div>
                      </div>
                    </div>
                    <div className="col-md-4">
                      <div className="card bg-success text-white">
                        <div className="card-body text-center">
                          <h3 className="mb-0">{stats.averageOverallRating || 0}</h3>
                          <p className="mb-0">Average Rating</p>
                        </div>
                      </div>
                    </div>
                    <div className="col-md-4">
                      <div className="card bg-info text-white">
                        <div className="card-body text-center">
                          <h3 className="mb-0">{stats.categoryDistribution ? Object.keys(stats.categoryDistribution).length : 0}</h3>
                          <p className="mb-0">Categories</p>
                        </div>
                      </div>
                    </div>
                  </div>
                  
                  <div className="mt-4">
                    {renderRatingDistribution(stats.ratingDistribution)}
                    {renderCategoryDistribution(stats.categoryDistribution)}
                  </div>
                </>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default FeedbackStats
