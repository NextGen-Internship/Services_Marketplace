import axios from 'axios';
import config from './config.js';
import isTokenExpired from "../utils/Utils.js";

axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("jwt_token");

    if (token && !config.headers.Authorization) {
      if (isTokenExpired(token)) {
        return Promise.reject({ message: "Token expired" });
      }
      config.headers["Authorization"] = `Bearer ${token}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      console.error("Unauthorized request. Possible token expiry");
    }
    return Promise.reject(error);
  }
);

const apiService = {

  getAllServices: async () => { },
  googleLogin: async () => {
    try {
      const response = await axios.post(
        config.baseUrl + config.googleLogin,
        {},
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      localStorage.setItem("role", response.data.company);
      return response.data;
    } catch (error) {
      console.error("There was an error with Google login", error);
      throw error;
    }
  },
};

const getAllServices = async () => {

  try {
    const response = await axios.get(config.baseUrl + config.getAllServices);
    return response.data;
  } catch (error) {
    console.error("Error fetching services", error);
    throw error;
  }
};

const getAllCategories = async () => {
  try {
    const response = await axios.get(config.baseUrl + config.getAllCategories);
    return response.data;
  } catch (error) {
    console.error("Error fetching categories", error);
    throw error;
  }
};

const getAllCities = async () => {
  try {
    const response = await axios.get(config.baseUrl + config.getAllCities);
    return response.data;
  } catch (error) {
    console.error("Error fetching cities", error);
    throw error;
  }
};

const createService = async (serviceData) => {
  try {
    const response = await axios.post(
      config.baseUrl + config.createService,
      serviceData,
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error creating service", error);
    throw error;
  }
};


const getPaginationServices = async (page, pageSize, sortingField, sortingDirection) => {
  try {
    const response = await axios.get(config.baseUrl + config.getPaginationServices + '/' + page + '/' + pageSize + '/' + sortingField + '/' + sortingDirection);
    return response.data;
  } catch (error) {
    console.error("Error fetching paginated services", error);
    throw error;
  }
};

const getPaginationFilteredServices = async (serviceFilterRequest) => {
  try {
    const queryParams = new URLSearchParams({
      minPrice: serviceFilterRequest.minPrice,
      maxPrice: serviceFilterRequest.maxPrice,
      categoryIds: serviceFilterRequest.categoryIds,
      cityIds: serviceFilterRequest.cityIds,
      page: serviceFilterRequest.page,
      pageSize: serviceFilterRequest.pageSize,
      sortingField: serviceFilterRequest.sortingField,
      sortingDirection: serviceFilterRequest.sortingDirection,
    });

    const response = await axios.get(config.baseUrl + config.getPaginationFilteredServices + '?' + queryParams);

    return response.data;
  } catch (error) {
    console.error("Error fetching filtered paginated services", error);
    throw error;
  }
};

const postLogin = async (formData) => {
  try {
    const response = await axios.post(config.baseUrl + config.postLogin, formData);
    return response.data;
  } catch (error) {
    console.error("Error login", error);
    throw error;
  }
};

const googleLogin = async (googleToken) => {
  try {
    const response = await axios.post(config.baseUrl + config.googleLogin, {
      token: googleToken,
    });
    return response.data;
  } catch (error) {
    console.error("Error login with google", error);
    throw error;
  }
};

const postRegister = async (formData) => {

  try {
    const response = await axios.post(config.baseUrl + config.postRegister, formData);
    return response.data;
  } catch (error) {
    console.error("Error creating registration", error);
    throw error;
  }
};

const getSubscriptionByUserId = async (userId) => {
    try {
      const response = await axios.get(`${config.baseUrl}${config.getSubscriptionByUserId}/${userId}`);
      return response.data;
    } catch (error) {
      console.error("Error fetching user data", error);
      throw error;
    }
  };

const getUserById = async (userId) => {
  try {
    const response = await axios.get(`${config.baseUrl}${config.getUserById}/${userId}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching user data", error);
    throw error;
  }
};


const getCurrentUser = async () => {
  try {
    const response = await axios.get(`${config.baseUrl}${config.getCurrentUser}`,{ headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${localStorage.getItem('Jwt_Token')}`
    }
  });
    return response.data;
  } catch (error) {
    console.error("Error fetching user data", error);
    throw error;
  }
}


const updateUser = async (userId, userData) => {
  try {
    const response = await axios.put(`${config.baseUrl}${config.getUserById}/${userId}`, userData, {
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${localStorage.getItem('Jwt_Token')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error("Error updating user data", error);
    throw error;
  }
};

const updateUserRole = async (userId, newRole) => {
  try {

    const providerRequestBody = { role: newRole };
    const response = await axios.put(`${config.baseUrl}${config.getUserById}/role/${userId}`, providerRequestBody, {
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${localStorage.getItem('Jwt_Token')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error("Error updating user role", error);
    throw error;
  }
};

// const getPicture = async () => {
//   try {
//     const response = await axios.get(config.baseUrl + config.getPicture, 
//       {
//         headers: {
//           "Authorization": `Bearer ${localStorage.getItem('Jwt_Token')}`
//         }
//       }
//     );
//     return response.data;
//   } catch (error) {
//     console.error("Error retrieving user profile picture", error);
//     throw error;
//   }
// };

// const updateCurrentUser = async (formData) => {
//     try {
//       const response = await axios.put(config.baseUrl + config.updateCurrentUser, formData, {
//         headers: {
//           "Content-Type": "multipart/form-data",
//           "Authorization": `Bearer ${localStorage.getItem('Jwt_Token')}`
//         }
//       });
//       return response.data;
//     } catch (error) {
//       console.error("Error uploading user picture", error);
//       throw error;
//     }
//   };

const updateCurrentUser = async (userToUpdate, file) => {
    try {
      const formData = new FormData();
      Object.entries(userToUpdate).forEach(([key, value]) => {
        formData.append(key, value); // Append user update request fields
      });
      formData.append('file', file); // Append the profile picture file
  
      const response = await axios.put(config.baseUrl + config.updateCurrentUser, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          "Authorization": `Bearer ${localStorage.getItem('Jwt_Token')}`
        }
      });
      return response.data;
    } catch (error) {
      console.error("Error uploading user picture", error);
      throw error;
    }
  };

const getServicesByCurrentUser = async() => {
    try {
        const response = await axios.get(`${config.baseUrl}${config.getServicesByCurrentUser}`,{ 
          headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${localStorage.getItem('Jwt_Token')}`
        }
      });
        return response.data;
      } catch (error) {
        console.error("Error fetching my services", error);
        throw error;
      }
}

const getOffersByUser = async () => {
  try {
    const response = await axios.get(`${config.baseUrl}${config.getOffersByUser}`,{ 
      headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${localStorage.getItem('Jwt_Token')}`
    }
  });
    return response.data;
  } catch (error) {
    console.error("Error fetching my offers", error);
    throw error;
  }
}



const updateService = async (serviceId, newServiceData) => {
  try {
    const response = await axios.put(`${config.baseUrl}${config.updateService}/${serviceId}`, newServiceData, {
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${localStorage.getItem('Jwt_Token')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error("Error updating my services", error);
    throw error;
  }
};



const getCategoryById = async (categoryId) => {
  try {
    const response = await axios.get(`${config.baseUrl}${config.getCategoryById}/${categoryId}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching category", error);
    throw error;
  }
};

const getCityById = async (cityId) => {
  try {
    const response = await axios.get(`${config.baseUrl}${config.getCityById}/${cityId}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching city", error);
    throw error;
  }
};

const getServiceById = async (serviceId) => {
  try {
    const response = await axios.get(`${config.baseUrl}${config.getServiceById}/${serviceId}`, {
      headers: {
        "Authorization": `Bearer ${localStorage.getItem('Jwt_Token')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching service by ID", error);
    throw error;
  }
};


const getRequestById = async(requestId) => {
  try {
    const response = await axios.get(`${config.baseUrl}${config.getRequestById}/${requestId}`, {
      headers: {
        "Authorization": `Bearer ${localStorage.getItem('Jwt_Token')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching request by ID", error);
    throw error;
  }
}

const createRequest = async (requestData) => {
  try {
    const response = await axios.post(
      config.baseUrl + config.createRequest,
      requestData,
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error creating request", error);
    throw error;
  }
};
const getRequestByProvider = async () => {
  try {
    const response = await axios.get(`${config.baseUrl}${config.getRequestByProvider}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('Jwt_Token')}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching requests by provider:", error.message);
    throw error;
  }
};

const createOffer = async (offerData) => {
  try {
    const response = await axios.post(
      config.baseUrl + config.createOffer,
      offerData,
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error creating request", error);
    throw error;
  }
};



export {
  getAllServices,
  getAllCategories,
  getAllCities,
  createService,
  getPaginationServices,
  getPaginationFilteredServices,
  postLogin,
  googleLogin,
  postRegister,
  getUserById,
  updateUser,
  updateUserRole,
  getCurrentUser,
  getSubscriptionByUserId,
  updateCurrentUser,
  getServicesByCurrentUser,
  updateService,
  getCategoryById,
  getCityById,
  getServiceById,
  createRequest,
  getRequestByProvider,
  createOffer,
  getOffersByUser,
  getRequestById,



}
export default apiService;


