package testprojectcore.dataprovider;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import java.io.File;
import java.io.IOException;


/**
 * @author Eren Demirel
 */
public class JacksonObjectMapper {

    private JacksonObjectMapper() {
    }

    /**
     * Maps json file to Java object via Jackson object mapper
     *
     * @param clazz    Object that is being mapped to
     * @param filePath File path of the json file to map from
     * @return t           Instance of object that is being returned from object mapper
     */
    public static <T> T mapJsonFileToObject(Class<T> clazz, String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        File file = new File(filePath);
        T t = mapper.readValue(file, clazz);
        return t;
    }
}
