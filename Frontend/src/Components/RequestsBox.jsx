import React from 'react'

const RequestsBox = ({ request }) => {
    return (
        <div key={request.id} className="request-box">
            <p>Description: {request.description}</p>
            <p>Customer ID: {request.customerId}</p>
            <p>Service ID: {request.serviceId}</p>
        </div>
    )
}

export default RequestsBox;