import { useState } from 'react';

export const useValidatedInput = (initialValue = '') => {
    const [value, setValue] = useState(initialValue);
    const [hasError, setHasError] = useState(false);

    const validate = () => {
        if (!value.trim()) {
            setHasError(true);
            return false;
        }
        setHasError(false);
        return true;
    };

    const reset = () => {
        setValue('');
        setHasError(false);
    };

    return {
        value,
        setValue,
        hasError,
        validate,
        reset,
    };
};
