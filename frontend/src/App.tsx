import React from 'react';
import logo from './logo.svg';
import './App.css';

function App() {
  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100">
      <img src="/logo192.png" alt="Logo" className="w-32 h-32 mb-6" />
      <input type="text" placeholder="Enter something..." className="border p-2 w-64 mb-4 rounded" />
      <button className="bg-blue-500 text-white px-6 py-2 rounded hover:bg-blue-600">
        Submit
      </button>
    </div>
  );
}

export default App;
