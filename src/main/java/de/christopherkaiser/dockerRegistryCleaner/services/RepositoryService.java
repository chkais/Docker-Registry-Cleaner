package de.christopherkaiser.dockerRegistryCleaner.services;

import com.google.inject.ImplementedBy;
import de.christopherkaiser.dockerRegistryCleaner.api.Catalog;
import de.christopherkaiser.dockerRegistryCleaner.api.Repository;

import java.io.IOException;
import java.util.List;

@ImplementedBy(RepositoryServiceImpl.class)
public interface RepositoryService {
    Repository getRepository(String repositoryName) throws IOException;

    List<Repository> getFilteredRepositories(Catalog catalog);

}
