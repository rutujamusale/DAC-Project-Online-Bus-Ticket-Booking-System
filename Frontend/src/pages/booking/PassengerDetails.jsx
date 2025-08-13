import { useState, useEffect } from "react"
import axios from "axios"
import { toast } from "react-toastify"
import PaymentForm from "../payment/PaymentForm"

const PassengerDetails = ({ selectedSeats, scheduleInfo, onBack, onBookingComplete }) => {
  const [passengers, setPassengers] = useState([])
  const [loading, setLoading] = useState(false)
  const [booking, setBooking] = useState(null)
  const [showPayment, setShowPayment] = useState(false)
  const [timeLeft, setTimeLeft] = useState(10 * 60) // 10 minutes in seconds
  const [dataRestored, setDataRestored] = useState(false) // Track if data has been restored
  const [seatsReserved, setSeatsReserved] = useState(false) // Track if seats are reserved

  // Initialize passengers array based on selected seats - only run once
  useEffect(() => {
    if (!selectedSeats || selectedSeats.length === 0 || !scheduleInfo) {
      toast.error("Invalid seat selection or schedule information")
      onBack()
      return
    }

    // Don't proceed if we're already in payment mode
    if (showPayment) {
      return
    }

    // Check if seats are reserved (user might be returning from payment)
    const hasReservedSeats = selectedSeats.some(seat => seat.status === "RESERVED")
    if (hasReservedSeats) {
      setSeatsReserved(true)
      toast.info("Your seats are already reserved. You can continue with payment.")
    }

    // Only try to restore data once when component first loads
    if (!dataRestored) {
      const savedData = localStorage.getItem(`passengerForm_${scheduleInfo.id}_${selectedSeats.map(s => s.id).join('_')}`)
      
      if (savedData) {
        try {
          const parsedData = JSON.parse(savedData)
          // Validate that the saved data matches the current seat selection
          if (parsedData.length === selectedSeats.length) {
            setPassengers(parsedData)
            toast.info("Previous form data has been restored")
          } else {
            initializePassengers()
          }
        } catch (error) {
          console.error("Error parsing saved data:", error)
          initializePassengers()
        }
      } else {
        initializePassengers()
      }
      setDataRestored(true)
    }
  }, [selectedSeats, scheduleInfo, onBack, dataRestored])

  const initializePassengers = () => {
    const initialPassengers = selectedSeats.map((seat, index) => ({
      id: index,
      seatNumber: seat.seatNumber,
      name: "",
      age: "",
      gender: "",
      phone: "",
      email: ""
    }))
    setPassengers(initialPassengers)
  }

  // Countdown timer - starts when seats are reserved, stops when payment starts
  useEffect(() => {
    // Don't start countdown if seats are not reserved or if we're in payment mode
    if (!seatsReserved || showPayment) {
      return
    }

    const timer = setInterval(() => {
      setTimeLeft(prev => {
        if (prev <= 1) {
          // Only redirect if we're not in payment mode
          if (!showPayment) {
            toast.error("Time's up! Your seats have been released. Please go back and select seats again.")
            // Clear saved form data before going back
            if (scheduleInfo && selectedSeats) {
              const storageKey = `passengerForm_${scheduleInfo.id}_${selectedSeats.map(s => s.id).join('_')}`
              localStorage.removeItem(storageKey)
            }
            // Redirect to home page instead of back to seat selection
            window.location.href = "/"
          }
          return 0
        }
        return prev - 1
      })
    }, 1000)

    return () => clearInterval(timer)
  }, [onBack, seatsReserved, showPayment, scheduleInfo, selectedSeats])

  // Stop countdown when payment starts
  useEffect(() => {
    if (showPayment) {
      // Clear any existing countdown when payment starts
      setTimeLeft(10 * 60) // Reset to 10 minutes
    }
  }, [showPayment])

  // Cleanup saved data when component unmounts
  useEffect(() => {
    return () => {
      // Only clear if we're not going to payment (payment completion will handle its own cleanup)
      if (!showPayment && scheduleInfo && selectedSeats) {
        const storageKey = `passengerForm_${scheduleInfo.id}_${selectedSeats.map(s => s.id).join('_')}`
        localStorage.removeItem(storageKey)
      }
    }
  }, [showPayment, scheduleInfo, selectedSeats])

  const formatTime = (seconds) => {
    const minutes = Math.floor(seconds / 60)
    const remainingSeconds = seconds % 60
    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`
  }

  const handleInputChange = (index, field, value) => {
    if (index < 0 || index >= passengers.length) return

    const updatedPassengers = [...passengers]
    
    // Format phone number input
    if (field === 'phone') {
      // Remove all non-numeric characters except + and spaces
      let cleanedValue = value.replace(/[^\d+\s]/g, '')
      
      // Handle +91 prefix
      if (cleanedValue.startsWith('+91')) {
        // Keep +91 and add up to 10 digits after it
        const digitsAfterPrefix = cleanedValue.substring(3).replace(/\D/g, '').slice(0, 10)
        updatedPassengers[index][field] = '+91' + digitsAfterPrefix
      } else if (cleanedValue.startsWith('91')) {
        // Handle 91 prefix
        const digitsAfterPrefix = cleanedValue.substring(2).replace(/\D/g, '').slice(0, 10)
        updatedPassengers[index][field] = '91' + digitsAfterPrefix
      } else {
        // Regular 10-digit number
        const numericValue = cleanedValue.replace(/\D/g, '').slice(0, 10)
        updatedPassengers[index][field] = numericValue
      }
    } else {
      updatedPassengers[index][field] = value
    }
    
    setPassengers(updatedPassengers)
    
    // Save to localStorage
    if (scheduleInfo && selectedSeats) {
      const storageKey = `passengerForm_${scheduleInfo.id}_${selectedSeats.map(s => s.id).join('_')}`
      localStorage.setItem(storageKey, JSON.stringify(updatedPassengers))
    }
  }

  const validatePhoneNumber = (phone) => {
    const phoneRegex = /^[6-9]\d{9}$|^\+91[6-9]\d{9}$|^91[6-9]\d{9}$/
    return phoneRegex.test(phone.trim())
  }

  const formatPhoneDisplay = (phone) => {
    if (!phone) return ''
    
    const cleanPhone = phone.replace(/\D/g, '')
    
    if (cleanPhone.length === 10) {
      return cleanPhone.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3')
    } else if (cleanPhone.length === 12 && cleanPhone.startsWith('91')) {
      const number = cleanPhone.substring(2)
      return `+91 ${number.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3')}`
    } else if (cleanPhone.length === 13 && cleanPhone.startsWith('91')) {
      const number = cleanPhone.substring(2)
      return `+91 ${number.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3')}`
    }
    
    return phone
  }

  const validatePassengerData = (passenger, index) => {
    if (!passenger.name.trim()) {
      toast.error(`Please enter name for Passenger ${index + 1}`)
      return false
    }
    
    if (!passenger.age || passenger.age < 1 || passenger.age > 120) {
      toast.error(`Please enter valid age (1-120) for Passenger ${index + 1}`)
      return false
    }
    
    if (!passenger.gender) {
      toast.error(`Please select gender for Passenger ${index + 1}`)
      return false
    }
    
    if (!passenger.phone.trim()) {
      toast.error(`Please enter phone number for Passenger ${index + 1}`)
      return false
    }
    
    // Enhanced phone validation matching backend pattern
    const cleanPhone = passenger.phone.trim()
    
    if (!validatePhoneNumber(cleanPhone)) {
      toast.error(`Please enter a valid Indian mobile number for Passenger ${index + 1}. 
        It should start with 6, 7, 8, or 9 and be 10 digits (e.g., 9876543210) or with +91/91 prefix.`)
      return false
    }
    
    if (!passenger.email.trim()) {
      toast.error(`Please enter email for Passenger ${index + 1}`)
      return false
    }
    
    // Basic email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(passenger.email)) {
      toast.error(`Please enter valid email for Passenger ${index + 1}`)
      return false
    }

    return true
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    if (!scheduleInfo || !selectedSeats || selectedSeats.length === 0) {
      toast.error("Invalid booking information")
      return
    }

    // Validate all fields are filled
    for (let i = 0; i < passengers.length; i++) {
      if (!validatePassengerData(passengers[i], i)) {
        return
      }
    }

    setLoading(true)
    
    try {
      // Get userId from localStorage
      const userId = localStorage.getItem("userId") || localStorage.getItem("userToken")
      if (!userId) {
        toast.error("Please login to continue")
        window.location.href = "/user-login"
        return
      }

      // First reserve the seats (this is when seats become RESERVED)
      const lockResponse = await axios.post("http://localhost:8080/api/seats/lock", {
        scheduleId: scheduleInfo.id,
        selectedSeatIds: selectedSeats.map(seat => seat.id)
      })

      if (!lockResponse.data.success) {
        toast.error(lockResponse.data.message || "Failed to reserve seats")
        setLoading(false)
        return
      }

      // Seats are now reserved, start the countdown
      setSeatsReserved(true)
      toast.success("Seats reserved successfully! You have 10 minutes to complete payment.")

      // Create booking with passenger details
      const bookingRequest = {
        userId: parseInt(userId),
        scheduleId: scheduleInfo.id,
        passengers: passengers.map((passenger, index) => ({
          seatId: selectedSeats[index].id,
          passengerName: passenger.name.trim(),
          passengerAge: parseInt(passenger.age),
          passengerGender: passenger.gender,
          passengerPhone: passenger.phone.trim(),
          passengerEmail: passenger.email.trim()
        }))
      }

      console.log("Sending booking request:", bookingRequest)

      const bookingResponse = await axios.post("http://localhost:8080/api/bookings", bookingRequest)
      
      if (bookingResponse.data) {
        toast.success("Booking created successfully! Please complete payment to confirm your seats.")
        setBooking(bookingResponse.data)
        setShowPayment(true)
      } else {
        toast.error("Failed to create booking")
      }
      
    } catch (error) {
      console.error("Booking creation error:", error)
      
      // Check for validation errors in the response
      if (error.response?.data?.message && error.response.data.message.includes('Invalid Indian contact number')) {
        toast.error("❌ Invalid phone number format! Please enter a valid Indian mobile number starting with 6, 7, 8, or 9 (e.g., 9876543210)")
      } else if (error.response?.status === 400) {
        // Parse validation errors from the response
        const errorMessage = error.response?.data?.message || "Please check all fields"
        if (errorMessage.includes('Validation failed')) {
          toast.error("❌ Please check your input data. Make sure all fields are filled correctly.")
        } else {
          toast.error("❌ " + errorMessage)
        }
      } else if (error.response?.status === 404) {
        toast.error("❌ User or seat not found. Please refresh and try again.")
      } else if (error.response?.status === 500) {
        toast.error("❌ Server error: " + (error.response?.data?.message || "Database error occurred. Please try again."))
        console.error("Server error details:", error.response?.data)
      } else if (error.code === 'NETWORK_ERROR' || error.message.includes('Network Error')) {
        toast.error("❌ Network error. Please check your internet connection and try again.")
      } else {
        toast.error("❌ Error creating booking: " + (error.response?.data?.message || error.message))
      }
    } finally {
      setLoading(false)
    }
  }

  const totalAmount = selectedSeats.reduce((sum, seat) => sum + (seat.price || 0), 0)

  const handlePaymentComplete = (paymentResponse) => {
    // Clear saved form data
    if (scheduleInfo && selectedSeats) {
      const storageKey = `passengerForm_${scheduleInfo.id}_${selectedSeats.map(s => s.id).join('_')}`
      localStorage.removeItem(storageKey)
    }
    
    toast.success("Payment completed successfully! Your seats are now confirmed.")
    // Wait a moment for the toast to show, then redirect
    setTimeout(() => {
      window.location.href = "/user-dashboard"
    }, 2000)
  }

  const handleBackToBooking = () => {
    setShowPayment(false)
    setBooking(null)
    // Reset the countdown when going back from payment
    setTimeLeft(10 * 60)
    // Show a message to the user
    toast.info("Returned to booking details. You can continue with payment or modify details.")
  }

  const handleBackToSeatSelection = () => {
    // Clear saved form data when going back
    if (scheduleInfo && selectedSeats) {
      const storageKey = `passengerForm_${scheduleInfo.id}_${selectedSeats.map(s => s.id).join('_')}`
      localStorage.removeItem(storageKey)
    }
    onBack()
  }



  // Show loading or error state if no valid data
  if (!scheduleInfo || !selectedSeats || selectedSeats.length === 0) {
    return (
      <div className="container py-4">
        <div className="text-center">
          <div className="spinner-border" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="mt-2">Loading passenger details...</p>
        </div>
      </div>
    )
  }

  // If we're in payment mode, don't show the passenger details form
  if (showPayment && booking) {
    return (
      <div>
        <PaymentForm 
          booking={booking}
          onPaymentComplete={handlePaymentComplete}
          onBack={handleBackToBooking}
        />
      </div>
    )
  }

  return (
    <div className="container py-4">
      <div className="row">
        <div className="col-lg-8">
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Passenger Details</h5>
              
              {/* Journey Summary */}
              <div className="alert alert-info mb-4">
                <h6>Journey Summary</h6>
                <p className="mb-1"><strong>From:</strong> {scheduleInfo?.source} <strong>To:</strong> {scheduleInfo?.destination}</p>
                <p className="mb-1"><strong>Date:</strong> {scheduleInfo?.scheduleDate} <strong>Time:</strong> {scheduleInfo?.departureTime}</p>
                <p className="mb-0"><strong>Selected Seats:</strong> {selectedSeats.map(seat => seat.seatNumber).join(", ")}</p>
              </div>

              {/* Time Limit Warning - only show after seats are reserved */}
              {seatsReserved && (
                <div className={`alert ${timeLeft < 300 ? 'alert-danger' : 'alert-warning'} mb-4`}>
                  <h6><i className="fas fa-clock me-2"></i>Payment Time Limit</h6>
                  <div className="mb-2">
                    <div className="d-flex justify-content-between align-items-center mb-1">
                      <span>Time remaining: <strong>{formatTime(timeLeft)}</strong></span>
                      <span>{Math.round((timeLeft / (10 * 60)) * 100)}%</span>
                    </div>
                    <div className="progress" style={{ height: '8px' }}>
                      <div 
                        className={`progress-bar ${timeLeft < 300 ? 'bg-danger' : 'bg-warning'}`}
                        style={{ width: `${(timeLeft / (10 * 60)) * 100}%` }}
                      ></div>
                    </div>
                  </div>
                  <p className="mb-1">You have <strong>10 minutes</strong> to complete payment. If you don't pay within this time, your seats will be released.</p>
                  <p className="mb-0">Please complete your payment to confirm your booking.</p>
                </div>
              )}

              {/* Instructions before reservation */}
              {!seatsReserved && (
                <div className="alert alert-info mb-4">
                  <h6><i className="fas fa-info-circle me-2"></i>Important Information</h6>
                  <p className="mb-1">Your seats are currently selected but not reserved.</p>
                  <p className="mb-1">When you click "Confirm Booking", your seats will be reserved for <strong>10 minutes</strong>.</p>
                  <p className="mb-0">You must complete payment within 10 minutes to confirm your booking.</p>
                </div>
              )}

              <form onSubmit={handleSubmit}>
                {passengers.map((passenger, index) => (
                  <div key={passenger.id} className="card mb-3">
                    <div className="card-header">
                      <h6 className="mb-0">Passenger {index + 1} - Seat {passenger.seatNumber}</h6>
                    </div>
                    <div className="card-body">
                      <div className="row">
                        <div className="col-md-6 mb-3">
                          <label className="form-label">Full Name *</label>
                          <input
                            type="text"
                            className="form-control"
                            value={passenger.name}
                            onChange={(e) => handleInputChange(index, "name", e.target.value)}
                            required
                            maxLength="50"
                            disabled={seatsReserved}
                          />
                        </div>
                        <div className="col-md-3 mb-3">
                          <label className="form-label">Age *</label>
                          <input
                            type="number"
                            className="form-control"
                            value={passenger.age}
                            onChange={(e) => handleInputChange(index, "age", e.target.value)}
                            min="1"
                            max="120"
                            required
                            disabled={seatsReserved}
                          />
                        </div>
                        <div className="col-md-3 mb-3">
                          <label className="form-label">Gender *</label>
                          <select
                            className="form-select"
                            value={passenger.gender}
                            onChange={(e) => handleInputChange(index, "gender", e.target.value)}
                            style={{ zIndex: 1050 }}
                            required
                            disabled={seatsReserved}
                          >
                            <option value="">Select</option>
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                            <option value="Other">Other</option>
                          </select>
                        </div>
                      </div>
                      <div className="row">
                        <div className="col-md-6 mb-3">
                          <label className="form-label">Phone Number *</label>
                          <input
                            type="tel"
                            className={`form-control ${passenger.phone && !validatePhoneNumber(passenger.phone) ? 'is-invalid' : passenger.phone && validatePhoneNumber(passenger.phone) ? 'is-valid' : ''}`}
                            value={passenger.phone}
                            onChange={(e) => handleInputChange(index, "phone", e.target.value)}
                            required
                            placeholder="e.g., 9876543210 or +919876543210"
                            disabled={seatsReserved}
                          />
                          <small className="text-muted">
                            Enter 10-digit Indian mobile number starting with 6, 7, 8, or 9
                          </small>
                          {passenger.phone && !validatePhoneNumber(passenger.phone) && (
                            <div className="invalid-feedback">
                              Please enter a valid Indian mobile number
                            </div>
                          )}
                          {passenger.phone && validatePhoneNumber(passenger.phone) && (
                            <div className="valid-feedback">
                              ✓ Valid phone number
                            </div>
                          )}
                        </div>
                        <div className="col-md-6 mb-3">
                          <label className="form-label">Email *</label>
                          <input
                            type="email"
                            className="form-control"
                            value={passenger.email}
                            onChange={(e) => handleInputChange(index, "email", e.target.value)}
                            required
                            maxLength="100"
                            disabled={seatsReserved}
                          />
                        </div>
                      </div>
                    </div>
                  </div>
                ))}

                <div className="d-flex justify-content-between">
                  <button
                    type="button"
                    className="btn btn-outline-secondary"
                    onClick={handleBackToSeatSelection}
                    disabled={loading || seatsReserved}
                  >
                    Back to Seat Selection
                  </button>
                  <button
                    type="submit"
                    className="btn btn-primary"
                    disabled={loading || seatsReserved}
                  >
                    {loading ? (
                      <>
                        <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                        Processing...
                      </>
                    ) : seatsReserved ? (
                      "Seats Reserved - Complete Payment"
                    ) : (
                      "Confirm Booking & Reserve Seats"
                    )}
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>

        <div className="col-lg-4">
          <div className="card sticky-top" style={{ top: "20px", zIndex: 1 }}>
            <div className="card-body">
              <h5 className="card-title">Booking Summary</h5>
              
              <div className="mb-3">
                <h6>Selected Seats:</h6>
                {selectedSeats.map((seat) => (
                  <div key={seat.id} className="d-flex justify-content-between mb-1">
                    <span>Seat {seat.seatNumber}</span>
                    <span>₹{seat.price || 0}</span>
                  </div>
                ))}
              </div>
              
              <hr />
              
              <div className="d-flex justify-content-between mb-3">
                <strong>Total Amount:</strong>
                <strong>₹{totalAmount}</strong>
              </div>
              
              <div className="alert alert-warning">
                <small>
                  <i className="fas fa-info-circle me-2"></i>
                  {seatsReserved 
                    ? "Seats are reserved. Complete payment within 10 minutes to confirm booking."
                    : "Please ensure all passenger details are accurate. Changes cannot be made after booking confirmation."
                  }
                </small>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default PassengerDetails 