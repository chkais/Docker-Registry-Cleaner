package de.christopherkaiser.dockerRegistryCleaner.api.factory;

import de.christopherkaiser.dockerRegistryCleaner.api.Catalog;
import de.christopherkaiser.dockerRegistryCleaner.api.Repository;
import de.christopherkaiser.dockerRegistryCleaner.api.dto.CatalogDTO;
import de.christopherkaiser.dockerRegistryCleaner.services.RepositoryService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
public class CatalogFactory {
    private RepositoryService repositoryService;

    @Inject
    public CatalogFactory(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public Catalog getCatalogFrom(CatalogDTO catalogDTO) {
        validate(catalogDTO);
        List<Repository> repositories = Arrays.stream(catalogDTO.getRepositories())
                .parallel()
                .map(this::getRepositoryFromRepo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return Catalog.builder()
                .repositories(repositories)
                .repositoriesAsString(catalogDTO.getRepositories())
                .build();
    }

    private void validate(CatalogDTO catalogDTO) {
        if (catalogDTO == null){
            throw new IllegalArgumentException("Could not get catalog from null!");
        }
    }

    private Repository getRepositoryFromRepo(String repo) {
        try {
            return repositoryService.getRepository(repo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
