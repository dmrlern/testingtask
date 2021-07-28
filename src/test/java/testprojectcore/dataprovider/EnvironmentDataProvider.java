package testprojectcore.dataprovider;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * @author Eren Demirel
 */
public class EnvironmentDataProvider {

    public static class ENVIRONMENTVARIABLE {
        public static String getEnvironmentVariable() throws IOException {
            return EnvVariableProvider.INSTANCE.getEnv();
        }
    }

    public static class TESTDATA {
        public static String getData(String key) throws IOException {
            return UseParsers.extractAValueFromJsonFile("src/test/resources/testdata/testdata.json", key);
        }
    }
}
