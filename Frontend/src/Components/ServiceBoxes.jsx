import React from 'react';
import ServiceBox from './ServiceBox';

const ServiceBoxes = ({ services, cities }) => {
    return (
        <>
            {services.map((service) => (
                <ServiceBox key={service.id} service={service} cities={cities} />
            ))}
        </>
    )
}

export default ServiceBoxes;
