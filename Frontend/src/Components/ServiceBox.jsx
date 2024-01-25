import React, { useState, useEffect } from 'react';
import '../styles/ServiceBox.css';
import ViewMoreBtn from './ViewMoreBtn';
import { getAllCities } from '../service/ApiService';
import moment from 'moment';

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
                <div>
                    <h3>{service.title}</h3>
                    <p>{service.price}</p>
                </div>
                <div>
                    <p>{service.description}</p>
                </div>
                <div>
                    <p>{getCitiesNames(service, cities)} - {formattedDate}</p>
                    <ViewMoreBtn onClick={() => console.log('click')} />
                </div>
            </div>
        </div>
    )
}

export default ServiceBox;
