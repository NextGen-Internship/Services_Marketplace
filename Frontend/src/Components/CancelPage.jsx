import React from 'react';
import { useNavigate } from 'react-router-dom';

const CancelPage = () => {
    const navigate = useNavigate();

    const onButtonClick = () => {
        navigate("/home-page")
    }

    return (
        <div>
            <h1>Subscription Canceled</h1>
            <p>Your subscription process has been canceled.</p>
            <button onClick={onButtonClick}>Go Home</button>
        </div>
    )
};

export default CancelPage;
