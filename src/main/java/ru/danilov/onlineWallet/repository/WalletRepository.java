package ru.danilov.onlineWallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danilov.onlineWallet.model.Client;
import ru.danilov.onlineWallet.model.Wallet;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    List<Wallet> findWalletsByOwnerId(int id);
}