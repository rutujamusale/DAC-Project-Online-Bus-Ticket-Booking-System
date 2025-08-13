"use client"

import { useState, useEffect, useCallback } from "react"
import axios from "axios"
import { toast } from "react-toastify"
import "./SeatSelection.css"
import PassengerDetails from "./PassengerDetails"

const SeatSelection = ({ scheduleId, onSeatsSelected, onBack }) => {
  const [seats, setSeats] = useState([])
  const [selectedSeats, setSelectedSeats] = useState([])
  const [loading, setLoading] = useState(true)
  const [scheduleInfo, setScheduleInfo] = useState(null)
  const [showPassengerDetails, setShowPassengerDetails] = useState(false)
  const [isUserLoggedIn, setIsUserLoggedIn] = useState(false)
  const [refreshing, setRefreshing] = useState(false)

  // Memoized fetch function to prevent unnecessary re-renders
  const fetchSeats = useCallback(async () => {
    try {
      setRefreshing(true)
      
      // First, unlock any expired seats
      try {
        await axios.post("http://localhost:8080/api/seats/unlock-expired")
      } catch (error) {
        console.error("Error unlocking expired seats:", error)
        // Don't show error to user as this is background operation
      }

      // Then fetch the updated seat status
      const response = await axios.get(`http://localhost:8080/api/seats/schedule/${scheduleId}`)
      console.log("Fetched seats:", response.data)
      setSeats(response.data)
      
             // Clear any selected seats that are no longer available
       setSelectedSeats(prev => 
         prev.filter(selectedSeat => {
           const updatedSeat = response.data.find(s => s.id === selectedSeat.id)
           // Keep seats that are still available or reserved (in case user is returning from payment)
           return updatedSeat && (updatedSeat.status === "AVAILABLE" || updatedSeat.status === "RESERVED")
         })
       )
    } catch (error) {
      console.error("Error fetching seats:", error)
      toast.error("Error fetching seats: " + (error.response?.data?.message || error.message))
    } finally {
      setRefreshing(false)
      setLoading(false)
    }
  }, [scheduleId])

  const fetchScheduleInfo = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/schedules/${scheduleId}`)
      setScheduleInfo(response.data)
    } catch (error) {
      console.error("Error fetching schedule info:", error)
      toast.error("Error fetching schedule information")
    }
  }

  const checkUserLoginStatus = () => {
    // Check if user is logged in by looking for JWT token in localStorage
    const token = localStorage.getItem("token") || localStorage.getItem("userToken")
    const userId = localStorage.getItem("userId")
    setIsUserLoggedIn(!!token && !!userId)
  }

  useEffect(() => {
    fetchSeats()
    fetchScheduleInfo()
    checkUserLoginStatus()
    
    // Set up periodic seat refresh to update status
    const refreshInterval = setInterval(() => {
      fetchSeats()
    }, 10000) // Refresh every 10 seconds to catch auto-unlocks faster
    
    return () => clearInterval(refreshInterval)
  }, [fetchSeats])

  const handleSeatClick = (seat) => {
    // Prevent selection of booked or reserved seats
    if (seat.status === "BOOKED") {
      toast.error("This seat is already booked and cannot be selected")
      return
    }
    
    if (seat.status === "RESERVED") {
      toast.warning("This seat is currently reserved by another user (pending payment)")
      return
    }

    // Only allow selection of available seats
    if (seat.status !== "AVAILABLE") {
      toast.warning("This seat is not available for selection")
      return
    }

    setSelectedSeats((prev) => {
      const isSelected = prev.find((s) => s.id === seat.id)
      if (isSelected) {
        return prev.filter((s) => s.id !== seat.id)
      } else {
        return [...prev, seat]
      }
    })
  }

  const handleConfirmSelection = async () => {
    if (selectedSeats.length === 0) {
      toast.error("Please select at least one seat")
      return
    }

    // Check if user is logged in
    if (!isUserLoggedIn) {
      toast.info("Please login to continue with booking")
      // Redirect to login page
      window.location.href = "/user-login"
      return
    }

    // If user is logged in, show passenger details
    setShowPassengerDetails(true)
  }



  const handleBackToSeatSelection = () => {
    setShowPassengerDetails(false)
  }

  const handleBookingComplete = () => {
    toast.success("Booking completed successfully!")
    // Redirect to home page or booking confirmation page
    window.location.href = "/"
  }

  const getSeatClass = (seat) => {
    let baseClass = "seat"

    if (seat.status === "BOOKED") {
      baseClass += " booked" // Red color for booked seats
    } else if (seat.status === "RESERVED") {
      baseClass += " reserved" // Yellow color for reserved seats
    } else if (selectedSeats.find((s) => s.id === seat.id)) {
      baseClass += " selected" // Blue color for selected seats
    } else {
      baseClass += " available" // Green color for available seats
    }

    return baseClass
  }

  const getSeatTypeIcon = (seatType) => {
    switch (seatType) {
      case "WINDOW":
        return "🪟"
      case "AISLE":
        return "🚶"
      case "MIDDLE":
        return "👤"
      default:
        return "💺"
    }
  }

  const getSeatStatusText = (seat) => {
    switch (seat.status) {
      case "AVAILABLE":
        return "Available"
      case "BOOKED":
        return "Booked"
      case "RESERVED":
        return "Reserved (Pending Payment)"
      default:
        return "Unknown"
    }
  }

  if (loading) {
    return (
      <div className="text-center py-5">
        <div className="spinner-border" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-2">Loading seats...</p>
      </div>
    )
  }

  // Show passenger details if user is logged in and has confirmed selection
  if (showPassengerDetails) {
    return (
      <PassengerDetails
        selectedSeats={selectedSeats}
        scheduleInfo={scheduleInfo}
        onBack={handleBackToSeatSelection}
        onBookingComplete={handleBookingComplete}
      />
    )
  }

  return (
    <div className="container py-4">
      <div className="row">
        <div className="col-lg-8">
          {/* Schedule Info */}
          {scheduleInfo && (
            <div className="card mb-4">
              <div className="card-body">
                <h5 className="card-title">Journey Details</h5>
                <div className="row">
                  <div className="col-md-6">
                    <p>
                      <strong>Bus:</strong> {scheduleInfo.busName}
                    </p>
                    <p>
                      <strong>From:</strong> {scheduleInfo.source}
                    </p>
                    <p>
                      <strong>To:</strong> {scheduleInfo.destination}
                    </p>
                  </div>
                  <div className="col-md-6">
                    <p>
                      <strong>Date:</strong> {scheduleInfo.scheduleDate}
                    </p>
                    <p>
                      <strong>Departure:</strong> {scheduleInfo.departureTime}
                    </p>
                    <p>
                      <strong>Arrival:</strong> {scheduleInfo.arrivalTime}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* Bus Layout */}
          <div className="card">
            <div className="card-body">
              <div className="d-flex justify-content-between align-items-center mb-4">
                <h5 className="card-title mb-0">Select Your Seats</h5>
                <button 
                  className="btn btn-outline-primary btn-sm" 
                  onClick={fetchSeats}
                  disabled={refreshing}
                >
                  {refreshing ? (
                    <>
                      <span className="spinner-border spinner-border-sm me-2" role="status"></span>
                      Refreshing...
                    </>
                  ) : (
                    <>
                      <i className="fas fa-sync-alt me-2"></i>
                      Refresh
                    </>
                  )}
                </button>
              </div>

              {/* Driver Area */}
              <div className="driver-area mb-3 text-center">
                <div className="driver-seat">
                  <i className="fas fa-user-tie fa-2x text-muted"></i>
                  <p className="small text-muted mt-1">Driver</p>
                </div>
              </div>

              {/* Seats Grid */}
              <div className="seats-container">
                {Array.from({ length: Math.ceil(seats.length / 4) }, (_, rowIndex) => (
                  <div key={rowIndex} className="seat-row mb-2">
                    {seats.slice(rowIndex * 4, (rowIndex + 1) * 4).map((seat) => (
                      <div
                        key={seat.id}
                        className={getSeatClass(seat)}
                        onClick={() => handleSeatClick(seat)}
                        title={`${seat.seatNumber} - ${seat.seatType} - ₹${seat.price} - ${getSeatStatusText(seat)}`}
                      >
                        <div className="seat-content">
                          <div className="seat-number">{seat.seatNumber}</div>
                          <div className="seat-type">{getSeatTypeIcon(seat.seatType)}</div>
                          <div className="seat-price">₹{seat.price}</div>
                        </div>
                      </div>
                    ))}
                  </div>
                ))}
              </div>

              {/* Legend */}
              <div className="seat-legend mt-4">
                <div className="row">
                  <div className="col-md-3">
                    <div className="legend-item">
                      <div className="seat available"></div>
                      <span>Available</span>
                    </div>
                  </div>
                  <div className="col-md-3">
                    <div className="legend-item">
                      <div className="seat selected"></div>
                      <span>Selected</span>
                    </div>
                  </div>
                  <div className="col-md-3">
                    <div className="legend-item">
                      <div className="seat booked"></div>
                      <span>Booked</span>
                    </div>
                  </div>
                  <div className="col-md-3">
                    <div className="legend-item">
                      <div className="seat reserved"></div>
                      <span>Reserved (Pending Payment)</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Selection Summary */}
        <div className="col-lg-4">
          <div className="card sticky-top" style={{ top: "20px" }}>
            <div className="card-body">
              <h5 className="card-title">Selection Summary</h5>

              {selectedSeats.length > 0 ? (
                <>
                  <div className="selected-seats mb-3">
                    <h6>Selected Seats:</h6>
                    {selectedSeats.map((seat) => (
                      <div key={seat.id} className="selected-seat-item">
                        <span>Seat {seat.seatNumber}</span>
                        <span>₹{seat.price}</span>
                      </div>
                    ))}
                  </div>

                  <div className="total-section">
                    <div className="d-flex justify-content-between">
                      <strong>Total Amount:</strong>
                      <strong>₹{selectedSeats.reduce((sum, seat) => sum + seat.price, 0)}</strong>
                    </div>
                  </div>

                  <div className="mt-3">
                    <button className="btn btn-primary w-100 mb-2" onClick={handleConfirmSelection}>
                      {isUserLoggedIn ? "Continue to Passenger Details" : "Login to Continue"}
                    </button>
                    <button className="btn btn-outline-secondary w-100" onClick={() => setSelectedSeats([])}>
                      Clear Selection
                    </button>
                  </div>
                </>
              ) : (
                <p className="text-muted">No seats selected</p>
              )}

              <div className="mt-3">
                <button className="btn btn-outline-primary w-100 mb-2" onClick={onBack}>
                  Back to Search
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default SeatSelection
