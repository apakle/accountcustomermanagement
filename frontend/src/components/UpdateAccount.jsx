import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from "react-router-dom";
import '../styles/UpdateAccount.css';

const UpdateAccount = () => {
  const [account, setAccount] = useState({
    type: '',
    balance: '',
    interestRate: '',
    nextCheckNumber: ''
  });

  const { id } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    const api = `http://localhost:8080/api/v1/accounts/${id}`;
    axios.get(api)
      .then(response => {
        setAccount(response.data);
      })
      .catch(error => {
        console.error('Error fetching account:', error);
      });
  }, [id]);

  const updateAccount = () => {
    const api = account.type === 'savings'
      ? `http://localhost:8080/api/v1/accounts/savings/${id}`
      : `http://localhost:8080/api/v1/accounts/checking/${id}`;

    const accountData = account.type === 'savings'
      ? { type: 'savings', balance: account.balance, interestRate: account.interestRate }
      : { type: 'checking', balance: account.balance, nextCheckNumber: account.nextCheckNumber };

    axios.put(api, accountData)
      .then(response => {
        console.log('Account updated:', response.status);
        navigate('/');
      })
      .catch(error => {
        console.error('Error updating account:', error);
        alert('Error updating account. Please try again.');
      });
  };

  const handleChange = (event) => {
    const { name, value } = event.target;
    setAccount({
      ...account,
      [name]: value
    });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    updateAccount();
  };

  return (
    <div className="update-account">
      <h2>Update Account</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Account Type: {account.type}
        </label>
        <label>
          Balance:
          <input type="number" name="balance" value={account.balance} onChange={handleChange} required />
        </label>
        {account.type === 'savings' && (
          <label>
            Interest Rate:
            <input type="number" name="interestRate" value={account.interestRate} onChange={handleChange} required />
          </label>
        )}
        {account.type === 'checking' && (
          <label>
            Next Check Number:
            <input type="number" name="nextCheckNumber" value={account.nextCheckNumber} onChange={handleChange} required />
          </label>
        )}
        <button type="submit">Update Account</button>
      </form>
    </div>
  );
};

export default UpdateAccount;
