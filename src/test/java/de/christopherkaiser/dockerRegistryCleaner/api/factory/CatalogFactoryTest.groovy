package de.christopherkaiser.dockerRegistryCleaner.api.factory

import spock.lang.Specification

class CatalogFactoryTest extends Specification {
    def "GetCatalogFrom with valid data"() {
        given: "A valid Catalog DTO with two repositories"
        and: "A Registry Connector Stub with two diferent repositories"
        when: "Factory Create Method is called"
        then: "Return a Catalog with two different repositories with the correct name and lastModifiedDate"
    }

    def "GetCatalogFrom with null value in repository list data"() {
        given: "A valid Catalog DTO with two repositories and one null value"
        and: "A Registry Connector Stub with two different repositories"
        when: "Factory Create Method is called"
        then: "Return a Catalog with two different repositories with the correct name and lastModifiedDate"
    }

    def "GetCatalogFrom with null object"() {
        given: "The null Object as CatalogDTO"
        when: "Factory Create Method is called"
        then: "Expect some Error thrown"
    }
}
