import React, { useState, useEffect } from 'react';
import '../styles/ServiceBox.css';
import { getAllCities } from '../service/ApiService';
import moment from 'moment';
import { Link } from 'react-router-dom';

const ServiceBox = ({ service, cities }) => {
    const formattedDate = moment(service.updatedAt, 'YYYY-MM-DD HH:mm:ss').toLocaleString();

    const getCitiesNames = (service, cities) => {
        const serviceCities = service.cityIds.map((cityId) => {
            const city = cities.find((city) => city.id === cityId);
            return city ? city.name : null;
        });

        return serviceCities.filter(Boolean).join(', ');
    };

    return (
        <div className="service-box">
            <div className='photo'>
                <img src="https://www.shutterstock.com/image-vector/service-tool-icon-this-isolated-260nw-274711127.jpg"></img>
            </div>
            <div className='service-info'>
                <div className="service-title">
                    <h3>{service.title}</h3>
                </div>
                <div className="service-price">
                    <p>{service.price}</p>
                </div>
                <div className="service-description">
                    <p>{service.description}</p>
                </div>
                <div className="service-details">
                    <p>{getCitiesNames(service, cities)} - {formattedDate}</p>
                    <Link to={`service-details`} className="view-more-btn">View More</Link>
                </div>
            </div>
        </div>
    )
}

export default ServiceBox;
