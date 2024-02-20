import React, { useState } from 'react'
import axios from 'axios';
import { OfferAddForm } from './OfferAddForm';
import { createOffer } from '../service/ApiService';
import { useEffect } from 'react';
import { getUserById, getServiceById } from '../service/ApiService';
import '../styles/RequestBox.css';


const RequestsBox = ({ request }) => {
    const [showMakeOfferForm, setShowMakeOfferForm] = useState(false);
    const [showCustomer, setShowCustomer] = useState(false);
    const [service, setService] = useState('');
    const [requestId, setRequestId] = useState(request.id);


    const handleMakeOfferForm = () => {
        setShowMakeOfferForm(!showMakeOfferForm);

    }


    const [Request, setRequest] = useState({
        customerId: request.providerId,
        serviceId: request.serviceId,
        description: request.description,
        requestStatus: request.requestStatus
    });



    const cancelRequest = async (requestId, Request) => {
        try {
            const response = await axios.post(`http://localhost:8080/v1/request/cancel/${requestId}`, Request)
            console.log('Request cancelled:', response.data);
        } catch (error) {
            console.error('Error cancelling request:', error);
        }
    };

    const handleDeclineRequest = async () => {
        if (requestId) {
            setRequest
                ({
                    customerId: request.providerId,
                    serviceId: request.serviceId,
                    description: request.description,
                    requestStatus: 'DECLINED'
                })
            cancelRequest(requestId, Request);
        } else {
            console.error('No request ID found');
        }
    };

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
            <p>Service: {service}</p>
            <button onClick={handleMakeOfferForm}>Make Offer</button>

            {showMakeOfferForm &&
                <OfferAddForm onAdd={addOffer} requestId={request.id} />
            }
            <br></br>
            <button onClick={handleDeclineRequest}>Decline Request</button>
        </div>

    )
}

export default RequestsBox;