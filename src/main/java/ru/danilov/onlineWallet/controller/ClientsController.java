package ru.danilov.onlineWallet.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.danilov.onlineWallet.dto.ClientDto;
import ru.danilov.onlineWallet.exceptions.ClientNotCreateException;
import ru.danilov.onlineWallet.model.Client;
import ru.danilov.onlineWallet.service.ClientService;
import ru.danilov.onlineWallet.util.ClientValidator;
import ru.danilov.onlineWallet.util.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.danilov.onlineWallet.util.ErrorsUtil.returnErrors;

@RestController
@RequestMapping("/client")
public class ClientsController {
    private final ClientService clientService;
    private final ModelMapper modelMapper;
    private final ClientValidator clientValidator;

    @Autowired
    public ClientsController(ClientService clientService, ModelMapper modelMapper, ClientValidator clientValidator) {
        this.clientService = clientService;
        this.modelMapper = modelMapper;
        this.clientValidator = clientValidator;
    }

    @GetMapping()
    public List<ClientDto> getAllClients() {
        return clientService.getAllClients().stream()
                .map(this::convertToClientDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ClientDto getClientById(@PathVariable("id") int id) {
        return convertToClientDto(clientService.getClientById(id));
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> createSensor(@RequestBody @Valid ClientDto clientDto, BindingResult bindingResult) {
        Client client = convertToClient(clientDto);
        clientValidator.validate(client, bindingResult);
        if (bindingResult.hasErrors()) {
            returnErrors(bindingResult);
        }
        clientService.saveClient(client);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid ClientDto clientDto, @PathVariable("id") int id, BindingResult bindingResult) {
        Client updateClient = convertToClient(clientDto);
        clientValidator.validate(updateClient, bindingResult);
        clientService.updateClient(updateClient, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        clientService.deleteClient(id);
        return "Пользователь " + id + " удален!";
    }


    private ClientDto convertToClientDto(Client client) {
        return modelMapper.map(client, ClientDto.class);
    }

    private Client convertToClient(ClientDto client) {
        return modelMapper.map(client, Client.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(ClassNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Пользователь с таким id не был найден",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(ClientNotCreateException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}