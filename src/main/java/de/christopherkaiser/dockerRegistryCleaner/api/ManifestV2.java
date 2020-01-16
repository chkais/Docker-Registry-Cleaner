package de.christopherkaiser.dockerRegistryCleaner.api;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class ManifestV2 {
    @Key
    Config config;

    @Key
    String mediaType;


}

