import React, { useState, useEffect } from 'react';
import '../styles/AddService.css';
import Multiselect from 'multiselect-react-dropdown';
import { getAllCategories, getAllCities, getUserById } from '../service/ApiService';
import { jwtDecode } from "jwt-decode";
import { useNavigate } from 'react-router-dom';

const AddService = ({ onAdd }) => {
  const [serviceTitle, setServiceTitle] = useState('');
  const [serviceDescription, setServiceDescription] = useState('');
  const [servicePrice, setServicePrice] = useState('');
  const [serviceCategory, setServiceCategory] = useState('');
  const [categoryList, setCategoryList] = useState([]);
  const [providerId, setProviderId] = useState();
  const [cities, setCities] = useState([]);
  const [chosen, setChosen] = useState([]);
  const [selectedFiles, setSelectedFiles] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const categories = await getAllCategories();
        setCategoryList(categories);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);

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
    const fetchUserData = async () => {
      const localToken = localStorage.getItem('Jwt_Token');

      if (!localToken) {
        console.error('No JWT token found');
        navigate('/sign-in');
        return;
      }

      const decodedToken = jwtDecode(localToken);
      const userId = decodedToken['jti'];
      const userRole = localStorage.getItem('Jwt_Token') ? jwtDecode(localStorage.getItem('Jwt_Token'))?.role : null;

      if (!userId || !userRole) {
        console.error('No user ID or role found');
        navigate('/sign-in');
        return;
      }

      if (userRole.includes('PROVIDER')) {
        try {
          const userData = await getUserById(userId);
          console.log('User data:' + userData);
          console.log('User role:', userRole);
          setProviderId(userId);
        } catch (error) {
          console.error('Error fetching user data:', error);
        }
      };
    }
    fetchUserData();
  }, [navigate]);

  const handleChange = (event) => {
    setServiceCategory(event.target.value);
  };

  const handleImageChange = (event) => {
    const files = event.target.files;
    setSelectedFiles(files);
}

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

    if (isNaN(parseFloat(servicePrice)) || !isFinite(parseFloat(servicePrice))) {
      alert('Price has to be a valid number');
      return;
    }

    if (!serviceCategory) {
      alert('Please choose category');
      return;
    }

    const serviceRequest = {
      title: serviceTitle,
      description: serviceDescription,
      serviceStatus: 'ACTIVE',
      price: parseFloat(servicePrice),
      providerId: providerId,
      categoryId: parseInt(serviceCategory),
      cityIds: chosen.map((city) => city.id)
    };

    onAdd(serviceRequest, selectedFiles);

    setServiceTitle('');
    setServiceDescription('');
    setServicePrice('');
    setServiceCategory('');
    setChosen([]);
    setSelectedFiles([]);
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
        <div className="form-control">
          <label>Pictures</label>
          <input type="file" onChange={handleImageChange} accept="image/*" multiple/>
        </div>
        <input type='submit' value='Add Service' className='btn btn-block' />
      </form>
    </div>
  );
};

export default AddService;
