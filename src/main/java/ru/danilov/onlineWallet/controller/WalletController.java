package ru.danilov.onlineWallet.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.danilov.onlineWallet.dto.WalletDto;
import ru.danilov.onlineWallet.exceptions.WalletNotFoundException;
import ru.danilov.onlineWallet.exceptions.WalletNotUpdateException;
import ru.danilov.onlineWallet.model.Wallet;
import ru.danilov.onlineWallet.service.WalletService;
import ru.danilov.onlineWallet.util.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    private final WalletService walletService;
    private final ModelMapper modelMapper;

    @Autowired
    public WalletController(WalletService walletService, ModelMapper modelMapper) {
        this.walletService = walletService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<WalletDto> getListWallets() {
        return walletService.getAllWallets().stream()
                .map(this::convertToWalletDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public List<WalletDto> getAllWalletsByClient(@PathVariable("id") int id) {
        return walletService.getWalletByClientId(id).stream()
                .map(this::convertToWalletDto).collect(Collectors.toList());
    }

    @GetMapping("/{clientId}/{walletId}")
    public WalletDto getWalletByClient(@PathVariable("clientId") int clientId, @PathVariable("walletId") int walletId) {
        return convertToWalletDto(walletService.getWalletByClient(clientId, walletId));
    }

    @PostMapping("/{ownerId}")
    public ResponseEntity<HttpStatus> saveNewWallet(@PathVariable("ownerId") int ownerId) {
        walletService.saveNewWallet(ownerId);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PatchMapping("/{walletId}")
    public ResponseEntity<HttpStatus> updateAmount(@RequestBody @Valid Wallet wallet, @PathVariable("walletId") int walletId) {
        return walletService.updateWalletsAmount(wallet, walletId);
    }

    @DeleteMapping("/{walletId}")
    public ResponseEntity<HttpStatus> deleteWallet(@PathVariable("walletId") int walletId) {
        return walletService.deleteWallet(walletId);
    }

    private WalletDto convertToWalletDto(Wallet wallet) {
        return modelMapper.map(wallet, WalletDto.class);
    }


    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(WalletNotFoundException ignoredE) {
        ErrorResponse response = new ErrorResponse(
                "Кошелек с таким id не был найден",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(WalletNotUpdateException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}