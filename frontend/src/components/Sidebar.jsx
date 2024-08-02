import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/Sidebar.css';
import searchIcon from '../search.svg'; // Import the search icon

const Sidebar = ({ show, toggleMenu }) => (
  <aside className={`sidebar ${show ? 'show' : ''}`}>
    <div className="search-container">
      <input type="text" placeholder="Search" />
      <button className="search-button">
        <img src={searchIcon} alt="Search" />
      </button>
    </div>
    <nav>
      <ul>
        <li><Link to="/" onClick={toggleMenu}>Home</Link></li>
        <li><Link to="/create-customer" onClick={toggleMenu}>Add User</Link></li>
        <li><Link to="/view_all_customers" onClick={toggleMenu}>All Users</Link></li>
        <li><Link to="/find-customer" onClick={toggleMenu}>Find User by Id</Link></li>
        <li><Link to="/login" onClick={toggleMenu}>Login</Link></li>
        <li><Link to="/register" onClick={toggleMenu}>Register</Link></li>
      </ul>
    </nav>
  </aside>
);

export default Sidebar;
