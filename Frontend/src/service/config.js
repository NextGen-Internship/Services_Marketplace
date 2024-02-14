const config = {
    baseUrl: 'http://localhost:8080',
    getAllServices: '/v1/services/all',
    getAllCategories: '/v1/categories',
    getAllCities: '/v1/cities/all',
    createService: '/v1/services/create',
    getPaginationServices: '/v1/services/getPaginationServices',
    getPaginationFilteredServices: '/v1/services/filter',
    postLogin: '/api/auth/login',
    googleLogin: '/api/auth/google/login',
    postRegister: '/api/auth/register',
    getUserById: '/v1/users',
    updateUser: '/v1/users',
    updateUserRole: '/v1/users/role',
    getUsers: '/v1/users',
    updateCurrentUser: '/v1/users/update/current',
    uploadUserPicture: '/file/upload',
    getPicture: '/file/getPicture',
    getCurrentUser: '/v1/users/current',
    getSubscriptionByUserId: '/api/subscriptions/user',
    getServicesByCurrentUser: '/v1/services/user/current',
    updateService: '/v1/services/update',
    getCategoryById: '/v1/categories',
    getCityById: '/v1/cities',
    getServiceById: '/v1/services',
    createRequest: '/v1/request/create',
    getRequestByProvider: '/v1/request/currentUser'
}


export default config;