package de.christopherkaiser.dockerRegistryCleaner.api.factory;

import de.christopherkaiser.dockerRegistryCleaner.api.Repository;
import de.christopherkaiser.dockerRegistryCleaner.api.Manifest;
import de.christopherkaiser.dockerRegistryCleaner.api.dto.RepositoryDTO;
import de.christopherkaiser.dockerRegistryCleaner.services.ManifestService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
public class RepositoryFactory {
    private ManifestService manifestService;

    @Inject
    public RepositoryFactory(ManifestService manifestService) {
        this.manifestService = manifestService;
    }

    public Repository getRepositoryFrom(RepositoryDTO repositoryDTO) {
        List<Manifest> tags = getTagsFrom(repositoryDTO);
        return Repository.builder()
                .name(repositoryDTO.getName())
                .tags(tags)
                .lastModified(getLastModifiedFromTags(tags))
                .build();
    }

    private List<Manifest> getTagsFrom(RepositoryDTO repositoryDTO) {
        // Get Manifest for all Tags
        List<String> tagStrings = repositoryDTO.getTags();
        List<Manifest> tags = tagStrings.stream()
                .parallel()
                .map(tagString -> getManifestFrom(repositoryDTO, tagString))
                .filter(Objects::nonNull).sorted(getManifestNewestFirstComparator())
                .collect(Collectors.toList());
        if (tags.size() < 1) {
            System.out.println("ℹ️ℹ️ℹ️️ Repository with no tags: " + repositoryDTO.getName());
        }
        return tags;
    }

    private Manifest getManifestFrom(RepositoryDTO repositoryDTO, String tagString) {
        try {
            return manifestService.getManifest(repositoryDTO.getName(), tagString);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private LocalDateTime getLastModifiedFromTags(List<Manifest> tags) {
        if (tags.size() > 0) {
            return (tags.get(0).getLastModified());
        }
        return null;
    }

    private Comparator<Manifest> getManifestNewestFirstComparator() {
        Comparator<Manifest> comparing = Comparator.comparing(Manifest::getLastModified);
        return comparing.reversed();
    }
}
