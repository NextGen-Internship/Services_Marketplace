import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import "./Navbar.jsx"
import '../styles/Profile.css';
import { getUserById, updateUser, updateUserRole, uploadUserPicture, updateUserEmail, getCurrentUser, updateCurrentUser } from '../service/ApiService.js';
import { jwtDecode } from "jwt-decode";
import PhoneInput from 'react-phone-number-input';


const Profile = () => {
  const defaultImageUrl = 'https://res.cloudinary.com/dpfknwlmw/image/upload/v1706630182/dpfknwlmw/nfkmrndg1biotismofqi.webp';

  const [showPersonalInfo, setShowPersonalInfo] = useState(true);
  const [showServices, setShowServices] = useState(false);
  const navigate = useNavigate();
  const [editMode, setEditMode] = useState(false);
  const [previewVisible, setPreviewVisible] = useState(false);
  const [profilePicture, setProfilePicture] = useState(defaultImageUrl);
  const [localFile, setLocalFile] = useState(null);
  const [phoneNumber, setPhoneNumber] = useState('');
  const [validPHoneNumber, setValidPhoneNUmbe] = useState(true);
  const [isEditing, setIsEditing] = useState(false);
  const [user, setUser] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    picture: '',
    role: ''
  });

  const [isEditingPicture, setIsEditingPicture] = useState(false);

  useEffect(() => {
    const fetchCurrentUser = async () => {
      try {
        const token = localStorage.getItem('Jwt_Token');
        if (token) {
          const userDetails = await getCurrentUser();
          setUser(userDetails);
        } else {
          console.error('No token found');
          navigate('/login');
          return;
        }
      } catch (error) {
        console.error('Error fetching current user:', error);
      }
    };

    fetchCurrentUser();
  }, []);

  const handleInputChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  useEffect(() => {
    const fetchPicture = async () => {
      try {
        const currentUser = await getCurrentUser();
        const pictureUrl = currentUser.picture;
        setProfilePicture(pictureUrl);
      } catch (error) {
        console.error('Error fetching user picture:', error);
      }
    };
    fetchPicture();
  }, []);

  const handleSaveProfile = async (e) => {
    const localToken = localStorage.getItem('Jwt_Token');
    if (!localToken) {
      console.error('No token found');
      navigate('/login');
      return;
    }

    const decodedToken = jwtDecode(localToken);

    console.log(decodedToken);

    const userId = decodedToken['jti'];
    console.log(userId);

    if (!userId) {
      console.error('No user email found');
      navigate('/login');
      return;
    }

    /////
    const updatedUserData = {
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      phoneNumber: user.phoneNumber
    };
    console.log(updatedUserData);

    try {
      const updatedUser = await updateCurrentUser(updatedUserData, localFile);
      console.log(updatedUser.picture);
      setProfilePicture(updatedUser.picture);
      console.log('Profile updated successfully:', updatedUser);
      setUser(updatedUser);
      setEditMode(false);
    } catch (error) {
      console.error('Error updating profile:', error);
    }
  };

  // const handleSaveProfile = async (e) => {
  //   e.preventDefault();
  //   // Update user data in the database
  //   try {
  //     const updatedUserData = {
  //       firstName: user.firstName,
  //       lastName: user.lastName,
  //       email: user.email,
  //       phoneNumber: user.phoneNumber
  //     };
  //     const updatedUser = await updateUser(userId, updatedUserData);
  //     setUser(updatedUser);
  //     setEditMode(false);
  
  //     // Check if a new picture file was selected
  //     if (localFile) {
  //       const uploadPictureData = new FormData();
  //       uploadPictureData.append('file', localFile);
  //       uploadPictureData.append('entityType', 'USER');
  
  //       // Upload the new picture to Cloudinary
  //       const imageUrl = await uploadUserPicture(uploadPictureData);
  //       console.log('Profile picture updated successfully:', imageUrl);
  //       setProfilePicture(imageUrl); // Update the profile picture in the UI
  //     }
  //   } catch (error) {
  //     console.error('Error updating profile:', error);
  //   }
  // };

  const handleImageChange = async (e) => {
    const file = e.target.files[0];
    setLocalFile(file);

    if (file) {
      try {
        const localToken = localStorage.getItem('Jwt_Token');
        if (!localToken) {
          console.error('No token found');
          navigate('/login');
          return;
        }

        const decodedToken = jwtDecode(localToken);
        const userId = decodedToken['jti'];
        if (!userId) {
          console.error('No user ID found');
          navigate('/login');
          return;
        }

        setPreviewVisible(true);
        setIsEditingPicture(false);
        console.log('Profile picture updated successfully');
      } catch (error) {
        console.error('Error updating profile picture:', error);
      }
    }
  };


  const handleBecomeProvider = async (newRole) => {
    setShowServices(false);
    setShowPersonalInfo(false);
    console.log('Request to become a provider sent');

    const localToken = localStorage.getItem('Jwt_Token');
    if (!localToken) {
      console.error('No token found');
      navigate('/login');
      return;
    }

    const decodedToken = jwtDecode(localToken);
    const userId = decodedToken['jti'];
    if (!userId) {
      console.error('No user ID found');
      navigate('/login');
      return;
    }

    try {
      const response = await updateUserRole(userId, newRole);
      console.log("Request data:", { userId, newRole });

      console.log('User role updated successfully:', response);

      setUser(prevUser => ({ ...prevUser, role: newRole }));

    } catch (error) {
      console.error('Error updating user role:', error);
    }
  };

  const handlePhoneChange = (phone) => {
    setPhoneNumber(phone);
    setUser(current => ({ ...current, phoneNumber: phone }));
  };

  useEffect(() => {
    const getPictureMethod = async () => {
      const currentUser = await getCurrentUser();
      const picUrl = currentUser.picture;
      console.log('polled url', picUrl);
      setProfilePicture(picUrl);
      setUser(prevUser => ({ ...prevUser, picture: picUrl }));
    };

    const fetchUserData = async () => {
      const localToken = localStorage['Jwt_Token'];

      if (!localToken) {
        console.error('No user found');
        navigate('/login');
        return;
      }

      try {
        const userData = await getCurrentUser();
        setUser(userData);
        setPhoneNumber(userData.phoneNumber);
        console.log('User data:');
        console.log(userData);

        if (user.picture !== defaultImageUrl) {
          console.log("custom avatar!")
          await getPictureMethod();
          console.log('User Avatar img: ', user.picture)
          console.log(profilePicture);
        }
        else {
          console.log("default");
        }

      } catch (error) {
        console.error('Error fetching user data:', error);
      }
    };
    console.log('Fetched user role:', user.role);

    fetchUserData();
  }, [profilePicture]);

  const becomeProviderButton = user.role !== 'provider' && (
    <button onClick={() => handleBecomeProvider('provider')}>Become a Provider</button>
  );

  const handlePersonalInfoToggle = () => {
    setShowPersonalInfo(!showPersonalInfo);
    setShowServices(false);
    setPreviewVisible(false);
  };

  const handleServicesToggle = () => {
    if (user.role !== 'provider') {
      console.log('Only providers can see their services');
      return;
    }
    setShowServices(!showServices);
    setShowPersonalInfo(false);
    setPreviewVisible(false);

  }

  return (
    <div className="profile-container">

      <h2 className="profile-title">About me</h2>
      {previewVisible ? (
        <img
        src={localFile}
        alt="User"
        className="profile-image"
      />
      ) : (
      <img
        src={user.picture || profilePicture || defaultImageUrl}
        alt="User"
        className="profile-image"
      />
      )
}
      <div className="profile-buttons">
        <button onClick={handlePersonalInfoToggle}>Personal Information</button>
        {user.role === 'provider' && (
          <button onClick={handleServicesToggle}>My Services</button>
        )}        {becomeProviderButton}
      </div>
      {isEditingPicture && (
        <div className="profile-picture-edit">
          <input type="file" onChange={handleImageChange} accept="image/*" />
          {user.picture && (
            <img src={user.picture} alt="Profile Preview" className="profile-preview-image" />
          )}
        </div>
      )}
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
                <input type="phone" name="phoneNumber" value={user.phoneNumber} onChange={handleInputChange} />
              </div>
              {/* <div className="input-group">
                <label>Phone:</label>
                <PhoneInput
                  international
                  countryCallingCodeEditable={false}
                  defaultCountry="BG"
                  value={phoneNumber}
                  onChange={handlePhoneChange}
                  name="phoneNumber"
                />
              </div> */}
              <div className="input-group">
                <label>Profile Picture:</label>
                <input type="file" onChange={handleImageChange} accept="image/*" />
                {user.imageUrl && (
                  <img src={user.picture} alt="Profile Preview" className="profile-preview-image" />
                )}
              </div>
              <button className='save-button' onClick={handleSaveProfile}>Save</button>
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