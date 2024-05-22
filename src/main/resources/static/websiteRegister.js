// server.js (or any other server file)

const express = require('express');
const bodyParser = require('body-parser');
const app = express();

// Middleware to parse JSON body
app.use(bodyParser.json());

// Mock database to store registered users
const users = [];

// Endpoint for user registration
app.post('/api/register', (req, res) => {
  const { username, email, password } = req.body;

  // Validate input
  if (!username || !email || !password) {
    return res.status(400).json({ message: 'Please provide username, email, and password' });
  }

  // Check if user already exists
  const existingUser = users.find(user => user.email === email);
  if (existingUser) {
    return res.status(400).json({ message: 'User already exists' });
  }

  // Create new user
  const newUser = { username, email, password };
  users.push(newUser);

  // Return success message
  res.status(201).json({ message: 'User registered successfully' });
});

// Start server
const port = 3000;
app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
