import React, { useState } from 'react';

const ChallengeModal = ({ challenge, onClose, onComplete }) => {
  if (!challenge) return null;

  const [submissionText, setSubmissionText] = useState(''); // For quiz/puzzle answers
  const [file, setFile] = useState(null); // For photo uploads
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async () => {
    setIsSubmitting(true);
    setError('');
    try {
      let dataToSend = submissionText;
      if (challenge.type === 'PHOTO') {
        // Photo submission logic would be different (FormData, etc.)
        // For now, we are not sending file data, just marking complete.
        // In a real app, onComplete might take FormData or handle file upload separately.
        console.log("Photo challenge submission - file handling not implemented in this step.");
        dataToSend = null; // Or some placeholder if backend expects something
      }
      await onComplete(challenge.id, dataToSend);
      // onClose(); // Consider if modal should always close or only on success from parent
    } catch (err) {
        setError(err.message || "Failed to submit challenge.");
    }
    setIsSubmitting(false);
  };

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const renderChallengeContent = () => {
    switch (challenge.type) {
      case 'PHOTO':
        return (
          <div className="space-y-2">
            <p className="italic text-gray-600">Take a photo that meets the challenge criteria and upload it!</p>
            <input type="file" accept="image/*" onChange={handleFileChange} className="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-indigo-50 file:text-indigo-700 hover:file:bg-indigo-100"/>
            {file && <p className="text-xs text-gray-500">Selected: {file.name}</p>}
            <p className="text-xs text-orange-500 italic mt-1">Note: Actual photo upload is not implemented in this step. Completing will mark it done.</p>
          </div>
        );
      case 'QUIZ':
        return (
          <div className="space-y-2">
            <p className="italic text-gray-600">Answer the question below:</p>
            <textarea 
              value={submissionText}
              onChange={(e) => setSubmissionText(e.target.value)}
              placeholder="Your answer here..."
              className="w-full p-2 border border-gray-300 rounded-md focus:ring-indigo-500 focus:border-indigo-500"
              rows={3}
            />
          </div>
        );
      case 'PUZZLE':
        return (
            <div className="space-y-2">
                <p className="italic text-gray-600">Solve the puzzle based on the description and enter your solution:</p>
                <input 
                    type="text"
                    value={submissionText}
                    onChange={(e) => setSubmissionText(e.target.value)}
                    placeholder="Puzzle solution..."
                    className="w-full p-2 border border-gray-300 rounded-md focus:ring-indigo-500 focus:border-indigo-500"
                />
            </div>
        );
      default:
        return <p className="italic text-gray-600">This challenge type ({challenge.type}) interaction is not fully implemented yet.</p>;
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-60 flex items-center justify-center p-4 z-[100] backdrop-blur-sm">
      <div className="bg-white p-6 rounded-lg shadow-xl max-w-lg w-full transform transition-all duration-300 ease-out scale-100">
        <div className="flex justify-between items-center mb-4">
            <h2 className="text-2xl font-bold text-gray-800">{challenge.name}</h2>
            <button onClick={onClose} className="text-gray-400 hover:text-gray-600">
                <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" /></svg>
            </button>
        </div>
        <p className="text-gray-700 mb-2 whitespace-pre-line">{challenge.description}</p>
        <p className="text-xs text-indigo-500 mb-4 uppercase tracking-wider font-semibold">Type: {challenge.type}</p>
        
        <div className="my-6 p-4 bg-gray-50 rounded-md min-h-[100px]">
            {renderChallengeContent()}
        </div>

        {error && <p className="text-sm text-red-600 bg-red-100 p-2 rounded text-center mb-3">{error}</p>}

        <div className="mt-6 flex flex-col sm:flex-row justify-end space-y-3 sm:space-y-0 sm:space-x-3">
          <button 
            onClick={onClose} 
            className="px-4 py-2 bg-gray-200 hover:bg-gray-300 text-gray-800 rounded-md transition-colors duration-300 w-full sm:w-auto order-2 sm:order-1"
          >
            Close
          </button>
          {!challenge.completed && (
            <button 
              onClick={handleSubmit} 
              disabled={isSubmitting}
              className="px-6 py-2 bg-green-500 hover:bg-green-600 text-white font-semibold rounded-md transition-colors duration-300 w-full sm:w-auto order-1 sm:order-2 disabled:bg-gray-400"
            >
              {isSubmitting ? 'Submitting...' : 'Mark as Completed / Submit'}
            </button>
          )}
          {challenge.completed && (
             <p className="px-4 py-2 bg-green-100 text-green-700 rounded-md text-center font-semibold w-full sm:w-auto order-1 sm:order-2">✓ Completed</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default ChallengeModal; 