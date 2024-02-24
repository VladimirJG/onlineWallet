package ru.danilov.onlineWallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.danilov.onlineWallet.exceptions.WalletNotFoundException;
import ru.danilov.onlineWallet.model.Wallet;
import ru.danilov.onlineWallet.service.WalletService;
import ru.danilov.onlineWallet.util.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping()
    public List<Wallet> getListWallets() {
        return walletService.getAllWallets();
    }

    @GetMapping("/{id}")
    public List<Wallet> getAllWalletsByClient(@PathVariable("id") int id) {
        return walletService.getWalletByClientId(id);
    }

    @GetMapping("/{clientId}/{walletId}")
    public Wallet getWalletByClient(@PathVariable("clientId") int clientId, @PathVariable("walletId") int walletId) {
        return walletService.getWalletByClient(clientId, walletId);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(WalletNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Кошелек с таким id не был найден",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
