import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import "./Navbar.jsx"
import '../styles/Profile.css';
import SubscriptionComponent from './SubscriptionComponent.jsx';
import axios from 'axios';
import '../styles/ServicesPage.css';
import { getUserById, updateUser, updateUserRole, getCurrentUser, getServicesByCurrentUser, updateService, getAllCategories, getAllCities, updateCurrentUser, getSubscriptionByUserId, getRequestByProvider, getOffersByUser } from '../service/ApiService.js';
import { jwtDecode } from "jwt-decode";
import MyServicesModal from './MyServicesModal';
import ReactPaginate from 'react-paginate';
import { FaRegEdit } from "react-icons/fa";
import Multiselect from 'multiselect-react-dropdown';
import RequestsBox from './RequestsBox.jsx';
import { OfferBox } from './OfferBox.jsx';

const Profile = () => {
  const defaultImageUrl = 'https://t4.ftcdn.net/jpg/00/64/67/63/360_F_64676383_LdbmhiNM6Ypzb3FM4PPuFP9rHe7ri8Ju.jpg';
  const [chosen, setChosen] = useState([]);
  const [showPersonalInfo, setShowPersonalInfo] = useState(true);
  const [showServices, setShowServices] = useState(false);
  const [showRequest, setShowRequest] = useState(false);
  const [showOffers, setShowOffers] = useState(false);
  const [showBecomeProviderForm, setShowBecomeProviderForm] = useState(false)
  const navigate = useNavigate();
  const [userServices, setUserServices] = useState([]);
  const [userRequest, setUserRequest] = useState([]);
  const [userOffer, setUserOffer] = useState(true);
  const [showReviews, setShowReviews] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [previewVisible, setPreviewVisible] = useState(false);
  const [profilePicture, setProfilePicture] = useState(defaultImageUrl);
  const [localFile, setLocalFile] = useState(null);
  const [phoneNumber, setPhoneNumber] = useState('');
  const [categories, setCategories] = useState([]);
  const [validPHoneNumber, setValidPhoneNUmbe] = useState(true);
  const [areMyServicesVisible, setAreMyServicesVisible] = useState(false);
  const [areMyRequestVisible, setAreMyRequestVisible] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [cities, setCities] = useState([]);
  const [beecomeProviderBtn, setBecomeProviderBtn] = useState(true);
  const [servicesPerPage] = useState(5);
  const [paginatedServices, setPaginatedServices] = useState([]);
  const [paginatedRequest, setPaginatedRequest] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [selectedCities, setSelectedCities] = useState([]);
  const [isEditing, setIsEditing] = useState(false);
  const [subscriptionId, setSubscriptionId] = useState('');
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [user, setUser] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    picture: '',
    roles: [],
    stripeAccountId: ''
  });
  const [editableService, setEditableService] = useState({
    id: 0,
    title: '',
    description: '',
    price: '',
    categoryId: '',
    cityIds: [],
    providerId: 0,
    serviceStatus: '',
  });
  const [formData, setFormData] = useState({
    email: '',
    firstMiddleName: '',
    lastName: '',
    dateOfBirth: '',
    phoneNumber: '',
    address: '',
    city: '',
    postalCode: '',
    iban: '',
  });
  const [serviceBoxIdToEdit, setServiceBoxIdToEdit] = useState(-1);
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

  const handleProviderInfoChange = (event) => {
    const { name, value } = event.target;
    setFormData({
      ...formData,
      [name]: value
    });
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

    const updatedUserData = {
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      phoneNumber: user.phoneNumber,
      roles: user.roles,
      stripeAccountId: user.stripeAccountId
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
    const getPictureMethod = async () => {
      const currentUser = await getCurrentUser();
      const picUrl = currentUser.picture;
      console.log('polled url', picUrl);
      setProfilePicture(picUrl);
      setUser(prevUser => ({ ...prevUser, picture: picUrl }));
    };
  }, []);

  useEffect(() => {
    const fetchCities = async () => {
      try {
        const fetchedCities = await getAllCities();
        setCities(fetchedCities);
      } catch (error) {
        console.error('Error fetching cities:', error);
      }
    };

    fetchCities();
  }, []);

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
  }, [navigate]);

  const handleBecomeProviderToggle = () => {
    setShowBecomeProviderForm(!showBecomeProviderForm);
    setShowServices(false);
    setPreviewVisible(false);
    setShowPersonalInfo(false);
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
    console.log('Fetched user role:', user.roles);

    fetchUserData();
  }, [profilePicture]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const fetchedCategories = await getAllCategories();
        setCategories(fetchedCategories);
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    };

    fetchCategories();
  }, []);

  const becomeProviderButton = !isProvider(user) && (
    <button onClick={() => handleBecomeProviderToggle()}>Become a Provider</button>
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
    setShowBecomeProviderForm(false);
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

  const handleOffersToggle = async () => {
    const offer = await getOffersByUser();
    setUserOffer(offer);
    setShowOffers(!showOffers);
  }


  const handleRequest = async () => {
    if (!isProvider(user)) {
      console.log('Only providers can see their requests');
      return;
    }
    try {
      const requests = await getRequestByProvider();

      setUserRequest(requests);
      setAreMyRequestVisible(true);
    } catch (error) {
      console.error('Error fetching requests:', error);
    }
    setShowRequest(!showRequest);
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

  const handleAccountCreation = async () => {
    try {
      console.log(formData);
      const response = await axios.post('http://localhost:8080/api/subscribe/createAccount', formData);
      console.log('Stripe account created:', response.data);
    } catch (error) {
      console.error('Error creating Stripe account:', error);
    }
  }

  const cancelSubscription = async (subscriptionId) => {
    try {
      const response = await axios.post(`http://localhost:8080/api/subscribe/cancel/${subscriptionId}`);
      console.log('Stripe subscription cancelled:', response.data);
    } catch (error) {
      console.error('Error cancelling Stripe subscription:', error);
    }
  };

  const fetchSubscription = async (userId) => {
    try {
      const response = await getSubscriptionByUserId(userId);
      console.log(response);
      setSubscriptionId(response.stripeId);
    } catch (error) {
      console.error('Error fetching subscription data:', error);
    }
  };

  const handleSubscriptionCancel = async () => {
    if (subscriptionId) {
      cancelSubscription(subscriptionId);
    } else {
      console.error('No subscription ID found');
    }
  };

  useEffect(() => {
    const localToken = localStorage.getItem('Jwt_Token');
    if (!localToken) {
      console.error('No token found');
      navigate('/login');
      return null;
    }

    const decodedToken = jwtDecode(localToken);
    const userId = decodedToken['jti'];
    if (!userId) {
      console.error('No user ID found');
      navigate('/login');
      return null;
    }

    fetchSubscription(userId);
  }, []);

  const saveServiceBox = async () => {
    try {
      const updatedService = await updateService(editableService.id, editableService);
      console.log('Service updated successfully:', updatedService);
      const updatedServices = userServices.map((service) =>
        service.id === editableService.id ? { ...service, ...editableService } : service
      );
      setUserServices(updatedServices);
      setServiceBoxIdToEdit(-1);
    } catch (error) {
      console.error('Error updating service:', error);
    }
  };



  const editServiceBox = (serviceId) => {
    const serviceToEdit = userServices.find(service => service.id === serviceId);
    if (serviceToEdit) {
      setServiceBoxIdToEdit(serviceId);
      setEditableService({
        id: serviceToEdit.id,
        title: serviceToEdit.title,
        description: serviceToEdit.description,
        price: serviceToEdit.price,
        categoryId: serviceToEdit.categoryId,
        cityIds: serviceToEdit.cityIds || [],
        serviceStatus: 'ACTIVE',
      });
    }
  };

  const handleServiceChange = (e, fieldName) => {
    if (fieldName === 'cityIds') {
      const selectedCityIds = e.map(option => option.id);
      setEditableService(prevState => ({
        ...prevState,
        cityIds: selectedCityIds
      }));
    } else {
      setEditableService((prevState) => ({
        ...prevState,
        [fieldName]: e.target.value,
      }));
    }
  };

  const showRequestsButton = (
    <button onClick={handleRequest}>Requests</button>
  );
  // const handleRequestDetails = (request) => {
  //   setSelectedRequest(request);
  //   // You can perform additional actions here if needed
  // };

  console.log(editableService);


  const renderServiceBox = (service) => {
    const isEditing = serviceBoxIdToEdit === service.id;

    return (
      <div key={service.id} className="service-box-profile">
        <div className="service-info">
          {isEditing ? (
            <>
              <label htmlFor="title">Title:</label>
              <input type="text" value={editableService.title} onChange={(e) => handleServiceChange(e, 'title')} />
              <label htmlFor="price">Price:</label>
              <input type="text" value={editableService.price} onChange={(e) => handleServiceChange(e, 'price')} />
              <label htmlFor="description">Description:</label>
              <textarea value={editableService.description} onChange={(e) => handleServiceChange(e, 'description')} />
              <label htmlFor="cityIds">Cities:</label>
              <Multiselect
                options={cities}
                selectedValues={chosen}
                onSelect={(selectedList) => { handleServiceChange(selectedList, 'cityIds'); setChosen(selectedList) }}
                onRemove={(selectedList) => { handleServiceChange(selectedList, 'cityIds'); setChosen(selectedList) }}
                displayValue='name'
              />
              <label htmlFor="categoryId">Category:</label>
              <select
                value={editableService.categoryId}
                onChange={(e) => setEditableService(prev => ({ ...prev, categoryId: e.target.value }))}
              >
                <option value="">Select a Category</option>
                {categories.map(category => (
                  <option key={category.id} value={category.id}>{category.name}</option>
                ))}
              </select>
              <button onClick={saveServiceBox}>Save</button>
              <button onClick={() => setServiceBoxIdToEdit(-1)}>Cancel</button>
            </>
          ) : (
            <>
              <h3>{service.title}</h3>
              <p>Price: {service.price} BGN.</p>
              <p>{service.description}</p>
              <button onClick={() => editServiceBox(service.id)}><FaRegEdit /></button>
            </>
          )}
        </div>
      </div>
    );
  };

  return (
    <div className="profile-container">

      <h2 className="profile-title">About me</h2>
      {previewVisible ? (
        <img
          src={localFile ? URL.createObjectURL(localFile) : defaultImageUrl}
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
        {isProvider(user) && (
          <button onClick={handleServicesToggle}>My Services</button>
        )}        {becomeProviderButton}
        {isProvider(user) && showRequestsButton}
        <button onClick={handleOffersToggle}>Offers</button>
        <div></div>
      </div>
      {isProvider(user) &&
        (<div className="provider-info">
          <button className='save-button' onClick={handleSubscriptionCancel} >Cancel Subscription</button>
        </div>)
      }
      {isEditingPicture && (
        <MyServicesModal
          isOpen={isModalVisible}
          onClose={() => setIsModalVisible(false)}
          services={userServices}
        />
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
      {showBecomeProviderForm && (
        <div className="provider-info">
          {!user.stripeAccountId && (
            <div>
              <div className="input-group">
                <label>First and Middle:</label>
                <input type="text" name="firstMiddleName" onChange={handleProviderInfoChange} />
              </div>
              <div className="input-group">
                <label>Last Name:</label>
                <input type="text" name="lastName" onChange={handleProviderInfoChange} />
              </div>
              <div className="input-group">
                <label>Date of birth:</label>
                <input type="date" name="dateOfBirth" onChange={handleProviderInfoChange} />
              </div>
              <div className="input-group">
                <label>Phone number:</label>
                <input type="text" name="phoneNumber" onChange={handleProviderInfoChange} />
              </div>
              <div className="input-group">
                <label>Email:</label>
                <input type="email" name="email" onChange={handleProviderInfoChange} />
              </div>
              <div className="input-group">
                <label>Address:</label>
                <input type="address" name="address" onChange={handleProviderInfoChange} />
              </div>
              <div className="input-group">
                <label>City:</label>
                <input type="city" name="city" onChange={handleProviderInfoChange} />
              </div>
              <div className="input-group">
                <label>Postal code:</label>
                <input type="Postal code" name="postalCode" onChange={handleProviderInfoChange} />
              </div>
              <div className="input-group">
                <label>IBAN:</label>
                <input type="text" name="iban" onChange={handleProviderInfoChange} />
              </div>
            </div>)
          }
          <div>
            <SubscriptionComponent handleAccountCreation={!user.stripeAccountId ? handleAccountCreation : undefined} />
          </div>
        </div>
      )
      }
      {showServices && (
        <div className="user-services-profile">
          {paginatedServices.length > 0 ? paginatedServices.map(renderServiceBox) : 'No Services to Show'}
          <div className="pagination-controls-profile">
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
      {(showRequest && isProvider) && (
        <div className="user-requests-profile">
          {userRequest.length > 0 ? (
            userRequest.map((request, index) => (
              <RequestsBox key={index} request={request} />
            ))
          ) : (
            'No reviews to show'
          )}
        </div>
      )}
      {showOffers && (
        <div className="user-offers-profile">
          {userOffer.length > 0 ? (
            userOffer.map((offer, index) => (
              <OfferBox key={index} offer={offer} />
            ))
          ) : (
            'No offers to show'
          )}
        </div>
      )}
    </div>
  );
};
export default Profile;