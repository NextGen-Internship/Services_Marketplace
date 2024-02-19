import React, { useEffect } from 'react'
import { useState } from 'react';
import { loadStripe } from '@stripe/stripe-js';
import { environment } from '../environment.js';
import { getUserById, getRequestById, getServiceById, OfferPaymentChechout } from '../service/ApiService';
import '../styles/OffersBox.css';

export const OfferBox = ({ offer }) => {
    const [provider, setProvide] = useState('');
    const [service, setService] = useState('');
    const [request, setRequest] = useState();
    const productRequest = {
        description: offer.description,
        price: offer.price,
        successUrl: 'http://localhost:3000/success',
        cancelUrl: 'http://localhost:3000/cancel',
        userId: offer.providerId,
        offerId: offer.id
    }
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

    const handleOfferPayment = async () => {
        try {
            const result = await OfferPaymentChechout(productRequest);
            console.log(result);
            const sessionId = result.sessionId;
            const stripe = await loadStripe(environment.stripe);

            if (stripe) {
                stripe.redirectToCheckout({
                    sessionId: sessionId
                });
            }

            } catch (error) {

                console.log('Error creating payment', error)
            }

        }

    return (
            <div>
                <div key={offer.id} className="offers-box">
                    <p>Provider: {provider}</p>
                    <p>Service: {service}</p>
                    <p>Description: {offer.description}</p>
                    <p>Price: {offer.price}</p>

                    <button onClick={handleOfferPayment}>Pay</button>
                    <br></br>
                    <button>Cancel Offer</button>
                </div>
            </div>
        )
    }


