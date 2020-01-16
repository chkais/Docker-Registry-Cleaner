package de.christopherkaiser.dockerRegistryCleaner.api;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class FsLayer {
    @Key
    String blobSum;
}
