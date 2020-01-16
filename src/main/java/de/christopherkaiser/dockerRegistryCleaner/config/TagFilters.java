package de.christopherkaiser.dockerRegistryCleaner.config;

import de.christopherkaiser.dockerRegistryCleaner.api.Manifest;
import de.christopherkaiser.dockerRegistryCleaner.config.IRegistryCleanerConfigProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.Arrays;

@Singleton
public class TagFilters {

    private IRegistryCleanerConfigProvider config;

    @Inject
    public TagFilters(IRegistryCleanerConfigProvider config) {
        this.config = config;
    }

    public boolean isTagNewerThan(Manifest tag) {
        long tagLastModifiedNewerThanHours = config.getTagLastModifiedNewerThanHours();
        if (tagLastModifiedNewerThanHours > 0) {
            return tag.getLastModified().isAfter(LocalDateTime.now().minusHours(tagLastModifiedNewerThanHours));
        }
        return true;
    }

    public boolean isTagOlderThan(Manifest tag) {
        long tagLastModifiedOlderThanHours = config.getTagLastModifiedOlderThanHours();
        if (tagLastModifiedOlderThanHours > 0) {
            return tag.getLastModified().isBefore(LocalDateTime.now().minusHours(tagLastModifiedOlderThanHours));
        }
        return true;

    }

    public boolean isTagNotIgnored(Manifest tag) {
        String[] tagIgnoreList = config.getTagIgnoreList();
        if (tagIgnoreList.length > 0) {
            return Arrays.stream(tagIgnoreList).parallel().noneMatch(s -> tag.getTag().matches(s));
        }
        return true;
    }

    public boolean isTagIncluded(Manifest tag) {
        String[] tagIncludeList = config.getTagIncludeList();
        if (tagIncludeList.length > 0) {
            return Arrays.stream(tagIncludeList).parallel().anyMatch(s -> tag.getTag().matches(s));
        }
        return true;
    }
}
