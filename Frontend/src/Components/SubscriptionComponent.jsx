import React, { useState } from 'react';
import { loadStripe } from '@stripe/stripe-js';
import { environment } from '../environment.js';
import axios from 'axios';
import { jwtDecode } from "jwt-decode";
import { useNavigate } from 'react-router-dom';
import Subscription from './Subscription.jsx';

const SubscriptionComponent = () => {
    const navigate = useNavigate();
    const [selectedPriceId, setSelectedPriceId] = useState(null);

    const handleCheckout = async () => {
        if (!selectedPriceId) {
            console.error('No price selected');
            return;
        }

        const localToken = localStorage.getItem('Jwt_Token');
        if (!localToken) {
            console.error('No token found');
            navigate('/login');
            return;
        }

        const decodedToken = jwtDecode(localToken);
        const userEmail = decodedToken['sub'];
        const userId = decodedToken['jti'];

        const checkout = {
            priceId: selectedPriceId,
            cancelUrl: 'http://localhost:3000/cancel',
            successUrl: 'http://localhost:3000/success',
            email: userEmail,
            userId: userId,
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
            <h2>Subscribe now!</h2>
            <div>
                <Subscription priceId={environment.monthlyPriceId} onSelected={setSelectedPriceId} />
                <Subscription priceId={environment.halfYearPriceId} onSelected={setSelectedPriceId} />
                <Subscription priceId={environment.yearlyPriceId} onSelected={setSelectedPriceId} />
                <button onClick={() => handleCheckout(selectedPriceId)}>
                    Subscribe
                </button>
            </div>
        </div>
    );
}

export default SubscriptionComponent;
