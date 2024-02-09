import React, { useEffect, useState } from 'react';
import { useParams, useLocation } from 'react-router-dom';
import { getServiceById, getCityById, getCategoryById, getUserById, getAllCities } from '../service/ApiService';
import '../styles/ServiceDetailsPage.css';
import moment from 'moment';
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
    const [cities, setCities] = useState([]);
    const getCitiesNames = (service, cities) => {
        const serviceCities = service.cityIds.map((cityId) => {
            const city = cities.find((city) => city.id === cityId);
            return city ? city.name : null;
        });

        return serviceCities.filter(Boolean).join(', ');
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getAllCities();

                const citiesWithLabel = response.map((city) => ({
                    id: city.id,
                    name: city.name,
                }));

                setCities(citiesWithLabel);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);

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

    const handleEditDetails = () => {
        console.log('Edit details triggered');
    };


    return (
        <div className='service-details-container'>
            <h2>{service.title}</h2>
            <p>{service.categoryName}</p>
            <p>Provider Name: {service.providerName}</p>
            <p>Description: {service.description}</p>
            <p>Price: {service.price} BGN.</p>
            <p>{getCitiesNames(service, cities)} </p>
            <p> {formattedDate}</p>
           
            <button className='pay-button'>Make a request</button>
            <div className="button-container">
                <button className='edit-details-button' onClick={handleEditDetails}>
                    <FaEdit />
                </button>
            </div>
        </div>
    );
};
export default ServiceDetailsPage;