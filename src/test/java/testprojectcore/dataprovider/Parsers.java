package testprojectcore.dataprovider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Enum implementation of strategy design pattern for parsers used in the test framework
 *
 * @author Eren Demirel
 */
enum Parsers {

    ExtractAValueFromJsonFile {

        JsonObject gsonJsonObject;
        Gson gson = new Gson();
        BufferedReader bufferedReader;
        private String dataGroup;

        @Override
        String execute(String filePath, String requestedData) throws IOException {
            dataGroup = EnvVariableProvider.INSTANCE.getEnv();
            filePath = filePath.replace('/', File.separatorChar);
            try {
                bufferedReader = new BufferedReader(new FileReader(filePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            gsonJsonObject = gson.fromJson(bufferedReader, JsonObject.class);
            return gsonJsonObject.getAsJsonObject(dataGroup).get(requestedData).getAsString();
        }

        @Override
        LinkedHashMap<String, String> execute(String propertyFilePath) throws IOException {
            return null;
        }
    },

    GetAPropertyFromConfigTxtFile {

        Properties properties;

        @Override
        String execute(String txtPropertyFilePath, String requestedData) throws IOException {
            txtPropertyFilePath = txtPropertyFilePath.replace('/', File.separatorChar);
            String propertyValue = "";

            InputStream reader = new FileInputStream(txtPropertyFilePath);
            try {
                properties = new Properties();
                try {
                    properties.load(reader);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new IOException("Error while loading the property file " + "'" + txtPropertyFilePath);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("config.txt not found at " + txtPropertyFilePath);
            }
            propertyValue = properties.getProperty(requestedData).toLowerCase();
            if (propertyValue != null) {
                return propertyValue;
            } else
                throw new RuntimeException("Can not read property" + "'" + propertyValue + "'" + "from the configuration file");
        }

        @Override
        LinkedHashMap<String, String> execute(String propertyFilePath) throws IOException {
            return null;
        }
    },

    GetAPropertyFromPropertyFiles {

        Logger logger = LogManager.getLogger(Parsers.class);

        String propertyGroup;{
            try {
                propertyGroup = EnvVariableProvider.INSTANCE.getEnv();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Properties properties;

        @Override
        String execute(String propertyFilePath, String propertyName) throws IOException {
            properties = new Properties();
            propertyFilePath = propertyFilePath.replace('/', File.separatorChar);
            InputStream reader = new FileInputStream(propertyFilePath);
            String propertyValue = "";
            try {
                properties.load(reader);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Property file not found at " + propertyFilePath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("Error while loading the property file " + propertyFilePath);
            }
            Set<Object> keys = properties.keySet();
            for (Object key : keys) {
                if (key.toString().startsWith(propertyGroup + ".")) {
                    propertyValue = properties.getProperty(propertyGroup + '.' + propertyName).toLowerCase();
                } else if (key.toString().equals(propertyName)) {
                    if (!propertyFilePath.toLowerCase().contains("webconfig") && !propertyFilePath.toLowerCase().contains("androidconfig") && !propertyFilePath.toLowerCase().contains("iosconfig")) {
                        logger.warn("No such environment property: " + "'" + propertyGroup + "." + propertyName + "'" + " in " + propertyFilePath + ". Setting the variable using " + "'" + propertyName + "' property(No environment). " + "Please check the file later");
                    }
                    propertyValue = properties.getProperty(propertyName).toLowerCase();
                }
            }
            if (propertyValue == null) {
                logger.error("Couldn't find " + "'" + propertyGroup + "." + propertyName + "'" + " or " + "'" + propertyName + "'" + " in " + propertyFilePath);
                throw new IOException();
            }
            return propertyValue;
        }

        @Override
        LinkedHashMap<String, String> execute(String propertyFilePath) throws IOException {
            return null;
        }
    },

    GetAllPropertiesFromPropertyFiles {

        Logger logger = LogManager.getLogger(Parsers.class);

        LinkedHashMap<String, String> propertiesInOrder = new LinkedHashMap<String, String>();

        String propertyGroup;{
            try {
                propertyGroup = EnvVariableProvider.INSTANCE.getEnv();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Properties properties;
        FileReader reader;


        @Override
        LinkedHashMap<String, String> execute(String propertyFilePath) throws IOException {
            properties = new Properties();
            propertyFilePath = propertyFilePath.replace('/', File.separatorChar);
            reader = new FileReader(propertyFilePath);
            try {
                properties.load(reader);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Property file not found at " + propertyFilePath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("Error while loading the property file " + propertyFilePath);
            }
            Set<Object> keys = properties.keySet();
            for (Object key : keys) {
                if (key.toString().startsWith(propertyGroup + ".")) {
                    propertiesInOrder.put(key.toString(), properties.getProperty(propertyGroup + '.' + key.toString()));
                }
            }
            //else return all properties after the loop
            if (!(propertiesInOrder.size() > 0) && keys != null) {
                if (!propertyFilePath.toLowerCase().contains("webconfig") && !propertyFilePath.toLowerCase().contains("androidconfig") && !propertyFilePath.toLowerCase().contains("iosconfig")) {
                    logger.warn("No environment properties in: " + propertyFilePath + ". Returning all properties. Please check the file later");
                }
                for (Object key : keys) {
                    propertiesInOrder.put(key.toString(), properties.getProperty(key.toString()));
                }
            }
            if (keys.toString() == null) {
                logger.error("No properties defined in" + propertyFilePath);
                throw new IOException();
            }
            reader.close();
            return propertiesInOrder;
        }

        @Override
        String execute(String fileName, String requestedData) throws IOException {
            return null;
        }
    };


    abstract String execute(String fileName, String requestedData) throws IOException;

    abstract LinkedHashMap<String, String> execute(String propertyFilePath) throws IOException;

}
