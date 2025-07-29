
import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import { ToastContainer } from "react-toastify"
import "react-toastify/dist/ReactToastify.css"

import Navbar from "./components/Navbar"
import Home from "./components/Home"
import About from "./components/About"
import FAQ from "./components/FAQ"
import UserLogin from "./components/UserLogin"
import UserSignup from "./components/UserSignup"
import VendorLogin from "./components/VendorLogin"
import VendorDashboard from "./components/VendorDashboard"
import AddBus from "./components/AddBus"

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
          <Route path="/vendor-login" element={<VendorLogin />} />
          <Route path="/vendor-dashboard" element={<VendorDashboard />} />
          <Route path="/add-bus" element={<AddBus />} />
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
