import React, { useState, useEffect } from 'react';
import { loadStripe } from '@stripe/stripe-js';
import { environment } from '../environment.js';
import axios from 'axios';
import { jwtDecode } from "jwt-decode";
import { useNavigate } from 'react-router-dom';
import Subscription from './Subscription.jsx';

const SubscriptionComponent = () => {
    const monthlyPriceId = 'price_1OcUNzI2KDxgMJyoxeNLRi93';
    const halfYearPriceId = 'price_1OeaSMI2KDxgMJyo18FOtvvQ';
    const yearlyPriceId = 'price_1OewOKI2KDxgMJyoQiYAv4zP';

    const navigate = useNavigate();

    const handleCheckout = async (priceId) => {
        const localToken = localStorage.getItem('Jwt_Token');
        if (!localToken) {
            console.error('No token found');
            navigate('/login');
            return;
        }

        const decodedToken = jwtDecode(localToken);
        const userEmail = decodedToken['sub'];

        const checkout = {
            priceId: priceId,
            cancelUrl: 'http://localhost:3000/cancel',
            successUrl: 'http://localhost:3000/success',
            email: userEmail,
        };

        try {
            const response = await axios.post(`http://localhost:8080/api/subscribe/subscription`, checkout);
            const sessionId = response.data.sessionId;
            const stripe = await loadStripe(environment.stripe);

            if (stripe) {
                stripe.redirectToCheckout({
                    sessionId: sessionId
                });
            }
        } catch (error) {
            console.error('Error during checkout:', error);
        }
    };

    return (
        <div className="example-card">
            <h1>Subscribe now!</h1>
            <div>
                <Subscription priceId={monthlyPriceId} />
                <Subscription priceId={halfYearPriceId} />
                <Subscription priceId={yearlyPriceId} />
                <button onClick={() => handleCheckout(monthlyPriceId)}>
                    Subscribe
                </button>
            </div>
        </div>
    );
}

export default SubscriptionComponent;