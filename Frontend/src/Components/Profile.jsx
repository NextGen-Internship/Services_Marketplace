import React, { useState, useEffect }  from 'react';
import { useNavigate } from 'react-router-dom'; 
import "./Navbar.jsx"
import '../styles/Profile.css';

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
    const handleSaveProfile = () => {
        console.log('Saving profile:', user);
        setEditMode(false);
    };


    useEffect(() => {
        const fetchUserData = async () => {
            const userData = {
                
            };
            setUser(userData);
        };

        fetchUserData();
    }, []);

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
                            <input type="text" name="firstName" value={user.firstName} onChange={handleInputChange} />
                            <input type="text" name="lastName" value={user.lastName} onChange={handleInputChange} />
                            <input type="email" name="email" value={user.email} onChange={handleInputChange} />
                            <input type="text" name="phoneNumber" value={user.phoneNumber} onChange={handleInputChange} />
                            <button onClick={handleSaveProfile}>Save</button>
                        </>
                    ) : (
                        <>
                            <p>First Name: {user.firstName}</p>
                            <p>Last Name: {user.lastName}</p>
                            <p>Email: {user.email}</p>
                            <p>Phone: {user.phoneNumber}</p>
                            <button onClick={handleEditToggle}>Edit Profile</button>
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