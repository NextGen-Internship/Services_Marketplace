import React from 'react'
import { Link, NavLink, useNavigate } from 'react-router-dom'
import "../styles/Navbar.css"
import { GiHamburgerMenu } from "react-icons/gi"
import { ImCross } from "react-icons/im"


function Navbar({ clicked, isClicked }) {
    const navigate = useNavigate();
    
    const handleClicked = () => {
        isClicked(!clicked);
        console.log("clicked")
    };

    const handleLogout = async () => {
        
        try {
            // Send a request to the server to invalidate the token
            //await axios.post('http://localhost:8080/api/auth/logout');

            // Clear the JWT token from local storage
            localStorage.removeItem('Jwt_Token');

            // Redirect to the login page or any other desired destination
            navigate('/home-page');
        } catch (error) {
            console.error('Error during logout:', error);
            // Handle logout error if needed
        }
    };
    return (
        <div className='Nav'>
            <ul className='NavbarWrapper'>
                <li className='NavLogo'><Link className='Link' to="/">Service Marketplace</Link></li>
                <li className='NavElements'><NavLink className='Link' to="/home-page">Home</NavLink></li>
                <li className='NavElements'><NavLink className='Link' to="/category-page">Categories</NavLink></li>
                <li className='NavElements'><NavLink className='Link' to="/profile">Profile</NavLink></li>
                <li className='NavElements'><NavLink className='Link' to="/services">Services</NavLink></li>
                <li className='NavElements'><NavLink className='Link' to="/add-service-page">Add Service</NavLink></li>
                <li className='NavButton'><NavLink className='BtnLink' to="/sign-in">Sign In</NavLink></li>
                <li className='NavButton'><NavLink className='BtnLink' to="/sign-up">Sign Up</NavLink></li>
                <li className='NavButton'><p className='BtnLink' onClick={handleLogout}>Logout</p></li>
            </ul>
            {!clicked ? (
                <GiHamburgerMenu onClick={handleClicked} className='Icon' />
            ) : (
                <ImCross onClick={handleClicked} className='Icon' />
            )}
        </div>
    )
}

export default Navbar