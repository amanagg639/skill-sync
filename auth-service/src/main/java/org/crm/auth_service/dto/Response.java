package org.crm.auth_service.dto;

import org.crm.auth_service.enums.ApiStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class Response<T> {

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Status cant be empty")
    private ApiStatus status;

    @NotBlank(message = "Model cant be empty")
    private String message;

    @NotNull(message = "Data cant be null")
    private T data;

    @NotNull(message = "Date cant be null")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
