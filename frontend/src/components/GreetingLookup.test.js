import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import GreetingLookup from './GreetingLookup';

// Mock the useValidatedInput hook
jest.mock('../hooks/useValidatedInput', () => {
    return {
        useValidatedInput: (initialValue = '') => {
            const [value, setValue] = require('react').useState(initialValue);
            return {
                value,
                setValue,
                hasError: false,
                validate: () => value.trim() !== '',
                reset: () => setValue(''),
            };
        },
    };
});

beforeEach(() => {
    global.fetch = jest.fn();
});

afterEach(() => {
    jest.resetAllMocks();
});

test('renders input and button', () => {
    render(<GreetingLookup />);
    expect(screen.getByTestId('find-greeting-input')).toBeInTheDocument();
    expect(screen.getByTestId('find-greeting-button')).toBeInTheDocument();
});

test('shows error if name is empty', async () => {
    render(<GreetingLookup />);
    fireEvent.click(screen.getByTestId('find-greeting-button'));

    await waitFor(() =>
        expect(screen.getByTestId('find-error-message')).toHaveTextContent('Name field may not be empty.')
    );
});

test('displays message on successful lookup', async () => {
    global.fetch.mockResolvedValueOnce({
        ok: true,
        text: async () => 'Hello Bill!',
    });

    render(<GreetingLookup />);
    fireEvent.change(screen.getByTestId('find-greeting-input'), { target: { value: 'Bill' } });
    fireEvent.click(screen.getByTestId('find-greeting-button'));

    await waitFor(() =>
        expect(screen.getByTestId('find-result-message')).toHaveTextContent('Hello Bill!')
    );
});

test('displays error when member not found', async () => {
    global.fetch.mockResolvedValueOnce({ ok: false, status: 404 });

    render(<GreetingLookup />);
    fireEvent.change(screen.getByTestId('find-greeting-input'), { target: { value: 'Ghost' } });
    fireEvent.click(screen.getByTestId('find-greeting-button'));

    await waitFor(() =>
        expect(screen.getByTestId('find-error-message')).toHaveTextContent('Ghost is not currently a member')
    );
});
