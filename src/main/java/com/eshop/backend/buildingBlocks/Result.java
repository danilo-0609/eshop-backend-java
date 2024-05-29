package com.eshop.backend.buildingBlocks;

import java.util.Optional;

public class Result<T> {
    private final boolean isError;
    private final T value;
    private final Error error;

    private Result(boolean isError, T value, Error error) {
        this.isError = isError;
        this.value = value;
        this.error = error;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(false, value, null);
    }

    public static <T> Result<T> success() {
        return new Result<>(false, null, null);
    }

    public static <T> Result<T> error(Error error) {
        return new Result<>(true, null, error);
    }

    public boolean isError() {
        return isError;
    }

    public Optional<T> getValue() {
        return Optional.ofNullable(value);
    }

    public Error getFirstError() {
        return error;
    }
}