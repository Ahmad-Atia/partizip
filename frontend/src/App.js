import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <header className="App-header">
          <nav>
            <h1>Partizip Platform</h1>
            <ul>
              <li><Link to="/">Home</Link></li>
              <li><Link to="/events">Events</Link></li>
              <li><Link to="/communities">Communities</Link></li>
              <li><Link to="/profile">Profile</Link></li>
            </ul>
          </nav>
        </header>
        
        <main>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/events" element={<Events />} />
            <Route path="/communities" element={<Communities />} />
            <Route path="/profile" element={<Profile />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

// Home Component
function Home() {
  return (
    <div className="page">
      <h2>Welcome to Partizip</h2>
      <p>Discover and participate in community events and activities.</p>
    </div>
  );
}

// Events Component
function Events() {
  return (
    <div className="page">
      <h2>Events</h2>
      <p>Find exciting events in your community.</p>
    </div>
  );
}

// Communities Component
function Communities() {
  return (
    <div className="page">
      <h2>Communities</h2>
      <p>Connect with local communities.</p>
    </div>
  );
}

// Profile Component
function Profile() {
  return (
    <div className="page">
      <h2>Profile</h2>
      <p>Manage your profile and preferences.</p>
    </div>
  );
}

export default App;
