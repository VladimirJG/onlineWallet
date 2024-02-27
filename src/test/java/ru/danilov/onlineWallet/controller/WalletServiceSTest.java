package ru.danilov.onlineWallet.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.danilov.onlineWallet.model.Operation;
import ru.danilov.onlineWallet.model.Wallet;
import ru.danilov.onlineWallet.repository.WalletRepository;
import ru.danilov.onlineWallet.service.WalletService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class WalletServiceSTest {
    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletRepository walletRepository;


    @Test
    public void testUpdateWalletsAmountConcurrently() {
//        walletService.updateWalletsAmount(new Wallet(1, 500, new Client(), Operation.MINUS), 1);
        Wallet wallet = walletRepository.findWalletById(1).orElseThrow();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                synchronized (wallet) {
                    for (int j = 0; j < 3; j++) {
                        Wallet updateWallet = new Wallet();
                        updateWallet.setAmount(100);
                        updateWallet.setOperation(Operation.PLUS);
                        ResponseEntity<HttpStatus> response = walletService.updateWalletsAmount(updateWallet, 1);
                    }
                }
            });
        }
        executor.shutdown();
    }

    @Test
    public void testUpdateWalletsAmount() {
//        walletService.updateWalletsAmount(new Wallet(1, 500, new Client(), Operation.MINUS), 1);
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                Wallet updateWallet = new Wallet();
                updateWallet.setAmount(100);
                updateWallet.setOperation(Operation.PLUS);
                ResponseEntity<HttpStatus> response = walletService.updateWalletsAmount(updateWallet, 1);
            });
        }
        executor.shutdown();
    }
}