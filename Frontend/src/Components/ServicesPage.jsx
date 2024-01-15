import React from 'react';
import ServicesPageHeader from './ServicesPageHeader';
import ServiceBoxes from './ServiceBoxes';
import { useState, useEffect } from 'react';
import axios from 'axios';

const ServicesPage = () => {
    const [services, setServices] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
          try {
            const response = await axios.get('http://localhost:8080/v1/services/all');
            setServices(response.data);
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
