package de.christopherkaiser.dockerRegistryCleaner.services;

import com.google.inject.ImplementedBy;
import de.christopherkaiser.dockerRegistryCleaner.api.Catalog;

import java.io.IOException;

@ImplementedBy(CatalogServiceImpl.class)
public interface CatalogService {
    Catalog getCatalog() throws IOException;
}
