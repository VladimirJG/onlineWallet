package ru.danilov.onlineWallet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wallet")
public class Wallet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "amount")
    private int amount;
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client owner;
    @Transient
    @Enumerated(EnumType.STRING)
    private Operation operation;
   /* @Transient
    @Version
    private int version;

    public Wallet(int id, int amount, Client owner, Operation operation) {
        this.id = id;
        this.amount = amount;
        this.owner = owner;
        this.operation = operation;
    }*/
}