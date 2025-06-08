import React, { useState, useEffect } from 'react';
import { useAuth } from '../services/AuthContext';
import { fetchUserProgress } from '../services/api';
import { Link } from 'react-router-dom';

const ProfilePage = () => {
  const { user, isLoading: authLoading } = useAuth();
  const [progressData, setProgressData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (authLoading) return; // Wait for auth state to be determined
    if (!user) {
      setLoading(false); // Should be redirected by ProtectedRoute if not authenticated
      return;
    }

    const loadProgress = async () => {
      setLoading(true);
      try {
        const data = await fetchUserProgress();
        setProgressData(data);
        setError(null);
      } catch (err) {
        console.error("Error fetching user progress:", err);
        setError(err.message || 'Failed to load profile data.');
      } finally {
        setLoading(false);
      }
    };

    loadProgress();
  }, [user, authLoading]);

  if (authLoading || loading) {
    return <div className="text-center p-10 text-xl">Loading profile...</div>;
  }

  if (error) {
    return <div className="text-center p-10 text-red-500">Error: {error}</div>;
  }

  if (!user || !progressData) {
    return <div className="text-center p-10 text-gray-600">Could not load profile data. Please try again.</div>;
  }

  const { 
    username,
    unlockedDestinationsCount = 0,
    completedChallengesCount = 0,
    unlockedDestinations = [],
    earnedDiscountsCount = 0,
    earnedDiscounts = []
  } = progressData;

  // Assuming a fixed total number of destinations for overall progress calculation
  // This should ideally come from the backend or be configurable
  const TOTAL_AVAILABLE_DESTINATIONS = 10; // Example value
  const overallProgressPercent = TOTAL_AVAILABLE_DESTINATIONS > 0 
    ? (unlockedDestinationsCount / TOTAL_AVAILABLE_DESTINATIONS) * 100 
    : 0;

  return (
    <div className="container mx-auto p-4 sm:p-6 lg:p-8">
      <h1 className="text-3xl sm:text-4xl font-bold text-center my-8 text-gray-800">Welcome, {username}!</h1>
      
      <div className="bg-white shadow-xl rounded-lg p-6 sm:p-8 max-w-2xl mx-auto mb-10">
        <h2 className="text-2xl font-semibold text-gray-700 mb-6 text-center">Your Exploration Progress</h2>
        
        <div className="mb-6">
          <p className="text-lg text-gray-700 mb-1">Destinations Unlocked: <span className="font-bold text-indigo-600">{unlockedDestinationsCount}</span> / {TOTAL_AVAILABLE_DESTINATIONS}</p>
          <div className="w-full bg-gray-200 rounded-full h-6 shadow-inner">
            <div 
              className="bg-indigo-500 h-6 rounded-full transition-all duration-500 ease-out text-xs font-medium text-blue-100 text-center p-0.5 leading-none"
              style={{ width: `${overallProgressPercent}%` }}
            >
              {Math.round(overallProgressPercent)}%
            </div>
          </div>
        </div>

        <div className="mb-8">
          <p className="text-lg text-gray-700">Challenges Completed: <span className="font-bold text-green-600">{completedChallengesCount}</span></p>
        </div>

        <div className="mb-10">
          <h3 className="text-xl font-semibold text-gray-700 mb-4">Unlocked Destinations:</h3>
          {unlockedDestinations.length > 0 ? (
            <ul className="space-y-3">
              {unlockedDestinations.map(destProg => (
                <li key={destProg.destinationId} className="p-4 bg-gray-50 rounded-md shadow-sm hover:shadow-md transition-shadow">
                  <Link to={`/destinations/${destProg.destinationId}`} className="font-semibold text-blue-600 hover:text-blue-800 hover:underline">
                    {destProg.destinationName}
                  </Link>
                  <p className="text-sm text-gray-500">Unlocked on: {new Date(destProg.unlockedAt).toLocaleDateString()}</p>
                  {destProg.allChallengesCompleted && 
                    <span className="text-xs font-bold text-white bg-green-500 px-2 py-1 rounded-full ml-2">All Challenges Done!</span>}
                </li>
              ))}
            </ul>
          ) : (
            <p className="text-gray-500 italic">You haven't unlocked any destinations yet. Start exploring!</p>
          )}
        </div>

        <div className="pt-6 border-t border-gray-200">
            <h3 className="text-xl font-semibold text-gray-700 mb-4">Your Rewards & Discounts ({earnedDiscountsCount})</h3>
            {earnedDiscounts.length > 0 ? (
                <ul className="space-y-4">
                    {earnedDiscounts.map(discount => (
                        <li key={discount.discountId} className={`p-4 rounded-lg shadow-sm border-l-4 ${discount.used ? 'bg-gray-100 border-gray-300 opacity-70' : 'bg-yellow-50 border-yellow-400'}`}>
                            <div className="flex justify-between items-start">
                                <div>
                                    <h4 className="font-bold text-lg text-yellow-700">{discount.description}</h4>
                                    <p className="text-sm text-gray-600">Code: <span className="font-mono bg-gray-200 px-1 rounded">{discount.code}</span></p>
                                    <p className="text-sm text-gray-500">For: {discount.destinationName}</p>
                                </div>
                                {discount.used && <span className="text-xs font-semibold text-red-500 bg-red-100 px-2 py-1 rounded-full">USED</span>}
                            </div>
                            <div className="text-xs text-gray-400 mt-2">
                                Earned: {new Date(discount.earnedAt).toLocaleDateString()} 
                                {discount.validUntil && ` | Valid Until: ${new Date(discount.validUntil).toLocaleDateString()}`}
                            </div>
                        </li>
                    ))}
                </ul>
            ) : (
                <p className="text-gray-500 italic">No discounts earned yet. Complete all challenges for a destination to earn rewards!</p>
            )}
        </div>

      </div>
    </div>
  );
};

export default ProfilePage; 