import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './Components/Navbar';
import { useState } from 'react';
import Menu from './Components/Menu';
import Home from './Components/Home';
import Category from './Components/Category';
import { SignIn } from './Components/Access/SignIn';
import SignUp from './Components/Access/SignUp';
import AddServicePage from './Components/AddServicePage';
import { GoogleOAuthProvider } from '@react-oauth/google';
import ServicesPage from './Components/ServicesPage';

function App() {
  const[clicked, isClicked] = useState(false)
  return (
    <GoogleOAuthProvider clientId="350761079008-0ipa8rk7sumieir1rq4b5ljg3pu78trt.apps.googleusercontent.com">
      <Router>
        <Navbar clicked={clicked} isClicked={isClicked}/>
        {clicked? <Menu/>:null}
        <Routes>
          <Route exact path="home-page" element={<Home/>}/>
          <Route exact path="category-page" element={<Category/>}/>
          <Route exact path="add-service-page" element={<AddServicePage/>}/>
          <Route exact path="services" element={<ServicesPage/>}/>
          <Route exact path="sign-in" element={<SignIn/>}/>
          <Route exact path="sign-up" element={<SignUp/>}/>
          
        </Routes>
      </Router>
    </GoogleOAuthProvider>
  );
}

export default App;
