import { useState, useEffect } from "react"
import { useNavigate } from "react-router-dom"
import axios from "axios"
import { toast } from "react-toastify"
import PaymentForm from "./PaymentForm"

const PaymentPage = () => {
  const [booking, setBooking] = useState(null)
  const [loading, setLoading] = useState(true)
  const navigate = useNavigate()

  useEffect(() => {
    const pendingBooking = localStorage.getItem("pendingBooking")
    if (pendingBooking) {
      try {
        const bookingData = JSON.parse(pendingBooking)
        setBooking(bookingData)
      } catch (error) {
        toast.error("Invalid booking data")
        navigate("/user-dashboard")
      }
    } else {
      toast.error("No pending booking found")
      navigate("/user-dashboard")
    }
    setLoading(false)
  }, [navigate])

  const handlePaymentComplete = (paymentResponse) => {
    toast.success("Payment completed successfully!")
    // Clear the pending booking from localStorage
    localStorage.removeItem("pendingBooking")
    // Redirect to ticket page
    navigate(`/ticket/${paymentResponse.bookingId}`)
  }

  const handleBackToDashboard = () => {
    localStorage.removeItem("pendingBooking")
    navigate("/user-dashboard")
  }

  if (loading) {
    return (
      <div className="container py-5">
        <div className="text-center">
          <div className="spinner-border" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="mt-2">Loading payment details...</p>
        </div>
      </div>
    )
  }

  if (!booking) {
    return (
      <div className="container py-5">
        <div className="text-center">
          <i className="fas fa-exclamation-triangle fa-3x text-warning mb-3"></i>
          <h5>No Booking Found</h5>
          <p>No pending booking was found for payment.</p>
          <button className="btn btn-primary" onClick={handleBackToDashboard}>
            Go to Dashboard
          </button>
        </div>
      </div>
    )
  }

  return (
    <div>
      <div className="container py-3">
        <div className="d-flex justify-content-between align-items-center">
          <h4>
            <i className="fas fa-credit-card me-2"></i>
            Complete Payment
          </h4>
          <button className="btn btn-outline-secondary" onClick={handleBackToDashboard}>
            <i className="fas fa-arrow-left me-2"></i>
            Back to Dashboard
          </button>
        </div>
      </div>
      
      <PaymentForm 
        booking={booking}
        onPaymentComplete={handlePaymentComplete}
        onBack={handleBackToDashboard}
      />
    </div>
  )
}

export default PaymentPage 