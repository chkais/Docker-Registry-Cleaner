package de.christopherkaiser.dockerRegistryCleaner.services

import de.christopherkaiser.dockerRegistryCleaner.api.RegistryConnector
import de.christopherkaiser.dockerRegistryCleaner.api.Repository
import de.christopherkaiser.dockerRegistryCleaner.api.dto.CatalogDTO
import de.christopherkaiser.dockerRegistryCleaner.api.factory.CatalogFactory
import spock.lang.Specification

import java.time.LocalDateTime

class CatalogServiceImplTest extends Specification {
    def "GetCatalog"() {
//        given:
//        Repository repo1 = Repository.builder().lastModified(LocalDateTime.now().minusHours(1)).name("Repo 1").build()
//        Repository repo2 = Repository.builder().lastModified(LocalDateTime.now().minusHours(2)).name("Repo 2").build()
//        CatalogDTO catalogDTO = CatalogDTO.builder().repositories([repo1.name, repo2.name ] as String[]).build()
//        CatalogFactory catalogFactory = Stub(){
//            getCatalogFrom(_) >>
//        }
//
//        RegistryConnector registryConnector = Stub() {
//            getCatalog() >> catalogDTO
//        }
//        def catalogService = new CatalogServiceImpl()
//
//
//        when:
//        def catalog = catalogService.getCatalog()
//
//        then:
//        catalog.repositories.size() == 3
//        catalog.repositories.containsAll(catalogItems)

    }

}