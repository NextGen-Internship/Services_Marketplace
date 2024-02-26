import React, { useState } from 'react';
import { forgotPassword } from '../service/ApiService';

const ForgotPassword = () => {
    const [error, setError] = useState(null);
    const [message, setMessage] = useState(null);
    const [email, setEmail] = useState('');

    const handleFormSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await forgotPassword(email);
            setMessage(response);
            setError(null);
        } catch (error) {
            setMessage(null);
            setError(error.response.data);
        }
    };

    return (
        <div>
            <h2>Forgot Password</h2>

            {error && (
                <div>
                    <p className="text-danger">{error}</p>
                </div>
            )}

            {message && (
                <div>
                    <p className="text-warning">{message}</p>
                </div>
            )}

            <form onSubmit={handleFormSubmit} style={{ maxWidth: '420px', margin: '0 auto' }}>
                <div className="border border-secondary rounded p-3">
                    <div>
                        <p>We will be sending a reset password link to your email.</p>
                    </div>
                    <div>
                        <p>
                            <input
                                type="email"
                                name="email"
                                className="form-control"
                                placeholder="Enter your e-mail"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                autoFocus
                            />
                        </p>
                        <p className="text-center">
                            <input type="submit" value="Send" className="btn btn-primary" />
                        </p>
                    </div>
                </div>
            </form>
        </div>
    );
};

export default ForgotPassword;
