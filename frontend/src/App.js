import React, { useState } from "react";
import logo from "./logo.svg";
import "./App.css";
import { HashRouter as Router, Routes, Route, useNavigate } from "react-router-dom";
import HotColdBluetoothPage from "./HotColdBluetoothPage";


function HomePage() {
  const navigate = useNavigate();
  const [errorMessage, setErrorMessage] = useState("");

  function handleSubmit() {
    navigate("/bluetoothsearch");
  }

  function sendDataToKotlin() {
    try {
      const inputValue = document.getElementById("bluetoothIdInput").value;
      if (window.AndroidBridge && inputValue.trim().length != 0) {
        window.AndroidBridge.receiveTestMessage(inputValue);
        handleSubmit();
      } else if (!window.AndroidBridge) {
        setErrorMessage("Backend connection is unavailable");
      } else if (inputValue.trim().length === 0) {
        setErrorMessage("Empty Bluetooth code");
      } else {
        setErrorMessage("UnknownError");
      }
    } catch (error) {
      setErrorMessage(error.toString());
    }
  }

  return (
    <div className="bg-sky-950 min-h-screen flex items-center justify-center">
      <div className="flex flex-col items-center justify-evenly h-screen">
        <img src={logo} alt="Logo" className="object-scale-down" />
        <div className="flex flex-col items-center gap-4">
          <input
            id="bluetoothIdInput"
            type="text"
            placeholder="Enter Bluetooth Code..."
            className="placeholder-black placeholder-opacity-50 green-style"
          />
          <button onClick={sendDataToKotlin} className="green-style">
            Submit
          </button>
          {errorMessage && (
            <div className="bg-red-500 text-white p-2 rounded mt-2">
              {errorMessage}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/bluetoothsearch" element={<HotColdBluetoothPage />} />
      </Routes>
    </Router>
  );
}

export default App;