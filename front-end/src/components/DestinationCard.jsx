import React from 'react';
import { Link } from 'react-router-dom';
import { unlockDestination as apiUnlockDestination } from '../services/api';
import { useAuth } from '../services/AuthContext';

const DestinationCard = ({ destination, isUserUnlocked, onUnlockSuccess }) => {
  const { isAuthenticated } = useAuth();
  const [isUnlocking, setIsUnlocking] = React.useState(false);
  const [unlockError, setUnlockError] = React.useState(null);

  // Use isUserUnlocked if provided (meaning user is authenticated and progress is known).
  // Fallback to destination.isUnlocked if isUserUnlocked is undefined (e.g. user not logged in, or old data structure).
  // If destination.isUnlocked is also undefined, default to false (locked).
  const displayAsUnlocked = typeof isUserUnlocked === 'boolean' ? isUserUnlocked : (destination.isUnlocked || false);

  const handleUnlock = async () => {
    if (!isAuthenticated || !destination || !destination.id) return;
    setIsUnlocking(true);
    setUnlockError(null);
    try {
      await apiUnlockDestination(destination.id);
      if (onUnlockSuccess) {
        onUnlockSuccess(destination.id);
      }
    } catch (err) {
      console.error("Failed to unlock destination:", err);
      setUnlockError(err.message || 'Could not unlock. Try again.');
    } finally {
      setIsUnlocking(false);
    }
  };

  return (
    <div className={`bg-white rounded-lg shadow-lg overflow-hidden transition-all duration-300 hover:shadow-xl ${displayAsUnlocked ? 'transform hover:scale-105' : 'opacity-75 hover:opacity-90'}`}>
      <div className="relative">
        <img 
            src={destination.imageUrl || 'https://via.placeholder.com/300x200?text=No+Image'} 
            alt={destination.name} 
            className={`w-full h-48 object-cover ${!displayAsUnlocked ? 'filter grayscale' : ''}`}
        />
        {!displayAsUnlocked && (
            <div className="absolute inset-0 bg-black bg-opacity-30 flex items-center justify-center">
                <svg xmlns="http://www.w3.org/2000/svg" className="h-12 w-12 text-white opacity-80" viewBox="0 0 20 20" fill="currentColor">
                    <path fillRule="evenodd" d="M10 1a4.5 4.5 0 00-4.5 4.5V9H5a2 2 0 00-2 2v6a2 2 0 002 2h10a2 2 0 002-2v-6a2 2 0 00-2-2h-.5V5.5A4.5 4.5 0 0010 1zm3 8V5.5a3 3 0 10-6 0V9h6zm-1.5 4.5a1 1 0 11-2 0 1 1 0 012 0z" clipRule="evenodd" />
                </svg>
            </div>
        )}
      </div>
      <div className="p-4">
        <h3 className="text-xl font-semibold mb-2 min-h-[56px]">{destination.name}</h3>
        <p className="text-gray-600 text-sm mb-3 h-12 overflow-hidden">{destination.description}</p>
        
        {displayAsUnlocked ? (
          <Link 
            to={`/destinations/${destination.id}`}
            className="block w-full text-center bg-green-500 hover:bg-green-600 text-white font-semibold py-2 px-4 rounded transition-colors duration-300 shadow-sm hover:shadow-md"
          >
            Explore
          </Link>
        ) : (
          isAuthenticated ? (
            <button
              onClick={handleUnlock}
              disabled={isUnlocking}
              className="block w-full text-center bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 px-4 rounded transition-colors duration-300 shadow-sm hover:shadow-md disabled:opacity-50"
            >
              {isUnlocking ? 'Unlocking...' : 'Unlock'}
            </button>
          ) : (
            <div className="text-center bg-gray-300 text-gray-700 font-semibold py-2 px-4 rounded cursor-not-allowed">
              Locked (Login to unlock)
            </div>
          )
        )}
        {unlockError && <p className="text-red-500 text-xs mt-2">{unlockError}</p>}
      </div>
    </div>
  );
};

export default DestinationCard; 