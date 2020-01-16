package de.christopherkaiser.dockerRegistryCleaner.config;

import de.christopherkaiser.dockerRegistryCleaner.api.Repository;
import de.christopherkaiser.dockerRegistryCleaner.config.IRegistryCleanerConfigProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.Arrays;

@Singleton
public class RepositoryFilters {
    private IRegistryCleanerConfigProvider config;
    @Inject
    public RepositoryFilters(IRegistryCleanerConfigProvider config) {
        this.config = config;
    }

    public boolean isRepositoryNewerThan(Repository repository) {
        long repositoryLastModifiedNewerThanHours = config.getRepositoryLastModifiedNewerThanHours();
        if (repositoryLastModifiedNewerThanHours >0 ){
            if (repository.getLastModified() == null){
                return false;
            }
            return repository.getLastModified().isAfter(LocalDateTime.now().minusHours(repositoryLastModifiedNewerThanHours));
        }
        return true;
    }

    public boolean isRepositoryOlderThan(Repository repository) {
        long repositoryLastModifiedOlderThanHours = config.getRepositoryLastModifiedOlderThanHours();
        if (repositoryLastModifiedOlderThanHours >0) {
            if (repository.getLastModified() == null){
                return false;
            }
            return repository.getLastModified().isBefore(LocalDateTime.now().minusHours(repositoryLastModifiedOlderThanHours));
        }
        return true;

    }

    public boolean isRepositoryNotIgnored(Repository repository) {
        String[] repositoryIgnoreList = config.getRepositoryIgnoreList();
        if (repositoryIgnoreList.length > 0) {
            return Arrays.stream(repositoryIgnoreList).parallel().noneMatch(s -> repository.getName().matches(s));
        }
        return true;
    }

    public boolean isRepositoryIncluded(Repository repository) {
        String[] repositoryIncludeList = config.getRepositoryIncludeList();
        if (repositoryIncludeList.length > 0) {
            return Arrays.stream(repositoryIncludeList).parallel().anyMatch(s -> repository.getName().matches(s));
        }
        return true;
    }
}