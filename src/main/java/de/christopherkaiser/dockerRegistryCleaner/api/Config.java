package de.christopherkaiser.dockerRegistryCleaner.api;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class Config {
    @Key
    String mediaType;

    @Key
    int size;

    @Key
    String digest;
}
