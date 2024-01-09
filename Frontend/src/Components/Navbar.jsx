import React from 'react'
import { Link, NavLink } from 'react-router-dom'
import "../styles/Navbar.css"
import {GiHamburgerMenu} from "react-icons/gi"
import {ImCross} from "react-icons/im"

function Navbar({clicked, isClicked}) {
    
    const handleClicked = () => {
        isClicked(!clicked);
        console.log("clicked")
    };
    return (
        <div className='Nav'>
            <ul className='NavbarWrapper'>
                <li className='NavLogo'><Link className='Link' to="/">Service Marketplace</Link></li>
                <li className='NavElements'><NavLink className='Link' to="/home-page">Home</NavLink></li>
                <li className='NavElements'><NavLink className='Link' to="/category-page">Category</NavLink></li>
                <li className='NavElements'><NavLink className='Link' to="/profile">Profile</NavLink></li>
                <li className='NavButton'><NavLink className='BtnLink' to="/sign-in">Sign In</NavLink></li>
                <li className='NavButton'><NavLink className='BtnLink' to="/sign-up">Sign Up</NavLink></li>
            </ul>
            {!clicked ? (
             <GiHamburgerMenu onClick={handleClicked} className='Icon'/>
            ) : (
                <ImCross onClick={handleClicked} className='Icon'/>
            )}
        </div>
    )
}

export default Navbar