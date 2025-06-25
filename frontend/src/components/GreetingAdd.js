import React, { useState } from 'react';
import { useValidatedInput } from '../hooks/useValidatedInput';
import { containerStyle, inputStyle, buttonStyle, messageStyle, errorStyle } from './styles/sharedStyles';

const GreetingAdd = () => {
    const nameField = useValidatedInput('');
    const messageField = useValidatedInput('');
    const [result, setResult] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async () => {
        const validName = nameField.validate();
        const validMessage = messageField.validate();

        if (!validName || !validMessage) {
            setError('Please enter all required information.');
            setResult('');
            return;
        }

        setError('');

        try {
            const response = await fetch('/greet/add', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name: nameField.value, message: messageField.value })
            });

            if (!response.ok) {
                throw new Error(`Failed to add greeting because the user already exists. To change the greeting for an existing member, use Update Greeting. Status ${response.status}`);
            }

            const data = await response.json();
            setResult(`New member ${data.name} added with message: ${data.message}`);
            nameField.reset();
            messageField.reset();
        } catch (err) {
            setError(err.message);
            setResult('');
        }
    };

    return (
        <div style={containerStyle}>
            <h2>Add a New Member</h2>
            <input
                type="text"
                placeholder="Name"
                value={nameField.value}
                onChange={(e) => nameField.setValue(e.target.value)}
                style={inputStyle(nameField.hasError)}
                data-testid="add-name-input"
            />
            <input
                type="text"
                placeholder="Greeting message"
                value={messageField.value}
                onChange={(e) => messageField.setValue(e.target.value)}
                style={inputStyle(messageField.hasError)}
                data-testid="add-greeting-input"
            />
            <button onClick={handleSubmit} style={buttonStyle()} data-testid="add-greeting-button">
                Add Greeting
            </button>
            {result && <p style={messageStyle} data-testid="add-result-message">{result}</p>}
            {error && <p style={errorStyle} data-testid="add-error-message">{error}</p>}
        </div>
    );
};

export default GreetingAdd;
