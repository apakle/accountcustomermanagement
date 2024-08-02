import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/CustomerList.css';

const CustomerList = () => {
  const api = 'http://localhost:8080/api/v1/customers';
  const [customers, setCustomers] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    loadCustomers();
  }, []);

  const loadCustomers = () => {
    axios.get(api)
      .then(response => setCustomers(response.data))
      .catch(error => console.error('Error fetching customers:', error));
  };

  const handleUpdate = (id, customerType) => {
    navigate(`/update-customer/${customerType.toLowerCase()}/${id}`);
  };

  const handleDelete = (id) => {
    if (window.confirm('Are you sure you want to delete this customer?')) {
      axios.delete(`${api}/${id}`)
        .then(response => {
          console.log('Customer deleted:', response.status);
          loadCustomers(); // Reload customers after deletion
        })
        .catch(error => {
          console.error('Error deleting customer:', error);
          alert('Error deleting customer. Please try again.');
        });
    }
  };

  const handleAddCustomer = () => {
    navigate(`/create-customer`);
};

  const handleAddAccount = (customerId) => {
      navigate(`/create-account/${customerId}`);
  };

  return (
    <div className="main-content-customers">
      <h2>View All Customers</h2>
      <div>
        <button onClick={handleAddCustomer} className="add-new-customer">Add New Customer</button>
      </div>
      <table>
        <thead>
          <tr>
            <th>Customer ID</th>
            <th>Name</th>
            <th>Street Number</th>
            <th>Postal Code</th>
            <th>City</th>
            <th>Province</th>
            <th>Type</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {customers.map(customer => (
            <tr key={customer.customerId}>
              <td>{customer.customerId}</td>
              <td>{customer.name}</td>
              <td>{customer.address.streetNumber}</td>
              <td>{customer.address.postalCode}</td>
              <td>{customer.address.city}</td>
              <td>{customer.address.province}</td>
              <td>{customer.customerType}</td> {/* Display the customer type */}
              <td>
                <button onClick={() => handleUpdate(customer.customerId, customer.customerType)} className="update">Update</button>
                <button onClick={() => handleDelete(customer.customerId)} className="delete">Delete</button>
                <button onClick={() => handleAddAccount(customer.customerId)} className="add-account-button">Add Account</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default CustomerList;
