import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from "react-router-dom";
import '../styles/NewAccount.css';

const NewAccount = () => {
  const [account, setAccount] = useState({
    balance: '',
    interestRate: '',
    nextCheckNumber: '',
    type: 'checking'
  });
  
  const [accountType, setAccountType] = useState('checking');
  const { customerId } = useParams();
  const navigate = useNavigate();

  const handleChange = (event) => {
    const { name, value } = event.target;
    if (name === 'accountType') {
      setAccountType(value);
    } else {
      setAccount({
        ...account,
        [name]: value
      });
    }
  };

  const createAccount = () => {
    const api = accountType === 'savings'
      ? `http://localhost:8080/api/v1/accounts/savings/${customerId}`
      : `http://localhost:8080/api/v1/accounts/checking/${customerId}`;

    const accountData = accountType === 'savings'
      ? { balance: account.balance, interestRate: account.interestRate, type: 'savings' }
      : { balance: account.balance, nextCheckNumber: account.nextCheckNumber, type: 'checking' };

    axios.post(api, accountData)
      .then(response => {
        console.log('Account created:', response.status);
        navigate('/');
      })
      .catch(error => {
        console.error('Error creating account:', error);
        alert('Error creating account. Please try again.');
      });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    createAccount();
  };

  return (
    <div className="create-account">
      <h2>Create New Account</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Account Type:
          <select name="accountType" value={accountType} onChange={handleChange}>
            <option value="savings">Savings</option>
            <option value="checking">Checking</option>
          </select>
        </label>
        <label>
          Balance:
          <input type="number" name="balance" value={account.balance} onChange={handleChange} required />
        </label>
        {accountType === 'savings' && (
          <label>
            Interest Rate:
            <input type="number" name="interestRate" value={account.interestRate} onChange={handleChange} required />
          </label>
        )}
        {accountType === 'checking' && (
          <label>
            Next Check Number:
            <input type="number" name="nextCheckNumber" value={account.nextCheckNumber} onChange={handleChange} required />
          </label>
        )}
        <button type="submit">Create Account</button>
      </form>
    </div>
  );
};

export default NewAccount;
