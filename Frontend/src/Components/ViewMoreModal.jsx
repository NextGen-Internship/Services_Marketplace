import React, { useState, useEffect } from 'react';
import '../styles/ViewMoreModal.css';

const ViewMoreModal = ({ isOpen, onClose, service, cities, categories }) => {
    if (!isOpen || !service) return null;

    const cityNames = service.cityIds.map(cityId => cities.find(city => city.id === cityId)?.name || 'Unknown City').join(', ');
    const categoryName = categories.find(category => category.id === service.categoryId)?.name || 'Unknown Category';

    return (
        <div className="modal-backdrop">
            <div className="modal-content">
                <h2>{service.title}</h2>
                <p><strong>Price:</strong> {service.price}</p>
                <p><strong>Description:</strong> {service.description}</p>
                <p><strong>Provider:</strong> {service.providerName} {/* Adjust as needed */}</p>
                <p><strong>Category:</strong> {categoryName}</p>
                <p><strong>Cities:</strong> {cityNames}</p>
                <p><strong>Created At:</strong> {service.createdAt}</p>
                <p><strong>Updated At:</strong> {service.updatedAt}</p>
                <button onClick={onClose}>Close</button>
            </div>
        </div>
    );
};

export default ViewMoreModal;