import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import "./Navbar.jsx"
import '../styles/Profile.css';
import '../styles/ServicesPage.css';
import { getUserById, updateUser, updateUserRole, uploadUserPicture, getPicture, updateUserEmail, getCurrentUser, getServicesByCurrentUser, updateService } from '../service/ApiService.js';
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
    //picture: '',
    roles: []
  });

  const [serviceBoxIdToEdit, setServiceBoxIdToEdit] = useState(-1);

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
      phoneNumber: user.phoneNumber,
      role: user.role
    };
    console.log(updatedUserData);

    try {
      const updatedUser = await updateUser(userId, user);
      console.log('Profile updated successfully:', updatedUser);
      setUser(updatedUser);
      setEditMode(false);
    } catch (error) {
      console.error('Error updating profile:', error);
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

      //setUser(prevUser => ({ ...prevUser, roles: prevUser.roles.push() }));
      setUser(await getCurrentUser())
      console.log('Updated role:', newRole);
      console.log('User role:', user.roles);

      if (isProvider(user)) {
        setShowPersonalInfo(true);
        setShowServices(true);
        setBecomeProviderBtn(false);
      }

    } catch (error) {
      console.error('Error updating user role:', error);
    }
  };

  

  const handlePhoneChange = (phone) => {
    setPhoneNumber(phone);
    setUser(current => ({ ...current, phoneNumber: phone }));
  };

  const isProvider = (usr) => {
    return Array.isArray(usr.roles) && usr.roles.some(role => role.authority === 'PROVIDER');
  }

  useEffect(() => {
    const fetchUserData = async () => {
      const localToken = localStorage['Jwt_Token'];
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
        const updatedUserData = await getCurrentUser();
        console.log('User data:', updatedUserData);
        setUser(updatedUserData);
  
        if (isProvider(updatedUserData)) {
          setShowPersonalInfo(true);
          setBecomeProviderBtn(false);
        } 
        
      } catch (error) {
        console.error('Error fetching user data:', error);
      }
    };
  
    fetchUserData();
  }, [navigate], [user.roles]);
  
  console.log("@@@@@@@@@@@@@@@@@@");
  console.log(user.roles);
  const becomeProviderButton = (user.roles == null || !isProvider(user)) && (
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
    if (!isProvider(user)) {
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

  // call this when you want to save the render service box
  const saveServiceBox = (service) => {
    updateService(service);
  }

  const editServiceBox = (service) => {
    setServiceBoxIdToEdit(service.id);
    console.log('Edit Mode ON FOR ' + service.id);
  }

  const handleServiceChange = (e, serviceId, fieldName) => {
    // Create a new array with updated service information
    const updatedServices = userServices.map(service => {
      if (service.id === serviceId) {
        return { ...service, [fieldName]: e.target.value };
      }
      return service;
    });
  
    // Update the userServices state with the new array
    setUserServices(updatedServices);
  };
  

  const renderServiceBox = (service) => {
    if (serviceBoxIdToEdit === service.id) {
      return (
        <div key={service.id} className="service-box">
          <div className="service-info">
            <label htmlFor="title">Title:</label>
            <input
              id="title"
              type="text"
              value={service.title}
              onChange={(e) => handleServiceChange(e, service.id, 'title')}
            />
            <label htmlFor="price">Price:</label>
            <input
              id="price"
              type="text"
              value={service.price}
              onChange={(e) => handleServiceChange(e, service.id, 'price')}
            />
            <label htmlFor="description">Description:</label>
            <textarea
              id="description"
              value={service.description}
              onChange={(e) => handleServiceChange(e, service.id, 'description')}
            />
            <button onClick={() => saveServiceBox(service)}>Save</button>
            <button onClick={() => setServiceBoxIdToEdit(-1)}>Cancel</button>
          </div>
        </div>
      );
    } else {
      return (
        <div key={service.id} className="service-box">
          <div className="service-info">
            <h3>{service.title}</h3>
            <p>{service.price}</p>
            <p>{service.description}</p>
            <button onClick={() => setServiceBoxIdToEdit(service.id)}>Edit</button>
          </div>
        </div>
      );
    }
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
        {isProvider(user) && (
        <button onClick={handleServicesToggle}>My Services</button>
  )}        {becomeProviderButton}
      </div>
      <MyServicesModal 
                isOpen={isModalVisible} 
                onClose={() => setIsModalVisible(false)} 
                services={userServices} 
            />
      {/* {isEditingPicture && (
        <div className="profile-picture-edit">
          <input type="file" onChange={handleImageChange} accept="image/*" />
          {user.picture && (
            <img src={user.picture} alt="Profile Preview" className="profile-preview-image" />
          )}
        </div>
      )} */}
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