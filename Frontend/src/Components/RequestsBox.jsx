import React, { useState } from 'react'
import { OfferAddForm } from './OfferAddForm';
import { createOffer } from '../service/ApiService';
import { useEffect } from 'react';
import { getUserById,getServiceById} from '../service/ApiService';


const RequestsBox = ({ request }) => {
    const [showMakeOfferForm, setShowMakeOfferForm] = useState(false);
    const [showCustomer, setShowCustomer] = useState(false);
    const [service, setService] = useState('');

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

    useEffect(() => {
        const fetchCurrentUser = async () => {
            try {
                const userDetails = await getUserById(request.customerId);
                const name = userDetails.firstName + " " + userDetails.lastName
                setShowCustomer(name);

            } catch (error) {
                console.error('Error fetching current user:', error);
            }
        }
        fetchCurrentUser();
    }, []);

    useEffect(() => {
        const fetchService = async () => {
            try {
                const serviceDetails = await getServiceById(request.serviceId);
                const name = serviceDetails.title;
                setService(name);

            } catch (error) {
                console.error('Error fetching current user:', error);
            }
        }
        fetchService();

    }, []);

    return (
     
        <div key={request.id} className="request-box">
            <p>Description: {request.description}</p>
            <p>Customer: {showCustomer}</p>
            <p>Service ID: {service}</p>
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