// Footer.jsx
import React from 'react';
import '../styles/Footer.css';

const Footer = () => {
  return (
    <>
      <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" />
      
      {/* GOOGLE FONTS */}
      <link rel="preconnect" href="https://fonts.gstatic.com" />
      <link href="https://fonts.googleapis.com/css2?family=Fredoka+One&family=Play&display=swap" rel="stylesheet" />

      <footer>
        <div class="footer">
          <div class="row">
            <a href="#"><i class="fa fa-facebook"></i></a>
            <a href="#"><i class="fa fa-instagram"></i></a>
            <a href="#"><i class="fa fa-youtube"></i></a>
            <a href="#"><i class="fa fa-twitter"></i></a>
          </div>

          <div class="row">
            <ul>
              <li><a href="#">Contact us</a></li>
              <li><a href="services">Our Services</a></li>
              <li><a href="#">Privacy Policy</a></li>
              <li><a href="#">Terms & Conditions</a></li>
              
            </ul>
          </div>

          <div class="row">
            Blankfactor Copyright © 2024 Blankfactor - All rights reserved 
          </div>
        </div>
      </footer>
    </>
  );
};

export default Footer;
