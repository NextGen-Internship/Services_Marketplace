import React, { useState, useEffect } from 'react';
import '../styles/Modal.css';
import axios from 'axios';
import Stripe from 'stripe';

function Modal({ setOpenModal }) {
    const [planData, setPlanData] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchPlanData = async () => {
            try {
                const response = await axios.get(
                    'https://api.stripe.com/v1/plans/price_1OcUNzI2KDxgMJyoxeNLRi93',
                    {
                        headers: {
                            Authorization: `Bearer sk_test_51OcQX6I2KDxgMJyoLEPzCcdVgucBUKxHjaTYal5aaj0i3z4PzUCktvxT1yjiJKCmOYiqes1OKtzkTvbNWjolFjrm00Tzq3PmyY`,
                        },
                    }
                );

                console.log('Stripe API response:', response.data);

                setPlanData(response.data);
                setLoading(false);
            } catch (error) {
                console.error('Error fetching plan data', error);
                setLoading(false);
            }
        };

        fetchPlanData();
    }, []);

    const checkout = async () => {
        try {
            if (!planData) {
                console.error('Plan data not available');
                return;
            }

            const response = await axios.post(
                'http://localhost:8080/api/subscribe/create-subscription-checkout-session',
                {
                    plan: planData.id,
                    quantity: 1,
                },
                {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                }
            );

            const { sessionId } = response.data;
            window.location.href = `https://checkout.stripe.com/checkout/session/${sessionId}`;
        } catch (error) {
            console.error('Error during checkout', error);
        }
    };

    return (
        <div className="modalBackground">
            <div className="modalContainer">
                <div className="titleCloseBtn">
                    <button onClick={() => setOpenModal(false)}>X</button>
                </div>
                <div className="title">
                    <h1>Become Provider</h1>
                </div>
                <div className="body">
                    {loading ? (
                        <p>Loading plan data...</p>
                    ) : planData ? (
                        <>
                            <p>${planData.amount / 100} per {planData.interval}</p>
                            <div className="footer">
                                <button onClick={checkout}>Subscribe</button>
                            </div>
                        </>
                    ) : (
                        <p>No plan data available.</p>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Modal;
