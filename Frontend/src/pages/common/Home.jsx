
import { useState, useEffect } from "react"
import axios from "axios"
import { toast } from "react-toastify"
import SeatSelection from "../booking/SeatSelection"

const Home = () => {
  const [searchData, setSearchData] = useState({
    source: "",
    destination: "",
    travelDate: "",
  })
  const [buses, setBuses] = useState([])
  const [cities, setCities] = useState([])
  const [loading, setLoading] = useState(false)
  const [searched, setSearched] = useState(false)
  const [selectedSchedule, setSelectedSchedule] = useState(null)
  const [showSeatSelection, setShowSeatSelection] = useState(false)

  useEffect(() => {
    fetchCities()
  }, [])

  const fetchCities = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/cities/names")
      setCities(response.data)
    } catch (error) {
      console.error("Error fetching cities:", error)
    }
  }

  const handleInputChange = (e) => {
    setSearchData({
      ...searchData,
      [e.target.name]: e.target.value,
    })
  }

  const handleSearch = async (e) => {
    e.preventDefault()

    if (!searchData.source || !searchData.destination || !searchData.travelDate) {
      toast.error("Please fill all search fields")
      return
    }

    if (searchData.source === searchData.destination) {
      toast.error("Source and destination cannot be the same")
      return
    }

    setLoading(true)
    try {
      const response = await axios.get("http://localhost:8080/api/schedules/search", {
        params: {
          source: searchData.source,
          destination: searchData.destination,
          date: searchData.travelDate,
        },
      })
      setBuses(response.data)
      setSearched(true)
      if (response.data.length === 0) {
        toast.info("No scheduled buses found for the selected route and date")
      }
    } catch (error) {
      toast.error("Error searching buses: " + (error.response?.data?.message || error.message))
    } finally {
      setLoading(false)
    }
  }

  const handleBookNow = (schedule) => {
    setSelectedSchedule(schedule)
    setShowSeatSelection(true)
  }

  const handleSeatsSelected = (selectedSeats) => {
    // Handle the selected seats - you can add payment flow here
    console.log("Selected seats:", selectedSeats)
    toast.success("Seats selected! Proceeding to payment...")
    // You can navigate to payment page or show payment modal here
  }

  const handleBackToSearch = () => {
    setShowSeatSelection(false)
    setSelectedSchedule(null)
  }

  if (showSeatSelection && selectedSchedule) {
    return (
      <SeatSelection
        scheduleId={selectedSchedule.id}
        onSeatsSelected={handleSeatsSelected}
        onBack={handleBackToSearch}
      />
    )
  }

  return (
    <div>
      {
      /* Hero Section */
      }
      <section
        className="hero-section py-5"
        style={{
          background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
          color: "white",
        }}
      >
        <div className="container">
          <div className="row align-items-center">
            <div className="col-lg-6">
              <h1 className="display-4 fw-bold mb-4">Book Your Bus Tickets Online</h1>
              <p className="lead mb-4">
                Travel comfortably and safely with our trusted bus booking platform. Find the best routes, compare
                prices, and book your journey in just a few clicks.
              </p>
              <div className="d-flex gap-3">
                <div className="text-center">
                  <i className="fas fa-shield-alt fa-2x mb-2"></i>
                  <p className="small">Safe & Secure</p>
                </div>
                <div className="text-center">
                  <i className="fas fa-clock fa-2x mb-2"></i>
                  <p className="small">24/7 Support</p>
                </div>
                <div className="text-center">
                  <i className="fas fa-money-bill-wave fa-2x mb-2"></i>
                  <p className="small">Best Prices</p>
                </div>
              </div>
            </div>
            <div className="col-lg-6">
              <div className="card shadow-lg border-0">
                <div className="card-body p-4">
                  <h4 className="card-title text-dark mb-4">
                    <i className="fas fa-search me-2"></i>Search Buses
                  </h4>
                  <form onSubmit={handleSearch}>
                    <div className="row">
                      <div className="col-md-6 mb-3">
                        <label className="form-label text-dark">
                          <i className="fas fa-map-marker-alt me-2"></i>From
                        </label>
                        <select
                          className="form-select"
                          name="source"
                          value={searchData.source}
                          onChange={handleInputChange}
                          required
                        >
                          <option value="">Select source city</option>
                          {cities.map((city, index) => (
                            <option key={index} value={city}>
                              {city}
                            </option>
                          ))}
                        </select>
                      </div>
                      <div className="col-md-6 mb-3">
                        <label className="form-label text-dark">
                          <i className="fas fa-map-marker-alt me-2"></i>To
                        </label>
                        <select
                          className="form-select"
                          name="destination"
                          value={searchData.destination}
                          onChange={handleInputChange}
                          required
                        >
                          <option value="">Select destination city</option>
                          {cities
                            .filter((city) => city !== searchData.source)
                            .map((city, index) => (
                              <option key={index} value={city}>
                                {city}
                              </option>
                            ))}
                        </select>
                      </div>
                    </div>
                    <div className="mb-3">
                      <label className="form-label text-dark">
                        <i className="fas fa-calendar-alt me-2"></i>Travel Date
                      </label>
                      <input
                        type="date"
                        className="form-control"
                        name="travelDate"
                        value={searchData.travelDate}
                        onChange={handleInputChange}
                        min={new Date().toISOString().split("T")[0]}
                        required
                      />
                    </div>
                    <button
                      type="submit"
                      className="btn btn-primary w-100 py-2"
                      disabled={loading}
                      style={{ background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)", border: "none" }}
                    >
                      {loading ? (
                        <>
                          <span className="spinner-border spinner-border-sm me-2"></span>
                          Searching...
                        </>
                      ) : (
                        <>
                          <i className="fas fa-search me-2"></i>
                          Search Buses
                        </>
                      )}
                    </button>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Search Results */}
      {searched && (
        <section className="py-5">
          <div className="container">
            <h3 className="mb-4">
              <i className="fas fa-bus me-2"></i>
              Available Buses ({buses.length})
            </h3>
            {buses.length > 0 ? (
              <div className="row">
                {buses.map((schedule) => (
                  <div key={schedule.id} className="col-lg-12 mb-3">
                    <div className="card shadow-sm border-0">
                      <div className="card-body">
                        <div className="row align-items-center">
                          <div className="col-md-3">
                            <h5 className="mb-1">{schedule.busName}</h5>
                            <small className="text-muted">{schedule.busNumber}</small>
                            <br />
                            <span className="badge bg-info">{schedule.busType || "Standard"}</span>
                          </div>
                          <div className="col-md-2 text-center">
                            <h6>{schedule.departureTime}</h6>
                            <small className="text-muted">{schedule.source}</small>
                          </div>
                          <div className="col-md-2 text-center">
                            <i className="fas fa-arrow-right text-primary"></i>
                            <br />
                            <small className="text-muted">Duration</small>
                          </div>
                          <div className="col-md-2 text-center">
                            <h6>{schedule.arrivalTime}</h6>
                            <small className="text-muted">{schedule.destination}</small>
                          </div>
                          <div className="col-md-2 text-center">
                            <h5 className="text-success">₹{schedule.price}</h5>
                            <small className="text-muted">{schedule.availableSeats} seats available</small>
                          </div>
                          <div className="col-md-1">
                            <button 
                              className="btn btn-primary btn-sm"
                              onClick={() => handleBookNow(schedule)}
                            >
                              Book Now
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="text-center py-5">
                <i className="fas fa-bus fa-4x text-muted mb-3"></i>
                <h4 className="text-muted">No scheduled buses found</h4>
                <p className="text-muted">Try searching for a different route or date.</p>
              </div>
            )}
          </div>
        </section>
      )}

      {
      /* Services Section */
      }
      <section className="py-5 bg-light">
        <div className="container">
          <div className="text-center mb-5">
            <h2 className="fw-bold">Why Choose Us?</h2>
            <p className="text-muted">Experience the best in bus travel booking</p>
          </div>
          <div className="row">
            <div className="col-md-4 mb-4">
              <div className="card h-100 border-0 shadow-sm">
                <div className="card-body text-center p-4">
                  <i className="fas fa-shield-alt fa-3x text-primary mb-3"></i>
                  <h5>Safe & Secure</h5>
                  <p className="text-muted">
                    Your safety is our priority. All our partner buses are verified and follow strict safety protocols.
                  </p>
                </div>
              </div>
            </div>
            <div className="col-md-4 mb-4">
              <div className="card h-100 border-0 shadow-sm">
                <div className="card-body text-center p-4">
                  <i className="fas fa-clock fa-3x text-success mb-3"></i>
                  <h5>24/7 Support</h5>
                  <p className="text-muted">
                    Round-the-clock customer support to assist you with bookings, cancellations, and queries.
                  </p>
                </div>
              </div>
            </div>
            <div className="col-md-4 mb-4">
              <div className="card h-100 border-0 shadow-sm">
                <div className="card-body text-center p-4">
                  <i className="fas fa-money-bill-wave fa-3x text-warning mb-3"></i>
                  <h5>Best Prices</h5>
                  <p className="text-muted">
                    Compare prices from multiple operators and get the best deals on your bus tickets.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Quick FAQ Section */}
      <section className="py-5">
        <div className="container">
          <div className="text-center mb-5">
            <h2 className="fw-bold">Frequently Asked Questions</h2>
          </div>
          <div className="row justify-content-center">
            <div className="col-lg-8">
              <div className="accordion" id="faqAccordion">
                <div className="accordion-item">
                  <h2 className="accordion-header">
                    <button className="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#faq1">
                      How do I book a bus ticket?
                    </button>
                  </h2>
                  <div id="faq1" className="accordion-collapse collapse show" data-bs-parent="#faqAccordion">
                    <div className="accordion-body">
                      Simply select your source and destination cities, choose your travel date, and browse available
                      buses. Click "Book Now" to complete your booking.
                    </div>
                  </div>
                </div>
                <div className="accordion-item">
                  <h2 className="accordion-header">
                    <button
                      className="accordion-button collapsed"
                      type="button"
                      data-bs-toggle="collapse"
                      data-bs-target="#faq2"
                    >
                      Can I cancel my booking?
                    </button>
                  </h2>
                  <div id="faq2" className="accordion-collapse collapse" data-bs-parent="#faqAccordion">
                    <div className="accordion-body">
                      Yes, you can cancel your booking up to 2 hours before departure. Cancellation charges may apply as
                      per the operator's policy.
                    </div>
                  </div>
                </div>
                <div className="accordion-item">
                  <h2 className="accordion-header">
                    <button
                      className="accordion-button collapsed"
                      type="button"
                      data-bs-toggle="collapse"
                      data-bs-target="#faq3"
                    >
                      What payment methods are accepted?
                    </button>
                  </h2>
                  <div id="faq3" className="accordion-collapse collapse" data-bs-parent="#faqAccordion">
                    <div className="accordion-body">
                      We accept all major credit cards, debit cards, net banking, and popular digital wallets for secure
                      and convenient payments.
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  )
}

export default Home
