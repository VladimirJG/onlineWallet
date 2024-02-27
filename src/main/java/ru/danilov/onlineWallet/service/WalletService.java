package ru.danilov.onlineWallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.danilov.onlineWallet.exceptions.WalletNotFoundException;
import ru.danilov.onlineWallet.exceptions.WalletNotUpdateException;
import ru.danilov.onlineWallet.model.Operation;
import ru.danilov.onlineWallet.model.Wallet;
import ru.danilov.onlineWallet.repository.WalletRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class WalletService {
    private final WalletRepository walletRepository;
    private final ClientService clientService;

    @Autowired
    public WalletService(WalletRepository walletRepository, ClientService clientService) {
        this.walletRepository = walletRepository;
        this.clientService = clientService;
    }

    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    public Wallet getWalletById(int id) {
        Optional<Wallet> optionalWallet = walletRepository.findById(id);
        return optionalWallet.orElseThrow(WalletNotFoundException::new);
    }

    public List<Wallet> getWalletByClientId(int id) {
        return walletRepository.findWalletsByOwnerId(id);
    }

    public Wallet getWalletByClient(int clientId, int walletId) {
        Optional<Wallet> first = walletRepository.findWalletsByOwnerId(clientId)
                .stream().filter(wallet -> wallet.getId() == walletId).findFirst();
        return first.orElseThrow(WalletNotFoundException::new);
    }

    @Transactional
    public void saveNewWallet(int ownerId) {
        Wallet wallet = new Wallet();
        wallet.setAmount(0);
        wallet.setOwner(clientService.getClientById(ownerId));
        walletRepository.save(wallet);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<HttpStatus> updateWalletsAmount(Wallet updateWallet, int walletId) {

        Wallet walletById = walletRepository.findWalletById(walletId).orElseThrow(WalletNotFoundException::new);
        boolean equals = updateWallet.getOperation().equals(Operation.MINUS);
        if (equals) {
            int newAmount = walletById.getAmount() - updateWallet.getAmount();
            if (newAmount < 0) {
                throw new WalletNotUpdateException("Запрошенная к снятию сумма превышает лимит кошелька");
            } else {
                walletById.setAmount(walletById.getAmount() - updateWallet.getAmount());
                walletRepository.save(walletById);
                return ResponseEntity.ok(HttpStatus.OK);
            }
        } else {
            walletById.setAmount(walletById.getAmount() + updateWallet.getAmount());
            walletRepository.save(walletById);
            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<HttpStatus> deleteWallet(int walletId) {
        getWalletByClientId(walletId);
        walletRepository.deleteById(walletId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}