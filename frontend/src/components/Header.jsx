import React from 'react';
import '../styles/Header.css'; 

const Header = ({ toggleMenu }) => (
  <header>
    <h1>BankAccount Application</h1>
    <div className="hamburger" onClick={toggleMenu}>
      <div></div>
      <div></div>
      <div></div>
    </div>
  </header>
);

export default Header;
