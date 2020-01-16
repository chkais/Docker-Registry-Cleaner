package de.christopherkaiser.dockerRegistryCleaner.config;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class Config {
    @Key
    private String registry;
    @Key
    private String username;
    @Key
    private String password;
    @Key
    private String[] repositoriesIncluded = new String[0];
    @Key
    private String[] repositoriesIgnored = new String[0];
    @Key
    private String[] tagsIncluded = new String[0];
    @Key
    private String[] tagsIgnored = new String[0];
    @Key
    private boolean isDryRun = false;
    @Key
    private int keepLastTags = 0;
    @Key
    private int tagNewerThan = -1;
    @Key
    private int tagOlderThan = -1;
    @Key
    private int repositorNewerThan = -1;
    @Key
    private int repositoryOlderThan = -1;


}
