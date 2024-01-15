import React from 'react'
import AddService from './AddService'
import { useState, useEffect } from 'react';
import '../styles/AddServicePage.css'

function AddServicePage() {
    const [services, setServices] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
        try {
            const response = await fetch('http://localhost:8080/v1/services/all');
            if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
            }
            const data = await response.json();
            setServices(data);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
        };
    
        fetchData();
    }, []);
    

    // add service
    const addService = async (service) => {
        await fetch('http://localhost:8080/v1/services/create', {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        },
        body: JSON.stringify(service),
    })
        .then((response) => response.json())
        .then((data) => {
        console.log(data); 
        setServices([...services, data]); 
        })
        .catch((error) => console.error(error));
    }

  return (
    <div className='AddServicePage'>
        <AddService onAdd={addService} />
    </div>
  )
}

export default AddServicePage
