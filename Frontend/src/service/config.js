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
    uploadUserPicture: '/file/upload',
    getPicture: '/file/getPicture'
}


export default config;