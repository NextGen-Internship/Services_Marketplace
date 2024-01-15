import React, { useState } from 'react';
import '../../styles/SignIn.css';
import { FaUserTie, FaFacebook } from 'react-icons/fa';
import { IoIosLock } from 'react-icons/io';
import { Link, useNavigate } from 'react-router-dom';
import { GoogleLogin } from '@react-oauth/google';
import axios from 'axios';

export const SignIn = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    try {
      // Send login request to the backend
      const response = await axios.post('http://localhost:8080/api/auth/login', formData);
      // Handle the backend response if needed
      console.log('Backend response:', response.data);

      // Navigate or perform other actions based on your application logic
      navigate('/dashboard'); // Redirect to the home page, for example
    } catch (error) {
      console.error('Error during login:', error);
      // Handle login error, e.g., display an error message to the user
    }
  };

  const handleGoogleLogin = async (response) => {
    try {
      console.log('Google Login Response:', response);
  
      if (response && response.tokenObj && response.tokenObj.id_token) {
        const googleToken = response.tokenObj.id_token;
        // Send the Google token to your backend using Axios
        const backendResponse = await axios.post('http://localhost:8080/api/auth/google/login', {
          googleToken: googleToken,
        });
  
        // Handle the backend response if needed
        console.log('Backend response:', backendResponse.data);
  
        // Navigate or perform other actions based on your application logic
        navigate('/home-page'); // Redirect to the home page, for example
      }
    } catch (error) {
      console.error('Error during Google login:', error);
    }
  };

  return (
    <div className='sign-in'>
      <div>
        <form onSubmit={handleFormSubmit}>
          <h1>Login</h1>
          <div className='input-box'>
            <input
              type='text'
              placeholder='Email'
              name='email'
              value={formData.email}
              onChange={handleChange}
              required
            />
            <FaUserTie className='icon' />
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
          <div className='remember-forgot'>
            <label>
              <input type='checkbox' />Remember me
            </label>
            <a href='#'>Forgot password</a>
          </div>
          <button type='submit'>Login</button>
          <div className='register-link'>
            <p>
              Don't have an account? <Link to='/sign-up'>Register</Link>
            </p>
          </div>
        </form>
        <div className='media-options'>
          <GoogleLogin
            onSuccess={handleGoogleLogin}
            onFailure={handleGoogleLogin}
            clientId="350761079008-0ipa8rk7sumieir1rq4b5ljg3pu78trt.apps.googleusercontent.com"
            buttonText="Login with Google"
            cookiePolicy={'single_host_origin'}
          />
          <button onClick={() => console.log('login with FB click')}>
            <div className='media-options-login-buttons'>
              <FaFacebook className='icon' />
              <span>Login with Facebook</span>
            </div>
          </button>
        </div>
      </div>
    </div>
  );
};

export default SignIn;
