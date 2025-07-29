const FAQ = () => {
  const faqs = [
    {
      id: 1,
      question: "How do I book a bus ticket?",
      answer:
        "To book a bus ticket, simply enter your source city, destination city, and travel date on our homepage. Browse through the available buses, select your preferred option, and proceed with the booking by providing passenger details and making payment.",
    },
    {
      id: 2,
      question: "What payment methods do you accept?",
      answer:
        "We accept all major credit cards (Visa, MasterCard, American Express), debit cards, net banking, and popular digital wallets like Paytm, PhonePe, and Google Pay. All transactions are secured with SSL encryption.",
    },
    {
      id: 3,
      question: "Can I cancel or modify my booking?",
      answer:
        "Yes, you can cancel or modify your booking up to 2 hours before the scheduled departure time. Cancellation and modification charges may apply as per the bus operator's policy. You can manage your bookings from the 'My Bookings' section.",
    },
    {
      id: 4,
      question: "How will I receive my ticket?",
      answer:
        "After successful booking, you will receive an e-ticket via email and SMS. You can also download your ticket from the 'My Bookings' section. Please carry a printed copy or show the digital ticket on your mobile device while boarding.",
    },
    {
      id: 5,
      question: "What if my bus is delayed or cancelled?",
      answer:
        "In case of bus delays or cancellations by the operator, you will be notified immediately via SMS and email. You can choose to get a full refund or reschedule your journey to the next available bus at no extra cost.",
    },
    {
      id: 6,
      question: "Is it safe to book tickets online?",
      answer:
        "Our platform uses industry-standard security measures including SSL encryption, secure payment gateways, and data protection protocols. Your personal and financial information is completely safe with us.",
    },
  ]

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-lg-10">
          {/* Header */}
          <div className="text-center mb-5">
            <h1 className="display-4 fw-bold mb-3">
              <i className="fas fa-question-circle me-3 text-primary"></i>
              Frequently Asked Questions
            </h1>
            <p className="lead text-muted">Find answers to common questions about our bus booking service</p>
          </div>

          {/* FAQ Accordion */}
          <div className="accordion" id="faqAccordion">
            {faqs.map((faq) => (
              <div key={faq.id} className="accordion-item mb-3 border-0 shadow-sm">
                <h2 className="accordion-header">
                  <button
                    className="accordion-button collapsed fw-semibold"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target={`#faq${faq.id}`}
                    aria-expanded="false"
                    style={{ borderRadius: "10px" }}
                  >
                    <i className="fas fa-question-circle me-3 text-primary"></i>
                    {faq.question}
                  </button>
                </h2>
                <div id={`faq${faq.id}`} className="accordion-collapse collapse" data-bs-parent="#faqAccordion">
                  <div className="accordion-body pt-0">
                    <div className="d-flex">
                      <i className="fas fa-answer text-success me-3 mt-1"></i>
                      <p className="text-muted mb-0">{faq.answer}</p>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>

          {/* Contact Support */}
          <div className="text-center mt-5">
            <div className="card border-0 shadow-sm">
              <div className="card-body p-4">
                <h4 className="mb-3">
                  <i className="fas fa-headset me-2 text-primary"></i>
                  Still have questions?
                </h4>
                <p className="text-muted mb-4">
                  Can't find the answer you're looking for? Our customer support team is here to help!
                </p>
                <div className="row justify-content-center">
                  <div className="col-md-6 mb-3">
                    <div className="d-flex align-items-center justify-content-center">
                      <i className="fas fa-phone fa-2x text-success me-3"></i>
                      <div>
                        <h6 className="mb-0">Call Us</h6>
                        <p className="text-muted mb-0">+91-1800-123-4567</p>
                        <small className="text-muted">24/7 Support</small>
                      </div>
                    </div>
                  </div>
                  <div className="col-md-6 mb-3">
                    <div className="d-flex align-items-center justify-content-center">
                      <i className="fas fa-envelope fa-2x text-primary me-3"></i>
                      <div>
                        <h6 className="mb-0">Email Us</h6>
                        <p className="text-muted mb-0">support@busbooking.com</p>
                        <small className="text-muted">Quick Response</small>
                      </div>
                    </div>
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

export default FAQ
