package com.technoshark.Loady.Utils;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.annotation.Nonnull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    protected final boolean success = true;

    @Nonnull
    protected String message;

    // @Nonnull
    protected T data;

    @Builder.Default
    protected final LocalDateTime timestamp = LocalDateTime.now();

    protected String path;
    protected int status;

}