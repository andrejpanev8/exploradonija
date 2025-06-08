import React, { createContext, useContext, useState, useEffect } from 'react';
import { fetchUserProgress } from './api'; // For fetching full profile later

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null); // Will store { id, username, ...progressData }
    const [token, setToken] = useState(localStorage.getItem('jwtToken'));
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const initializeAuth = async () => {
            if (token) {
                try {
                    // Basic user info from token (sub claim for username)
                    const decodedToken = parseJwt(token);
                    if (decodedToken && decodedToken.sub) {
                         // Set basic user, full profile can be fetched on demand
                        setUser({ username: decodedToken.sub, id: decodedToken.userId /* if included */ }); 

                    } else {
                        // Invalid token structure
                        handleLogout();
                    }
                } catch (error) {
                    console.error("Token parsing/validation error on init:", error);
                    handleLogout(); // Clear invalid token and user state
                }
            }
            setIsLoading(false);
        };

        initializeAuth();
    }, [token]); // Rerun if token changes (e.g. on login/logout)

    const login = (authResponse) => {
        localStorage.setItem('jwtToken', authResponse.jwt);
        setToken(authResponse.jwt);
        setUser({ id: authResponse.userId, username: authResponse.username });

    };

    const logout = () => {
        localStorage.removeItem('jwtToken');
        setToken(null);
        setUser(null);
    };

    // Helper to parse JWT (basic, for claims extraction, not signature verification)
    const parseJwt = (tokenToParse) => {
        if (!tokenToParse) return null;
        try {
            const base64Url = tokenToParse.split('.')[1];
            if (!base64Url) return null;
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            }).join(''));
            return JSON.parse(jsonPayload);
        } catch (e) {
            console.error("Failed to parse JWT:", e);
            return null;
        }
    };
    

    // The value provided to context consumers
    const contextValue = {
        user,
        token,
        login,
        logout,
        isAuthenticated: !!user && !!token, // More robust check
        isLoading,

    };

    return (
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const contextValue = useContext(AuthContext);
    if (contextValue === null) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return contextValue;
}; 