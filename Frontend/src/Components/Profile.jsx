import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import "./Navbar.jsx"
import '../styles/Profile.css';
import '../styles/ServicesPage.css';
import { getUserById, updateUser, updateUserRole, uploadUserPicture, getPicture, updateUserEmail, getCurrentUser, getServicesByCurrentUser } from '../service/ApiService.js';
import { jwtDecode } from "jwt-decode";
import PhoneInput from 'react-phone-number-input';
import MyServicesModal from './MyServicesModal';
import ReactPaginate from 'react-paginate';


const Profile = () => {
  //const defaultImageUrl = 'https://m.media-amazon.com/images/I/51ZjBEW+qNL._AC_UF894,1000_QL80_.jpg';

  const [showPersonalInfo, setShowPersonalInfo] = useState(true);
  const [showServices, setShowServices] = useState(false);
  const navigate = useNavigate();
  const [userServices, setUserServices] = useState([]);
  const [editMode, setEditMode] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [previewVisible, setPreviewVisible] = useState(false);
  //const [profilePicture, setProfilePicture] = useState(defaultImageUrl);
  const [localFile, setLocalFile] = useState(null);
  const [phoneNumber, setPhoneNumber] = useState('');
  const [validPHoneNumber, setValidPhoneNUmbe] = useState(true);
  const [areMyServicesVisible, setAreMyServicesVisible] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [beecomeProviderBtn, setBecomeProviderBtn] = useState(true);
  const [servicesPerPage] = useState(5); // or any number you prefer
  const [paginatedServices, setPaginatedServices] = useState([]);  
  const [totalPages, setTotalPages] = useState(0);
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

  const handleImageUrlChange = (e) => {
    setUser({ ...user, imageUrl: e.target.value });
  };

  const handleInputChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const handleChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };
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

    const updatedUserData = {
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      phoneNumber: user.phoneNumber
    };
    console.log(updatedUserData);

    try {
      const updatedUser = await updateUser(userId, updatedUserData);
      console.log('Profile updated successfully:', updatedUser);
      setUser(updatedUser);
      setEditMode(false);
    } catch (error) {
      console.error('Error updating profile:', error);
    }
  };

  const handleImageChange = async (e) => {
    const file = e.target.files[0];
    setLocalFile(file);
    // if (file) {
    //   try {
    //     const localToken = localStorage.getItem('Jwt_Token');
    //     if (!localToken) {
    //       console.error('No token found');
    //       navigate('/login');
    //       return;
    //     }

    //     const decodedToken = jwtDecode(localToken);
    //     const userId = decodedToken['jti'];
    //     if (!userId) {
    //       console.error('No user ID found');
    //       navigate('/login');
    //       return;
    //     }

    //     const imageUrl = await uploadUserPicture(userId, file);
    //     setPreviewVisible(true);
    //     setIsEditingPicture(false);
    //     setProfilePicture(imageUrl);
    //     setUser(prevUser => ({ ...prevUser, imageUrl: imageUrl }));
    //     console.log('Profile picture updated successfully');
    //   } catch (error) {
    //     console.error('Error updating profile picture:', error);
    //   }
    // }
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
      localStorage.setItem('userRole', newRole);
      if (newRole === 'provider') {
        setShowPersonalInfo(true);
        setShowServices(true);
        setBecomeProviderBtn(false);
      }

      //if(response.newToken) {
      // localStorage.setItem('Jwt_Token', response.newToken);
    } catch (error) {
      console.error('Error updating user role:', error);
    }
  };

  const handlePhoneChange = (phone) => {
    setPhoneNumber(phone);
    setUser(current => ({ ...current, phoneNumber: phone }));
  };

  useEffect(() => {
    // const getPictureMethod = async () => {
    //   const localToken = localStorage['Jwt_Token'];
    //   const decodedToken = jwtDecode(localToken);
    //   const userId = decodedToken['jti'];

    //   const picUrl = await getPicture(userId);
    //   console.log('polled url', picUrl);
    //   setProfilePicture(picUrl);
    //   setUser(prevUser => ({ ...prevUser, imageUrl: picUrl }));
    // };

    const fetchUserData = async () => {
      const localToken = localStorage['Jwt_Token'];
      const decodedToken = jwtDecode(localToken);
      const userId = decodedToken['jti'];
      //const userEmail = decodedToken['sub'];

      if (!userId) {
        console.error('No user ID found');
        navigate('/login');
        return;
      }

      try {
        //const userData = await getUserById(userId);
        const userData = await getCurrentUser();
        //setUser(userData);
        //setPhoneNumber(userData.phoneNumber);
        console.log('User data:');  
        console.log(userData);
        const savedRole = localStorage.getItem('userRole');

    if (savedRole === userData.role) {
      setUser(prevUser => ({ ...prevUser, ...userData, role: savedRole }));
    } else {
      setUser(prevUser => ({ ...prevUser, ...userData }));
    }

        // if (user.imageUrl !== defaultImageUrl) {
        //   console.log("custom avatar!")
        //   await getPictureMethod();
        //   console.log('User Avatar img: ', user.imageUrl)
        //   console.log(profilePicture);
        //   //setUser(({ ...user, imageUrl: profilePicture }));
        // }
        // else {
        //   console.log("default");
        // }

        

      console.log('Fetched user role:', user.role);

      if (savedRole && savedRole === userData.role) {
        setUser(prevUser => ({ ...prevUser, ...userData, role: savedRole }));
      } else {
        setUser(prevUser => ({ ...prevUser, ...userData }));
      } 

      } catch (error) {
        console.error('Error fetching user data:', error);
      }
    };

    fetchUserData();
  }, [navigate]);

  console.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
  console.log(user.role);

  const becomeProviderButton = (user.role !== 'provider') && (
    <button onClick={() => handleBecomeProvider('provider')}>Become a Provider</button>
  );

  const handleEditPictureToggle = () => {
    setIsEditingPicture(!isEditingPicture);
    setEditMode(false);
    setPreviewVisible(false);
  };


  const handlePersonalInfoToggle = () => {
    setShowPersonalInfo(!showPersonalInfo);
    setShowServices(false);
    setPreviewVisible(false);
  };

  const handleServicesToggle = async () => {
    if (user.role !== 'provider') {
      console.log('Only providers can see their services');
      return;
    }
    try {
      const services = await getServicesByCurrentUser(); 
      setUserServices(services);
      const indexOfLastService = (currentPage + 1) * servicesPerPage;
      const indexOfFirstService = indexOfLastService - servicesPerPage;
      setPaginatedServices(services.slice(indexOfFirstService, indexOfLastService));
      setTotalPages(Math.ceil(services.length / servicesPerPage));
      setAreMyServicesVisible(true);
    } catch (error) {
      console.error('Error fetching services:', error);
    }
    setShowServices(!showServices);
    setShowPersonalInfo(false);
    setPreviewVisible(false);
};

const handlePageChange = (selectedPage) => {
  setCurrentPage(selectedPage);
  const indexOfLastService = (selectedPage + 1) * servicesPerPage;
  const indexOfFirstService = indexOfLastService - servicesPerPage;
  setPaginatedServices(userServices.slice(indexOfFirstService, indexOfLastService));
};

  const handleEditProfile = () => {
    navigate('/edit-information');
  };

  const renderServiceBox = (service) => {
    return (
      <div key={service.id} className="service-box">
        <div className="service-info">
          <h3>{service.title}</h3>
          <p> {service.price}</p>
          <p>{service.description}</p>
        </div>
      </div>
    );
  };

  return (
    <div className="profile-container">

      <h2 className="profile-title">Profile</h2>
      {/* <img
        src={user.picture || profilePicture}
        alt="User"
        className="profile-image"
      /> */}
      <div className="profile-buttons">
        <button onClick={handlePersonalInfoToggle}>Personal Information</button>
        {user.role === 'provider' && (
        <button onClick={handleServicesToggle}>My Services</button>
  )}        {becomeProviderButton}
      </div>
      <MyServicesModal 
                isOpen={isModalVisible} 
                onClose={() => setIsModalVisible(false)} 
                services={userServices} 
            />
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
              {/* <div className="input-group">
                <label>Profile Picture:</label>
                <input type="file" onChange={handleImageChange} accept="image/*" />
                {user.imageUrl && (
                  <img src={user.imageUrl} alt="Profile Preview" className="profile-preview-image" />
                )}
              </div> */}
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
          {paginatedServices.length > 0 ? paginatedServices.map(renderServiceBox) : 'No Services to Show'}
          <div className="pagination-controls">
            <ReactPaginate
              pageCount={totalPages}
              pageRangeDisplayed={2}
              marginPagesDisplayed={1}
              onPageChange={({ selected }) => handlePageChange(selected)}
              containerClassName="pagination"
              activeClassName="active"
              initialPage={currentPage}
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default Profile;