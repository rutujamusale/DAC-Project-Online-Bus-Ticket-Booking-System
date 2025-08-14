import { useState, useEffect } from "react"
import { useParams, useNavigate } from "react-router-dom"
import axios from "axios"
import { toast } from "react-toastify"

const Ticket = () => {
  const [booking, setBooking] = useState(null)
  const [loading, setLoading] = useState(true)
  const { bookingId } = useParams()
  const navigate = useNavigate()

  useEffect(() => {
    fetchBookingDetails()
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

  const handlePrint = () => {
    window.print()
  }

  const handleDownload = () => {
    const ticketContent = generateTicketContent()
    const blob = new Blob([ticketContent], { type: 'text/plain' })
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `ticket-${booking.id}.txt`
    document.body.appendChild(a)
    a.click()
    window.URL.revokeObjectURL(url)
    document.body.removeChild(a)
  }

  const generateTicketContent = () => {
    if (!booking) return ""
    
    return `
BUS TICKET
==========================================
Ticket ID: ${booking.id}
Booking Date: ${new Date(booking.bookingDate).toLocaleDateString()}
Payment Status: ${booking.paymentStatus}

JOURNEY DETAILS
==========================================
Bus: ${booking.busName}
From: ${booking.source}
To: ${booking.destination}
Date: ${booking.scheduleDate}
Departure: ${booking.departureTime}
Arrival: ${booking.arrivalTime}

PASSENGER DETAILS
==========================================
${booking.passengers?.map((passenger, index) => `
Passenger ${index + 1}:
  Name: ${passenger.name}
  Age: ${passenger.age}
  Gender: ${passenger.gender}
  Contact: ${passenger.contact}
  Email: ${passenger.email}
  UID: ${passenger.uid}
`).join('\n')}

SEAT DETAILS
==========================================
${booking.bookingSeats?.map((seat, index) => `
Seat ${index + 1}: ${seat.seatNumber}
  Passenger: ${seat.passengerName}
  Price: ₹${seat.seatPrice}
`).join('\n')}

TOTAL AMOUNT: ₹${booking.totalAmount}
==========================================

IMPORTANT NOTES:
- Please arrive 30 minutes before departure
- Carry valid ID proof
- This ticket is non-transferable
- Cancellation charges apply as per policy

For support: support@busbooking.com
Phone: +91-1800-123-4567
    `.trim()
  }

  if (loading) {
    return (
      <div className="container py-5">
        <div className="text-center">
          <div className="spinner-border" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="mt-2">Loading ticket details...</p>
        </div>
      </div>
    )
  }

  if (!booking) {
    return (
      <div className="container py-5">
        <div className="text-center">
          <i className="fas fa-exclamation-triangle fa-3x text-warning mb-3"></i>
          <h5>Ticket Not Found</h5>
          <p>The requested ticket could not be found.</p>
          <button className="btn btn-primary" onClick={() => navigate("/user-dashboard")}>
            Go to Dashboard
          </button>
        </div>
      </div>
    )
  }

  return (
    <div className="container py-4">
      {/* Print/Download Buttons - Hidden when printing */}
      <div className="d-print-none mb-4">
        <div className="d-flex justify-content-between align-items-center">
          <h4>
            <i className="fas fa-ticket-alt me-2"></i>
            Your Ticket
          </h4>
                     <div>
             <button className="btn btn-outline-primary me-2" onClick={handleDownload}>
               <i className="fas fa-download me-2"></i>
               Download
             </button>
             <button className="btn btn-primary me-2" onClick={handlePrint}>
               <i className="fas fa-print me-2"></i>
               Print
             </button>
             <button className="btn btn-success me-2" onClick={() => navigate(`/feedback/${booking.id}`)}>
               <i className="fas fa-comment-alt me-2"></i>
               Give Feedback
             </button>
             <button className="btn btn-outline-secondary" onClick={() => navigate("/user-dashboard")}>
               <i className="fas fa-arrow-left me-2"></i>
               Back to Dashboard
             </button>
           </div>
        </div>
      </div>

      {/* Ticket Content */}
      <div className="card border-2 border-primary">
        <div className="card-header bg-primary text-white text-center">
          <h3 className="mb-0">
            <i className="fas fa-bus me-2"></i>
            BUS TICKET
          </h3>
        </div>
        <div className="card-body">
          {/* Ticket Header */}
          <div className="row mb-4">
            <div className="col-md-6">
              <h6 className="text-muted">Ticket ID</h6>
              <h5 className="fw-bold">#{booking.id}</h5>
            </div>
            <div className="col-md-6 text-md-end">
              <h6 className="text-muted">Booking Date</h6>
              <h5 className="fw-bold">{new Date(booking.bookingDate).toLocaleDateString()}</h5>
            </div>
          </div>

          {/* Journey Details */}
          <div className="row mb-4">
            <div className="col-12">
              <h5 className="border-bottom pb-2 mb-3">
                <i className="fas fa-route me-2"></i>
                Journey Details
              </h5>
            </div>
            <div className="col-md-6">
              <p><strong>Bus:</strong> {booking.busName}</p>
              <p><strong>From:</strong> {booking.source}</p>
              <p><strong>To:</strong> {booking.destination}</p>
            </div>
            <div className="col-md-6">
              <p><strong>Date:</strong> {booking.scheduleDate}</p>
              <p><strong>Departure:</strong> {booking.departureTime}</p>
              <p><strong>Arrival:</strong> {booking.arrivalTime}</p>
            </div>
          </div>

          {/* Passenger Details */}
          {booking.passengers && booking.passengers.length > 0 && (
            <div className="row mb-4">
              <div className="col-12">
                <h5 className="border-bottom pb-2 mb-3">
                  <i className="fas fa-users me-2"></i>
                  Passenger Details
                </h5>
                <div className="row">
                  {booking.passengers.map((passenger, index) => (
                    <div key={passenger.id} className="col-md-6 mb-3">
                      <div className="card border-light">
                        <div className="card-body">
                          <h6 className="card-title">Passenger {index + 1}</h6>
                          <p className="mb-1"><strong>Name:</strong> {passenger.name}</p>
                          <p className="mb-1"><strong>Age:</strong> {passenger.age}</p>
                          <p className="mb-1"><strong>Gender:</strong> {passenger.gender}</p>
                          <p className="mb-1"><strong>Contact:</strong> {passenger.contact}</p>
                          <p className="mb-1"><strong>Email:</strong> {passenger.email}</p>
                          <p className="mb-0"><strong>UID:</strong> {passenger.uid}</p>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          )}

          {/* Seat Details */}
          {booking.bookingSeats && booking.bookingSeats.length > 0 && (
            <div className="row mb-4">
              <div className="col-12">
                <h5 className="border-bottom pb-2 mb-3">
                  <i className="fas fa-chair me-2"></i>
                  Seat Details
                </h5>
                <div className="table-responsive">
                  <table className="table table-bordered">
                    <thead className="table-light">
                      <tr>
                        <th>Seat Number</th>
                        <th>Passenger Name</th>
                        <th>Price</th>
                      </tr>
                    </thead>
                    <tbody>
                      {booking.bookingSeats.map((seat) => (
                        <tr key={seat.id}>
                          <td><strong>{seat.seatNumber}</strong></td>
                          <td>{seat.passengerName}</td>
                          <td>₹{seat.seatPrice}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          )}

          {/* Total Amount */}
          <div className="row">
            <div className="col-12">
              <div className="alert alert-success">
                <h5 className="mb-0">
                  <i className="fas fa-rupee-sign me-2"></i>
                  Total Amount: ₹{booking.totalAmount}
                </h5>
              </div>
            </div>
          </div>

          {/* Important Notes */}
          <div className="row mt-4">
            <div className="col-12">
              <div className="alert alert-warning">
                <h6><i className="fas fa-exclamation-triangle me-2"></i>Important Notes:</h6>
                <ul className="mb-0">
                  <li>Please arrive 30 minutes before departure</li>
                  <li>Carry valid ID proof</li>
                  <li>This ticket is non-transferable</li>
                  <li>Cancellation charges apply as per policy</li>
                </ul>
              </div>
            </div>
          </div>

          {/* Contact Information */}
          <div className="row mt-3">
            <div className="col-12 text-center">
              <small className="text-muted">
                For support: support@busbooking.com | Phone: +91-1800-123-4567
              </small>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Ticket 