package org.example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Objects;
import java.util.Properties;

public class FileWatcher {

    public static File addSalary = new File("/Users/hbj/Study/java-classloader-study/salary-system/target/salary-system-encry-1.0-SNAPSHOT.jar");

    public static File normalSalary = new File("/Users/hbj/Study/java-classloader-study/salary-system/target/salary-system-encry-2.0-SNAPSHOT.jar");

    private static Properties properties = new Properties();

    private static final String FILE_NAME = "config.properties";  // 要监听的文件名

    private static final String DIRECTORY_PATH = "/Users/hbj/Study/java-classloader-study/oa-system/src/main/resources/";             // 文件所在目录

    public static String getSalaryVersionConfig() {
        return properties.getProperty("salary.version");
    }

    public static File getSalaryVersion() {
        return "add".equals(properties.getProperty("salary_version")) ? addSalary : normalSalary;
    }

    public static void start() {
        new Thread(() -> {
            try {
                watchFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static void watchFile() throws IOException, InterruptedException {
        Path directoryPath = Paths.get(DIRECTORY_PATH);  // 监听当前目录
        WatchService watchService = FileSystems.getDefault().newWatchService();
        directoryPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

        System.out.println("Watching for changes in " + FILE_NAME);

        while (true) {
            WatchKey key = watchService.take();  // 阻塞，等待事件
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                Path changedFile = pathEvent.context();

                if (changedFile.getFileName().toString().equals(FILE_NAME)) {
                    System.out.println(FILE_NAME + " has changed. Reloading...");
                    reloadConfig();
                }
            }
            if (!key.reset()) {
                break;
            }
        }
    }

    // 重新加载配置文件
    public static void reloadConfig() {
        Properties newProps = new Properties();
        try (InputStream inputStream = Files.newInputStream(Paths.get(DIRECTORY_PATH + File.separator + FILE_NAME))) {
            newProps.load(inputStream);
            System.out.println("Configuration reloaded:");
            newProps.forEach((key, value) -> System.out.println(key + " = " + value));
            properties = newProps;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


