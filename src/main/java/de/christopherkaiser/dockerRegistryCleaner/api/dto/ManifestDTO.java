package de.christopherkaiser.dockerRegistryCleaner.api.dto;

import com.google.api.client.util.Key;
import de.christopherkaiser.dockerRegistryCleaner.api.FsLayer;
import lombok.Data;

import java.util.List;

@Data
public class ManifestDTO {
    @Key
    String name;
    @Key
    String tag;

    @Key
    List<FsLayer> fsLayers;

    @Key
    HistoryEntryDTO[] history;
}
