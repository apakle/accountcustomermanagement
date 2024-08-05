import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';
import '../styles/DepositComponent.css';

const DepositComponent = () => {
  const [amount, setAmount] = useState('');
  const { accountId } = useParams();
  const navigate = useNavigate();

  const handleDeposit = (event) => {
    event.preventDefault();
    if (!amount || isNaN(amount) || amount<0) {
      alert('Please enter a valid positive amount.');
      return;
    }

    const api = `http://localhost:8080/api/v1/accounts/${accountId}/deposit?amount=${amount}`;
    axios.put(api)
      .then(response => {
        console.log('Deposit successful:', response.status);
        navigate("/");
      })
      .catch(error => {
        console.error('Error making deposit:', error);
        alert('Error making deposit. Please try again.');
      });
  };

  return (
    <div className="deposit">
      <h2>Make a Deposit</h2>
      <form onSubmit={handleDeposit}>
        <label>
        <strong>Amount</strong>:
          <input 
            type="text" 
            value={amount} 
            onChange={(event) => setAmount(event.target.value)} 
            required 
          />
        </label>
        <button type="submit">Deposit</button>
      </form>
    </div>
  );
};

export default DepositComponent;
