package de.christopherkaiser.dockerRegistryCleaner.api.dto;

import com.google.api.client.util.Key;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryEntryDTO {
    @Key
    String v1Compatibility;
}