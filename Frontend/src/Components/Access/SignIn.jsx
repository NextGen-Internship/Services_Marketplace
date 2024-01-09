// SignIn.js
import React from 'react';
import '../../styles/SignIn.css'
import { FaUserTie } from 'react-icons/fa6';
import { IoIosLock } from 'react-icons/io';
import { FaGoogle } from 'react-icons/fa';
import { FaFacebook } from 'react-icons/fa';
import { Link } from 'react-router-dom';

export const SignIn = () => {
  return (
    <div className='sign-in'>
      <form action="">
        <h1>Login</h1>
        <div className='input-box'>
          <input type='text' placeholder='Username' required />
          <FaUserTie className='icon' />
        </div>
        <div className='input-box'>
          <input type='password' placeholder='Password' required />
          <IoIosLock className='icon' />
        </div>
        <div className='remember-forgot'>
          <label><input type='checkbox' />Remember me</label>
          <a href='#'>Forgot password</a>
        </div>
        <button type='submit'>Login</button>
        <div className='register-link'>
          <p>Don't have an account? <Link to='/sign-up'>Register</Link></p>
        </div>
      </form>
      <div className='media-options'>
        <a href='#'>
          <FaGoogle className='icon' />
          <span>Login with Google</span>
        </a>
        <a href='#'>
          <FaFacebook className='icon' />
          <span>Login with Facebook</span>
        </a>
      </div>
    </div>
  );
};
