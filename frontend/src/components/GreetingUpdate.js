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
        borderColor: field.touched && !validateField(field) ? 'red' : 'black'
    });

    const handleFieldChange = (setter) => (e) => {
        setter({ value: e.target.value, touched: true });
    };

    return (
        <div>
            <h2>Update Greeting</h2>
            <div>
                <label>Name:</label>
                <input
                    type="text"
                    value={nameField.value}
                    onChange={handleFieldChange(setNameField)}
                    style={inputStyle(nameField)}
                    data-testid="update-name-input"
                />
            </div>
            <div>
                <label>Greeting:</label>
                <input
                    type="text"
                    value={messageField.value}
                    onChange={handleFieldChange(setMessageField)}
                    style={inputStyle(messageField)}
                    data-testid="update-greeting-input"
                />
            </div>
            <button
                onClick={handleUpdate}
                data-testid="update-greeting-button"
                style={{ backgroundColor: '#ffa500', padding: '8px', marginTop: '10px' }}
            >
                Update
            </button>

            {feedback && <p style={{ color: 'green' }} data-testid="add-result-message">{feedback}</p>}
            {error && <p style={{ color: 'red' }} data-testid="update-error-message">{error}</p>}
        </div>
    );
};

export default GreetingUpdate;
