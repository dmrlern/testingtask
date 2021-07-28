package testprojectcore.testcontext;



import java.util.*;

/**
 * This approach using dependency injection of type contructor injection with Picocontainer is thread-safe
 * as discussed here by a former Cucumber developer: https://stackoverflow.com/a/34531404
 * <p>
 * However, when same step definition is used by the scenarios in different feature files, store the references and
 * variables in one of the hash maps below, key always being the current thread id(Thread.currentThread().getId()) to
 * achieve thread safety
 */
public class TestContext {

    public HashMap<Long, String> map1 = new HashMap<>();
    public HashMap<Long, Map> map2 = new HashMap<>();

}
