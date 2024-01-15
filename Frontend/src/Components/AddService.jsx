// AddService.jsx
import React, {useState,useEffect} from 'react'
import '../styles/AddService.css'

const AddService = ({ onAdd }) => {
    const [serviceTitle, setServiceTitle] = useState("");
    const [serviceDescription, setServiceDescription] = useState("");
    const [servicePrice, setServicePrice] = useState("");
    const [serviceCategory, setServiceCategory] = useState("");
    const [serviceLocation, setServiceLocation] = useState("");
    const [categoryList, setCategoryList] = useState([]);

    useEffect(() => {
      const fetchData = async () => {
        try {
          const response = await fetch('http://localhost:8080/v1/categories');
          if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
          }
          const data = await response.json();
          setCategoryList(data);
        } catch (error) {
          console.error('Error fetching data:', error);
        }
      };
    
      fetchData();
    }, []);

    const handleChange = (event) =>{
        setServiceCategory(event.target.value);
    }

    const onSubmit = (e) => {
        e.preventDefault();

        if(!serviceTitle) {
            alert('Please add title');
            return;
        }

        if (serviceTitle.length < 2) {
            alert('The title have to be more than 2 symbols');
            return;
        }

        if(!serviceDescription) {
            alert('Please add description');
            return;
        }

        if (serviceDescription.length < 2) {
            alert('The description have to be more than 2 symbols');
            return;
        }

        if(!servicePrice) {
            alert('Please add price');
            return;
        }

        function isNumber(value) {
            return typeof value === 'number';
          }

        if (!isNumber(servicePrice)) {
            alert('Price have to be number');
            return;
        }

        onAdd({ serviceTitle, serviceDescription, servicePrice, serviceCategory, serviceLocation });

        setServiceTitle('');
        setServiceDescription('');
        setServicePrice('');
        setServiceCategory('');
        setServiceLocation('');
    }

    return (
        <div className='add-service'>
        <form className='add-service-form' onSubmit={onSubmit}>
          <div className='form-control'>
            <label>Title</label>
            <input type='text' placeholder='Write service title' value={serviceTitle} onChange={(e) => setServiceTitle(e.target.value)} />
          </div>
          <div className='form-control'>
            <label>Description</label>
            <input type='text' placeholder='Write service description' value={serviceDescription} onChange={(e) => setServiceDescription(e.target.value)} />
          </div>
          <div className='form-control'>
            <label>Price</label>
            <input type='text' placeholder='Write service price' value={servicePrice} onChange={(e) => setServicePrice(parseFloat(e.target.value))} />
          </div>
          <div className='form-control'>
            <label>Category</label>
            <select className="form-control" value={serviceCategory} onChange={handleChange}>
              <option value="">Choose Service Category</option>

                {categoryList.map(category => (
                    <option value={category.name} key={category.id} >{category.name}</option>
                ))
                }

            </select>
          </div>
          <div className='form-control'>
          <label>Location</label>
            <input type='text' placeholder='Write the location of the service' value={serviceLocation} onChange={(e) => setServiceLocation(e.target.value)} />
          </div>
    
          <input type='submit' value='Add Service' className='btn btn-block' />
        </form>
        </div>
      )
}

export default AddService;