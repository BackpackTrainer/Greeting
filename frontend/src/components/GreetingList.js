import React, { useState } from 'react';

const GreetingList = () => {
    const [greetings, setGreetings] = useState([]);
    const [error, setError] = useState('');

    const fetchGreetings = async () => {
        try {
            const response = await fetch('/greet/all');
            if (!response.ok) throw new Error('Failed to fetch greetings');
            const data = await response.json();
            setGreetings(data);
            setError('');
        } catch (err) {
            setError(err.message);
        }
    };

    const clearUI = () => {
        setGreetings([]);
        setError('');
    };

    return (
        <div style={styles.container}>
            <h2>All Greetings</h2>
            <button onClick={fetchGreetings} style={styles.button}>Get All Members</button>
            <button onClick={clearUI} style={{ ...styles.button, backgroundColor: '#ff9800' }}>Clear Display</button>
            {error && <p style={styles.error}>❌ {error}</p>}
            <ul style={styles.list}>
                {greetings.map((greeting, index) => (
                    <li key={index}>
                        <strong>{greeting.name}:</strong> {greeting.message}
                    </li>
                ))}
            </ul>
        </div>
    );
};

const styles = {
    container: {
        background: '#e0f7fa',
        padding: '1rem',
        margin: '1rem',
        borderRadius: '10px',
        boxShadow: '2px 2px 10px #ccc',
        textAlign: 'center',
    },
    button: {
        margin: '0.5rem',
        padding: '0.5rem 1rem',
        background: '#4CAF50',
        color: 'white',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
    },
    list: {
        listStyleType: 'none',
        padding: 0,
    },
    error: {
        marginTop: '1rem',
        color: 'red',
    },
};

export default GreetingList;
