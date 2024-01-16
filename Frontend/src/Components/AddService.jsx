// AddService.jsx
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../styles/AddService.css';
import Multiselect from 'multiselect-react-dropdown';

const AddService = ({ onAdd }) => {
  const [serviceTitle, setServiceTitle] = useState('');
  const [serviceDescription, setServiceDescription] = useState('');
  const [servicePrice, setServicePrice] = useState('');
  const [serviceCategory, setServiceCategory] = useState('');
  const [categoryList, setCategoryList] = useState([]);
  const providerId = 4;
  const [cities, setCities] = useState([]);
  const [chosen, setChosen] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get('http://localhost:8080/v1/categories');

        if (response.status !== 200) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        setCategoryList(response.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get('http://localhost:8080/v1/cities/all');

        if (response.status !== 200) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const citiesWithLabel = response.data.map((city) => ({
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

  const handleChange = (event) => {
    setServiceCategory(event.target.value);
  };

  const onSubmit = (e) => {
    e.preventDefault();

    if (!serviceTitle) {
      alert('Please add title');
      return;
    }

    if (serviceTitle.length < 2) {
      alert('The title has to be more than 2 symbols');
      return;
    }

    if (!serviceDescription) {
      alert('Please add description');
      return;
    }

    if (serviceDescription.length < 2) {
      alert('The description has to be more than 2 symbols');
      return;
    }

    if (!servicePrice) {
      alert('Please add price');
      return;
    }

    function isNumber(value) {
      return typeof value === 'number';
    }

    if (!isNumber(parseFloat(servicePrice))) {
      alert('Price has to be a number');
      return;
    }

    onAdd({
      title: serviceTitle,
      description: serviceDescription,
      serviceStatus: 'ACTIVE',
      price: parseFloat(servicePrice),
      providerId: providerId,
      categoryId: parseInt(serviceCategory),
      cityIds: chosen.map((city) => city.id)
    });

    setServiceTitle('');
    setServiceDescription('');
    setServicePrice('');
    setServiceCategory('');
    setCities([]);
    setChosen([]); 
  };

  return (
    <div className='add-service'>
      <form className='add-service-form' onSubmit={onSubmit}>
        <div className='form-control'>
          <label>Title</label>
          <input
            type='text'
            placeholder='Write service title'
            value={serviceTitle}
            onChange={(e) => setServiceTitle(e.target.value)}
          />
        </div>
        <div className='form-control'>
          <label>Description</label>
          <input
            type='text'
            placeholder='Write service description'
            value={serviceDescription}
            onChange={(e) => setServiceDescription(e.target.value)}
          />
        </div>
        <div className='form-control'>
          <label>Price</label>
          <input
            type='text'
            placeholder='Write service price'
            value={servicePrice}
            onChange={(e) => setServicePrice(e.target.value)}
          />
        </div>
        <div className='form-control'>
          <label>Category</label>
          <select
            className='form-control'
            value={serviceCategory}
            onChange={handleChange}
          >
            <option value=''>Choose Service Category</option>
            {categoryList.map((category) => (
              <option value={category.id} key={category.id}>
                {category.name}
              </option>
            ))}
          </select>
        </div>
        <div className='form-control'>
          <label>Cities</label>
          <Multiselect
            options={cities}
            selectedValues={chosen}
            onSelect={(selectedList) => setChosen(selectedList)}
            onRemove={(selectedList) => setChosen(selectedList)}
            displayValue='name'
          />
        </div>
        <input type='submit' value='Add Service' className='btn btn-block' />
      </form>
    </div>
  );
};

export default AddService;
