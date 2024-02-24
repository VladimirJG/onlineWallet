package ru.danilov.onlineWallet.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    @NotEmpty
    private String name;
    @NotNull
    @Min(value = 1000, message = "min value = 1000")
    @Max(value = 3000, message = "max value = 3000")
    private long password;
}