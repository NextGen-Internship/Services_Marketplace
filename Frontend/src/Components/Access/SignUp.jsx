import React, { useState } from 'react';
import '../../styles/SignUp.css'
import { FaUserPen, FaUserTie } from 'react-icons/fa6';
import { IoIosLock } from 'react-icons/io';
import { MdOutlineEmail } from 'react-icons/md';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';

const SignUp = () => {
  // State to hold form data
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: ''
  });

  // Handle form input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();

    // Perform any client-side validation if needed

    try {
      console.log(formData)
      // Send registration data to the backend
      const response = await axios.post('http://localhost:8080/api/auth/register', formData);

      // Handle the backend response if needed
      console.log('Backend response:', response.data);
      navigate('/sign-in');

      // Redirect or perform other actions based on your application logic
    } catch (error) {
      console.error('Error during registration:', error);
    }
  };

  return (
    <div className='sign-up'>
      <form onSubmit={handleSubmit}>
        <h1>Registration</h1>
        <div className='input-box'>
          <input
            type='text'
            placeholder='First Name'
            name='firstName'
            value={formData.firstName}
            onChange={handleChange}
            required
          />
          <FaUserPen className='icon' />
        </div>
        <div className='input-box'>
          <input
            type='text'
            placeholder='Last Name'
            name='lastName'
            value={formData.lastName}
            onChange={handleChange}
            required
          />
          <FaUserPen className='icon' />
        </div>
        <div className='input-box'>
          <input
            type='text'
            placeholder='Email'
            name='email'
            value={formData.email}
            onChange={handleChange}
            required
          />
          <MdOutlineEmail className='icon' />
        </div>
        <div className='input-box'>
          <input
            type='password'
            placeholder='Password'
            name='password'
            value={formData.password}
            onChange={handleChange}
            required
          />
          <IoIosLock className='icon' />
        </div>
        <div className='input-box'>
          <input
            type='password'
            placeholder='Confirm password'
            name='confirmPassword'
            value={formData.confirmPassword}
            onChange={handleChange}
            required
          />
          <IoIosLock className='icon' />
        </div>
        <div>
          <button type='submit'>Register</button>
          <div className='have-register'>
            <p>Already have an account? <Link to='/sign-in'>Login</Link></p>
          </div>
        </div>
      </form>
    </div>
  );
};

export default SignUp;
