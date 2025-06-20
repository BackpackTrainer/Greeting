import React, { useState } from 'react';

const GreetingUpdate = () => {
    const [name, setName] = useState('');
    const [message, setMessage] = useState('');
    const [feedback, setFeedback] = useState('');
    const [error, setError] = useState('');

    const handleUpdate = async () => {
        if (!name.trim() || !message.trim()) {
            setError('Name and message cannot be empty.');
            setFeedback('');
            return;
        }

        try {
            const response = await fetch('/greet', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name, message }),
            });

            if (response.ok) {
                const data = await response.json();
                setFeedback(`✅ Greeting for ${data.name} was successfully updated.`);
                setError('');
            } else if (response.status === 404) {
                setFeedback('');
                setError(`❌ No update made: Member "${name}" was not found in the database.`);
            } else {
                throw new Error(`Unexpected status: ${response.status}`);
            }
        } catch (err) {
            setFeedback('');
            setError(`An error occurred: ${err.message}`);
        }
    };

    return (
        <div style={styles.container}>
            <h2>Update Greeting</h2>
            <input
                type="text"
                placeholder="Enter member name..."
                value={name}
                onChange={(e) => setName(e.target.value)}
                style={styles.input}
                data-testid="update-name-input"
            />
            <input
                type="text"
                placeholder="Enter new greeting..."
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                style={styles.input}
                data-testid="update-greeting-input"
            />
            <button
                onClick={handleUpdate}
                style={styles.button}
                data-testid="update-greeting-button"
            >
                Update
            </button>

            {feedback && (
                <p style={styles.feedback} data-testid="update-result-message">
                    {feedback}
                </p>
            )}
            {error && (
                <p style={styles.error} data-testid="update-error-message">
                    {error}
                </p>
            )}
        </div>
    );
};

const styles = {
    container: {
        background: '#fff8dc',
        padding: '1rem',
        margin: '1rem',
        borderRadius: '10px',
        boxShadow: '2px 2px 10px #ccc',
        textAlign: 'center',
    },
    input: {
        padding: '0.5rem',
        margin: '0.5rem',
        borderRadius: '5px',
        border: '1px solid #ccc',
        width: '80%',
        fontSize: '1rem',
    },
    button: {
        padding: '0.5rem 1rem',
        background: '#ffa500',
        color: '#fff',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        fontWeight: 'bold',
    },
    feedback: {
        marginTop: '1rem',
        color: 'green',
        fontWeight: 'bold',
    },
    error: {
        marginTop: '1rem',
        color: 'red',
        fontWeight: 'bold',
    },
};

export default GreetingUpdate;
