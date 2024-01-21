import React, { useState, useEffect }  from 'react';
import { useNavigate } from 'react-router-dom'; 
import "./Navbar.jsx"
import '../styles/Profile.css';
import { getUserById, updateUser, getUserByIdTest } from '../service/ApiService.js';
import {jwtDecode} from "jwt-decode";

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
        imageUrl: '' 
    });

    const defaultImageUrl = 'https://m.media-amazon.com/images/I/51ZjBEW+qNL._AC_UF894,1000_QL80_.jpg';

    const handleImageUrlChange = (e) => {
        setUser({ ...user, imageUrl: e.target.value });
    };

    const handleInputChange = (e) => {
        setUser({ ...user, [e.target.name]: e.target.value });
    };

    const handleEditToggle = () => setIsEditing(!isEditing);

    const handleChange = (e) => {
        setUser({ ...user, [e.target.name]: e.target.value });
    };
    const handleSaveProfile = async () => {
        const localToken = localStorage.getItem('Jwt_Token');
        if (!localToken) {
            console.error('No token found');
            navigate('/login');
            return;
        }
    
        const decodedToken = jwtDecode(localToken);
        const userId = decodedToken['jti'];
        //console.log(userId);
    
        if (!userId) {
            console.error('No user ID found');
            navigate('/login');
            return;
        }
    
        const updatedUserData = {
            firstName: user.firstName,
            lastName: user.lastName,
            email: user.email,
            phoneNumber: user.phoneNumber,
        };
        //console.log(updatedUserData);
    
        try {
            const updatedUser = await updateUser(userId, updatedUserData);
            console.log('Profile updated successfully:', updatedUser);
            setUser(updatedUser); 
            setEditMode(false); 
        } catch (error) {
            console.error('Error updating profile:', error);
        }
    };
    


    useEffect(() => {
        const fetchUserData = async () => {
            const localToken = localStorage['Jwt_Token'];
            const decodedToken = jwtDecode(localToken);
            const userId = decodedToken['jti'];

            if (!userId) {
                console.error('No user ID found');
                navigate('/login');
                return;
            }
    
            try {
                const userData = await getUserById(userId);
                const testData = await getUserByIdTest(userId);
                console.log('User data:');
                console.log(userData);
                setUser(userData);
            } catch (error) {
                console.error('Error fetching user data:', error);
                // Optionally, handle errors, like showing a message to the user
            }
        };
    
        fetchUserData();
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
                <div className="input-group">
                    <label>First Name:</label>
                    <input type="text" name="firstName" value={user.firstName} onChange={handleInputChange} />
                </div>
                <div className="input-group">
                    <label>Last Name:</label>
                    <input type="text" name="lastName" value={user.lastName} onChange={handleInputChange} />
                </div>
                <div className="input-group">
                    <label>Email:</label>
                    <input type="email" name="email" value={user.email} onChange={handleInputChange} />
                </div>
                <div className="input-group">
                    <label>Phone:</label>
                    <input type="text" name="phoneNumber" value={user.phoneNumber} onChange={handleInputChange} />
                </div>
                <button className='save-button'onClick={handleSaveProfile}>Save</button>
            </>
        ) : (
            <>
                <p>First Name: {user.firstName}</p>
                <p>Last Name: {user.lastName}</p>
                <p>Email: {user.email}</p>
                <p>Phone: {user.phoneNumber}</p>
                <button className='edit-button' onClick={() => setEditMode(true)}>Edit Information</button>
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