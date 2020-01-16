package de.christopherkaiser.dockerRegistryCleaner.services;

import de.christopherkaiser.dockerRegistryCleaner.api.Catalog;
import de.christopherkaiser.dockerRegistryCleaner.api.RegistryConnector;
import de.christopherkaiser.dockerRegistryCleaner.api.factory.CatalogFactory;
import de.christopherkaiser.dockerRegistryCleaner.api.dto.CatalogDTO;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;


@Singleton
public class CatalogServiceImpl implements CatalogService {
    private RegistryConnector registryConnector;
    private CatalogFactory catalogFactory;

    @Inject
    public CatalogServiceImpl(RegistryConnector registryConnector, CatalogFactory catalogFactory) {
        this.registryConnector = registryConnector;
        this.catalogFactory = catalogFactory;
    }

    @Override
    public Catalog getCatalog() throws IOException {
        CatalogDTO catalog = registryConnector.getCatalog();
        return catalogFactory.getCatalogFrom(catalog);
    }
}
