package ru.danilov.onlineWallet.exceptions;

public class ClientNotCreateException extends RuntimeException {
    public ClientNotCreateException(String message) {
        super(message);
    }
}