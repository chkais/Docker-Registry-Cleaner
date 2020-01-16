package de.christopherkaiser.dockerRegistryCleaner.api;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class V1Compatibility {
    private String id;
    private LocalDateTime created;
}
