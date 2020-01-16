package de.christopherkaiser.dockerRegistryCleaner.config;


import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import javax.inject.Singleton;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;

@Singleton
public class RegistryCleanerConfigProvider implements IRegistryCleanerConfigProvider {
    private static JsonFactory JSON_FACTORY = new JacksonFactory();

    private Config config = new Config();

    public RegistryCleanerConfigProvider() throws IOException {

        File configFile = getConfigFile();
        JsonObjectParser jsonObjectParser = new JsonObjectParser(JSON_FACTORY);
        readConfigFile(configFile, jsonObjectParser);

    }

    private void readConfigFile(File configFile, JsonObjectParser jsonObjectParser) throws IOException {
        if (configFile.exists()){
            Reader targetReader = new FileReader(configFile);
            config = jsonObjectParser.parseAndClose(targetReader, Config.class);
        } else {
            System.out.println("No config file found! Please provide configuration file!");
            System.exit(1);
        }
    }

    private File getConfigFile() {
        String currentDirectory = System.getProperty("user.dir");
        return Paths.get(currentDirectory, "config.json").toFile();
    }

    @Override
    public String getRegistry() {
        return config.getRegistry();
    }

    @Override
    public boolean hasAuthData() {
        return config.getUsername() != null && config.getPassword() != null;
    }

    @Override
    public String getUserName() {
        return config.getUsername();
    }

    @Override
    public String getPassword() {
        return config.getPassword();
    }

    @Override
    public String[] getRepositoryIncludeList() {
        return config.getRepositoriesIncluded();
    }

    @Override
    public String[] getRepositoryIgnoreList() {
        return config.getRepositoriesIgnored();
    }

    @Override
    public boolean isDryRun() {
        return config.isDryRun();
    }

    @Override
    public int getFirstTagIndexToLookAt() {
        return config.getKeepLastTags();
    }

    @Override
    public String[] getTagIgnoreList() {
        return config.getTagsIgnored();
    }

    @Override
    public String[] getTagIncludeList() {
        return config.getTagsIncluded();
    }

    @Override
    public long getTagLastModifiedNewerThanHours() {
        return config.getTagNewerThan();
    }

    @Override
    public long getTagLastModifiedOlderThanHours() {
        return config.getTagOlderThan();
    }

    @Override
    public long getRepositoryLastModifiedNewerThanHours() {
        return config.getRepositorNewerThan();
    }

    @Override
    public long getRepositoryLastModifiedOlderThanHours() {
        return config.getRepositoryOlderThan();
    }
}
