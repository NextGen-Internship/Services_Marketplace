import React from 'react'
import AddService from './AddService'
import { useState, useEffect } from 'react';
import '../styles/AddServicePage.css'
import { getAllServices, createService } from '../service/ApiService';

function AddServicePage() {
    const [services, setServices] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const services = await getAllServices();
                setServices(services);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);

    const addService = async (service, files) => {
        try {
            const newService = await createService(service, files);

            setServices([...services, newService]);
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

export default AddServicePage;
