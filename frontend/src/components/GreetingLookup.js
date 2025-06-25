import React, { useState } from 'react';
import { useValidatedInput } from '../hooks/useValidatedInput';
import { containerStyle, inputStyle, buttonStyle, messageStyle, errorStyle } from './styles/sharedStyles';

const GreetingLookup = () => {
    const nameField = useValidatedInput('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');

    const fetchGreeting = async () => {
        const isValid = nameField.validate();
        if (!isValid) {
            setError("Name field may not be empty.");
            setMessage('');
            return;
        }

        setError('');
        setMessage('');

        try {
            const response = await fetch(`/greet/${nameField.value}`);
            if (!response.ok) {
                throw new Error(`Server error: ${response.status}`);
            }
            const text = await response.text();
            setMessage(text);
        } catch (err) {
            setError(`${nameField.value} is not currently a member. Use the Add a New Member function if you would like to add them.`);
        }
    };

    return (
        <div style={containerStyle}>
            <h2>Find Greeting by Name</h2>
            <input
                type="text"
                value={nameField.value}
                placeholder="Enter name..."
                onChange={(e) => nameField.setValue(e.target.value)}
                style={inputStyle(nameField.hasError)}
                data-testid="find-greeting-input"
            />
            <button
                onClick={fetchGreeting}
                style={buttonStyle('#4CAF50')}
                data-testid="find-greeting-button"
            >
                Get Greeting
            </button>

            {message && <p style={messageStyle} data-testid="result-message">{message}</p>}
            {error && <p style={errorStyle} data-testid="error-message">{error}</p>}
        </div>
    );
};

export default GreetingLookup;
