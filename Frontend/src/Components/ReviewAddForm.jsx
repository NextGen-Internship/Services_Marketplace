import React from 'react';
import { createReview } from '../service/ApiService';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from "jwt-decode";
import { useState } from 'react';

const ReviewAddForm = ({ serviceId }) => {
    const [reviewDescription, setReviewDescription] = useState('');
    const [reviewRating, setReviewRating] = useState('');
    const [reviews, setReviews] = useState([]);
    const navigate = useNavigate();
    const localToken = localStorage.getItem('Jwt_Token');

    if (!localToken) {
        console.error('No JWT token found');
        navigate('/sign-in');
        return;
    }

    const decodedToken = jwtDecode(localToken);
    const userId = decodedToken['jti'];

    const addReview = async (review) => {
        try {
            const newReview = await createReview(review);
        } catch (error) {
            console.error(error);
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!reviewDescription) {
            alert('Please add description');
            return;
        }

        if (reviewDescription.length < 2) {
            alert('The description has to be more than 2 symbols');
            return;
        }

        if (!reviewRating) {
            alert('Please add rating');
            return;
        }

        if (isNaN(parseFloat(reviewRating)) || !isFinite(parseFloat(reviewRating))) {
            alert('Rating has to be a valid number');
            return;
        }

        if (parseFloat(reviewRating) < 0 || parseFloat(reviewRating) > 5) {
            alert('Rating has to be between 0 and 5 inclusive');
            return;
        }

        const reviewRequest = {
            description: reviewDescription,
            rating: parseFloat(reviewRating),
            customerId: userId,
            serviceId: serviceId,
            isActive: true
        };

        addReview(reviewRequest);

        setReviewDescription('');
        setReviewRating('');
    };

    return (
        <div>
            <h2>Add Review</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Description:</label>
                    <input
                        type="text"
                        value={reviewDescription}
                        onChange={(e) => setReviewDescription(e.target.value)}
                    />
                </div>
                <div>
                    <label>Rating:</label>
                    <input
                        type="number"
                        value={reviewRating}
                        onChange={(e) => setReviewRating(e.target.value)}
                        min="0"
                        max="5"
                    />
                </div>
                <button type="submit">Submit</button>
            </form>
        </div>
    );
};

export default ReviewAddForm;
