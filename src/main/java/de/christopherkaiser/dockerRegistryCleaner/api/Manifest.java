package de.christopherkaiser.dockerRegistryCleaner.api;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Manifest {
    private String repositoryName;
    private String tag;
    private List<FsLayer> fsLayers;
    private List<HistoryEntry> history;
    private LocalDateTime lastModified;
}
