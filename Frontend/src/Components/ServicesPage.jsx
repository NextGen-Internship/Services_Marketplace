import React from 'react';
import ServicesPageHeader from './ServicesPageHeader';
import ServiceBoxes from './ServiceBoxes';
import { useState, useEffect } from 'react';
import { getAllServices } from '../service/ApiService';

const ServicesPage = () => {
    const [services, setServices] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
          try {
            const servicesList = await getAllServices();
            setServices(servicesList);
          } catch (error) {
            console.error('Error fetching data:', error);
          }
        };
    
        fetchData();
      }, []);

    return (
        <div>
            <ServicesPageHeader/>
            {services.length > 0 ? <ServiceBoxes services={services}/> : 'No Services to Show'}
        </div>
    )
}

export default ServicesPage
