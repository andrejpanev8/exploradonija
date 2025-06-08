import React, { useState, useEffect, useCallback } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import ChallengeModal from '../components/ChallengeModal';
import { 
    fetchDestinationById, 
    fetchChallengesByDestinationId, 
    unlockDestination, 
    completeUserChallenge, 
    fetchUserProgress 
} from '../services/api';
import { useAuth } from '../services/AuthContext';

const DestinationPage = () => {
    const { id: destinationId } = useParams();
    const { user, isAuthenticated, isLoading: authLoading } = useAuth();
    const navigate = useNavigate();

    const [destination, setDestination] = useState(null);
    const [challenges, setChallenges] = useState([]);
    const [userProgress, setUserProgress] = useState(null);
    const [selectedChallenge, setSelectedChallenge] = useState(null);
    
    const [isDestinationUnlockedForUser, setIsDestinationUnlockedForUser] = useState(false);
    const [completedChallengeIds, setCompletedChallengeIds] = useState(new Set());

    const [loadingDestination, setLoadingDestination] = useState(true);
    const [loadingChallenges, setLoadingChallenges] = useState(true);
    const [loadingUserProgress, setLoadingUserProgress] = useState(true);
    const [error, setError] = useState(null);
    const [actionInProgress, setActionInProgress] = useState(false);

    const loadAllData = useCallback(async () => {
        if (!destinationId || authLoading) return;
        if (!isAuthenticated) {
             // This page is protected, so this case might not be hit if ProtectedRoute works correctly
            navigate('/login');
            return;
        }

        setLoadingDestination(true);
        setLoadingChallenges(true);
        setLoadingUserProgress(true);
        setError(null);

        try {
            // Fetch destination details
            const destData = await fetchDestinationById(destinationId);
            setDestination(destData);
            setLoadingDestination(false);

            // Fetch challenges for this destination
            const chalData = await fetchChallengesByDestinationId(destinationId);
            setChallenges(chalData || []);
            setLoadingChallenges(false);

            // Fetch user's overall progress
            const progressData = await fetchUserProgress(); 
            setUserProgress(progressData);
            if (progressData) {
                const unlockedDest = progressData.unlockedDestinations?.find(ud => ud.destinationId === parseInt(destinationId));
                setIsDestinationUnlockedForUser(!!unlockedDest);

                const completedIds = new Set(
                    progressData.completedChallenges
                        ?.filter(cc => cc.destinationId === parseInt(destinationId))
                        .map(cc => cc.challengeId)
                );
                setCompletedChallengeIds(completedIds);
            }
            setLoadingUserProgress(false);

        } catch (err) {
            console.error("Error fetching destination page data:", err);
            setError(err.message || 'Failed to load destination details.');
            setLoadingDestination(false);
            setLoadingChallenges(false);
            setLoadingUserProgress(false);
        }
    }, [destinationId, isAuthenticated, authLoading, navigate]);

    useEffect(() => {
        loadAllData();
    }, [loadAllData]);

    const handleUnlockDestination = async () => {
        setActionInProgress(true);
        setError(null);
        try {
            await unlockDestination(destinationId);
            setIsDestinationUnlockedForUser(true);
            // Refresh user progress to be sure or update local state optimistically
            const progressData = await fetchUserProgress(); 
            setUserProgress(progressData);
            alert('Destination Unlocked!');
        } catch (err) {
            setError(err.message || 'Failed to unlock destination.');
        }
        setActionInProgress(false);
    };

    const handleOpenChallenge = (challenge) => {
        setSelectedChallenge(challenge);
    };

    const handleCloseModal = () => {
        setSelectedChallenge(null);
    };

    const handleCompleteChallenge = async (challengeId, submissionData) => {
        setActionInProgress(true);
        try {
            await completeUserChallenge(challengeId, submissionData);
            setCompletedChallengeIds(prevIds => new Set(prevIds).add(challengeId));
            setSelectedChallenge(prev => prev ? { ...prev, completed: true } : null);
            const progressData = await fetchUserProgress(); 
            setUserProgress(progressData);
            alert('Challenge completed!');
            handleCloseModal(); // Close modal after successful completion
        } catch (err) {
            console.error("Error completing challenge:", err);
            // Potentially set an error state within the modal or on the page
            alert(`Failed to complete challenge: ${err.message}`); 
        }
        setActionInProgress(false);
    };

    if (authLoading || loadingDestination || loadingChallenges || loadingUserProgress) {
        return <div className="text-center p-10 text-xl">Loading destination...</div>;
    }

    if (error) {
        return <div className="text-center p-10 text-red-600 bg-red-100 border border-red-400 rounded-md shadow-md">
            <h2 className="text-2xl font-semibold mb-2">Error</h2>
            <p>{error}</p>
            <Link to="/" className="mt-4 inline-block px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-700 transition-colors duration-300">
                Go back to Destinations
            </Link>
        </div>;
    }

    if (!destination) {
        return <div className="text-center p-10 text-gray-600">Destination not found. <Link to="/" className="text-blue-500 hover:underline">Go back home</Link></div>;
    }

    return (
        <div className="container mx-auto p-4">
            <Link to="/" className="text-blue-600 hover:text-blue-800 hover:underline mb-6 inline-flex items-center">
                <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-1" viewBox="0 0 20 20" fill="currentColor"><path fillRule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clipRule="evenodd" /></svg>
                Back to Destinations
            </Link>
            <div className="bg-white rounded-lg shadow-xl overflow-hidden">
                <img src={destination.imageUrl || 'https://via.placeholder.com/800x400?text=No+Image'} alt={destination.name} className="w-full h-64 md:h-96 object-cover" />
                <div className="p-6 md:p-8">
                    <h1 className="text-3xl md:text-4xl font-bold mb-3 text-gray-800">{destination.name}</h1>
                    <p className="text-gray-700 mb-6 leading-relaxed whitespace-pre-line">{destination.description}</p>

                    {!isDestinationUnlockedForUser && (
                        <button 
                            onClick={handleUnlockDestination}
                            disabled={actionInProgress}
                            className="mb-6 w-full px-6 py-3 bg-green-500 hover:bg-green-600 text-white font-semibold rounded-lg shadow-md transition-colors duration-300 disabled:bg-gray-400"
                        >
                            {actionInProgress ? 'Unlocking...' : 'Unlock this Destination to see Challenges'}
                        </button>
                    )}
                    
                    {isDestinationUnlockedForUser && (
                        <>
                            <h2 className="text-2xl font-semibold mb-4 text-gray-800">Challenges</h2>
                            {challenges && challenges.length > 0 ? (
                                <ul className="space-y-4">
                                    {challenges.map(challenge => {
                                        const isCompleted = completedChallengeIds.has(challenge.id);
                                        return (
                                            <li key={challenge.id} 
                                                className={`p-4 rounded-lg flex justify-between items-center transition-all duration-300 shadow-sm ${isCompleted ? 'bg-green-100 border-l-4 border-green-500' : 'bg-yellow-100 border-l-4 border-yellow-500 hover:bg-yellow-200 cursor-pointer'}`}
                                                onClick={() => !isCompleted && handleOpenChallenge({...challenge, completed: isCompleted})}
                                            >
                                                <div className="flex-grow">
                                                    <h3 className="font-semibold text-lg text-gray-700">{challenge.name}</h3>
                                                    <p className="text-sm text-gray-600">Type: {challenge.type} - {isCompleted ? <span className="font-semibold text-green-600">Completed</span> : <span className="font-semibold text-yellow-700">Pending</span>}</p>
                                                </div>
                                                {!isCompleted && 
                                                    <button 
                                                        onClick={(e) => { e.stopPropagation(); handleOpenChallenge({...challenge, completed: isCompleted}); }}
                                                        className="ml-4 px-3 py-1 bg-blue-500 text-white text-sm rounded hover:bg-blue-600 transition-colors duration-200 whitespace-nowrap"
                                                    >
                                                        View Details
                                                    </button>
                                                }
                                                {isCompleted && <span className="ml-4 text-2xl text-green-500">✓</span>}
                                            </li>
                                        );
                                    })}
                                </ul>
                            ) : (
                                <p className="text-gray-600 italic">No challenges available for this destination yet.</p>
                            )}
                        </>
                    )}
                </div>
            </div>
            {selectedChallenge && (
                <ChallengeModal 
                    challenge={selectedChallenge} 
                    onClose={handleCloseModal} 
                    onComplete={handleCompleteChallenge} 
                />
            )}
        </div>
    );
};

export default DestinationPage; 