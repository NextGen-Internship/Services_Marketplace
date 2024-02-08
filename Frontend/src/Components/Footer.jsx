// Footer.jsx

import React from 'react';
import "../styles/Footer.css";

const Footer = () => {
  return (
    <div className='footer-container'>
      <h1 className='title-footer'>Service Marketplace</h1>
      <div className='text-footer'>
        <p>
          Created with a focus on user convenience, our application offers you an easy and intuitive way to publish service. The time required to post your ad is minimized while simultaneously providing you with rich customization options.
        </p>
      </div>
      <div className='text-footer'>
        <p>
          Our diverse categories allow you to find exactly what you're looking for or reach the right audience for your service. Whether you are in a big city or a small village, our application connects you with numerous users from across the country. Discover new opportunities and establish connections with people who share your interests.
        </p>
      </div>
      <div className='rights'>
        <p>All rights reserved by BlankFactor</p>
      </div>
    </div>
  );
}

export default Footer;
