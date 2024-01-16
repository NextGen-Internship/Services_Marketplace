import React from 'react'
import AddService from './AddService'
import { useState, useEffect } from 'react';
import axios from 'axios';
import '../styles/AddServicePage.css'
import apiService from '../service/ApiService'

function AddServicePage() {
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
    

    // add service
    const addService = async (service) => {
        try {
            const response = await axios.post('http://localhost:8080/v1/services/create', service, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            const data = response.data;
            console.log(data);
            setServices([...services, data]);
        } catch (error) {
            console.error(error);
        }
    };

  return (
    <div className='AddServicePage'>
        <AddService onAdd={addService} />
    </div>
  )
}

export default AddServicePage
