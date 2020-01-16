package de.christopherkaiser.dockerRegistryCleaner.api.factory;

import de.christopherkaiser.dockerRegistryCleaner.api.HistoryEntry;
import de.christopherkaiser.dockerRegistryCleaner.api.V1Compatibility;
import de.christopherkaiser.dockerRegistryCleaner.api.dto.HistoryEntryDTO;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class HistoryEntryFactory {
    private V1CompatibilityFactory v1CompatibilityFactory;

    @Inject
    public HistoryEntryFactory(V1CompatibilityFactory v1CompatibilityFactory) {
        this.v1CompatibilityFactory = v1CompatibilityFactory;
    }

    HistoryEntry createHistoryEntryFrom(HistoryEntryDTO historyEntryDTO) throws IOException {
        V1Compatibility v1Compatibility = v1CompatibilityFactory.createFromJson(historyEntryDTO.getV1Compatibility());
        return HistoryEntry.builder().v1Compatibility(v1Compatibility).build();
    }
}
