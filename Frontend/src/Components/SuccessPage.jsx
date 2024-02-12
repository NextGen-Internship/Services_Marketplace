import React from 'react';
import { useLocation } from 'react-router-dom';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/SuccessPage.css';


const SuccessPage = () => {
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        const queryParams = new URLSearchParams(location.search);
        const sessionId = queryParams.get('session_id');
        console.log('Subscription success! Session ID:', sessionId);
    }, [location.search]);

    const onButtonClick = () => {
        navigate("/home-page")
    }

    return (
        <div className="success-page-container"> 
            <h1>Subscription Successful!</h1>
            <p>Thank you for subscribing to our service.</p>
            <button onClick={onButtonClick}>Go Home</button>
        </div>
    )
};

export default SuccessPage;