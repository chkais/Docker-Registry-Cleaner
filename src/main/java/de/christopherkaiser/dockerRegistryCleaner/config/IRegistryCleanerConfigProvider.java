package de.christopherkaiser.dockerRegistryCleaner.config;


import com.google.inject.ImplementedBy;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;

@ImplementedBy(RegistryCleanerConfigProvider.class)
public interface IRegistryCleanerConfigProvider {
    String getRegistry();

    boolean hasAuthData();

    String getUserName();

    String getPassword();

    String[] getRepositoryIncludeList();
    String[] getRepositoryIgnoreList();

    boolean isDryRun();

    int getFirstTagIndexToLookAt();

    String[] getTagIgnoreList();

    String[] getTagIncludeList();

    long getTagLastModifiedNewerThanHours();

    long getTagLastModifiedOlderThanHours();

    long getRepositoryLastModifiedNewerThanHours();

    long getRepositoryLastModifiedOlderThanHours();
}
