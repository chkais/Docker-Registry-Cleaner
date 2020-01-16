package de.christopherkaiser.dockerRegistryCleaner;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.christopherkaiser.dockerRegistryCleaner.api.Catalog;
import de.christopherkaiser.dockerRegistryCleaner.api.Manifest;
import de.christopherkaiser.dockerRegistryCleaner.api.Repository;
import de.christopherkaiser.dockerRegistryCleaner.config.GuiceModule;
import de.christopherkaiser.dockerRegistryCleaner.services.CatalogService;
import de.christopherkaiser.dockerRegistryCleaner.services.ManifestService;
import de.christopherkaiser.dockerRegistryCleaner.services.RepositoryService;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Main {


    private static Injector injector;
    private static ManifestService manifestService;
    private static RepositoryService repositoryService;
    private static CatalogService catalogService;

    public static void main(String[] args) throws IOException {
        initializeServices();


        Catalog catalog = getCatalogFromRegistry();
        List<Repository> filteredRepositories = repositoryService.getFilteredRepositories(catalog);
        List<Manifest> filteredImages = manifestService.getFilteredImages(filteredRepositories);
        manifestService.deleteAllTags(filteredImages);

        System.out.println("‼️‼️‼️ Registry CleanUp finished! Please perform a garbage collection on the registry ‼️‼️‼️");
    }

    private static Catalog getCatalogFromRegistry() throws IOException {
        Instant start = Instant.now();
        System.out.println("⏱⏱⏱ Reading registry - this could take a while...");
        Catalog catalog = catalogService.getCatalog();
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toSeconds();
        System.out.println("⏱⏱⏱ Finished reading in " + timeElapsed + " seconds");
        return catalog;
    }

    private static void initializeServices() {
        injector = Guice.createInjector(new GuiceModule());
        manifestService = injector.getInstance(ManifestService.class);
        repositoryService = injector.getInstance(RepositoryService.class);
        catalogService = injector.getInstance(CatalogService.class);
    }


}
