import React, { useState } from 'react';

const GreetingLookup = () => {
    const [name, setName] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');

    const fetchGreeting = async () => {
        if (!name.trim()) return;

        try {
            const response = await fetch(`/greet/${name}`);
            if (!response.ok) {
                throw new Error(`Server error: ${response.status}`);
            }
            const text = await response.text();
            setMessage(text);
            setError('');
        } catch (err) {
            setMessage('');
            setError(`${name} is not currently a member. Use the Add a New Member function if you would like to add them.`);
        }
    };

    return (
        <div style={styles.container}>
            <h2>Find Greeting by Name</h2>
            <input
                type="text"
                value={name}
                placeholder="Enter name..."
                onChange={(e) => setName(e.target.value)}
                style={styles.input}
                data-testid="find-greeting-input"
            />
            <button
                onClick={fetchGreeting}
                style={styles.button}
                data-testid="find-greeting-button"
            >
                Get Greeting
            </button>
            {message && (
                <p style={styles.message} data-testid="greeting-message">
                    💬 {message}
                </p>
            )}
            {error && (
                <p style={styles.error} data-testid="greeting-error">
                    ❌ {error}
                </p>
                )}
        </div>
    );
};

const styles = {
    container: {
        background: '#f0f8ff',
        padding: '1rem',
        margin: '1rem',
        borderRadius: '10px',
        boxShadow: '2px 2px 10px #ccc',
        textAlign: 'center',
    },
    input: {
        padding: '0.5rem',
        marginRight: '1rem',
        borderRadius: '5px',
        border: '1px solid #ccc',
        fontSize: '1rem',
    },
    button: {
        padding: '0.5rem 1rem',
        background: '#4CAF50',
        color: '#fff',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
    },
    message: {
        marginTop: '1rem',
        color: '#2e8b57',
        fontWeight: 'bold',
    },
    error: {
        marginTop: '1rem',
        color: 'red',
    },
};

export default GreetingLookup;
