import React, { useState } from 'react';
import { Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Sidebar from './components/Sidebar';
import CustomerList from './components/CustomerList';
import NewCustomer from './components/NewCustomer';
import UpdateCustomer from './components/UpdateCustomer';
import AccountList from './components/AccountList'; 
import NewAccount from './components/NewAccount'; 
import UpdateAccount from './components/UpdateAccount';
import ViewCustomer from './components/ViewCustomer'; 
import ComingSoon from './components/ComingSoon';
import './App.css';

function App() {
  const [showSidebar, setShowSidebar] = useState(false);

  const toggleMenu = () => {
    setShowSidebar(!showSidebar);
  };

  return (
    <div className="App">
      <Header toggleMenu={toggleMenu} />
      <div className="container">
        <Sidebar show={showSidebar} toggleMenu={toggleMenu} />
        <main>
          <Routes>
            <Route path="/" element={<AccountList />} /> 
            <Route path="/view_all_customers" element={<CustomerList />} />
            <Route path="/create-customer" element={<NewCustomer />} />
            <Route path="/update-customer/:customerType/:id" element={<UpdateCustomer />} />
            <Route path="/create-account/:customerId" element={<NewAccount />} /> 
            <Route path="/update-account/:id" element={<UpdateAccount />} /> 
            <Route path="/find-customer" element={<ViewCustomer />} />
            <Route path="/login" element={<ComingSoon />} /> 
            <Route path="/register" element={<ComingSoon />} />
          </Routes>
        </main>
        <aside className="right-sidebar">
          {/* Right sidebar content can be added here if needed */}
        </aside>
      </div>
    </div>
  );
}

export default App;
