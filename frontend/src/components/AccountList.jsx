import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/AccountList.css';

const AccountList = () => {
  const accountsApi = 'http://localhost:8080/api/v1/accounts';
  const customersApi = 'http://localhost:8080/api/v1/customers';
  const [accounts, setAccounts] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [selectedCustomerId, setSelectedCustomerId] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    loadAccounts();
    loadCustomers();
  }, []);

  const loadAccounts = () => {
    axios.get(accountsApi)
      .then(response => setAccounts(response.data))
      .catch(error => console.error('Error fetching accounts:', error));
  };

  const loadCustomers = () => {
    axios.get(customersApi)
      .then(response => setCustomers(response.data))
      .catch(error => console.error('Error fetching customers:', error));
  };

  const getCustomerNameByAccountId = (accountId) => {
    for (const customer of customers) {
      if (customer.accounts.some(account => account.accountId === accountId)) {
        return customer.name;
      }
    }
    return 'Unknown';
  };

  const handleUpdate = (id) => {
    navigate(`/update-account/${id}`);
  };

  const handleDelete = (id) => {
    if (window.confirm('Are you sure you want to delete this account?')) {
      axios.delete(`${accountsApi}/${id}`)
        .then(response => {
          console.log('Account deleted:', response.status);
          loadAccounts(); // Reload accounts after deletion
        })
        .catch(error => {
          console.error('Error deleting account:', error);
          alert('Error deleting account. Please try again.');
        });
    }
  };

  const handleCustomerChange = (event) => {
    setSelectedCustomerId(event.target.value);
  };

  const handleAddAccount = () => {
    if (selectedCustomerId) {
      navigate(`/create-account/${selectedCustomerId}`);
    } else {
      alert('Please select a customer to add an account.');
    }
  };

  return (
    <div className="main-content">
      <h2>View All Accounts</h2>
      <div className="add-account-container">
        <button onClick={handleAddAccount} className="add-new-account">Add New Account to:</button>
        <select value={selectedCustomerId} onChange={handleCustomerChange}>
          <option value="">Select Customer</option>
          {customers.map(customer => (
            <option key={customer.customerId} value={customer.customerId}>
              {customer.name} (ID: {customer.customerId})
            </option>
          ))}
        </select>        
      </div>
      <table>
        <thead>
          <tr>
            <th>Account Id</th>
            <th>Customer</th>
            <th>Account Balance</th>
            <th>Interest Rate</th>
            <th>Next Check Number</th>
            <th>Account Type</th>            
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {accounts.map(account => (
            <tr key={account.accountId}>
              <td>{account.accountId}</td>
              <td>{getCustomerNameByAccountId(account.accountId)}</td>
              <td>{account.balance.toFixed(2)}</td>
              <td>{account.type === 'savings' ? `${account.interestRate}%` : '-'}</td>
              <td>{account.type === 'checking' ? account.nextCheckNumber : '-'}</td>
              <td>{account.type}</td>
              <td>
                <button onClick={() => handleUpdate(account.accountId)} className="update">Update</button>
                <button onClick={() => handleDelete(account.accountId)} className="delete">Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AccountList;
