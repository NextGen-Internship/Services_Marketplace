import React from 'react';
import '../styles/ServiceBox.css';

const ServiceBox = ({ service }) => {
    return (
        <div className="service-box">
            <h3>{service.title}</h3>
            <p>{service.description}</p>
        </div>
    )
}

export default ServiceBox
