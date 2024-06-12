package xyz.ludwicz.librarysystem;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {

    private static final String CONFIG_FILE_NAME = "config.json";


    public static String DB_HOST;
    public static int DB_PORT;
    public static String DB_NAME;
    public static String DB_USERNAME;
    public static String DB_PASSWORD;
    public static String DB_FLAGS;
    public static int DB_POOLING_MAX_POOL_SIZE;
    public static int DB_POOLING_MAX_LIFETIME;
    public static int DB_POOLING_CONNECTION_TIMEOUT;

    static {
        try {
            Path configPath = getConfigFilePath();
            File configFile = configPath.toFile();

            JSONObject jsonObject;
            if (configFile.exists()) {
                FileReader reader = new FileReader(configFile);
                JSONParser parser = new JSONParser();
                jsonObject = (JSONObject) parser.parse(reader);
            } else {
                jsonObject = createDefaultConfig(configFile);
            }

            JSONObject dbSection = (JSONObject) jsonObject.get("database");
            DB_HOST = (String) dbSection.get("host");
            DB_PORT = Integer.parseInt(dbSection.get("port").toString());
            DB_NAME = (String) dbSection.get("name");
            DB_USERNAME = (String) dbSection.get("username");
            DB_PASSWORD = (String) dbSection.get("password");
            DB_FLAGS = (String) dbSection.get("flags");

            JSONObject poolingSection = (JSONObject) dbSection.get("pooling");
            DB_POOLING_MAX_POOL_SIZE = Integer.parseInt(poolingSection.get("maxPoolSize").toString());
            DB_POOLING_MAX_LIFETIME = Integer.parseInt(poolingSection.get("maxLifetime").toString());
            DB_POOLING_CONNECTION_TIMEOUT = Integer.parseInt(poolingSection.get("connectionTimeout").toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    private static Path getConfigFilePath() {
        String jarDir = System.getProperty("user.dir");
        return Paths.get(jarDir, CONFIG_FILE_NAME);
    }

    private static JSONObject createDefaultConfig(File configFile) throws IOException, ParseException {
        InputStream inputStream = Main.class.getResourceAsStream("/" + CONFIG_FILE_NAME);
        if (inputStream == null) {
            throw new IOException("Resource " + CONFIG_FILE_NAME + " not found in JAR.");
        }

        Files.copy(inputStream, configFile.toPath());

        try (FileReader reader = new FileReader(configFile)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            return jsonObject;
        }
    }

    private Config() {

    }
}
