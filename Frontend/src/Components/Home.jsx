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
        <h1>Category</h1>
        <div className="content-box">
          <div className="card">
           
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
        <button className='view-more-btn' type='Submit'>View More</button>
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
        <button className='view-more-btn' type='Submit'>View More</button>
      </div>
    </div>

  );
};

export default Home;
