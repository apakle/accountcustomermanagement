# BankAccount Application



## Overview

This is a React-based web application for managing bank accounts and customers. The application consumes a REST API and provides functionalities for creating, updating, and viewing customers and accounts. The project uses a Spring Boot backend to manage data and provide RESTful endpoints.

## Features
- View all accounts
- Add a new account to a customer
- Update existing accounts
- Delete accounts
- View all customers
- Add new customers
- Update customer details
- Delete customers
- View customer details, including associated accounts

## Prerequisites
- Node.js (v14 or higher)
- npm (v6 or higher)
- Java (JDK 8 or higher)
- Spring Boot (for the backend API)
- Git

## Setup and Running the Application
### Backend (Spring Boot)

1. Clone the repository:
```
git clone https://git.fdmgroup.com/Andre.Kleber/accountcustomermanagement_epic5.git
cd <repository-folder>/backend

```
2. Run the backend application using Eclipse. The backend will start on http://localhost:8080.

### Frontend (React)
1. Navigate to the frontend directory:
```
cd <repository-folder>/frontend
```
2. Install the dependencies:
```
npm install
npm install react-router-dom
npm install axios
```
3. Start the React application:
```
npm start
```

## Application Pages and Components

## Home (/)
- **Component**: AccountList
- **Description**: Displays all accounts with functionalities to add a new account to a customer, update existing accounts, and delete accounts.


