import logo from './logo.svg';
import './App.css';
import './styles/tailwind.css';

// React Router imports
import React from 'react';
import {BrowserRouter as Router, Routes, Route, Link} from 'react-router-dom';

// Import the necessary components
import NavBar from './components/common/NavBar';
import Dashboard from './components/dashboard/Dashboard';
import FetchContent from './components/fetch/FetchContent';
import AnalyzeContent from './components/analyze/AnalyzeContent';
import ViewInsights from './components/insights/ViewInsights';
import ToxicityMonitor from './components/toxicity/ToxicityMonitor';

function App() {
  return (
    <Router>
      <div className="app-container">
        {/* Navigation Bar Component */}
        <NavBar />

        {/* Define Routes */}
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/fetch" element={<FetchContent />} />
          <Route path="/analyze" element={<AnalyzeContent />} />
          <Route path="/insights" element={<ViewInsights />} />
          <Route path="/toxicity" element={<ToxicityMonitor />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
