import axios from "axios";
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

const postLogin = async (formData) => {
  try {
    const response = await axios.post(config.baseUrl + config.postLogin, formData);
    return response.data;
  } catch (error) {
    console.error("Error fetching services", error);
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
    console.error("Error fetching services", error);
    throw error;
  }
};

const postRegister = async (formData) => {
  try {
    const response = await axios.post(config.baseUrl + config.postRegister, formData);
    return response.data;
  } catch (error) {
    console.error("Error fetching services", error);
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

const getUserByIdTest = async (userId) => {
  try {
    const response = await axios.get(`${config.baseUrl}${config.getUserById}/${userId}`).then(e => {
      console.log("REsponse test: ", e)
    })
  } catch (error) {
    console.error("Error fetching user data", error);
    throw error;
  }
};


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

export {
  getAllServices,
  getAllCategories,
  getAllCities,
  createService,
  postLogin,
  googleLogin,
  postRegister,
  getUserById,
  updateUser,
  getUserByIdTest,
  updateUserRole
}
export default apiService;
