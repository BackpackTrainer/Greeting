import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import GreetingUpdate from './GreetingUpdate';

beforeEach(() => {
    global.fetch = jest.fn();
});

afterEach(() => {
    jest.resetAllMocks();
});

test('renders name and greeting input and button', () => {
    render(<GreetingUpdate />);
    expect(screen.getByTestId('update-name-input')).toBeInTheDocument();
    expect(screen.getByTestId('update-greeting-input')).toBeInTheDocument();
    expect(screen.getByTestId('update-greeting-button')).toBeInTheDocument();
});

test('shows error if both fields are empty', async () => {
    render(<GreetingUpdate />);
    fireEvent.click(screen.getByTestId('update-greeting-button'));

    await waitFor(() => {
        expect(screen.getByTestId('update-error-message')).toHaveTextContent('Please enter all required information.');
    });
});

test('shows error if name is missing', async () => {
    render(<GreetingUpdate />);
    fireEvent.change(screen.getByTestId('update-greeting-input'), { target: { value: 'Updated message' } });
    fireEvent.click(screen.getByTestId('update-greeting-button'));

    await waitFor(() => {
        expect(screen.getByTestId('update-error-message')).toHaveTextContent('Name is required.');
    });
});

test('shows error if message is missing', async () => {
    render(<GreetingUpdate />);
    fireEvent.change(screen.getByTestId('update-name-input'), { target: { value: 'Bill' } });
    fireEvent.click(screen.getByTestId('update-greeting-button'));

    await waitFor(() => {
        expect(screen.getByTestId('update-error-message')).toHaveTextContent('Greeting message is required.');
    });
});

test('displays success message on successful update', async () => {
    global.fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => ({ name: 'Bill', message: 'Updated message' }),
    });

    render(<GreetingUpdate />);
    fireEvent.change(screen.getByTestId('update-name-input'), { target: { value: 'Bill' } });
    fireEvent.change(screen.getByTestId('update-greeting-input'), { target: { value: 'Updated message' } });
    fireEvent.click(screen.getByTestId('update-greeting-button'));

    await waitFor(() => {
        expect(screen.getByTestId('update-result-message')).toHaveTextContent('Greeting for Bill was successfully updated.');
    });
});

test('displays 404 error from server', async () => {
    global.fetch.mockResolvedValueOnce({
        ok: false,
        status: 404,
        text: async () => 'Bill is not a member.',
    });

    render(<GreetingUpdate />);
    fireEvent.change(screen.getByTestId('update-name-input'), { target: { value: 'Bill' } });
    fireEvent.change(screen.getByTestId('update-greeting-input'), { target: { value: 'Hello again' } });
    fireEvent.click(screen.getByTestId('update-greeting-button'));

    await waitFor(() => {
        expect(screen.getByTestId('update-error-message')).toHaveTextContent('Bill is not a member.');
    });
});

test('displays unexpected error', async () => {
    global.fetch.mockRejectedValueOnce(new Error('Network error'));

    render(<GreetingUpdate />);
    fireEvent.change(screen.getByTestId('update-name-input'), { target: { value: 'Bill' } });
    fireEvent.change(screen.getByTestId('update-greeting-input'), { target: { value: 'Retry this' } });
    fireEvent.click(screen.getByTestId('update-greeting-button'));

    await waitFor(() => {
        expect(screen.getByTestId('update-error-message')).toHaveTextContent('An unexpected error occurred.');
    });
});
