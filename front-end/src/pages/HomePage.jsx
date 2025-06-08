import React, { useState, useEffect, useCallback } from 'react';
import DestinationCard from '../components/DestinationCard';
import { fetchDestinations, fetchUserProgress } from '../services/api';
import { useAuth } from '../services/AuthContext';

const HomePage = () => {
  const { isAuthenticated, isLoading: authLoading } = useAuth();
  const [destinations, setDestinations] = useState([]);
  const [userProgress, setUserProgress] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadData = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const destinationsData = await fetchDestinations();
      setDestinations(destinationsData || []);

      if (isAuthenticated) {
        const progressData = await fetchUserProgress();
        setUserProgress(progressData);
      } else {
        setUserProgress(null); // Clear progress if user logs out
      }
    } catch (err) {
      console.error("Error fetching data for home page:", err);
      setError(err.message || 'Failed to load page data. Please try again later.');
      setDestinations([]);
      setUserProgress(null);
    } finally {
      setLoading(false);
    }
  }, [isAuthenticated]); 

  useEffect(() => {
    if (!authLoading) { 
        loadData();
    }
  }, [authLoading, loadData]);

  const handleUnlockSuccess = (unlockedDestinationId) => {
    setUserProgress(prevProgress => {
      const newUnlockedDestinations = [
        ...(prevProgress?.unlockedDestinations || []),
        
        { destinationId: unlockedDestinationId, unlockedAt: new Date().toISOString(), allChallengesCompleted: false }
      ];
      return {
        ...(prevProgress || {}),
        unlockedDestinations: newUnlockedDestinations,
      };
    });
    
  };

  if (authLoading || loading) {
    return <div className="text-center p-10 text-xl">Loading destinations...</div>;
  }

  if (error) {
    return <div className="text-center p-10 text-red-600 bg-red-100 border border-red-400 rounded-md shadow-md">
      <h2 className="text-2xl font-semibold mb-2">Error</h2>
      <p>{error}</p>
      <button 
        onClick={loadData} 
        className="mt-4 px-4 py-2 bg-red-500 text-white rounded hover:bg-red-700 transition-colors duration-300"
      >
        Try Again
      </button>
    </div>;
  }

  const unlockedDestinationIds = new Set(
    userProgress?.unlockedDestinations?.map(ud => ud.destinationId) || []
  );

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-4xl font-bold text-center my-8 text-gray-800">Explore Macedonia</h1>
      {destinations.length === 0 && !loading && (
        <p className='text-center text-gray-600 text-lg'>No destinations available at the moment. Check back later!</p>
      )}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8">
        {destinations.map(dest => {
          const isUserUnlocked = isAuthenticated ? unlockedDestinationIds.has(dest.id) : false;
          
          return (
            <DestinationCard 
              key={dest.id} 
              destination={dest} 
              isUserUnlocked={isUserUnlocked} 
              onUnlockSuccess={handleUnlockSuccess}
            />
          );
        })}
      </div>
    </div>
  );
};

export default HomePage; 