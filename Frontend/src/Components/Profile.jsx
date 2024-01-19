import React, { useState, useEffect }  from 'react';
import { useNavigate } from 'react-router-dom'; 
import "./Navbar.jsx"
import '../styles/Profile.css';
import { decode } from 'jsonwebtoken'; 
import axios from 'axios';


const Profile = () => {
    const [showPersonalInfo, setShowPersonalInfo] = useState(false);
    const [showServices, setShowServices] = useState(false);
    const navigate = useNavigate(); 
    const [editMode, setEditMode] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [user, setUser] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phoneNumber: '',
        imageUrl: '',
        isProvider: false
    });

    useEffect(() => {
        const jwtToken = localStorage.getItem('Jwt_Token');
    
        if (!jwtToken) {
          navigate('/login');
        } else {
          const decodedToken = decode(jwtToken);
    
          setUser({
            firstName: decodedToken.firstName,
            lastName: decodedToken.lastName,
            email: decodedToken.email,
            phoneNumber: decodedToken.phoneNumber,
            imageUrl: decodedToken.imageUrl,
            isProvider: decodedToken.isProvider, // Set isProvider based on decoded token
          });
    
          // Fetch user's services if they are a provider
          if (decodedToken.isProvider) {
            fetchUserServices();
          }
        }
      }, [navigate]);

      const fetchUserServices = async () => {
        try {
          const response = await axios.get(`http://localhost:3000/v1/services/user/${user.id}`, {
            // Adjust the URL and options as needed for your API
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('Jwt_Token')}`,
            },
          });
    
          if (response.status === 200) {
            const userServices = response.data;
            // Handle the fetched services data as needed
            console.log('User Services:', userServices);
          } else {
            console.error('Failed to fetch user services');
          }
        } catch (error) {
          console.error('Error fetching user services:', error);
        }
      };
    

    const defaultImageUrl = 'https://m.media-amazon.com/images/I/51ZjBEW+qNL._AC_UF894,1000_QL80_.jpg';

    const handleImageUrlChange = (e) => {
        setUser({ ...user, imageUrl: e.target.value });
    };

    const handleInputChange = (e) => {
        setUser({ ...user, [e.target.name]: e.target.value });
    };

    const handleEditToggle = () => {
        setEditMode(!editMode);
      };
      
    const handleChange = (e) => {
        setUser({ ...user, [e.target.name]: e.target.value });
    };
    const handleSaveProfile = () => {
        console.log('Saving profile:', user);
        setEditMode(false);
    };

    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const jwtToken = localStorage.getItem('Jwt_Token');
    
        if (!jwtToken) {
          navigate('/login');
        } else {
          const decodedToken = decode(jwtToken);
              setUser({
            firstName: decodedToken.firstName,
            lastName: decodedToken.lastName,
            email: decodedToken.email,
            phoneNumber: decodedToken.phoneNumber,
            imageUrl: decodedToken.imageUrl,
          });
        }
      }, [navigate]);

    const handlePersonalInfoToggle = () => {
        setShowPersonalInfo(!showPersonalInfo);
        setShowServices(false); 
    };

    const handleServicesToggle = () => {
        setShowServices(!showServices);
        setShowPersonalInfo(false); 
    };

    const handleBecomeProvider = () => {
        setShowServices(false);
        setShowPersonalInfo(false); 
        console.log('Request to become a provider sent');
    };

    const handleEditProfile = () => {
        navigate('/edit-information'); 
    };

    if (isLoading) {
        return <p>Loading...</p>; // Show a loading message while fetching data
      }

    return (
        <div className="profile-container">
            
            <h2 className="profile-title">About me</h2>
            <img 
                src={user.imageUrl || defaultImageUrl} 
                alt="User" 
                className="profile-image"
            />
            <div className="profile-buttons">
                <button onClick={handlePersonalInfoToggle}>Personal Information</button>
                <button onClick={handleServicesToggle}>My Services</button>
                <button onClick={handleBecomeProvider}>Become a Provider</button>
            </div>

            {showPersonalInfo && (
  <div className="personal-info">
    {editMode ? (
      <>
        <div className="input-container">
          <label>
            First Name:
            <input
              type="text"
              name="firstName"
              value={user.firstName}
              onChange={handleInputChange}
            />
          </label>
        </div>
        <div className="input-container">
          <label>
            Last Name:
            <input
              type="text"
              name="lastName"
              value={user.lastName}
              onChange={handleInputChange}
            />
          </label>
        </div>
        <div className="input-container">
          <label>
            Email:
            <input
              type="email"
              name="email"
              value={user.email}
              onChange={handleInputChange}
            />
          </label>
        </div>
        <div className="input-container">
          <label>
            Phone:
            <input
              type="text"
              name="phoneNumber"
              value={user.phoneNumber}
              onChange={handleInputChange}
            />
          </label>
        </div>
        <button className="save-button" onClick={handleSaveProfile}>Save</button>
      </>
    ) : (
      <>
        <p>First Name: {user.firstName}</p>
        <p>Last Name: {user.lastName}</p>
        <p>Email: {user.email}</p>
        <p>Phone: {user.phoneNumber}</p>
        <button className="edit-button" onClick={handleEditToggle}>Edit Information</button>
      </>
    )}
  </div>
)}


            {showServices && (
                <div className="user-services">
                    <p>Services will be listed here...</p>
                </div>
            )}
        </div>
    );
};

export default Profile;