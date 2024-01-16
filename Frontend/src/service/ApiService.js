import config from './config';
import axios from 'axios';

const apiService = axios.create({
  baseURL: config.baseUrl,
});

apiService.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwt_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

apiService.interceptors.response.use(
  (response) => {
    // TODO
    // You can add your response logic here
    return response;
  },
  async (error) => {
    const originalRequest = error.config;
    return Promise.reject(error);
  }
);

// Function to refresh the access token (Implement according to your backend logic)
const refreshAccessToken = async () => {
  // Implement your token refresh logic here
  // Example: Make a request to the server to get a new access token
  const response = await axios.post('https://your-api-base-url.com/refresh-token', {
    // Include any necessary data for token refresh
  });

  return response.data.newAccessToken;
};

const getAllServices = async () => {

}

export {
    getAllServices,
} 
export default apiService;
