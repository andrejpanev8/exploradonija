const API_BASE_URL = 'http://explorandija.local/api'; // Adjust if your backend runs on a different port

// Helper function for API requests
const request = async (url, options = {}) => {
  const token = localStorage.getItem('jwtToken');
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  try {
    const response = await fetch(url, {
      ...options,
      headers,
    });
    if (!response.ok) {
      // Try to parse error response, otherwise use statusText
      let errorData;
      try {
        errorData = await response.json();
      } catch (e) {
        errorData = { message: response.statusText, status: response.status };
      }
      throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
    }
    // Handle cases where response might be empty (e.g., 204 No Content)
    if (response.status === 204) {
        return null; 
    }
    // For text responses (like simple success messages from backend)
    const contentType = response.headers.get("content-type");
    if (contentType && contentType.indexOf("application/json") === -1 && contentType.indexOf("text/plain") === -1) { // Allow text/plain too
        // If not JSON or plain text, and not a 204, could be an issue or unhandled type
        // For now, we try to return text(), but robust handling might differ
        console.warn(`Received non-JSON/text response type: ${contentType}`);
    }
    // Try to parse as JSON first, if fails and it's text, return as text.
    // This primarily handles the case where backend sends simple strings like "Success"
    try {
        // Clone response to be able to read it twice (once as JSON, once as text if JSON fails)
        const clonedResponse = response.clone();
        return await clonedResponse.json();
    } catch (jsonError) {
        // If JSON parsing fails, and it's a text type, return the text content.
        if (contentType && (contentType.indexOf("text/plain") !== -1 || contentType.indexOf("text/html") !== -1)) {
            return response.text(); 
        }
        // If it's not text either, or another error, rethrow the original JSON error or a new one.
        console.error('Failed to parse as JSON and not plain text:', jsonError);
        throw new Error('Received response was not valid JSON and not identified as plain text.');
    }
  } catch (error) {
    console.error('API call error:', error.message);
    throw error;
  }
};

// Authentication Endpoints
export const loginUser = (credentials) => {
  return request(`${API_BASE_URL}/auth/login`, { method: 'POST', body: JSON.stringify(credentials) });
};

export const registerUser = (userData) => {
  return request(`${API_BASE_URL}/auth/register`, { method: 'POST', body: JSON.stringify(userData) });
};

// Destination Endpoints
export const fetchDestinations = () => {
  return request(`${API_BASE_URL}/destinations`);
};

export const fetchDestinationById = (id) => {
  return request(`${API_BASE_URL}/destinations/${id}`);
};

// User Progress Endpoints
export const unlockDestination = (destinationId) => {
    return request(`${API_BASE_URL}/progress/destinations/unlock`, { 
        method: 'POST', 
        body: JSON.stringify({ destinationId })
    });
};

export const completeUserChallenge = (challengeId, submissionText) => { 
  return request(`${API_BASE_URL}/progress/challenges/complete`, { 
    method: 'POST', 
    body: JSON.stringify({ challengeId, submission: submissionText })
  });
};

export const fetchUserProgress = () => {
    return request(`${API_BASE_URL}/progress/me`);
};

// Challenge Endpoints
export const fetchChallengesByDestinationId = (destinationId) => {
  return request(`${API_BASE_URL}/challenges/destination/${destinationId}`);
};

// This needs to be properly implemented in the backend to associate with the logged-in user
export const completeChallenge = (challengeId) => { 
  // The backend should ideally infer the user from the JWT
  return request(`${API_BASE_URL}/challenges/${challengeId}/complete`, { method: 'PATCH' }); 
};

// User Endpoints
export const fetchUserByUsername = (username) => {
  // This might be a protected endpoint if it returns sensitive info
  return request(`${API_BASE_URL}/users/username/${username}`);
};

// createUser is now registerUser via /auth/register
// export const createUser = (userData) => {
//   return request(`${API_BASE_URL}/users`, { 
//     method: 'POST', 
//     body: JSON.stringify(userData) 
//   });
// };

// Example of a protected route to get current user's profile
export const fetchUserProfile = () => {
    return request(`${API_BASE_URL}/users/me`); // Assuming a /me endpoint exists or will be created
};

// Add more functions for other API endpoints as you build them (updateDestination, deleteChallenge, login, etc.) 