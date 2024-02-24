package ru.danilov.onlineWallet.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.danilov.onlineWallet.exceptions.ClientNotCreateException;

import java.util.List;

public class ErrorsUtil {
    public static void returnErrors(BindingResult bindingResult) {
        StringBuilder errorMsg = new StringBuilder();

        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errorMsg.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append(";");
        }
        throw new ClientNotCreateException(errorMsg.toString());
    }
}