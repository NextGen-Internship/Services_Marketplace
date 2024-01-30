import React, { useState, useEffect } from 'react';
import '../styles/Modal.css';
import axios from 'axios';
import Stripe from 'stripe';
import { useNavigate } from 'react-router-dom';

function Modal({ setOpenModal }) {
    const [planData, setPlanData] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

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

    const navigateToNewSubscription = () => {
        navigate('/new-subscription');
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
                                <button onClick={navigateToNewSubscription}>Subscribe</button>
                            </div>
                        </>
                    ) : (
                        <div>
                        <p>No plan data available.</p>
                        <button onClick={navigateToNewSubscription}>Subscribe</button>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}

export default Modal;
