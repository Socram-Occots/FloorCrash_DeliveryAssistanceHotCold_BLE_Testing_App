import React from "react";
import { Link } from "react-router-dom";

function HotColdBluetoothPage() {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-green-200">
      <h1 className="text-3xl font-bold text-green-800">Success!</h1>
      <p className="text-lg text-gray-600">You have successfully submitted.</p>
      <Link to="/">
        <button className="mt-4 bg-blue-500 text-white px-6 py-2 rounded hover:bg-blue-600">
          Go Back
        </button>
      </Link>
    </div>
  );
}

export default HotColdBluetoothPage;