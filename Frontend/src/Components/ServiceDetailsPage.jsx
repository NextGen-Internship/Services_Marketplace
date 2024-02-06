import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getServiceById, getCityById, getCategoryById, getUserById } from '../service/ApiService';
import '../styles/ServiceDetailsPage.css';


const ServiceDetailsPage = () => {
    const [service, setService] = useState({
        id: 0,
        title: '',
        providerId: 0,
        description: '',
        price: '',
        categoryId: '',
        cityIds: [],
    });
    console.log('Rendering ServiceDetailsPage', service.id); 


    useEffect(() => {
        const getServiceDetails = async () => {
            const serviceDetails = await getServiceById(service.id);
            setService(serviceDetails);
        };

        getServiceDetails();
    }, [service.id]);

    useEffect(() => {
        const getProvider = async () => {
            const provider = await getUserById(service.providerId);
            //setService(providerId:provider);
        }
    })

    if (!service) {
        return <div>Loading...</div>;
    }

    return (
        <div className='service-details'>
            <h2>{service.title}</h2>
            <p>Service ID: {service.id}</p>
            <p>Provider Name: {service.providerName}</p>
            <p>Description: {service.description}</p>
            <p>Price: {service.price}</p>
            <p>Category: {service.categoryId}</p>
        </div>
    );
};

export default ServiceDetailsPage;