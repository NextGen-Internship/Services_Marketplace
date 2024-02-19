import React, { useEffect } from 'react'
import { useState } from 'react';
import { getUserById, getRequestById, getServiceById } from '../service/ApiService';
import '../styles/OffersBox.css';

export const OfferBox = ({ offer }) => {
    const [provider, setProvide] = useState('');
    const [service, setService] = useState('');
    const [request, setRequest] = useState();
    const [showOffers, setShowOffers] = useState(false);
    const handleShowOffers = () => {
        setShowOffers(!showOffers);
    }

    useEffect(() => {
        const fetchCurrentUser = async () => {
            try {
                const userDetails = await getUserById(offer.providerId);
                const name = userDetails.firstName + " " + userDetails.lastName
                setProvide(name);

            } catch (error) {
                console.error('Error fetching current user:', error);
            }
        }
        fetchCurrentUser();
    }, []);

    useEffect(() => {
        const fetchRequest = async () => {
            try {
                const requestDetails = await getRequestById(offer.requestId);
                setRequest(requestDetails);

            } catch (error) {
                console.error('Error fetching current user:', error);
            }
        }
        fetchRequest();
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
        <div>
            <div key={offer.id} className="offers-box">
                <p>Provider: {provider}</p>
                <p>Service: {service}</p>
                <p>Description: {offer.description}</p>
                <p>Price: {offer.price}</p>

                <button>Pay</button>
                <br></br>
                <button>Cancel Offer</button>
            </div>
        </div>
    )
}


