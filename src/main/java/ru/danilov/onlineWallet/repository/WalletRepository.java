package ru.danilov.onlineWallet.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import ru.danilov.onlineWallet.model.Wallet;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    List<Wallet> findWalletsByOwnerId(int id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Wallet> findWalletById(int walletId);
}