import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import { ToastContainer } from "react-toastify"
import "react-toastify/dist/ReactToastify.css"

import Navbar from "./pages/common/Navbar"
import Home from "./pages/common/Home"
import About from "./pages/common/About"
import FAQ from "./pages/common/FAQ"
import UserLogin from "./pages/auth/UserLogin"
import UserSignup from "./pages/auth/UserSignup"
import VendorLogin from "./pages/auth/VendorLogin"
import VendorRegister from "./pages/auth/VendorRegister"
import VendorDashboard from "./pages/vendor/VendorDashboard"
import AddBus from "./pages/vendor/AddBus"
import UserDashboard from "./pages/user/UserDashboard"
import UserProfile from "./pages/user/UserProfile"
import UserTransactions from "./pages/user/UserTransactions"
import PaymentPage from "./pages/payment/PaymentPage"
import Ticket from "./pages/booking/Ticket"
import Feedback from "./pages/common/Feedback"
import UserFeedback from "./pages/user/UserFeedback"
import FeedbackStats from "./pages/common/FeedbackStats"
import SeatSelection from "./pages/booking/SeatSelection"
import ProtectedRoute from "./pages/common/ProtectedRoute"
import AccountDeactivation from "./pages/user/AccountDeactivation"
import VendorFeedback from "./pages/vendor/VendorFeedback"
import VendorProfile from "./pages/vendor/VendorProfile"
import VendorChangePassword from "./pages/vendor/VendorChangePassword"

function App() {
  return (
    <Router>
      <div className="App">
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/about" element={<About />} />
          <Route path="/faq" element={<FAQ />} />
                           <Route path="/user-login" element={<UserLogin />} />
                 <Route path="/user-signup" element={<UserSignup />} />
                 <Route path="/user-dashboard" element={<ProtectedRoute><UserDashboard /></ProtectedRoute>} />
                 <Route path="/user-profile" element={<ProtectedRoute><UserProfile /></ProtectedRoute>} />
                 <Route path="/user-transactions" element={<ProtectedRoute><UserTransactions /></ProtectedRoute>} />
                 <Route path="/payment" element={<ProtectedRoute><PaymentPage /></ProtectedRoute>} />
                 <Route path="/ticket/:bookingId" element={<ProtectedRoute><Ticket /></ProtectedRoute>} />
                 <Route path="/feedback/:bookingId" element={<ProtectedRoute><Feedback /></ProtectedRoute>} />
                 <Route path="/user-feedback" element={<ProtectedRoute><UserFeedback /></ProtectedRoute>} />
                 <Route path="/feedback-stats" element={<ProtectedRoute><FeedbackStats /></ProtectedRoute>} />
                 <Route path="/account-deactivation" element={<AccountDeactivation />} />
                 <Route path="/vendor-feedback" element={<VendorFeedback />} />
                 <Route path="/vendor-login" element={<VendorLogin />} />
                 <Route path="/vendor-register" element={<VendorRegister />} />
                 <Route path="/vendor-profile" element={<VendorProfile />} />
                 <Route path="/vendor-change-password" element={<VendorChangePassword />} />
          <Route path="/vendor-dashboard" element={<VendorDashboard />} />
          <Route path="/add-bus" element={<AddBus />} />
                           <Route path="/seat-selection/:scheduleId" element={<ProtectedRoute><SeatSelection /></ProtectedRoute>} />
        </Routes>
        <ToastContainer
          position="top-right"
          autoClose={3000}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="light"
        />
      </div>
    </Router>
  )
}

export default App
