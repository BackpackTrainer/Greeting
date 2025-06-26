import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import GreetingList from './GreetingList';

beforeEach(() => {
    global.fetch = jest.fn(); // mock fetch globally
});

afterEach(() => {
    jest.resetAllMocks();
});

test('renders header and buttons', () => {
    render(<GreetingList />);

    expect(screen.getByTestId('greeting-header')).toHaveTextContent('All Greetings');
    expect(screen.getByTestId('get-all-members')).toBeInTheDocument();
    expect(screen.getByTestId('clear-display-button')).toBeInTheDocument();
});

test('fetches and displays greetings', async () => {
    const mockData = [
        { name: 'Bill', message: 'Welcome!' },
        { name: 'Alice', message: 'Hello!' },
    ];

    fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockData,
    });

    render(<GreetingList />);
    fireEvent.click(screen.getByTestId('get-all-members'));

    await waitFor(() => {
        const rows = screen.getAllByTestId('member-row');
        expect(rows.length).toBe(2);
        expect(rows[0]).toHaveTextContent('Bill');
        expect(rows[1]).toHaveTextContent('Alice');
    });
});

test('shows error on failed fetch', async () => {
    fetch.mockResolvedValueOnce({ ok: false });

    render(<GreetingList />);
    fireEvent.click(screen.getByTestId('get-all-members'));

    await waitFor(() => {
        expect(screen.getByTestId('error-message')).toHaveTextContent('Failed to fetch greetings');
    });
});

test('clears the display', async () => {
    // Populate with some greetings first
    const mockData = [{ name: 'Bill', message: 'Howdy!' }];
    fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockData,
    });

    render(<GreetingList />);
    fireEvent.click(screen.getByTestId('get-all-members'));

    // Wait for items to appear
    await waitFor(() => {
        expect(screen.getAllByTestId('member-row').length).toBe(1);
    });

    // Clear the display
    fireEvent.click(screen.getByTestId('clear-display-button'));

    // Expect no member rows
    await waitFor(() => {
        expect(screen.queryAllByTestId('member-row').length).toBe(0);
    });
});
