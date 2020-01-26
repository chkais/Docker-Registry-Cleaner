package de.christopherkaiser.dockerRegistryCleaner.api.factory


import de.christopherkaiser.dockerRegistryCleaner.api.HistoryEntry
import de.christopherkaiser.dockerRegistryCleaner.api.Manifest
import de.christopherkaiser.dockerRegistryCleaner.api.V1Compatibility
import de.christopherkaiser.dockerRegistryCleaner.api.dto.HistoryEntryDTO
import de.christopherkaiser.dockerRegistryCleaner.api.dto.ManifestDTO
import spock.lang.Specification

import java.time.LocalDateTime

class ManifestFactoryTest extends Specification {
    def "Get Manifest From Valid Manifest DTO"() {
        given: "A valid manfestDTO"
        HistoryEntryDTO historyEntryDTO = HistoryEntryDTO.builder().build()
        HistoryEntryDTO historyEntryDTO2 = HistoryEntryDTO.builder().build()
        ManifestDTO manifestDTO = ManifestDTO.builder().name("repoName").tag("tagName").history([historyEntryDTO, historyEntryDTO2] as HistoryEntryDTO[]).build()

        and: "A history factory mock which return a given history entry object"
        LocalDateTime localDateTimem2 = LocalDateTime.now().minusHours(2)
        V1Compatibility v1Compatibility = V1Compatibility.builder().id("someId").created(localDateTimem2).build()
        HistoryEntry historyEntry = HistoryEntry.builder().v1Compatibility(v1Compatibility).build()

        LocalDateTime localDateTimem3 = LocalDateTime.now().minusHours(3)
        V1Compatibility v1Compatibility2 = V1Compatibility.builder().id("someId").created(localDateTimem3).build()
        HistoryEntry historyEntry2 = HistoryEntry.builder().v1Compatibility(v1Compatibility2).build()

        HistoryEntryFactory historyEntryFactory = Mock(HistoryEntryFactory)
        historyEntryFactory.createHistoryEntryFrom(historyEntryDTO) >> historyEntry
        historyEntryFactory.createHistoryEntryFrom(historyEntryDTO2) >> historyEntry2


        and: "A Manifest Factory"
        ManifestFactory manifestFactory = new ManifestFactory(historyEntryFactory)

        when: "Factory Create Method is called"
        Manifest manifest = manifestFactory.getManifestFrom(manifestDTO);

        then: "Return a Catalog with two different repositories and the right date"
        manifest.getLastModified() == localDateTimem2;
        manifest.getRepositoryName() == "repoName"
        manifest.getTag() == "tagName"
        manifest.getHistory().size() == 2
    }


    def "Get Manifest From null Object"() {
        given: "A history entry factory"
        HistoryEntryFactory historyEntryFactory = Mock(HistoryEntryFactory)
        historyEntryFactory.createHistoryEntryFrom(_) >> HistoryEntry.builder().build()

        and: "A Manifest Factory"
        ManifestFactory manifestFactory = new ManifestFactory(historyEntryFactory)

        when: "Factory Create Method is called"
        manifestFactory.getManifestFrom(null)

        then: "Throw a illegal argument exception"
        thrown(IllegalArgumentException)
    }
}
