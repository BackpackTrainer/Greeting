export const containerStyle = {
    background: '#fff8dc',
    padding: '1rem',
    margin: '1rem',
    borderRadius: '10px',
    boxShadow: '2px 2px 10px #ccc',
    textAlign: 'center',
};

export const inputStyle = (hasError) => ({
    padding: '0.5rem',
    margin: '0.5rem',
    borderRadius: '5px',
    border: `1px solid ${hasError ? 'red' : '#ccc'}`,
    width: '80%',
    fontSize: '1rem',
});

export const buttonStyle = (backgroundColor = '#2196f3') => ({
    padding: '0.5rem 1rem',
    background: backgroundColor,
    color: '#fff',
    border: 'none',
    borderRadius: '5px',
    cursor: 'pointer',
    marginTop: '0.5rem',
    fontWeight: 'bold',
});

export const messageStyle = {
    marginTop: '1rem',
    color: 'green',
    fontWeight: 'bold',
};

export const errorStyle = {
    marginTop: '1rem',
    color: 'red',
    fontWeight: 'bold',
};
