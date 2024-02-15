import React, { useState } from 'react'
import { OfferAddForm } from './OfferAddForm';
import { createOffer } from '../service/ApiService';

const RequestsBox = ({ request }) => {
    const [showMakeOfferForm, setShowMakeOfferForm] = useState(false);

    const handleMakeOfferForm = () => {
        setShowMakeOfferForm(!showMakeOfferForm);

    }
    const addOffer = async (offer) => {
        try {
            const addOffer = await createOffer(offer);
            console.log(offer)
        } catch (error) {
            console.error(error);
        }
    };
    console.log(request)
    return (
     
        <div key={request.id} className="request-box">
            <p>Description: {request.description}</p>
            <p>Customer ID: {request.customerId}</p>
            <p>Service ID: {request.serviceId}</p>
            <button onClick={handleMakeOfferForm}>Make Offer</button>
            
            {showMakeOfferForm &&
                <OfferAddForm onAdd={addOffer} requestId={request.id} />
            }
            <br></br>
            <button>Decline Request</button>
        </div>

    )
}

export default RequestsBox;