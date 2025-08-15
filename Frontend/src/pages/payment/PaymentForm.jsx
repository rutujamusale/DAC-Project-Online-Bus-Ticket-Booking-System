import { useState } from "react"
import axios from "axios"
import { toast } from "react-toastify"

const PaymentForm = ({ booking, onPaymentComplete, onBack }) => {
  const [paymentMethod, setPaymentMethod] = useState("")
  const [loading, setLoading] = useState(false)
  const [paymentData, setPaymentData] = useState({
    cardNumber: "",
    cardHolderName: "",
    expiryDate: "",
    cvv: "",
    cardType: "",
    cardLastFourDigits: "",
    upiId: "",
    bankName: "",
    accountNumber: "",
    ifscCode: "",
    walletType: "",
    walletId: "",
    walletName: "",
    processingFee: 0.0,
    taxAmount: 0.0,
    discountAmount: 0.0,
    currency: "INR",
    otp: "",
    transactionPassword: ""
  })

  const handleInputChange = (field, value) => {
    setPaymentData(prev => ({
      ...prev,
      [field]: value
    }))
  }

  const validatePaymentData = () => {
    switch (paymentMethod) {
      case "CREDIT_CARD":
      case "DEBIT_CARD":
        if (!paymentData.cardNumber || !paymentData.cardHolderName || 
            !paymentData.expiryDate || !paymentData.cvv) {
          toast.error("Please fill all card details")
          return false
        }
        // Extract last 4 digits from card number
        const lastFourDigits = paymentData.cardNumber.replace(/\s/g, '').slice(-4)
        setPaymentData(prev => ({ ...prev, cardLastFourDigits: lastFourDigits }))
        break
        
      case "UPI":
        if (!paymentData.upiId) {
          toast.error("Please enter UPI ID")
          return false
        }
        break
        
      case "NET_BANKING":
        if (!paymentData.bankName || !paymentData.accountNumber || !paymentData.ifscCode) {
          toast.error("Please fill all net banking details")
          return false
        }
        break
        
      case "WALLET":
        if (!paymentData.walletType || !paymentData.walletId) {
          toast.error("Please fill all wallet details")
          return false
        }
        // Set wallet name based on type
        const walletNames = {
          "PAYTM": "Paytm",
          "PHONEPE": "PhonePe", 
          "GOOGLEPAY": "Google Pay",
          "AMAZONPAY": "Amazon Pay"
        }
        setPaymentData(prev => ({ 
          ...prev, 
          walletName: walletNames[paymentData.walletType] || paymentData.walletType 
        }))
        break
        
      default:
        toast.error("Please select a payment method")
        return false
    }
    return true
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    if (!paymentMethod) {
      toast.error("Please select a payment method")
      return
    }

    if (!validatePaymentData()) {
      return
    }

    setLoading(true)
    
    try {
      const paymentRequest = {
        bookingId: booking.id,
        paymentMethod: paymentMethod,
        ...paymentData
      }

      console.log("Sending payment request:", paymentRequest)

      const response = await axios.post("http://localhost:8080/api/payments/process", paymentRequest)
      
      if (response.data && response.data.success) {
        toast.success("Payment completed successfully! Your seats are now confirmed.")
        // Update booking status and redirect
        onPaymentComplete(response.data)
      } else {
        toast.error(response.data?.message || "Payment failed. Please try again.")
      }
      
    } catch (error) {
      console.error("Payment error:", error)
      toast.error("Error processing payment: " + (error.response?.data?.message || error.message))
    } finally {
      setLoading(false)
    }
  }

  const renderPaymentFields = () => {
    switch (paymentMethod) {
      case "CREDIT_CARD":
      case "DEBIT_CARD":
        return (
          <div className="row">
            <div className="col-md-6 mb-3">
              <label className="form-label">Card Number *</label>
              <input
                type="text"
                className="form-control"
                value={paymentData.cardNumber}
                onChange={(e) => {
                  // Format card number with spaces
                  const value = e.target.value.replace(/\s/g, '').replace(/(\d{4})/g, '$1 ').trim()
                  handleInputChange("cardNumber", value)
                }}
                placeholder="1234 5678 9012 3456"
                maxLength="19"
                required
              />
            </div>
            <div className="col-md-6 mb-3">
              <label className="form-label">Card Holder Name *</label>
              <input
                type="text"
                className="form-control"
                value={paymentData.cardHolderName}
                onChange={(e) => handleInputChange("cardHolderName", e.target.value)}
                placeholder="John Doe"
                required
              />
            </div>
            <div className="col-md-4 mb-3">
              <label className="form-label">Card Type *</label>
              <select
                className="form-select"
                value={paymentData.cardType}
                onChange={(e) => handleInputChange("cardType", e.target.value)}
                required
              >
                <option value="">Select Card Type</option>
                <option value="VISA">Visa</option>
                <option value="MASTERCARD">Mastercard</option>
                <option value="AMEX">American Express</option>
                <option value="RUPAY">RuPay</option>
              </select>
            </div>
            <div className="col-md-4 mb-3">
              <label className="form-label">Expiry Date *</label>
              <input
                type="text"
                className="form-control"
                value={paymentData.expiryDate}
                onChange={(e) => {
                  // Format expiry date
                  const value = e.target.value.replace(/\D/g, '').replace(/(\d{2})(\d)/, '$1/$2')
                  handleInputChange("expiryDate", value)
                }}
                placeholder="MM/YY"
                maxLength="5"
                required
              />
            </div>
            <div className="col-md-4 mb-3">
              <label className="form-label">CVV *</label>
              <input
                type="password"
                className="form-control"
                value={paymentData.cvv}
                onChange={(e) => handleInputChange("cvv", e.target.value.replace(/\D/g, ''))}
                placeholder="123"
                maxLength="4"
                required
              />
            </div>
            <div className="col-md-6 mb-3">
              <label className="form-label">OTP (if required)</label>
              <input
                type="text"
                className="form-control"
                value={paymentData.otp}
                onChange={(e) => handleInputChange("otp", e.target.value)}
                placeholder="Enter OTP"
                maxLength="6"
              />
            </div>
          </div>
        )
      
      case "UPI":
        return (
          <div className="row">
            <div className="col-md-6 mb-3">
              <label className="form-label">UPI ID *</label>
              <input
                type="text"
                className="form-control"
                value={paymentData.upiId}
                onChange={(e) => handleInputChange("upiId", e.target.value)}
                placeholder="username@upi"
                required
              />
            </div>
            <div className="col-md-6 mb-3">
              <label className="form-label">UPI PIN (if required)</label>
              <input
                type="password"
                className="form-control"
                value={paymentData.transactionPassword}
                onChange={(e) => handleInputChange("transactionPassword", e.target.value)}
                placeholder="Enter UPI PIN"
                maxLength="6"
              />
            </div>
          </div>
        )
      
      case "NET_BANKING":
        return (
          <div className="row">
            <div className="col-md-6 mb-3">
              <label className="form-label">Bank Name *</label>
              <select
                className="form-select"
                value={paymentData.bankName}
                onChange={(e) => handleInputChange("bankName", e.target.value)}
                required
              >
                <option value="">Select Bank</option>
                <option value="SBI">State Bank of India</option>
                <option value="HDFC">HDFC Bank</option>
                <option value="ICICI">ICICI Bank</option>
                <option value="AXIS">Axis Bank</option>
                <option value="PNB">Punjab National Bank</option>
                <option value="CANARA">Canara Bank</option>
                <option value="BOB">Bank of Baroda</option>
                <option value="UNION">Union Bank of India</option>
              </select>
            </div>
            <div className="col-md-6 mb-3">
              <label className="form-label">Account Number *</label>
              <input
                type="text"
                className="form-control"
                value={paymentData.accountNumber}
                onChange={(e) => handleInputChange("accountNumber", e.target.value)}
                placeholder="Account Number"
                required
              />
            </div>
            <div className="col-md-6 mb-3">
              <label className="form-label">IFSC Code *</label>
              <input
                type="text"
                className="form-control"
                value={paymentData.ifscCode}
                onChange={(e) => handleInputChange("ifscCode", e.target.value.toUpperCase())}
                placeholder="IFSC Code"
                required
              />
            </div>
            <div className="col-md-6 mb-3">
              <label className="form-label">Transaction Password</label>
              <input
                type="password"
                className="form-control"
                value={paymentData.transactionPassword}
                onChange={(e) => handleInputChange("transactionPassword", e.target.value)}
                placeholder="Enter transaction password"
              />
            </div>
          </div>
        )
      
      case "WALLET":
        return (
          <div className="row">
            <div className="col-md-6 mb-3">
              <label className="form-label">Wallet Type *</label>
              <select
                className="form-select"
                value={paymentData.walletType}
                onChange={(e) => handleInputChange("walletType", e.target.value)}
                required
              >
                <option value="">Select Wallet</option>
                <option value="PAYTM">Paytm</option>
                <option value="PHONEPE">PhonePe</option>
                <option value="GOOGLEPAY">Google Pay</option>
                <option value="AMAZONPAY">Amazon Pay</option>
                <option value="BHIM">BHIM</option>
                <option value="FREECHARGE">FreeCharge</option>
              </select>
            </div>
            <div className="col-md-6 mb-3">
              <label className="form-label">Wallet ID/Phone Number *</label>
              <input
                type="text"
                className="form-control"
                value={paymentData.walletId}
                onChange={(e) => handleInputChange("walletId", e.target.value)}
                placeholder="Wallet ID/Phone Number"
                required
              />
            </div>
            <div className="col-md-6 mb-3">
              <label className="form-label">MPIN/Password</label>
              <input
                type="password"
                className="form-control"
                value={paymentData.transactionPassword}
                onChange={(e) => handleInputChange("transactionPassword", e.target.value)}
                placeholder="Enter MPIN/Password"
                maxLength="6"
              />
            </div>
          </div>
        )
      
      default:
        return null
    }
  }

  return (
    <div className="container py-4">
      <div className="row">
        <div className="col-lg-8">
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Payment Details</h5>
              
              {/* Booking Summary */}
              <div className="alert alert-info mb-4">
                <h6>Booking Summary</h6>
                <p className="mb-1"><strong>Booking ID:</strong> {booking.id}</p>
                <p className="mb-1"><strong>Total Amount:</strong> ₹{booking.totalAmount}</p>
                <p className="mb-0"><strong>Status:</strong> {booking.status}</p>
              </div>

              <form onSubmit={handleSubmit}>
                {/* Payment Method Selection */}
                <div className="mb-4">
                  <label className="form-label">Payment Method *</label>
                  <div className="row">
                    <div className="col-md-3 mb-2">
                      <div className="form-check">
                        <input
                          className="form-check-input"
                          type="radio"
                          name="paymentMethod"
                          id="creditCard"
                          value="CREDIT_CARD"
                          checked={paymentMethod === "CREDIT_CARD"}
                          onChange={(e) => setPaymentMethod(e.target.value)}
                        />
                        <label className="form-check-label" htmlFor="creditCard">
                          <i className="fas fa-credit-card me-2"></i>
                          Credit Card
                        </label>
                      </div>
                    </div>
                    <div className="col-md-3 mb-2">
                      <div className="form-check">
                        <input
                          className="form-check-input"
                          type="radio"
                          name="paymentMethod"
                          id="debitCard"
                          value="DEBIT_CARD"
                          checked={paymentMethod === "DEBIT_CARD"}
                          onChange={(e) => setPaymentMethod(e.target.value)}
                        />
                        <label className="form-check-label" htmlFor="debitCard">
                          <i className="fas fa-credit-card me-2"></i>
                          Debit Card
                        </label>
                      </div>
                    </div>
                    <div className="col-md-3 mb-2">
                      <div className="form-check">
                        <input
                          className="form-check-input"
                          type="radio"
                          name="paymentMethod"
                          id="upi"
                          value="UPI"
                          checked={paymentMethod === "UPI"}
                          onChange={(e) => setPaymentMethod(e.target.value)}
                        />
                        <label className="form-check-label" htmlFor="upi">
                          <i className="fas fa-mobile-alt me-2"></i>
                          UPI
                        </label>
                      </div>
                    </div>
                    <div className="col-md-3 mb-2">
                      <div className="form-check">
                        <input
                          className="form-check-input"
                          type="radio"
                          name="paymentMethod"
                          id="netbanking"
                          value="NET_BANKING"
                          checked={paymentMethod === "NET_BANKING"}
                          onChange={(e) => setPaymentMethod(e.target.value)}
                        />
                        <label className="form-check-label" htmlFor="netbanking">
                          <i className="fas fa-university me-2"></i>
                          Net Banking
                        </label>
                      </div>
                    </div>
                    <div className="col-md-3 mb-2">
                      <div className="form-check">
                        <input
                          className="form-check-input"
                          type="radio"
                          name="paymentMethod"
                          id="wallet"
                          value="WALLET"
                          checked={paymentMethod === "WALLET"}
                          onChange={(e) => setPaymentMethod(e.target.value)}
                        />
                        <label className="form-check-label" htmlFor="wallet">
                          <i className="fas fa-wallet me-2"></i>
                          Wallet
                        </label>
                      </div>
                    </div>
                  </div>
                </div>

                {/* Payment Fields */}
                {renderPaymentFields()}

                <div className="d-flex justify-content-between">
                  <button
                    type="button"
                    className="btn btn-outline-secondary"
                    onClick={onBack}
                  >
                    Back to Booking
                  </button>
                  <button
                    type="submit"
                    className="btn btn-primary"
                    disabled={loading}
                  >
                    {loading ? "Processing..." : "Pay ₹" + booking.totalAmount}
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>

        <div className="col-lg-4">
          <div className="card sticky-top" style={{ top: "20px" }}>
            <div className="card-body">
              <h5 className="card-title">Payment Summary</h5>
              
              <div className="mb-3">
                <div className="d-flex justify-content-between mb-2">
                  <span>Booking Amount:</span>
                  <span>₹{booking.totalAmount}</span>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>Convenience Fee:</span>
                  <span>₹{paymentData.processingFee || 0}</span>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>Tax:</span>
                  <span>₹{paymentData.taxAmount || 0}</span>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>Discount:</span>
                  <span>-₹{paymentData.discountAmount || 0}</span>
                </div>
                <hr />
                <div className="d-flex justify-content-between mb-3">
                  <strong>Total Amount:</strong>
                  <strong>₹{booking.totalAmount + (paymentData.processingFee || 0) + (paymentData.taxAmount || 0) - (paymentData.discountAmount || 0)}</strong>
                </div>
              </div>
              
              <div className="alert alert-warning">
                <small>
                  <i className="fas fa-shield-alt me-2"></i>
                  Your payment information is secure and encrypted.
                </small>
              </div>
              
              <div className="alert alert-info">
                <small>
                  <i className="fas fa-info-circle me-2"></i>
                  Payment will be processed securely through our payment gateway.
                </small>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default PaymentForm 