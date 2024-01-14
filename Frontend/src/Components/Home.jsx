import React, { useState } from 'react';
import '../styles/Home.css';
import { FaSearch } from 'react-icons/fa';

const Home = () => {
  const [searchQuery, setSearchQuery] = useState('');

  const handleSearch = (e) => {
    e.preventDefault();
    // Add your search logic here, using the searchQuery state
    console.log('Searching for:', searchQuery);
    // You can implement further logic, such as fetching data or updating the UI based on the searchQuery
  };

  return (
    <div>
      <div className="home-category">
        <div className='home-search'>
          <form onSubmit={handleSearch}>
            <div>
              <input
                className='input-search'
                type='text'
                placeholder='Search here...'
                autoComplete='off'
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                required
              />
              <button type='submit' className='search-btn'>
                <FaSearch className='icon-input' />
              </button>
            </div>
          </form>
        </div>

        <h1>Category</h1>
        <div className="content-box">
          <div className="card">
            {/* Your category content here */}
          </div>
          <div className="card">
            {/* Your category content here */}
          </div>
          <div className="card">
            {/* Your category content here */}
          </div>
          <div className="card">
            {/* Your category content here */}
          </div>
          <div className="card">
            {/* Your category content here */}
          </div>
          <div className="card">
            {/* Your category content here */}
          </div>

          {/* Add more cards as needed */}
        </div>
      </div>

      <div className='home-service'>
        <h1>Service</h1>
        <div className="content-box">
          <div className="card">
            {/* Your service content here */}
          </div>
          <div className="card">
            {/* Your service content here */}
          </div>
          <div className="card">
            {/* Your service content here */}
          </div>
          <div className="card">
            {/* Your service content here */}
          </div>
          <div className="card">
            {/* Your service content here */}
          </div>
          <div className="card">
            {/* Your service content here */}
          </div>
          {/* Add more cards as needed */}
        </div>
      </div>
    </div>

  );
};

export default Home;
