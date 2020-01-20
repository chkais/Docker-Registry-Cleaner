package de.christopherkaiser.dockerRegistryCleaner.api.factory

import de.christopherkaiser.dockerRegistryCleaner.api.V1Compatibility
import de.christopherkaiser.dockerRegistryCleaner.api.dto.HistoryEntryDTO
import spock.lang.Specification

import java.time.LocalDateTime

class HistoryEntryFactoryTest extends Specification {
    private String v1HistoryString="\"{\\\"architecture\\\":\\\"amd64\\\",\\\"config\\\":{\\\"Hostname\\\":\\\"\\\",\\\"Domainname\\\":\\\"\\\",\\\"User\\\":\\\"\\\",\\\"AttachStdin\\\":false,\\\"AttachStdout\\\":false,\\\"AttachStderr\\\":false,\\\"ExposedPorts\\\":{\\\"80/tcp\\\":{}},\\\"Tty\\\":false,\\\"OpenStdin\\\":false,\\\"StdinOnce\\\":false,\\\"Env\\\":[\\\"PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin\\\",\\\"NGINX_VERSION=1.17.0\\\",\\\"NJS_VERSION=0.3.2\\\",\\\"PKG_RELEASE=1\\\"],\\\"Cmd\\\":[\\\"nginx\\\",\\\"-g\\\",\\\"daemon off;\\\"],\\\"ArgsEscaped\\\":true,\\\"Image\\\":\\\"sha256:1a0c2321e397862f028ce407638cd547956e56b7b11b42d75e8fd1e0f437cb4b\\\",\\\"Volumes\\\":null,\\\"WorkingDir\\\":\\\"\\\",\\\"Entrypoint\\\":null,\\\"OnBuild\\\":null,\\\"Labels\\\":{\\\"maintainer\\\":\\\"NGINX Docker Maintainers \\\\u003cdocker-maint@nginx.com\\\\u003e\\\"},\\\"StopSignal\\\":\\\"SIGTERM\\\"},\\\"container_config\\\":{\\\"Hostname\\\":\\\"\\\",\\\"Domainname\\\":\\\"\\\",\\\"User\\\":\\\"\\\",\\\"AttachStdin\\\":false,\\\"AttachStdout\\\":false,\\\"AttachStderr\\\":false,\\\"ExposedPorts\\\":{\\\"80/tcp\\\":{}},\\\"Tty\\\":false,\\\"OpenStdin\\\":false,\\\"StdinOnce\\\":false,\\\"Env\\\":[\\\"PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin\\\",\\\"NGINX_VERSION=1.17.0\\\",\\\"NJS_VERSION=0.3.2\\\",\\\"PKG_RELEASE=1\\\"],\\\"Cmd\\\":[\\\"/bin/sh\\\",\\\"-c\\\",\\\"#(nop) COPY file:47ca4ec5b89d8caa7b95321a5a6e5d29b0008759e3bb323afcf397378ad03b6d in /usr/share/nginx/html/index.html \\\"],\\\"ArgsEscaped\\\":true,\\\"Image\\\":\\\"sha256:1a0c2321e397862f028ce407638cd547956e56b7b11b42d75e8fd1e0f437cb4b\\\",\\\"Volumes\\\":null,\\\"WorkingDir\\\":\\\"\\\",\\\"Entrypoint\\\":null,\\\"OnBuild\\\":null,\\\"Labels\\\":{\\\"maintainer\\\":\\\"NGINX Docker Maintainers \\\\u003cdocker-maint@nginx.com\\\\u003e\\\"},\\\"StopSignal\\\":\\\"SIGTERM\\\"},\\\"created\\\":\\\"2019-06-18T19:22:39.8808086Z\\\",\\\"docker_version\\\":\\\"18.09.2\\\",\\\"id\\\":\\\"e0bc5fa6a680914057a02b36f20cc06673833ea8b7d0c067c762971924607b25\\\",\\\"os\\\":\\\"linux\\\",\\\"parent\\\":\\\"b43a106862a27502031e87d8b018d588e77792f0a45b9890d5375361495cde84\\\"}\""

    def "CreateHistoryEntryFrom historyDTO with valid data"() {
        given:
        HistoryEntryDTO historyEntryDTO = HistoryEntryDTO.builder().v1Compatibility(v1HistoryString).build()
        V1CompatibilityFactory v1CompatibilityFactory = Mock(V1CompatibilityFactory)
        def time = LocalDateTime.now().minusHours(2)
        def v1Compatibility = V1Compatibility.builder().id("some-id").created(time).build()
        v1CompatibilityFactory.createFromJson(v1HistoryString) >> v1Compatibility
        HistoryEntryFactory historyEntryFactory = new HistoryEntryFactory(v1CompatibilityFactory)


        when:
        def historyEntry = historyEntryFactory.createHistoryEntryFrom(historyEntryDTO)

        then:
        historyEntry != null
        historyEntry.v1Compatibility == v1Compatibility
        historyEntry.v1Compatibility.id == "some-id"
        historyEntry.v1Compatibility.created == time
    }

    def "CreateHistoryEntryFrom historyDTO with null"() {
        given:
        HistoryEntryDTO historyEntryDTO = null
        V1CompatibilityFactory v1CompatibilityFactory = Mock(V1CompatibilityFactory)
        HistoryEntryFactory historyEntryFactory = new HistoryEntryFactory(v1CompatibilityFactory)


        when:
        def historyEntry = historyEntryFactory.createHistoryEntryFrom(historyEntryDTO)

        then:
        thrown(IllegalArgumentException)
    }
}
