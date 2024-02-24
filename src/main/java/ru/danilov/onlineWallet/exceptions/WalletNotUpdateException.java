package ru.danilov.onlineWallet.exceptions;

public class WalletNotUpdateException extends RuntimeException {
    public WalletNotUpdateException(String message) {
        super(message);
    }
}