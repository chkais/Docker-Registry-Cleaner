package de.christopherkaiser.dockerRegistryCleaner.services;

import de.christopherkaiser.dockerRegistryCleaner.api.Manifest;
import de.christopherkaiser.dockerRegistryCleaner.api.RegistryConnector;
import de.christopherkaiser.dockerRegistryCleaner.api.Repository;
import de.christopherkaiser.dockerRegistryCleaner.api.dto.ManifestDTO;
import de.christopherkaiser.dockerRegistryCleaner.api.factory.ManifestFactory;
import de.christopherkaiser.dockerRegistryCleaner.config.IRegistryCleanerConfigProvider;
import de.christopherkaiser.dockerRegistryCleaner.config.TagFilters;

import javax.inject.Inject;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ManifestServiceImpl implements ManifestService {
    private RegistryConnector registryConnector;
    private ManifestFactory manifestFactory;
    private IRegistryCleanerConfigProvider configProvider;
    private TagFilters tagFilters;

    @Inject
    public ManifestServiceImpl(RegistryConnector registryConnector, ManifestFactory manifestFactory, IRegistryCleanerConfigProvider configProvider, TagFilters tagFilters) {
        this.registryConnector = registryConnector;
        this.manifestFactory = manifestFactory;
        this.configProvider = configProvider;
        this.tagFilters = tagFilters;
    }

    @Override
    public Manifest getManifest(String repositoryName, String tag) throws IOException {
        ManifestDTO manifestDTO = registryConnector.getManifest(repositoryName, tag);
        return manifestFactory.getManifestFrom(manifestDTO);
    }

    public List<Manifest> getFilteredImages(List<Repository> filteredRepositories) {
        List<Manifest>imagesToDelete = new LinkedList<>();

        System.out.println("⏱⏱⏱ Determining tags to look into");
        filteredRepositories.stream()
                .parallel()
                .forEach(repository -> {
                    int firstImage = getFirstTagIndex(repository);
                    List<Manifest> filteredTags = applyTagFilters(tagFilters, repository, firstImage);
                    imagesToDelete.addAll(filteredTags);
                    printFilterResult(repository, filteredTags);
                });
        return imagesToDelete;
    }

    @Override
    public void deleteAllTags(List<Manifest> tagsToLookAt) {
        tagsToLookAt.stream()
                .parallel()
                .forEach(manifest -> {
                    if (!configProvider.isDryRun()) {
                        try {
                            this.delete(manifest);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("❌❌❌ Would delete tag '" + manifest.getRepositoryName() + ":" + manifest.getTag() + "'");
                    }

                });
    }

    private  int getFirstTagIndex(Repository repository) {
        int firstTag = 0;
        if (configProvider.getFirstTagIndexToLookAt() < repository.getTags().size()) {
            firstTag = configProvider.getFirstTagIndexToLookAt();
        }
        return firstTag;
    }

    private static List<Manifest> applyTagFilters(TagFilters tagFilters, Repository repository, int firstTag) {
        return repository.getTags().subList(firstTag, repository.getTags().size())
                .stream()
                .parallel()
                .filter(tagFilters::isTagIncluded)
                .filter(tagFilters::isTagNotIgnored)
                .filter(tagFilters::isTagNewerThan)
                .filter(tagFilters::isTagOlderThan)
                .collect(Collectors.toList());
    }

    private static void printFilterResult(Repository repository, List<Manifest> tagsToLookAt) {
        if (tagsToLookAt.size() > 0) {
            System.out.println("ℹ️️ℹ️️ℹ️ Found " + tagsToLookAt.size() + " tags in repository " + repository.getName() + " to delete");
            System.out.println("ℹ️️ℹ️️ℹ️ Keeping " + (repository.getTags().size() - tagsToLookAt.size()) + " tags in repository ");
        } else {
            System.out.println("ℹ️️ℹ️️ℹ️ Keeping all tags in repository '" + repository.getName() + "'...");
        }
    }

    private void delete(Manifest manifest) throws IOException {
        System.out.println("❌❗️❌❗️❌❗️ Deleting '" + manifest.getRepositoryName() + ":" + manifest.getTag() + "'");
        String digest = registryConnector.getDigestForManifest(manifest);
        registryConnector.deleteImage(manifest,digest);
    }
}
