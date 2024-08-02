import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from "react-router-dom";
// import '../styles/UpdateCustomer.css';

const UpdateCustomer = () => {
  const [customer, setCustomer] = useState({
    name: '',
    address: {
      postalCode: '',
      city: '',
      province: ''
    }
  });
  const [customerType, setCustomerType] = useState('');

  const navigate = useNavigate();
  const { id } = useParams();

  useEffect(() => {
    const api = `http://localhost:8080/api/v1/customers/${id}`;
    axios.get(api)
      .then(response => {
        setCustomer(response.data);
        setCustomerType(response.data.customerType);
      })
      .catch(error => {
        console.error('Error fetching customer:', error);
      });
  }, [id]);

  const updateCustomer = () => {
    const api = customerType === 'PERSON'
      ? `http://localhost:8080/api/v1/customers/person/${id}`
      : `http://localhost:8080/api/v1/customers/company/${id}`;
      
    axios.put(api, customer)
      .then(response => {
        console.log('Customer updated:', response.status);
        navigate('/view_all_customers');
      })
      .catch(error => {
        console.error('Error updating customer:', error);
        alert('Error updating customer. Please try again.');
      });
  };

  const handleChange = (event) => {
    const { name, value } = event.target;
    if (name in customer.address) {
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

  const handleSubmit = (event) => {
    event.preventDefault();
    updateCustomer();
  };

  return (
    <div className="update-customer">
      <h2>Update Customer</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Name:
          <input type="text" name="name" value={customer.name} onChange={handleChange} required />
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
        <button type="submit">Update Customer</button>
      </form>
    </div>
  );
};

export default UpdateCustomer;
