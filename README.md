# Description #
Little Commandline Tool to performa a docker registry cleanUp. It works in the following way:
   1. Get all repositories in a registry (i.e. the catalog)
   2. Check Repositories 
   3. Validate each repository against configuration
   4. Validate each tag in each valid repository against configuration

Each valid tag will be deleted. There is a "dryRun"-switch to test the configuration before hand.

The logic behind include-exclude is:
   1. Get all repositories
   2. Keep only repositories which match one of the include patterns
   3. Discard repositories which match one of the ignore patterns
   4. If no include or exclude patterns are provided, the rule will not be applied
 
 Same logic applies to tags



# Configuration #
The application expects an config file 'config.json' in the working directory.
The following JSON provides a full example.

    {
      "registry": "<yourRegistry>",
      "username": "testRegistry",
      "password": "testPassword",
      "repositoriesIncluded": [
        "picasso",
        "fresenius"
      ],
      "repositoriesIgnored": [
        "apo"
      ],
      "tagsIncluded": [
        "\\d",
        "test"
      ],
      "tagsIgnored": [
        "develop","master"
      ],
      "isDryRun": true,
      "keepLastTags": 20,
      "tagNewerThan": 25,
      "tagOlderThan": 20,
      "repositorNewerThan": 25,
      "repositoryOlderThan": 20
    }

## Parameters ##
All Parameters except for the 'registry' parameter are optional.

    registry                base url for the registry (with https:// and w/o "/v2" - i.e. https://registry.domain.com
    username                basic auth username
    password                basic auth password
    isDryRun                do not delete anything, only print a list
    
    repositoriesIncluded    list of regex patterns to include. If one of the patterns matches the repository name the all tags will be checked for deletion
    repositoriesIgnored     list of regex patterns to ignore. If one of the patterns matches the repository name the repository will be ignored
    repositorNewerThan      number in hours - check in repositories that are newer than X hours (newest tag newer than X hours)
    repositoryOlderThan     number in hours - check in repositories that are older than X hours (newest tag older than X hours)
        
    tagsIncluded            list of regex patterns to include. If one of the patterns matches the image name the tag will be checked for deletion
    tagsIgnored             list of regex patterns to ignore. If one of the patterns matches the image name the tag will be ignored
    tagNewerThan            number in hours - delete tags that are newer than X hours
    tagOlderThan            number in hours - delete tags that are older than X hours
    
    keepLastTags            number of images per repository to keep regardless of the rules above
 


# Usage #
## With Docker ##
Either mount a volume with the config.json into the working directory of the docker container.
Otherwise the default configuration will be used.
### docker run example ###
Create config.json as described above (or download and rename file from repository). Then run the docker container with the mounted config file:

    docker run -v "$(pwd)"/config.json:/config.json chkais/docker-registry-cleaner:0.1.0

## From CommandLine ##
Checkout the repository and build the file with gradle:
    
    gradle build
    
A jar file is generated in the folder 'build/libs'.  From the the base directory you can run the application (important: a config.json must be present! )

    java -jar build/libs/docker-registry-cleaner-1.0-SNAPSHOT.jar


    
    
