import React from 'react';
import '../styles/ServiceBox.css';
import ViewMoreBtn from './ViewMoreBtn';

const ServiceBox = ({ service }) => {
    return (
        <div className="service-box">
            <div className='service-info'>
                <h3>{service.title}</h3>
                <p>{service.description}<ViewMoreBtn onClick={() => console.log('click')}/></p>
            </div>
        </div>
    )
}

export default ServiceBox
