
import { Link, useNavigate } from "react-router-dom"

const Navbar = () => {
  const navigate = useNavigate()
  const vendorLoggedIn = localStorage.getItem("vendorLoggedIn")
  const userLoggedIn = localStorage.getItem("userLoggedIn")
  const userFirstName = localStorage.getItem("userFirstName")
  const userLastName = localStorage.getItem("userLastName")

  const handleLogout = () => {
    localStorage.removeItem("vendorLoggedIn")
    localStorage.removeItem("vendorId")
    localStorage.removeItem("vendorName")
    localStorage.removeItem("userLoggedIn")
    localStorage.removeItem("userToken")
    localStorage.removeItem("userId")
    localStorage.removeItem("userEmail")
    localStorage.removeItem("userFirstName")
    localStorage.removeItem("userLastName")
    navigate("/")
  }

  return (
    <nav
      className="navbar navbar-expand-lg navbar-dark"
      style={{ background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)" }}
    >
      <div className="container">
        <Link className="navbar-brand fw-bold" to="/">
          <i className="fas fa-bus me-2"></i>
          BusBooking
        </Link>

        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav me-auto">
            <li className="nav-item">
              <Link className="nav-link" to="/">
                <i className="fas fa-home me-1"></i>Home
              </Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/about">
                <i className="fas fa-info-circle me-1"></i>About Us
              </Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/faq">
                <i className="fas fa-question-circle me-1"></i>FAQ's
              </Link>
            </li>
          </ul>

          <ul className="navbar-nav">
            {!userLoggedIn && !vendorLoggedIn && (
              <>
                <li className="nav-item dropdown">
                  <a className="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                    <i className="fas fa-user me-1"></i>User Login
                  </a>
                  <ul className="dropdown-menu">
                    <li>
                      <Link className="dropdown-item" to="/user-login">
                        Login
                      </Link>
                    </li>
                    <li>
                      <Link className="dropdown-item" to="/user-signup">
                        Sign Up
                      </Link>
                    </li>
                  </ul>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/vendor-login">
                    <i className="fas fa-building me-1"></i>Vendor Login
                  </Link>
                </li>
              </>
            )}

            {vendorLoggedIn && (
              <li className="nav-item dropdown">
                <a className="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                  <i className="fas fa-user-tie me-1"></i>
                  {localStorage.getItem("vendorName")}
                </a>
                <ul className="dropdown-menu">
                  <li>
                    <Link className="dropdown-item" to="/vendor-dashboard">
                      Dashboard
                    </Link>
                  </li>
                  <li>
                    <Link className="dropdown-item" to="/add-bus">
                      Add Bus
                    </Link>
                  </li>
                  <li>
                    <hr className="dropdown-divider" />
                  </li>
                  <li>
                    <button className="dropdown-item" onClick={handleLogout}>
                      Logout
                    </button>
                  </li>
                </ul>
              </li>
            )}

            {userLoggedIn && (
              <li className="nav-item dropdown">
                <a className="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                  <i className="fas fa-user me-1"></i>
                  {userFirstName} {userLastName}
                </a>
                <ul className="dropdown-menu">
                  <li>
                    <Link className="dropdown-item" to="/profile">
                      Profile
                    </Link>
                  </li>
                  <li>
                    <Link className="dropdown-item" to="/bookings">
                      My Bookings
                    </Link>
                  </li>
                  <li>
                    <hr className="dropdown-divider" />
                  </li>
                  <li>
                    <button className="dropdown-item" onClick={handleLogout}>
                      Logout
                    </button>
                  </li>
                </ul>
              </li>
            )}
          </ul>
        </div>
      </div>
    </nav>
  )
}

export default Navbar
