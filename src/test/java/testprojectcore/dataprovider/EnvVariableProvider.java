package testprojectcore.dataprovider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


/**
 * @author Eren Demirel
 */
enum EnvVariableProvider {

    INSTANCE;

    private String env;
    Boolean warningLogged = false;


    public String getEnv() throws IOException {

        Logger logger = LogManager.getLogger(EnvVariableProvider.class);

        try {
            env = System.getProperty("env").toLowerCase();
        } catch (Exception e) {
            if (!warningLogged) {
                logger.warn("Environment parameter was not passed in run command, setting the value from the config file...");
            }
            env = UseParsers.getAValueFromTxtPropertyFile("src/test/resources/config/config.txt", "env");
            warningLogged = true;
        } finally {
            if (env == null || env.equals("")) {
                logger.warn("Could not set environment parameter. It is either null or empty.");
            }
        }
        return env;
    }
}
