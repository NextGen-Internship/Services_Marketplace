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

function App() {
  const[clicked, isClicked] = useState(false)
  return (
    <Router>
      <Navbar clicked={clicked} isClicked={isClicked}/>
      {clicked? <Menu/>:null}
      <Routes>
        <Route exact path="home-page" element={<Home/>}/>
        <Route exact path="category-page" element={<Category/>}/>
        <Route exact path="add-service-page" element={<AddServicePage/>}/>
        <Route exact path="sign-in" element={<SignIn/>}/>
        <Route exact path="sign-up" element={<SignUp/>}/>
      </Routes>
    </Router>
  );
}

export default App;
