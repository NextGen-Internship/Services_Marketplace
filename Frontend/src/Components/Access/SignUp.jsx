// SignUp.js
import React from 'react';
import '../../styles/SignUp.css'
import { FaUserPen, FaUserTie } from 'react-icons/fa6';
import { IoIosLock } from 'react-icons/io';
import { MdOutlineEmail } from 'react-icons/md';
import { Link } from 'react-router-dom';

const SignUp = () => {
  return (
    <div className='sign-up'>
      <form action="">
        <h1>Registration</h1>
        <div className='input-box'>
          <input type='text' placeholder='First Name' required />
          <FaUserPen className='icon' />
        </div>
        <div className='input-box'>
          <input type='text' placeholder='Last Name' required />
          <FaUserPen className='icon' />
        </div>
        <div className='input-box'>
          <input type='text' placeholder='Username' required />
          <FaUserTie className='icon' />
        </div>
        <div className='input-box'>
          <input type='text' placeholder='Email' required />
          <MdOutlineEmail className='icon' />
        </div>
        <div className='input-box'>
          <input type='password' placeholder='Password' required />
          <IoIosLock className='icon' />
        </div>
        <div className='input-box'>
          <input type='text' placeholder='Confirm password' required />
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
