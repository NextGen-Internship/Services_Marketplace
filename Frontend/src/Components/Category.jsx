import React, { useState, useEffect } from 'react';
import '../styles/Category.css';
import { FaSearch } from 'react-icons/fa';

const Category = () => {
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch('http://localhost:8080/v1/categories');
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const data = await response.json();
        setCategories(data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
  
    fetchData();
  }, []);

  const [searchQuery, setSearchQuery] = useState('');

  const filteredCategories = categories.filter((category) =>
    category.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleSearch = (e) => {
    e.preventDefault();
  };

  return (
    <div className='category-wrapper'>
      <div className='category-page'>
        <div className='search-box'>
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

        <div className='category-list'>
          {filteredCategories.length > 0 && <h2>Categories</h2>}
          <div className='category-cards'>
            {filteredCategories.length === 0 ? (
              <p className='no-matching-message'>
                There are no categories matching your search.
              </p>
            ) : (
              filteredCategories.map((category) => (
                <a key={category.id} href={`#${category.name}`} className='category-card'>
                  <h3>{category.name}</h3>
                  <p>{category.description}</p>
                </a>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Category;
