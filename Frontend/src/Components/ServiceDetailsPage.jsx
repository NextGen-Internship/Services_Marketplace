import React, { useEffect, useState } from 'react';
import { useParams, useLocation } from 'react-router-dom';
import { getServiceById, getCityById, getCategoryById, getUserById } from '../service/ApiService';
import '../styles/ServiceDetailsPage.css';
import moment from 'moment';
import { ImFilePicture } from "react-icons/im";
import { FaEdit } from "react-icons/fa";


const ServiceDetailsPage = () => {
    const [service, setService] = useState({
        id: 0,
        title: '',
        providerId: 0,
        providerName: '',
        description: '',
        price: '',
        categoryId: '',
        categoryName: '',
        cityIds: [],
        updatedAt: []
    });
    const { serviceId } = useParams()

    useEffect(() => {
        async function loadServiceDetails() {
            console.log('Rendering ServiceDetailsPage', serviceId);

            const getServiceDetails = async () => {
                const serviceDetails = await getServiceById(serviceId);
                setService(serviceDetails);
                return serviceDetails
            };

            const localService = await getServiceDetails();

            const getProvider = async () => {
                const provider = await getUserById(localService.providerId);
                const providerName = provider.firstName + ' ' + provider.lastName;
                setService((prevService) => ({ ...prevService, providerName: providerName }))
            }

            const getCategory = async () => {
                const category = await getCategoryById(localService.categoryId);
                const categoryName = category.name;
                setService((prevService) => ({ ...prevService, categoryName: categoryName }))
            }

            getProvider();
            getCategory();
        }

        loadServiceDetails();
    }, [serviceId])

    if (!service) {
        return <div>Loading...</div>;
    }
    const formattedDate = moment(service.updatedAt, 'YYYY-MM-DD HH:mm:ss').toLocaleString();

    const handleImageUpload = () => {
        console.log('Image upload triggered');
        // Implement the upload functionality here
    };

    const handleEditDetails = () => {
        console.log('Edit details triggered');
        // Implement the edit functionality here
    };
    

    return (
        <div className='service-details'>
            <h2>{service.title}</h2>
            <p>Provider Name: {service.providerName}</p>
            <p>Description: {service.description}</p>
            <p>Price: {service.price}</p>
            <p>Category: {service.categoryName}</p>
            <p>Updated At: {formattedDate} </p>
            <button className='pay-button'>Pay Now</button>
            <button className='image-upload-button' onClick={handleImageUpload}>
                <ImFilePicture /> 
            </button>
            <button className='edit-button' onClick={handleEditDetails}>
                <ImFilePicture /> 
            </button>
        </div>
    );
};

export default ServiceDetailsPage;