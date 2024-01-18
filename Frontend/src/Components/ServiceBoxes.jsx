import React from 'react';
import ServiceBox from './ServiceBox';

const ServiceBoxes = ({ services }) => {
    return (
        <>
        {services.map((service) => (
                <ServiceBox key={service.id} service={service} />
            ))}
        </>
    )
}

export default ServiceBoxes;
