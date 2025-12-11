package Utils.Reports;

import Utils.OSUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Stream;


public class AllureBinaryManager {

    private static class LazyHolder
    {
        static final String VERSION = resolveVersion(); //Get allure version from pom.properties

        private static String resolveVersion() {
            String[] possiblePaths = {
                    "/META-INF/maven/io.qameta.allure/allure-testng/pom.properties",
                    "/META-INF/maven/io.qameta.allure/allure-junit5/pom.properties",
                    "/META-INF/maven/io.qameta.allure/allure-java-commons/pom.properties"
            };

            for (String path : possiblePaths) {
                try (InputStream input = AllureBinaryManager.class.getResourceAsStream(path)) {
                    if (input != null) {
                        Properties props = new Properties();
                        props.load(input);
                        return props.getProperty("version");
                    }
                } catch (Exception ignored) {}
            }

            try {
                Path extractionDir = AllureConstants.EXTRACTION_DIR;
                try (Stream<Path> paths = Files.list(extractionDir)) {
                    return paths
                            .filter(Files::isDirectory)
                            .map(path -> path.getFileName().toString())
                            .filter(name -> name.startsWith("allure-"))
                            .map(name -> name.replace("allure-", ""))
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("No allure-* folder found in " + extractionDir));
                }
            } catch (IOException e) {
                throw new IllegalStateException("Unable to determine Allure version", e);
            }
        }
    }

    public static Path getExecutable() {
        String version = LazyHolder.VERSION;
        //C:\Users\Mohamed\.m2\repository\allure\allure-2.34.1\bin
        Path binaryPath = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), "allure-" + version, "bin", "allure");
        return OSUtils.getCurrentOS() == OSUtils.OS.WINDOWS
                ? binaryPath.resolveSibling(binaryPath.getFileName() + ".bat")
                : binaryPath;
    }
}


