import React from 'react';
import '../../styles/SignIn.css';
import { FaUserTie, FaFacebook } from 'react-icons/fa';
import { IoIosLock } from 'react-icons/io';
import { Link, useNavigate } from 'react-router-dom';
import { useGoogleLogin, GoogleLogin } from '@react-oauth/google';

export const SignIn = () => {
  const navigate = useNavigate();

  const responseGoogle = (response) => {
    console.log(response);
    // Handle Google login response here if needed
  };

  return (
    <div className='sign-in'>
      <div>
        <form action=''>
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
            onSuccess={responseGoogle}
            onFailure={responseGoogle}
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
