"use client"

import { useState, useEffect } from "react"
import axios from "axios"
import { toast } from "react-toastify"

const UserDashboard = () => {
  const [bookings, setBookings] = useState([])
  const [loading, setLoading] = useState(true)
  const [userInfo, setUserInfo] = useState(null)

  useEffect(() => {
    fetchUserInfo()
    fetchUserBookings()
  }, [])

  const fetchUserInfo = () => {
    const userId = localStorage.getItem("userId")
    const firstName = localStorage.getItem("userFirstName")
    const lastName = localStorage.getItem("userLastName")
    const email = localStorage.getItem("userEmail")
    
    setUserInfo({
      userId,
      firstName,
      lastName,
      email
    })
  }

  const fetchUserBookings = async () => {
    try {
      const userId = localStorage.getItem("userId")
      const response = await axios.get(`http://localhost:8080/api/bookings/user/${userId}`)
      setBookings(response.data)
    } catch (error) {
      toast.error("Error fetching bookings: " + (error.response?.data?.message || error.message))
    } finally {
      setLoading(false)
    }
  }

  const handleUnlockSeats = async (bookingId) => {
    try {
      const userId = localStorage.getItem("userId")
      const response = await axios.post(`http://localhost:8080/api/bookings/${bookingId}/unlock?userId=${userId}`)
      
      if (response.data.success) {
        toast.success("Seats unlocked successfully!")
        fetchUserBookings() // Refresh bookings
      } else {
        toast.error(response.data.message)
      }
    } catch (error) {
      toast.error("Error unlocking seats: " + (error.response?.data?.message || error.message))
    }
  }

  const handleCancelBooking = async (bookingId) => {
    if (!window.confirm("Are you sure you want to cancel this booking?")) {
      return
    }

    try {
      const userId = localStorage.getItem("userId")
      const response = await axios.post(`http://localhost:8080/api/bookings/${bookingId}/cancel?userId=${userId}`)
      
      if (response.data.success) {
        toast.success("Booking cancelled successfully!")
        fetchUserBookings() // Refresh bookings
      } else {
        toast.error(response.data.message)
      }
    } catch (error) {
      toast.error("Error cancelling booking: " + (error.response?.data?.message || error.message))
    }
  }

  const handleContinueBooking = (booking) => {
    // Store booking data in localStorage for payment form
    localStorage.setItem("pendingBooking", JSON.stringify(booking))
    // Navigate to payment form
    window.location.href = "/payment"
  }

  const handleViewTicket = (bookingId) => {
    window.open(`/ticket/${bookingId}`, '_blank')
  }

  const handleDownloadTicket = (bookingId) => {
    window.open(`/ticket/${bookingId}`, '_blank')
  }

  const handleGiveFeedback = (bookingId) => {
    window.location.href = `/feedback/${bookingId}`
  }

  const getStatusBadge = (status) => {
    switch (status) {
      case "PENDING":
        return <span className="badge bg-warning">Pending</span>
      case "CONFIRMED":
        return <span className="badge bg-success">Confirmed</span>
      case "CANCELLED":
        return <span className="badge bg-danger">Cancelled</span>
      case "COMPLETED":
        return <span className="badge bg-info">Completed</span>
      default:
        return <span className="badge bg-secondary">{status}</span>
    }
  }

  if (loading) {
    return (
      <div className="container py-5">
        <div className="text-center">
          <div className="spinner-border" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="mt-2">Loading your bookings...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="container py-4">
      <div className="row">
        <div className="col-12">
          <div className="card">
            <div className="card-header d-flex justify-content-between align-items-center">
              <div>
                <h4 className="mb-0">
                  <i className="fas fa-user me-2"></i>
                  Welcome, {userInfo?.firstName} {userInfo?.lastName}
                </h4>
                <p className="text-muted mb-0">{userInfo?.email}</p>
              </div>
              <div>
                <button 
                  className="btn btn-outline-danger"
                  onClick={() => window.location.href = "/account-deactivation"}
                >
                  <i className="fas fa-user-times me-2"></i>
                  Deactivate Account
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="row mt-4">
        <div className="col-12">
          <div className="card">
            <div className="card-header">
              <h5 className="mb-0">
                <i className="fas fa-ticket-alt me-2"></i>
                My Bookings
              </h5>
            </div>
            <div className="card-body">
              {bookings.length === 0 ? (
                <div className="text-center py-4">
                  <i className="fas fa-ticket-alt fa-3x text-muted mb-3"></i>
                  <h5 className="text-muted">No bookings found</h5>
                  <p className="text-muted">You haven't made any bookings yet.</p>
                  <a href="/" className="btn btn-primary">
                    <i className="fas fa-search me-2"></i>
                    Search for Buses
                  </a>
                </div>
              ) : (
                <div className="row">
                  {bookings.map((booking) => (
                    <div key={booking.id} className="col-12 mb-4">
                      <div className="card border">
                        <div className="card-header d-flex justify-content-between align-items-center">
                          <div>
                            <h6 className="mb-1">
                              <i className="fas fa-bus me-2"></i>
                              {booking.busName}
                            </h6>
                            <p className="mb-0 text-muted">
                              {booking.source} → {booking.destination}
                            </p>
                          </div>
                          <div className="text-end">
                            {getStatusBadge(booking.status)}
                            <p className="mb-0 text-muted small">
                              {new Date(booking.bookingDate).toLocaleDateString()}
                            </p>
                          </div>
                        </div>
                        <div className="card-body">
                          <div className="row">
                            <div className="col-md-6">
                              <p><strong>Date:</strong> {booking.scheduleDate}</p>
                              <p><strong>Departure:</strong> {booking.departureTime}</p>
                              <p><strong>Arrival:</strong> {booking.arrivalTime}</p>
                            </div>
                            <div className="col-md-6">
                              <p><strong>Total Amount:</strong> ₹{booking.totalAmount}</p>
                              <p><strong>Seats:</strong> {booking.bookingSeats.length}</p>
                              <p><strong>Status:</strong> {booking.status}</p>
                              <p><strong>Payment Status:</strong> {booking.paymentStatus || "PENDING"}</p>
                            </div>
                          </div>

                          {/* Seat and Passenger Details */}
                          <div className="mt-3">
                            <h6>Seat and Passenger Details:</h6>
                            <div className="table-responsive">
                              <table className="table table-sm">
                                <thead>
                                  <tr>
                                    <th>Seat</th>
                                    <th>Name</th>
                                    <th>Age</th>
                                    <th>Gender</th>
                                    <th>Phone</th>
                                    <th>Email</th>
                                    <th>Price</th>
                                  </tr>
                                </thead>
                                <tbody>
                                  {booking.bookingSeats.map((seat) => (
                                    <tr key={seat.id}>
                                      <td>{seat.seatNumber}</td>
                                      <td>{seat.passengerName}</td>
                                      <td>{seat.passengerAge}</td>
                                      <td>{seat.passengerGender}</td>
                                      <td>{seat.passengerPhone}</td>
                                      <td>{seat.passengerEmail}</td>
                                      <td>₹{seat.seatPrice}</td>
                                    </tr>
                                  ))}
                                </tbody>
                              </table>
                            </div>
                          </div>

                          {/* Additional Passenger Details */}
                          {booking.passengers && booking.passengers.length > 0 && (
                            <div className="mt-3">
                              <h6>Passenger Information:</h6>
                              <div className="row">
                                {booking.passengers.map((passenger) => (
                                  <div key={passenger.id} className="col-md-6 mb-2">
                                    <div className="card border-light">
                                      <div className="card-body p-2">
                                        <div className="d-flex justify-content-between">
                                          <strong>{passenger.name}</strong>
                                          <small className="text-muted">UID: {passenger.uid}</small>
                                        </div>
                                        <div className="small text-muted">
                                          Age: {passenger.age} | Gender: {passenger.gender}
                                        </div>
                                        <div className="small text-muted">
                                          Contact: {passenger.contact} | Email: {passenger.email}
                                        </div>
                                      </div>
                                    </div>
                                  </div>
                                ))}
                              </div>
                            </div>
                          )}

                                                     {/* Action Buttons */}
                           {booking.status === "PENDING" && (
                             <div className="mt-3">
                               <button
                                 className="btn btn-primary btn-sm me-2"
                                 onClick={() => handleContinueBooking(booking)}
                               >
                                 <i className="fas fa-credit-card me-1"></i>
                                 Continue to Payment
                               </button>
                               <button
                                 className="btn btn-warning btn-sm me-2"
                                 onClick={() => handleUnlockSeats(booking.id)}
                               >
                                 <i className="fas fa-unlock me-1"></i>
                                 Unlock Seats
                               </button>
                               <button
                                 className="btn btn-danger btn-sm"
                                 onClick={() => handleCancelBooking(booking.id)}
                               >
                                 <i className="fas fa-times me-1"></i>
                                 Cancel Booking
                               </button>
                             </div>
                           )}
                           
                           {/* Ticket and Feedback Buttons for Confirmed/Completed Bookings */}
                           {(booking.status === "CONFIRMED" || booking.status === "COMPLETED") && (
                             <div className="mt-3">
                               <button
                                 className="btn btn-success btn-sm me-2"
                                 onClick={() => handleViewTicket(booking.id)}
                               >
                                 <i className="fas fa-ticket-alt me-1"></i>
                                 View Ticket
                               </button>
                               <button
                                 className="btn btn-info btn-sm me-2"
                                 onClick={() => handleDownloadTicket(booking.id)}
                               >
                                 <i className="fas fa-download me-1"></i>
                                 Download
                               </button>
                               <button
                                 className="btn btn-warning btn-sm"
                                 onClick={() => handleGiveFeedback(booking.id)}
                               >
                                 <i className="fas fa-comment-alt me-1"></i>
                                 Give Feedback
                               </button>
                             </div>
                           )}
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

export default UserDashboard
