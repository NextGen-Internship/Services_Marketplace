import React from 'react'
import AddService from './AddService'

function AddServicePage() {
    const onAdd = async (service) => {
        console.log(service);
        // const id = Math.floor(Math.random() * 10000) + 1;
        // const newTask = { id, ...task };
    
    //     await fetch('http://localhost:8080/v1/task/create', {
    //     method: 'POST',
    //     headers: {
    //       'Content-Type': 'application/json',
    //     },
    //     body: JSON.stringify(task),
    //   })
    //     .then((response) => response.json())
    //     .then((data) => {
    //       console.log(data); 
    //       setTasks([...tasks, data]); 
    //     })
    //     .catch((error) => console.error(error));
      }

  return (
    <div>
        <AddService onAdd={onAdd} />
    </div>
  )
}

export default AddServicePage
