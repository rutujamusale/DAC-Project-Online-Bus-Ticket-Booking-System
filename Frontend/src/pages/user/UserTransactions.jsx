import { useState, useEffect } from "react"
import axios from "axios"
import { toast } from "react-toastify"

const UserTransactions = () => {
  const [transactions, setTransactions] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchUserTransactions()
  }, [])

  const fetchUserTransactions = async () => {
    try {
      const userId = localStorage.getItem("userId")
      const response = await axios.get(`http://localhost:8080/api/payments/user/${userId}`)
      setTransactions(response.data)
    } catch (error) {
      toast.error("Error fetching transactions: " + (error.response?.data?.message || error.message))
    } finally {
      setLoading(false)
    }
  }

  const getPaymentStatusBadge = (status) => {
    switch (status) {
      case "COMPLETED":
        return <span className="badge bg-success">Completed</span>
      case "PENDING":
        return <span className="badge bg-warning">Pending</span>
      case "FAILED":
        return <span className="badge bg-danger">Failed</span>
      default:
        return <span className="badge bg-secondary">{status}</span>
    }
  }

  const getPaymentMethodIcon = (method) => {
    switch (method) {
      case "CARD":
        return <i className="fas fa-credit-card me-2"></i>
      case "UPI":
        return <i className="fas fa-mobile-alt me-2"></i>
      case "NET_BANKING":
        return <i className="fas fa-university me-2"></i>
      case "WALLET":
        return <i className="fas fa-wallet me-2"></i>
      default:
        return <i className="fas fa-money-bill me-2"></i>
    }
  }

  if (loading) {
    return (
      <div className="container py-5">
        <div className="text-center">
          <div className="spinner-border" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="mt-2">Loading your transactions...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="container py-4">
      <div className="row">
        <div className="col-12">
          <div className="card">
            <div className="card-header">
              <h4 className="mb-0">
                <i className="fas fa-credit-card me-2"></i>
                Payment History
              </h4>
            </div>
            <div className="card-body">
              {transactions.length === 0 ? (
                <div className="text-center py-4">
                  <i className="fas fa-credit-card fa-3x text-muted mb-3"></i>
                  <h5 className="text-muted">No transactions found</h5>
                  <p className="text-muted">You haven't made any payments yet.</p>
                  <a href="/" className="btn btn-primary">
                    <i className="fas fa-search me-2"></i>
                    Search for Buses
                  </a>
                </div>
              ) : (
                <div className="table-responsive">
                  <table className="table table-hover">
                    <thead className="table-light">
                      <tr>
                        <th>Transaction ID</th>
                        <th>Booking ID</th>
                        <th>Amount</th>
                        <th>Payment Method</th>
                        <th>Status</th>
                        <th>Date</th>
                        <th>Reference</th>
                      </tr>
                    </thead>
                    <tbody>
                      {transactions.map((transaction) => (
                        <tr key={transaction.transactionId}>
                          <td>
                            <strong>#{transaction.transactionId}</strong>
                          </td>
                          <td>
                            {transaction.bookingId ? (
                              <span className="badge bg-info">#{transaction.bookingId}</span>
                            ) : (
                              <span className="text-muted">-</span>
                            )}
                          </td>
                          <td>
                            <strong>₹{transaction.amount}</strong>
                          </td>
                          <td>
                            {getPaymentMethodIcon(transaction.paymentMethod)}
                            {transaction.paymentMethod}
                          </td>
                          <td>
                            {getPaymentStatusBadge(transaction.paymentStatus)}
                          </td>
                          <td>
                            {transaction.paymentDate ?
                              new Date(transaction.paymentDate).toLocaleDateString() :
                              new Date().toLocaleDateString()
                            }
                          </td>
                          <td>
                            <small className="text-muted">
                              {transaction.transactionReference || "N/A"}
                            </small>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default UserTransactions 