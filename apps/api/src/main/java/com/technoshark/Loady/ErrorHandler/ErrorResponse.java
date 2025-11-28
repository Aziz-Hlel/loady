package com.technoshark.Loady.ErrorHandler;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    protected final boolean success = false;

    // @Nonnull
    protected String message;

    protected final LocalDateTime timestamp = LocalDateTime.now();

    // @Nonnull
    protected String path;

    protected int status;

}
