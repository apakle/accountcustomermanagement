import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';
import '../styles/WithdrawComponent.css';

const WithdrawComponent = () => {
  const [amount, setAmount] = useState('');
  const { accountId } = useParams();
  const navigate = useNavigate();
  const [account, setAccount] = useState(null);

  useEffect(() => {
    const fetchAccount = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/v1/accounts/${accountId}`);
        setAccount(response.data);
      } catch (error) {
        console.error('Error fetching account:', error);
      }
    };
    fetchAccount();
  }, [accountId]);

  const handleWithdraw = async (event) => {
    event.preventDefault();
    if (!amount || isNaN(amount) || amount<0) {
        alert('Please enter a valid positive amount.');
        return;
      }

    if (account && account.type === 'savings' && amount > account.balance) {
      alert('Insufficient funds for savings account.');
      return;
    }

    const api = `http://localhost:8080/api/v1/accounts/${accountId}/withdraw?amount=${amount}`;
    try {
      await axios.put(api);
      console.log('Withdraw successful');
      navigate("/");
    } catch (error) {
      console.error('Error making withdrawal:', error);
      alert('Error making withdrawal. Please try again.');
    }
  };

  return (
    <div className="withdraw">
      <h2>Withdraw Money</h2>
      <form onSubmit={handleWithdraw}>
        <label>
          <strong>Amount:</strong>
          <input 
            type="text" 
            value={amount} 
            onChange={(event) => setAmount(event.target.value)} 
            required 
          />
        </label>
        <button type="submit">Withdraw</button>
      </form>
    </div>
  );
};

export default WithdrawComponent;
