package de.christopherkaiser.dockerRegistryCleaner.api.dto;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class CatalogDTO {
    @Key
    String[] repositories;
}
