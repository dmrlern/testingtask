package testprojectcore.dataprovider;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Eren Demirel
 */
enum ConfigFilePathProvider {


    INSTANCE;

    private String configFilePath;
    Boolean warningLogged = false;

    public String getConfigFilePath() {
        Logger logger = LogManager.getLogger(ConfigFilePathProvider.class);

        try {
            configFilePath = System.getProperty("configfilepath").toLowerCase();
        } catch (Exception e) {
            if (!warningLogged) {
                logger.warn("Config file path parameter was not passed in run command, reading from the default config file under " + "'src/test/resources/config/config.txt'...");
            }
            configFilePath = "src/test/resources/config/config.txt";
            warningLogged = true;
        } finally {
            if (configFilePath == null || configFilePath.equals("")) {
                logger.warn("Could not set config file path parameter. It is either null or empty.");
            }
        }
        return configFilePath;
    }
}
