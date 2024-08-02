import React from 'react';
import { useLocation } from 'react-router-dom';

const ComingSoon = () => {
  const location = useLocation();
  const pageTitle = location.pathname === '/login' ? 'Login' : 'Register';

  return (
    <div className="coming-soon">
      <h2>{pageTitle}</h2>
      <p>Coming soon!</p>
    </div>
  );
};

export default ComingSoon;
