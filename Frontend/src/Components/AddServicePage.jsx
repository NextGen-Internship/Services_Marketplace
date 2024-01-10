import React from 'react'
import AddService from './AddService'
import { useState, useEffect } from 'react';

function AddServicePage() {
    const [services, setServices] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
        try {
            const response = await fetch('http://localhost:8080/v1/services');
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
        const id = Math.floor(Math.random() * 10000) + 1;
        // const newTask = { id, ...task };

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

    // delete service
    const deleteService = async (id) => {
        setServices(services.filter((service) => service.id !== id));

        await fetch(`http://localhost:8080/v1/services/delete/${id}`, {
        method: 'DELETE',
        })
        .then((response) => {
        if (!response.ok) {
            throw new Error(`Failed to delete task with id ${id}`);
        }
        })
        .catch((error) => console.error(error));
    };

    // const onAdd = async (service) => {
    //     console.log(service);
    //     // const id = Math.floor(Math.random() * 10000) + 1;
    //     // const newTask = { id, ...task };
    
    // //     await fetch('http://localhost:8080/v1/task/create', {
    // //     method: 'POST',
    // //     headers: {
    // //       'Content-Type': 'application/json',
    // //     },
    // //     body: JSON.stringify(task),
    // //   })
    // //     .then((response) => response.json())
    // //     .then((data) => {
    // //       console.log(data); 
    // //       setTasks([...tasks, data]); 
    // //     })
    // //     .catch((error) => console.error(error));
    //   }

  return (
    <div>
        <AddService onAdd={addService} />
    </div>
  )
}

export default AddServicePage
