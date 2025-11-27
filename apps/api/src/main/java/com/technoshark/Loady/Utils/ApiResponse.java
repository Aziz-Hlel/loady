package com.technoshark.Loady.Utils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    protected boolean success;
    protected String message;
    protected T data;

    @Builder.Default
    protected LocalDateTime timestamp = LocalDateTime.now();

    protected String path;
    protected int status;

    @JsonInclude(JsonInclude.Include.NON_EMPTY) // Forgetting null safety: Not using @JsonInclude properly, sending
                                                // unnecessary null fields
    protected Map<String, Object> metadata;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected List<String> errors;
}