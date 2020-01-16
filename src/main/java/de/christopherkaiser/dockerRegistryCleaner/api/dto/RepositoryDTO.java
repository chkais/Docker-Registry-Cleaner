package de.christopherkaiser.dockerRegistryCleaner.api.dto;

import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class RepositoryDTO {
    @Key
    private List<String> tags;

    @Key
    private String name;
}
