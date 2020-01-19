package de.christopherkaiser.dockerRegistryCleaner;

import de.christopherkaiser.dockerRegistryCleaner.api.Catalog;
import de.christopherkaiser.dockerRegistryCleaner.api.Manifest;
import de.christopherkaiser.dockerRegistryCleaner.api.Repository;
import de.christopherkaiser.dockerRegistryCleaner.services.CatalogService;
import de.christopherkaiser.dockerRegistryCleaner.services.ManifestService;
import de.christopherkaiser.dockerRegistryCleaner.services.RepositoryServiceImpl;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Singleton
public class RegistryCleaner {
    private CatalogService catalogService;
    private RepositoryServiceImpl repositoryService;
    private ManifestService manifestService;

    @Inject
    public RegistryCleaner(CatalogService catalogService, RepositoryServiceImpl repositoryService, ManifestService manifestService) {
        this.catalogService = catalogService;
        this.repositoryService = repositoryService;
        this.manifestService = manifestService;
    }

    void performCleanUp() throws IOException {
        Catalog catalog = getCatalogFromRegistry();
        List<Repository> filteredRepositories = repositoryService.getFilteredRepositories(catalog);
        List<Manifest> filteredImages = manifestService.getFilteredImages(filteredRepositories);
        manifestService.deleteAllTags(filteredImages);
    }

    private  Catalog getCatalogFromRegistry() throws IOException {
        Instant start = Instant.now();
        System.out.println("⏱⏱⏱ Reading registry - this could take a while...");
        Catalog catalog = catalogService.getCatalog();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toSeconds();
        System.out.println("⏱⏱⏱ Finished reading in " + timeElapsed + " seconds");
        return catalog;
    }
}
