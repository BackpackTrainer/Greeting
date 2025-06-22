import React, { useState } from 'react';

const GreetingUpdate = () => {
    const [nameField, setNameField] = useState({ value: '', touched: false });
    const [messageField, setMessageField] = useState({ value: '', touched: false });
    const [feedback, setFeedback] = useState('');
    const [error, setError] = useState('');

    const validateField = (field) => {
        return field.value.trim() !== '';
    };

    const handleUpdate = async () => {
        console.log("handleUpdate() function invoked");

        const validName = validateField(nameField);
        const validMessage = validateField(messageField);

        // Update touched state for both fields
        setNameField(prev => ({ ...prev, touched: true }));
        setMessageField(prev => ({ ...prev, touched: true }));

        // Determine error message based on which fields are missing
        if (!validName && !validMessage) {
            setFeedback('');
            setError('Please enter all required information.');
            return;
        }
        if (!validName) {
            setFeedback('');
            setError('Name is required.');
            return;
        }
        if (!validMessage) {
            setFeedback('');
            setError('Greeting message is required.');
            return;
        }

        try {
            const response = await fetch('/greet', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name: nameField.value, message: messageField.value }),
            });

            if (response.ok) {
                const result = await response.json();
                console.log("Update successful:", result);
                setFeedback(`Greeting for ${result.name} was successfully updated.`);
                setError('');
            } else if (response.status === 404) {
                const errorMessage = await response.text();
                setError(errorMessage);
                setFeedback('');
            } else {
                setError(`Unexpected status: ${response.status}`);
                setFeedback('');
            }
        } catch (err) {
            console.error("An unexpected error occurred:", err);
            setError("An unexpected error occurred.");
            setFeedback('');
        }
    };

    const inputStyle = (field) => ({
        borderColor: field.touched && !validateField(field) ? 'red' : 'black',
        borderRadius: '5px',
        padding: '0.5rem',
        fontSize: '1rem',
        width: '100%'
    });

    const handleFieldChange = (setter) => (e) => {
        setter({ value: e.target.value, touched: true });
    };

    return (
        <div style={{
            backgroundColor: '#DFF2BF',
            padding: '1rem',
            margin: '1rem',
            borderRadius: '10px',
            boxShadow: '2px 2px 10px #ccc',
        }}>
            <h2 style={{ textAlign: 'center' }}>Update Greeting</h2>

            <div style={{ marginBottom: '1rem' }}>
                <input
                    type="text"
                    value={nameField.value}
                    onChange={handleFieldChange(setNameField)}
                    style={inputStyle(nameField)}
                    placeholder="Name"
                    data-testid="update-name-input"
                />
            </div>

            <div style={{ display: 'flex', alignItems: 'center' }}>
                <input
                    type="text"
                    value={messageField.value}
                    onChange={handleFieldChange(setMessageField)}
                    style={inputStyle(messageField)}
                    placeholder="Greeting message"
                    data-testid="update-greeting-input"
                />
                <button
                    onClick={handleUpdate}
                    data-testid="update-greeting-button"
                    style={{
                        backgroundColor: '#4CAF50',
                        color: 'white',
                        padding: '0.5rem 1rem',
                        border: 'none',
                        borderRadius: '5px',
                        cursor: 'pointer',
                        fontWeight: 'bold',
                        marginLeft: '1rem'
                    }}
                >
                    Update
                </button>
            </div>

            {feedback && <p style={{ color: 'green' }} data-testid="add-result-message">{feedback}</p>}
            {error && <p style={{ color: 'red' }} data-testid="update-error-message">{error}</p>}
        </div>
    );
};

export default GreetingUpdate;
