package de.christopherkaiser.dockerRegistryCleaner.services;

import com.google.inject.ImplementedBy;
import de.christopherkaiser.dockerRegistryCleaner.api.Manifest;
import de.christopherkaiser.dockerRegistryCleaner.api.Repository;

import java.io.IOException;
import java.util.List;

@ImplementedBy(ManifestServiceImpl.class)
public interface ManifestService {
    Manifest getManifest(String repositoryName, String tag) throws IOException;

    List<Manifest> getFilteredImages(List<Repository> filteredRepositories);

    void deleteAllTags(List<Manifest> filteredImages);
}
