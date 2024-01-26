import React from 'react';
import { useLocation } from 'react-router-dom';
import { useEffect } from 'react';

const SuccessPage = () => {
    const location = useLocation();

    useEffect(() => {
        const queryParams = new URLSearchParams(location.search);
        const sessionId = queryParams.get('session_id');
        console.log('Subscription success! Session ID:', sessionId);
    }, [location.search]);

    return (
        <div>
            <h1>Subscription Successful!</h1>
            <p>Thank you for subscribing to our service.</p>
        </div>
    )
};

export default SuccessPage;
