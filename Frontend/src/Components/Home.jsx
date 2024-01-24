// Home.js
import React from 'react';
import '../styles/Home.css';
import { FaSearch } from 'react-icons/fa';
import Modal from './Modal';
import { useState } from 'react';

export const Home = () => {
  const [modalOpen, setModalOpen] = useState(false);

  return (
    <div className='home-wrapper'>
      <div className='search-box'>
        <div className='search-container'>
          <input
            className='input-search'
            type='text'
            placeholder='Search here...'
            autoComplete='off'
            required
          />
          <button className='search-btn'>
            <FaSearch className='icon-input' />
          </button>
        </div>
        <div>
          <button className='openModalBtn' onClick={() => {
          setModalOpen(true);}}>
            Subscribe
            </button>
            {modalOpen && <Modal setOpenModal={setModalOpen} />}
        </div>
      </div>

      </div>
  );
};

export default Home;
