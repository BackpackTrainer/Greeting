import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import GreetingAdd from './GreetingAdd';

// Mock the custom hook used in the component
jest.mock('../hooks/useValidatedInput', () => {
    return {
        useValidatedInput: (initialValue = '') => {
            const [value, setValue] = require('react').useState(initialValue);
            return {
                value,
                setValue,
                hasError: false,
                validate: () => value.trim() !== '',
                reset: () => setValue('')
            };
        }
    };
});

beforeEach(() => {
    // Ensure fetch is mocked fresh for each test
    global.fetch = jest.fn();
});

afterEach(() => {
    jest.resetAllMocks();
});

test('renders inputs and button', () => {
    render(<GreetingAdd />);

    expect(screen.getByTestId('add-name-input')).toBeInTheDocument();
    expect(screen.getByTestId('add-greeting-input')).toBeInTheDocument();
    expect(screen.getByTestId('add-greeting-button')).toBeInTheDocument();
});

test('shows error when fields are empty', async () => {
    render(<GreetingAdd />);

    const button = screen.getByTestId('add-greeting-button');
    fireEvent.click(button);

    expect(await screen.findByTestId('add-error-message')).toHaveTextContent('Please enter all required information.');
});

test('shows success message on valid submission', async () => {
    render(<GreetingAdd />);

    // Fill in inputs
    fireEvent.change(screen.getByTestId('add-name-input'), { target: { value: 'Bill' } });
    fireEvent.change(screen.getByTestId('add-greeting-input'), { target: { value: 'Hello there!' } });

    // Mock fetch success
    global.fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => ({ name: 'Bill', message: 'Hello there!' }),
    });

    fireEvent.click(screen.getByTestId('add-greeting-button'));

    await waitFor(() =>
        expect(screen.getByTestId('add-result-message')).toHaveTextContent('New member Bill added with message: Hello there!')
    );
});

test('shows error when fetch fails', async () => {
    render(<GreetingAdd />);

    fireEvent.change(screen.getByTestId('add-name-input'), { target: { value: 'Bill' } });
    fireEvent.change(screen.getByTestId('add-greeting-input'), { target: { value: 'Hi!' } });

    global.fetch.mockResolvedValueOnce({ ok: false, status: 409 });

    fireEvent.click(screen.getByTestId('add-greeting-button'));

    await waitFor(() =>
        expect(screen.getByTestId('add-error-message')).toHaveTextContent(/failed to add greeting/i)
    );
});
