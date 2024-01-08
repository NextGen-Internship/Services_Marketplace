// Home.js
import React from 'react';
import '../styles/Home.css'
import { FaSearch } from "react-icons/fa";

export const Home = () => {
  return (
    <div className='home-wrapper'>
      <div className='home-page'>
        <div className='search-box'>
          <form action=''>
            <div>
              <input
                className='input-search'
                type='text'
                placeholder='Search here...'
                autoComplete='off'
                required
              />
            
              <button className='search-btn'><FaSearch className='icon-input' /></button>
              
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Home;
