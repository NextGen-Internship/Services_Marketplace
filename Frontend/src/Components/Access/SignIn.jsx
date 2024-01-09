import React, { useEffect } from 'react';
import '../../styles/SignIn.css';
import { FaUserTie } from 'react-icons/fa6';
import { IoIosLock } from 'react-icons/io';
import { FaGoogle } from 'react-icons/fa';
import { FaFacebook } from 'react-icons/fa6';
import { Link, useNavigate } from 'react-router-dom';
import { useGoogleOAuth, GoogleLogin, useGoogleLogin } from '@react-oauth/google';

export const SignIn = () => {
  const navigate = useNavigate();
  const { signInWithGoogle } = useGoogleOAuth();

  useEffect(() => {
    // No need to manually initialize auth2; the library takes care of it
  }, []);

  const login = useGoogleLogin({
    onSuccess: (tokenResponse) => {
      console.log(tokenResponse); // Add a semicolon here

      // Navigate to the desired page or perform any other action after successful login
      navigate('/home-page');
    },
    onError: (error) => {
      console.error('Google login error:', error);
      navigate('/sign-in');
    },
  });

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
          <button onClick={login}>
            <div className='media-options-login-buttons'>
              <FaGoogle className='icon' />
              <span>Login with Google</span>
            </div>
          </button>
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
