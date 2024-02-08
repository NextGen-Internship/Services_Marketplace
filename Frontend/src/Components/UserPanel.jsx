// import React, { useState } from 'react';
// import '../styles/UserPanel.css';
// import axios from 'axios';

// const UserPanel = ({ userId }) => {
//     const [isLoading, setIsLoading] = useState(false);
//     const [error, setError] = useState(null);
//     const [isProvider, setIsProvider] = useState(false); 

//     const becomeProvider = async () => {
//         setIsLoading(true);
//         setError(null);
//         try {
//             await axios.put(`/api/users/${userId}/become-provider`);
//             setIsProvider(true);
//         } catch (err) {
//             setError('Failed to become a provider. Please try again.');
//             console.error(err);
//         }
//         setIsLoading(false);
//     };
//     return (
//         <div className="user-panel">
//             {!isProvider && (
//                 <button
//                     className='provider-button'
//                     onClick={becomeProvider}
//                     disabled={isLoading}
//                 >
//                     {isLoading ? 'Processing...' : 'Become a Provider'}
//                 </button>
//             )}
//             {isProvider && <p>You are now a provider!</p>}
//             {error && <p className='error-message'>{error}</p>}
//         </div>
//     );
// };

// export default UserPanel;
