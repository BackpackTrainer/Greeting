import React, { useState } from 'react';

const GreetingAdd = () => {
    const [name, setName] = useState('');
    const [message, setMessage] = useState('');
    const [result, setResult] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async () => {
        if (!name.trim() || !message.trim()) {
            setError('Both name and message are required.');
            setResult('');
            return;
        }

        try {
            const response = await fetch('/greet/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name, message })
            });

            if (!response.ok) {
                throw new Error(`Failed to add greeting because the user already exists.  To change the greeting for an existing member, use Update Greeting.  Status ${response.status}`);
            }

            const data = await response.json();
            setResult(`✅ Greeting added for ${data.name}`);
            setError('');
            setName('');
            setMessage('');
        } catch (err) {
            setError(`❌ ${err.message}`);
            setResult('');
        }
    };

    return (
        <div style={styles.container}>
            <h2>Add a New Member</h2>
            <input
                type="text"
                placeholder="Name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                style={styles.input}
            />
            <input
                type="text"
                placeholder="Greeting message"
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                style={styles.input}
            />
            <button onClick={handleSubmit} style={styles.button}>Add Greeting</button>
            {result && <p style={styles.result}>{result}</p>}
            {error && <p style={styles.error}>{error}</p>}
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
        margin: '0.5rem',
        padding: '0.5rem',
        borderRadius: '5px',
        border: '1px solid #ccc',
        fontSize: '1rem',
        width: '80%',
    },
    button: {
        padding: '0.5rem 1rem',
        background: '#2196f3',
        color: '#fff',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        marginTop: '0.5rem'
    },
    result: {
        marginTop: '1rem',
        color: 'green',
        fontWeight: 'bold',
    },
    error: {
        marginTop: '1rem',
        color: 'red',
    }
};

export default GreetingAdd;
