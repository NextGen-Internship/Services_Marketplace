import React from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './Components/Navbar';
import { useState } from 'react';
import Menu from './Components/Menu';
import Home from './Components/Home';
import Category from './Components/Category';
import { SignIn } from './Components/Access/SignIn';
import SignUp from './Components/Access/SignUp';
import { GoogleOAuthProvider } from '@react-oauth/google';

function App() {
  const [clicked, isClicked] = useState(false);

  return (
    <GoogleOAuthProvider clientId="350761079008-0ipa8rk7sumieir1rq4b5ljg3pu78trt.apps.googleusercontent.com">
      <Router>
        <Navbar clicked={clicked} isClicked={isClicked} />
        {clicked ? <Menu /> : null}
        <Routes>
          <Route path="/home-page" element={<Home />} />
          <Route path="/category-page" element={<Category />} />
          <Route path="/sign-in" element={<SignIn />} />
          <Route path="/sign-up" element={<SignUp />} />
          {/* Add a default route or redirect if needed */}
          <Route path="*" element={<Navigate to="/home-page" />} />
        </Routes>
      </Router>
    </GoogleOAuthProvider>
  );
}

export default App;
