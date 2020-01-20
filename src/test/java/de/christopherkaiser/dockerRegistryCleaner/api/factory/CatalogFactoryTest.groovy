package de.christopherkaiser.dockerRegistryCleaner.api.factory

import de.christopherkaiser.dockerRegistryCleaner.api.Catalog
import de.christopherkaiser.dockerRegistryCleaner.api.Repository
import de.christopherkaiser.dockerRegistryCleaner.api.dto.CatalogDTO
import de.christopherkaiser.dockerRegistryCleaner.services.RepositoryService
import spock.lang.Specification

import java.time.LocalDateTime

class CatalogFactoryTest extends Specification {
    def "GetCatalogFrom with valid data"() {

        given: "A valid Catalog DTO with two repositories"
        CatalogDTO catalogDTO = CatalogDTO.builder().repositories(["Repo 1", "Repo 2"] as String[]).build()
        and: "A RepositoryService Stub with two corresponding Repositories"
        RepositoryService repositoryService = Mock(RepositoryService)
        Repository repo1 = Repository.builder().name("Repo 1").lastModified(LocalDateTime.now().minusHours(1)).build()
        Repository repo2 = Repository.builder().name("Repo 2").lastModified(LocalDateTime.now().minusHours(2)).build()
        repositoryService.getRepository("Repo 1") >> repo1
        repositoryService.getRepository("Repo 2") >> repo2
        and: "A CategoryFactory"
        CatalogFactory catalogFactory = new CatalogFactory(repositoryService)

        when: "Factory Create Method is called"
        Catalog catalog = catalogFactory.getCatalogFrom(catalogDTO)

        then: "Return a Catalog with the given two different repositories"
        catalog.repositories.size() == 2
        catalog.repositories.containsAll([repo1, repo2])
    }

    def "GetCatalogFrom with null value in repository list data"() {
        given: "A valid Catalog DTO with two repositories and one null value"
        CatalogDTO catalogDTO = CatalogDTO.builder().repositories(["Repo 1", "Repo 2", null] as String[]).build()
        and: "A RepositoryService Stub with two corresponding Repositories"
        RepositoryService repositoryService = Mock(RepositoryService)
        Repository repo1 = Repository.builder().name("Repo 1").lastModified(LocalDateTime.now().minusHours(1)).build()
        Repository repo2 = Repository.builder().name("Repo 2").lastModified(LocalDateTime.now().minusHours(2)).build()
        repositoryService.getRepository("Repo 1") >> repo1
        repositoryService.getRepository("Repo 2") >> repo2
        and: "A CategoryFactory"
        CatalogFactory catalogFactory = new CatalogFactory(repositoryService)

        when: "Factory Create Method is called"
        Catalog catalog = catalogFactory.getCatalogFrom(catalogDTO)

        then: "Return a Catalog with two different repositories"
        catalog.repositories.size() == 2
        catalog.repositories.containsAll([repo1, repo2])
        !catalog.repositories.contains(null)

    }

    def "GetCatalogFrom with null object"() {
        given: "The null Object as CatalogDTO"
        CatalogDTO catalogDTO = null
        and: "a repositoryService Mock"
        RepositoryService repositoryService = Mock(RepositoryService)
        and: "A CategoryFactory"
        CatalogFactory catalogFactory = new CatalogFactory(repositoryService)

        when: "Factory Create Method is called"
        catalogFactory.getCatalogFrom(catalogDTO)

        then: "Expect IllegalArgumentException"
        thrown(IllegalArgumentException)
    }
}
