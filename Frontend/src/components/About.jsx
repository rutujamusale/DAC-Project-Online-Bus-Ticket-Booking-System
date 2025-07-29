const About = () => {
  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-lg-10">
          {/* Header Section */}
          <div className="text-center mb-5">
            <h1 className="display-4 fw-bold mb-3">About BusBooking</h1>
            <p className="lead text-muted">
              Your trusted partner for comfortable and convenient bus travel across the country
            </p>
          </div>

          {/* Mission Section */}
          <div className="row mb-5">
            <div className="col-md-6">
              <div className="card h-100 border-0 shadow-sm">
                <div className="card-body p-4">
                  <div className="text-center mb-3">
                    <i className="fas fa-bullseye fa-3x text-primary"></i>
                  </div>
                  <h4 className="text-center mb-3">Our Mission</h4>
                  <p className="text-muted">
                    To revolutionize bus travel by providing a seamless, reliable, and user-friendly platform that
                    connects travelers with the best bus operators across the nation. We strive to make bus booking as
                    simple as a few clicks.
                  </p>
                </div>
              </div>
            </div>
            <div className="col-md-6">
              <div className="card h-100 border-0 shadow-sm">
                <div className="card-body p-4">
                  <div className="text-center mb-3">
                    <i className="fas fa-eye fa-3x text-success"></i>
                  </div>
                  <h4 className="text-center mb-3">Our Vision</h4>
                  <p className="text-muted">
                    To become the leading bus booking platform in the country, known for our commitment to customer
                    satisfaction, safety, and innovation. We envision a future where bus travel is the preferred choice
                    for millions.
                  </p>
                </div>
              </div>
            </div>
          </div>

          {/* Story Section */}
          <div className="row mb-5">
            <div className="col-12">
              <div className="card border-0 shadow-sm">
                <div className="card-body p-5">
                  <h3 className="mb-4">
                    <i className="fas fa-book-open me-2 text-primary"></i>
                    Our Story
                  </h3>
                  <p className="text-muted mb-3">
                    Founded in 2024, BusBooking was born out of a simple idea: to make bus travel booking easier and
                    more accessible for everyone. Our founders, passionate about technology and travel, recognized the
                    need for a comprehensive platform that could bridge the gap between bus operators and travelers.
                  </p>
                  <p className="text-muted mb-3">
                    Starting with just a handful of routes, we have grown to become a trusted name in the industry,
                    serving thousands of customers daily. Our platform now connects hundreds of bus operators with
                    millions of travelers, making us one of the fastest-growing bus booking platforms in the region.
                  </p>
                  <p className="text-muted">
                    Today, we continue to innovate and expand our services, always keeping our customers' needs at the
                    heart of everything we do.
                  </p>
                </div>
              </div>
            </div>
          </div>

          {/* Values Section */}
          <div className="mb-5">
            <h3 className="text-center mb-4">Our Core Values</h3>
            <div className="row">
              <div className="col-md-3 mb-4">
                <div className="text-center">
                  <div
                    className="bg-primary rounded-circle d-inline-flex align-items-center justify-content-center mb-3"
                    style={{ width: "80px", height: "80px" }}
                  >
                    <i className="fas fa-users fa-2x text-white"></i>
                  </div>
                  <h5>Customer First</h5>
                  <p className="text-muted small">
                    Every decision we make is centered around providing the best experience for our customers.
                  </p>
                </div>
              </div>
              <div className="col-md-3 mb-4">
                <div className="text-center">
                  <div
                    className="bg-success rounded-circle d-inline-flex align-items-center justify-content-center mb-3"
                    style={{ width: "80px", height: "80px" }}
                  >
                    <i className="fas fa-shield-alt fa-2x text-white"></i>
                  </div>
                  <h5>Safety & Security</h5>
                  <p className="text-muted small">
                    We prioritize the safety and security of our travelers and their personal information.
                  </p>
                </div>
              </div>
              <div className="col-md-3 mb-4">
                <div className="text-center">
                  <div
                    className="bg-warning rounded-circle d-inline-flex align-items-center justify-content-center mb-3"
                    style={{ width: "80px", height: "80px" }}
                  >
                    <i className="fas fa-lightbulb fa-2x text-white"></i>
                  </div>
                  <h5>Innovation</h5>
                  <p className="text-muted small">
                    We continuously innovate to improve our platform and services for better user experience.
                  </p>
                </div>
              </div>
              <div className="col-md-3 mb-4">
                <div className="text-center">
                  <div
                    className="bg-info rounded-circle d-inline-flex align-items-center justify-content-center mb-3"
                    style={{ width: "80px", height: "80px" }}
                  >
                    <i className="fas fa-handshake fa-2x text-white"></i>
                  </div>
                  <h5>Trust</h5>
                  <p className="text-muted small">
                    Building and maintaining trust with our customers and partners is fundamental to our success.
                  </p>
                </div>
              </div>
            </div>
          </div>

          {/* Statistics Section */}
          <div className="row mb-5">
            <div className="col-12">
              <div
                className="card border-0 shadow-sm"
                style={{ background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)" }}
              >
                <div className="card-body p-5 text-white">
                  <h3 className="text-center mb-4">Our Achievements</h3>
                  <div className="row text-center">
                    <div className="col-md-3 mb-3">
                      <h2 className="fw-bold">1M+</h2>
                      <p>Happy Customers</p>
                    </div>
                    <div className="col-md-3 mb-3">
                      <h2 className="fw-bold">500+</h2>
                      <p>Bus Operators</p>
                    </div>
                    <div className="col-md-3 mb-3">
                      <h2 className="fw-bold">1000+</h2>
                      <p>Routes Covered</p>
                    </div>
                    <div className="col-md-3 mb-3">
                      <h2 className="fw-bold">24/7</h2>
                      <p>Customer Support</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Contact CTA */}
          <div className="text-center">
            <div className="card border-0 shadow-sm">
              <div className="card-body p-5">
                <h4 className="mb-3">Get in Touch</h4>
                <p className="text-muted mb-4">Have questions or suggestions? We'd love to hear from you!</p>
                <div className="row justify-content-center">
                  <div className="col-md-4 mb-3">
                    <i className="fas fa-envelope fa-2x text-primary mb-2"></i>
                    <p className="mb-0">support@busbooking.com</p>
                  </div>
                  <div className="col-md-4 mb-3">
                    <i className="fas fa-phone fa-2x text-success mb-2"></i>
                    <p className="mb-0">+91-1800-123-4567</p>
                  </div>
                  <div className="col-md-4 mb-3">
                    <i className="fas fa-map-marker-alt fa-2x text-warning mb-2"></i>
                    <p className="mb-0">Mumbai, India</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default About
