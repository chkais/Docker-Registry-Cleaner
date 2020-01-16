package de.christopherkaiser.dockerRegistryCleaner.services;

import de.christopherkaiser.dockerRegistryCleaner.api.Catalog;
import de.christopherkaiser.dockerRegistryCleaner.api.Repository;
import de.christopherkaiser.dockerRegistryCleaner.api.RegistryConnector;
import de.christopherkaiser.dockerRegistryCleaner.api.factory.RepositoryFactory;
import de.christopherkaiser.dockerRegistryCleaner.api.dto.RepositoryDTO;
import de.christopherkaiser.dockerRegistryCleaner.config.RepositoryFilters;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class RepositoryServiceImpl implements RepositoryService {
    private RegistryConnector registryConnector;
    private RepositoryFactory repositoryFactory;
    private RepositoryFilters repositoryFilters;

    @Inject
    public RepositoryServiceImpl(RegistryConnector registryConnector, RepositoryFactory repositoryFactory, RepositoryFilters repositoryFilters) {
        this.registryConnector = registryConnector;
        this.repositoryFactory = repositoryFactory;
        this.repositoryFilters = repositoryFilters;
    }

    @Override
    public Repository getRepository(String repositoryName) throws IOException {
        RepositoryDTO repositoryDTO = registryConnector.getRepository(repositoryName);
        return repositoryFactory.getRepositoryFrom(repositoryDTO);
    }

    public List<Repository> getFilteredRepositories(Catalog catalog) {
        System.out.println("⏱⏱⏱ Determining repositories to look into..");
        System.out.println("ℹ️️ℹ️️ℹ️ Catalog has " + catalog.getRepositories().size() + " repositories");
        List<Repository> repositoriesToLookAt = catalog.getRepositories().stream()
                .parallel()
                .filter(repositoryFilters::isRepositoryIncluded)
                .filter(repositoryFilters::isRepositoryNotIgnored)
                .filter(repositoryFilters::isRepositoryNewerThan)
                .filter(repositoryFilters::isRepositoryOlderThan)
                .collect(Collectors.toList());
        System.out.println("ℹ️️ℹ️️ℹ️ Found " + repositoriesToLookAt.size() + " repositories to look into");
        return repositoriesToLookAt;
    }

}
