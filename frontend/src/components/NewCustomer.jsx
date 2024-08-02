import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import '../styles/NewCustomer.css';

const NewCustomer = () => {
  const [customer, setCustomer] = useState({
    name: '',
    address: {
      streetNumber: '',
      postalCode: '',
      city: '',
      province: ''
    }
  });

  const [customerType, setCustomerType] = useState('person');
  const navigate = useNavigate();

  const handleChange = (event) => {
    const { name, value } = event.target;
    if (name === 'customerType') {
      setCustomerType(value);
    } else if (name in customer.address) {
      setCustomer({
        ...customer,
        address: {
          ...customer.address,
          [name]: value
        }
      });
    } else {
      setCustomer({
        ...customer,
        [name]: value
      });
    }
  };

  const createCustomer = () => {
    const api = customerType === 'person'
      ? 'http://localhost:8080/api/v1/customers/person'
      : 'http://localhost:8080/api/v1/customers/company';

    axios.post(api, customer)
      .then(response => {
        console.log('Customer created:', response.status);
        navigate('/view_all_customers');
      })
      .catch(error => {
        console.error('Error creating customer:', error);
        alert('Error creating customer. Please try again.');
      });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    createCustomer();
  };

  return (
    <div className="create-customer">
      <h2>Create New Customer</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Customer Type:
          <select name="customerType" value={customerType} onChange={handleChange}>
            <option value="person">Person</option>
            <option value="company">Company</option>
          </select>
        </label>
        <label>
          Name:
          <input type="text" name="name" value={customer.name} onChange={handleChange} required />
        </label>
        <label>
          Street Number:
          <input type="text" name="streetNumber" value={customer.address.streetNumber} onChange={handleChange} required />
        </label>
        <label>
          Postal Code:
          <input type="text" name="postalCode" value={customer.address.postalCode} onChange={handleChange} required />
        </label>
        <label>
          City:
          <input type="text" name="city" value={customer.address.city} onChange={handleChange} required />
        </label>
        <label>
          Province:
          <input type="text" name="province" value={customer.address.province} onChange={handleChange} required />
        </label>
        <button type="submit">Create Customer</button>
      </form>
    </div>
  );
};

export default NewCustomer;
