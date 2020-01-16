package de.christopherkaiser.dockerRegistryCleaner.api;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Catalog {
    String[] repositoriesAsString;

    List<Repository> repositories;

}
