package de.christopherkaiser.dockerRegistryCleaner.api.dto;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class V1CompatibilityDTO {
    @Key
    private String created;
    @Key
    private String id;
}
