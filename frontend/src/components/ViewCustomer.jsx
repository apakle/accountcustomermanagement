import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useLocation } from 'react-router-dom';
import '../styles/ViewCustomer.css';

const ViewCustomer = () => {
  const [customerId, setCustomerId] = useState('');
  const [customerData, setCustomerData] = useState(null);
  const [customers, setCustomers] = useState([]);
  const [error, setError] = useState('');

  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    loadCustomers();
  }, []);

  useEffect(() => {
    if (location.state && location.state.customerId) {
      const id = location.state.customerId;
      setCustomerId(id);
      loadCustomerData(id);
    }
  }, [location.state]);

  const loadCustomerData = (id) => {
    const api = `http://localhost:8080/api/v1/customers/${id}`;
    axios.get(api)
      .then(response => {
        setCustomerData(response.data);
        setError('');
      })
      .catch(error => {
        console.error('Error fetching customer:', error);
        setCustomerData(null);
        setError('Customer not found or error fetching customer data.');
      });
  };

  const loadCustomers = () => {
    axios.get('http://localhost:8080/api/v1/customers')
      .then(response => setCustomers(response.data))
      .catch(error => console.error('Error fetching customers:', error));
  };

  const handleCustomerChange = (event) => {
    const selectedCustomerId = event.target.value;
    setCustomerId(selectedCustomerId);
    if (selectedCustomerId) {
      loadCustomerData(selectedCustomerId);
    } else {
      setCustomerData(null);
    }
  };

  const handleDeposit = (accountId) => {
    navigate(`/deposit/${accountId}`);
  };

  const handleWithdraw = (accountId) => {
    navigate(`/withdraw/${accountId}`);
  };

  return (
    <div className="view-customer">
      <h2>View Customer</h2>
      <form>
        <select value={customerId} onChange={handleCustomerChange}>
          <option value="">Select Customer</option>
          {customers.map(customer => (
            <option key={customer.customerId} value={customer.customerId}>
              {customer.name} (ID: {customer.customerId})
            </option>
          ))}
        </select>
      </form>
      {error && <p>{error}</p>}
      {customerData && (
        <div className="customer-details">
          <h3>Customer Details</h3>
          <p><strong>Name:</strong> {customerData.name}</p>
          <p><strong>Type:</strong> {customerData.customerType}</p>
          <p><strong>Street Number:</strong> {customerData.address.streetNumber}</p>
          <p><strong>Postal Code:</strong> {customerData.address.postalCode}</p>
          <p><strong>City:</strong> {customerData.address.city}</p>
          <p><strong>Province:</strong> {customerData.address.province}</p>
          <div className="accounts-container">
            <h3>Accounts of customer {customerData.name} (ID: {customerData.customerId})</h3>
            <table>
              <thead>
                <tr>
                  <th>Account ID</th>
                  <th>Balance</th>
                  <th>Interest Rate</th>
                  <th>Next Check Number</th>
                  <th>Account Type</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {customerData.accounts.map(account => (
                  <tr key={account.accountId}>
                    <td>{account.accountId}</td>
                    <td>{account.balance.toFixed(2)}</td>
                    <td>{account.type === 'savings' ? `${account.interestRate}%` : '-'}</td>
                    <td>{account.type === 'checking' ? account.nextCheckNumber : '-'}</td>
                    <td>{account.type}</td>
                    <td>
                      <button onClick={() => handleDeposit(account.accountId)} className="deposit">Deposit Money</button>
                      <button onClick={() => handleWithdraw(account.accountId)} className="withdraw">Withdraw Money</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
};

export default ViewCustomer;
