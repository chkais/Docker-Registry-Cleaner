package de.christopherkaiser.dockerRegistryCleaner.api;

import de.christopherkaiser.dockerRegistryCleaner.api.dto.CatalogDTO;
import de.christopherkaiser.dockerRegistryCleaner.api.dto.ManifestDTO;
import de.christopherkaiser.dockerRegistryCleaner.api.dto.RepositoryDTO;

import java.io.IOException;

public interface RegistryConnector {
    CatalogDTO getCatalog() throws IOException;

    RepositoryDTO getRepository(String repositoryName) throws IOException;

    ManifestDTO getManifest(String repositoryName, String tag) throws IOException;

    String getDigestForManifest(Manifest manifest) throws IOException;

    String deleteImage(Manifest manifest, String digest) throws IOException;
}
