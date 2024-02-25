package ru.danilov.onlineWallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.danilov.onlineWallet.exceptions.ClientNotFoundException;
import ru.danilov.onlineWallet.model.Client;
import ru.danilov.onlineWallet.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(int id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.orElseThrow(ClientNotFoundException::new);
    }

    public Optional<Client> getClientByName(String name) {
        return clientRepository.findClientByName(name);
    }

    @Transactional
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Transactional
    public void updateClient(Client updateClient, int id) {
        updateClient.setId(id);
        clientRepository.save(updateClient);
    }

    @Transactional
    public void deleteClient(int id) {
        clientRepository.deleteById(id);
    }
}