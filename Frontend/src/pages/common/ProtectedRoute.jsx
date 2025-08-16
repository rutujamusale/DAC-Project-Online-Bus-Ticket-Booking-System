import { Navigate } from "react-router-dom"

const ProtectedRoute = ({ children }) => {
  const isAuthenticated = () => {
    const token = localStorage.getItem("token")
    const userId = localStorage.getItem("userId")
    return token && userId
  }

  if (!isAuthenticated()) {
    // Redirect to login page if not authenticated
    return <Navigate to="/user-login" replace />
  }

  return children
}

export default ProtectedRoute 