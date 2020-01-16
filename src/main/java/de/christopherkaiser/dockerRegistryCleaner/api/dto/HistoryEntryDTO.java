package de.christopherkaiser.dockerRegistryCleaner.api.dto;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class HistoryEntryDTO {
    @Key
    String v1Compatibility;
}