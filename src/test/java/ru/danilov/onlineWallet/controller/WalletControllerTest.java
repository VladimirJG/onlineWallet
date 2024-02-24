package ru.danilov.onlineWallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.danilov.onlineWallet.dto.WalletDto;
import ru.danilov.onlineWallet.model.Wallet;
import ru.danilov.onlineWallet.service.WalletService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @Mock
    private WalletService walletService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private WalletController walletController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    void getListWallets() {
        List<Wallet> wallets = Arrays.asList(new Wallet(), new Wallet());
        when(walletService.getAllWallets()).thenReturn(wallets);
        List<WalletDto> result = walletController.getListWallets();
        assertEquals(2, result.size());
    }


    @Test
    void getAllWalletsByClient() {
        int clientId = 1;
        List<Wallet> wallets = Arrays.asList(new Wallet(), new Wallet());
        when(walletService.getWalletByClientId(clientId)).thenReturn(wallets);
        List<WalletDto> result = walletController.getAllWalletsByClient(clientId);
        assertEquals(2, result.size());
    }

    @Test
    void getWalletByClient() {
        int clientId = 1;
        int walletId = 1;
        Wallet wallet = new Wallet();
        when(walletService.getWalletByClient(clientId, walletId)).thenReturn(wallet);
        WalletDto expectedDto = new WalletDto();
        when(modelMapper.map(wallet, WalletDto.class)).thenReturn(expectedDto);
        WalletDto result = walletController.getWalletByClient(clientId, walletId);
        assertNotNull(result);
        assertEquals(expectedDto, result);
    }

    @Test
    void saveNewWallet() throws Exception {
        mockMvc.perform(get("/wallet/{ownerId}", 10)).andExpect(status().isOk());
    }

    @Test
    void updateAmount() throws Exception {
        Wallet wallet = new Wallet();
        int walletId = 1;
        mockMvc.perform(patch("/wallet/{walletId}", walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(wallet)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteWallet() throws Exception {
        int walletId = 1;
        mockMvc.perform(delete("/wallet/{walletId}", walletId))
                .andExpect(status().isOk());
    }
}