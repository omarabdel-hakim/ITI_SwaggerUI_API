package Utils.Reports;

import com.google.common.collect.ImmutableMap;

import java.io.File;

import static Utils.DataReader.PropertyReader.getProperty;
import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

public class AllureEnvironmentManager {
    public static void setEnvironmentVariables() {
        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("OS", getProperty("os.name"))
                        .put("Java version:", getProperty("java.runtime.version"))
                        .put("URL", getProperty("baseURI"))
                        .build(), String.valueOf(AllureConstants.RESULTS_FOLDER) + File.separator
        );

    }
}

