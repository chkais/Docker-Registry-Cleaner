package de.christopherkaiser.dockerRegistryCleaner.api.factory;

import de.christopherkaiser.dockerRegistryCleaner.api.HistoryEntry;
import de.christopherkaiser.dockerRegistryCleaner.api.Manifest;
import de.christopherkaiser.dockerRegistryCleaner.api.dto.HistoryEntryDTO;
import de.christopherkaiser.dockerRegistryCleaner.api.dto.ManifestDTO;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
public class ManifestFactory {
    private HistoryEntryFactory historyEntryFactory;

    @Inject
    public ManifestFactory(HistoryEntryFactory historyEntryFactory) {
        this.historyEntryFactory = historyEntryFactory;
    }

    public Manifest getManifestFrom(ManifestDTO manifestDTO) {
        List<HistoryEntry> historyEntriesFromManifest = getHistoryEntriesFromManifest(manifestDTO);
        return Manifest.builder()
                .repositoryName(manifestDTO.getName())
                .tag(manifestDTO.getTag())
                .fsLayers(manifestDTO.getFsLayers())
                .history(historyEntriesFromManifest)
                .lastModified(getLastModified(historyEntriesFromManifest))
                .build();
    }

    private List<HistoryEntry> getHistoryEntriesFromManifest(ManifestDTO manifestDTO) {
        return Arrays.stream(manifestDTO.getHistory())
                .parallel()
                .map(historyEntryDTO -> getHistoryEntryFromDTO(historyEntryDTO))
                .filter(Objects::nonNull)
                .sorted(getHistoryEntryNewestFirstComparator())
                .collect(Collectors.toList());
    }

    private HistoryEntry getHistoryEntryFromDTO(HistoryEntryDTO historyEntryDTO) {
        try {
            return historyEntryFactory.createHistoryEntryFrom(historyEntryDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private LocalDateTime getLastModified(List<HistoryEntry> historyEntriesFromManifest) {
        if (historyEntriesFromManifest.size() > 0) {
            return historyEntriesFromManifest.get(0).getV1Compatibility().getCreated();
        }
        return LocalDateTime.MIN;
    }

    private Comparator<HistoryEntry> getHistoryEntryNewestFirstComparator() {
        Comparator<HistoryEntry> historyEntryComparator = Comparator.comparing(o -> o.getV1Compatibility().getCreated());
        return historyEntryComparator.reversed();
    }
}
