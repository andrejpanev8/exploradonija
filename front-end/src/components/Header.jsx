import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../services/AuthContext'; // Will update this to .jsx if AuthContext is renamed

const Header = () => {
  const { isAuthenticated, user, logout, isLoading } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="bg-blue-600 text-white p-4 shadow-md sticky top-0 z-50">
      <div className="container mx-auto flex flex-wrap justify-between items-center">
        <Link to="/" className="text-2xl font-bold hover:text-blue-200 transition-colors">
          Macedonia Explorer
        </Link>
        <nav className="space-x-2 md:space-x-4 text-sm md:text-base">
          <Link to="/" className="px-3 py-2 hover:bg-blue-700 rounded transition-colors">Home</Link>
          {!isLoading && (
            <>
              {isAuthenticated ? (
                <>
                  <Link to="/profile" className="px-3 py-2 hover:bg-blue-700 rounded transition-colors">Profile ({user?.username})</Link>
                  <button 
                    onClick={handleLogout} 
                    className="px-3 py-2 hover:bg-blue-700 rounded transition-colors bg-blue-500 hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
                  >
                    Logout
                  </button>
                </>
              ) : (
                <>
                  <Link to="/login" className="px-3 py-2 hover:bg-blue-700 rounded transition-colors">Login</Link>
                  <Link to="/register" className="px-3 py-2 bg-green-500 hover:bg-green-600 rounded transition-colors">Register</Link>
                </>
              )}
            </>
          )}
          {isLoading && <span className="px-3 py-2">Loading...</span>}
        </nav>
      </div>
    </header>
  );
};

export default Header; 