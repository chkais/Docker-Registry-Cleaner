package de.christopherkaiser.dockerRegistryCleaner.api;

import com.google.api.client.http.*;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import de.christopherkaiser.dockerRegistryCleaner.config.IRegistryCleanerConfigProvider;
import com.google.api.client.http.javanet.NetHttpTransport;
import de.christopherkaiser.dockerRegistryCleaner.api.dto.CatalogDTO;
import de.christopherkaiser.dockerRegistryCleaner.api.dto.RepositoryDTO;
import de.christopherkaiser.dockerRegistryCleaner.api.dto.ManifestDTO;

import javax.inject.Inject;
import java.io.IOException;


public class RegistryConnector {
    private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static JsonFactory JSON_FACTORY = new JacksonFactory();
    private IRegistryCleanerConfigProvider config;
    private HttpRequestFactory requestFactory;


    @Inject
    public RegistryConnector(IRegistryCleanerConfigProvider config) {
        this.config = config;
        this.requestFactory = HTTP_TRANSPORT.createRequestFactory(
                (HttpRequest request) -> {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                    if (config.hasAuthData()) {
                        request.setInterceptor(new BasicAuthentication(config.getUserName(), config.getPassword()));
                    }
                });
    }

    public CatalogDTO getCatalog() throws IOException {
        HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(config.getRegistry() + "/v2/_catalog?n=10000"));
        return request.execute().parseAs(CatalogDTO.class);
    }

    public RepositoryDTO getRepository(String repositoryName) throws IOException {
        HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(config.getRegistry() + "/v2/" + repositoryName + "/tags/list"));
        return request.execute().parseAs(RepositoryDTO.class);
    }

    public ManifestDTO getManifest(String repositoryName, String tag) throws IOException {
        HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(config.getRegistry() + "/v2/" + repositoryName + "/manifests/" + tag));
        return request.execute().parseAs(ManifestDTO.class);
    }

    public String getDigestForManifest(Manifest manifest) throws IOException {
        String path = config.getRegistry() + "/v2/" + manifest.getRepositoryName() + "/manifests/" + manifest.getTag();
        HttpRequest request = requestFactory.buildHeadRequest(new GenericUrl(path));
        request.getHeaders().set("Accept","application/vnd.docker.distribution.manifest.v2+json");
        return request.execute().getHeaders().getFirstHeaderStringValue("Docker-Content-Digest");
        //return request.execute().parseAs(ManifestV2.class).getConfig().getDigest();
    }

    public String deleteImage(Manifest manifest, String digest) throws IOException {
        String path = config.getRegistry() + "/v2/" + manifest.getRepositoryName() + "/manifests/" + digest;
        HttpRequest request = requestFactory.buildDeleteRequest(new GenericUrl(path));

        HttpResponse response;
        try {
            response = request.execute();
            return response.getStatusCode() + " - " + response.getStatusMessage();
        } catch (HttpResponseException responseException){
            System.out.println(responseException.getStatusCode() + " - " + responseException.getStatusMessage());
            return responseException.getStatusCode() + " - " + responseException.getStatusMessage();
        }
    }
}
