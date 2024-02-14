import React, { useState, useEffect } from 'react';
import { Carousel } from 'react-responsive-carousel';
import moment from 'moment';
import { getFilesByReviewId } from '../service/ApiService';
import '../styles/ReviewBox.css';

const ReviewBox = ({ review }) => {
    const [reviewImages, setReviewImages] = useState([]);
    const reviewDate = moment(review.updatedAt, 'YYYY-MM-DD HH:mm:ss').toLocaleString();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getFilesByReviewId(review.id);

                const imagesUrls = response.map((image) => image.url);

                setReviewImages(imagesUrls);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);

    const carouselSettings = {
        showThumbs: false,
        interval: 3000,
        infiniteLoop: true,
        autoPlay: true,
        transitionTime: 600,
        stopOnHover: false,
        dynamicHeight: false,
    };

    return (
        <div key={review.id} className="review-box-service">
            <div className="review-info">
                {(
                    <>
                        <h3>Customer: {review.customerId}</h3>
                        <p>Added on: {reviewDate}</p>
                        <div className='review-carousel'>
                            <Carousel {...carouselSettings}>
                                {reviewImages.map((imageUrl, index) => (
                                    <div key={index}>
                                        <img src={imageUrl} alt={`Photo ${index + 1}`} />
                                    </div>
                                ))}
                            </Carousel>
                        </div>
                        <p>{review.description}</p>
                        <p>Rating: {review.rating}/5</p>

                    </>
                )}
            </div>
        </div>
    );
};

export default ReviewBox;
