import { useState, useEffect } from "react"
import { useLocation, useNavigate } from "react-router-dom"

const BusSearchResults = () => {
  const location = useLocation()
  const navigate = useNavigate()
  const [buses, setBuses] = useState([])
  const [searchData, setSearchData] = useState({})

  useEffect(() => {
    if (location.state) {
      setBuses(location.state.buses || [])
      setSearchData(location.state.searchData || {})
    } else {
      navigate("/")
    }
  }, [location.state, navigate])

  const handleBookNow = (bus) => {
    // Check if user is logged in
    const token = localStorage.getItem("token") || localStorage.getItem("userToken")
    if (!token) {
      // Redirect to login with bus info
      navigate("/login", { state: { selectedBus: bus, searchData } })
    } else {
      // Navigate to seat selection
      navigate("/seat-selection", { state: { bus, searchData } })
    }
  }

  return (
    <div className="container-fluid">
      {
      /* Navigation */
      }
      <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
        <div className="container">
          <a className="navbar-brand" href="/">
            <i className="fas fa-bus me-2"></i>
            BusBooking
          </a>
          <div className="navbar-nav ms-auto">
            <a className="nav-link" href="/login">
              User Login
            </a>
            <a className="nav-link" href="/vendor-login">
              Vendor Login
            </a>
          </div>
        </div>
      </nav>

      <div className="container py-4">
        {
        /* Search Summary */
        }
        <div className="row mb-4">
          <div className="col-12">
            <div className="card bg-light">
              <div className="card-body">
                <div className="row align-items-center">
                  <div className="col-md-8">
                    <h5 className="mb-0">
                      <i className="fas fa-route me-2"></i>
                      {searchData.source} → {searchData.destination}
                    </h5>
                    <small className="text-muted">
                      <i className="fas fa-calendar me-1"></i>
                      {searchData.date}
                    </small>
                  </div>
                  <div className="col-md-4 text-end">
                    <button className="btn btn-outline-primary" onClick={() => navigate("/")}>
                      <i className="fas fa-edit me-2"></i>
                      Modify Search
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {
        /* Bus Results */
        }
        <div className="row">
          <div className="col-12">
            <h4 className="mb-3">Available Buses ({buses.length} found)</h4>

            {buses.length === 0 ? (
              <div className="text-center py-5">
                <i className="fas fa-bus fa-3x text-muted mb-3"></i>
                <h5>No buses found</h5>
                <p className="text-muted">Try searching for different cities or dates</p>
                <button className="btn btn-primary" onClick={() => navigate("/")}>
                  Search Again
                </button>
              </div>
            ) : (
              buses.map((bus, index) => (
                <div key={index} className="card mb-3 shadow-sm">
                  <div className="card-body">
                    <div className="row align-items-center">
                      <div className="col-md-3">
                        <h5 className="mb-1">{bus.busName || "Bus Name"}</h5>
                        <small className="text-muted">{bus.busType || "AC Sleeper"}</small>
                      </div>
                      <div className="col-md-2">
                        <div className="text-center">
                          <h6 className="mb-0">{bus.departureTime || "10:00 PM"}</h6>
                          <small className="text-muted">{searchData.source}</small>
                        </div>
                      </div>
                      <div className="col-md-2">
                        <div className="text-center">
                          <i className="fas fa-arrow-right text-muted"></i>
                          <br />
                          <small className="text-muted">{bus.duration || "8h 30m"}</small>
                        </div>
                      </div>
                      <div className="col-md-2">
                        <div className="text-center">
                          <h6 className="mb-0">{bus.arrivalTime || "06:30 AM"}</h6>
                          <small className="text-muted">{searchData.destination}</small>
                        </div>
                      </div>
                      <div className="col-md-2">
                        <div className="text-center">
                          <h5 className="text-success mb-0">₹{bus.price || "800"}</h5>
                          <small className="text-muted">{bus.availableSeats || "15"} seats left</small>
                        </div>
                      </div>
                      <div className="col-md-1">
                        <button className="btn btn-primary btn-sm w-100" onClick={() => handleBookNow(bus)}>
                          Book Now
                        </button>
                      </div>
                    </div>

                    {
                    /* Bus Amenities */
                    }
                    <div className="row mt-3">
                      <div className="col-12">
                        <div className="d-flex flex-wrap gap-2">
                          <span className="badge bg-light text-dark">
                            <i className="fas fa-wifi me-1"></i>WiFi
                          </span>
                          <span className="badge bg-light text-dark">
                            <i className="fas fa-snowflake me-1"></i>AC
                          </span>
                          <span className="badge bg-light text-dark">
                            <i className="fas fa-charging-station me-1"></i>Charging Point
                          </span>
                          <span className="badge bg-light text-dark">
                            <i className="fas fa-tv me-1"></i>Entertainment
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  )
}

export default BusSearchResults
