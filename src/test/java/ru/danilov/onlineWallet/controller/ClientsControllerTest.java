package ru.danilov.onlineWallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.danilov.onlineWallet.model.Client;
import ru.danilov.onlineWallet.service.ClientService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ClientsController.class)
class ClientsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Test
    public void testGetAllClients() throws Exception {
        List<Client> clients = Arrays.asList(new Client(), new Client());
        when(clientService.getAllClients()).thenReturn(clients);
        mockMvc.perform(get("/client"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetClientById() throws Exception {
        int clientId = 1;
        Client client = new Client();
        when(clientService.getClientById(clientId)).thenReturn(client);
        mockMvc.perform(get("/client/{id}", clientId))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateSensor() throws Exception {
        Client client = new Client(1, "Nom", 2525L);
        mockMvc.perform(post("/client/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(client)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdate() throws Exception {
        int clientId = 1;
        Client client = new Client(1, "Nom", 2525L);
        mockMvc.perform(patch("/client/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(client)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDelete() throws Exception {
        int clientId = 1;
        mockMvc.perform(delete("/client/{id}", clientId))
                .andExpect(status().isOk());
    }
}