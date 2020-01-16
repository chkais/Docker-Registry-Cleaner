package de.christopherkaiser.dockerRegistryCleaner.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoryEntry {
    V1Compatibility v1Compatibility;
}
