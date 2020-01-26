package de.christopherkaiser.dockerRegistryCleaner.api.dto;

import com.google.api.client.util.Key;
import de.christopherkaiser.dockerRegistryCleaner.api.FsLayer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
