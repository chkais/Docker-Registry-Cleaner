package de.christopherkaiser.dockerRegistryCleaner.api;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Repository {
    List<Manifest> tags;
    LocalDateTime lastModified;
    String name;
}
