import React, { useEffect } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import '../styles/Navbar.css';
import { GiHamburgerMenu } from 'react-icons/gi';
import { ImCross } from 'react-icons/im';

function Navbar({ clicked, isClicked }) {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('Jwt_Token');
    if (token) {
      navigate('/home-page');
    }
  }, []);

  const handleClicked = () => {
    isClicked(!clicked);
    console.log('clicked');
  };

  const handleLogout = async () => {
    try {
      localStorage.removeItem('Jwt_Token');

      navigate('/home-page');
    } catch (error) {
      console.error('Error during logout:', error);
    }
  };

  return (
    <div className="Nav">
      <ul className="NavbarWrapper">
        <li className="NavLogo">
          <Link className="Link" to="/">
            Service Marketplace
          </Link>
        </li>
        <li className="NavElements">
          <NavLink className="Link" to="/home-page">
            Home
          </NavLink>
        </li>
        <li className="NavElements">
          <NavLink className="Link" to="/category-page">
            Categories
          </NavLink>
        </li>
        <li className="NavElements">
          <NavLink className="Link" to="/profile">
            Profile
          </NavLink>
        </li>
        <li className="NavElements">
          <NavLink className="Link" to="/services">
            Services
          </NavLink>
        </li>
        <li className="NavElements">
          <NavLink className="Link" to="/add-service-page">
            Add Service
          </NavLink>
        </li>

     
        {localStorage.getItem('Jwt_Token') ? (
          <li className="NavButton">
            <NavLink className="BtnLink" onClick={handleLogout}>
              Logout
            </NavLink>
          </li>
        ) : (
          <>
            <li className="NavButton">
              <NavLink className="BtnLink" to="/sign-in">
                Sign In
              </NavLink>
            </li>
            <li className="NavButton">
              <NavLink className="BtnLink" to="/sign-up">
                Sign Up
              </NavLink>
            </li>
          </>
        )}
      </ul>
      {!clicked ? (
        <GiHamburgerMenu onClick={handleClicked} className="Icon" />
      ) : (
        <ImCross onClick={handleClicked} className="Icon" />
      )}
    </div>
  );
}

export default Navbar;
