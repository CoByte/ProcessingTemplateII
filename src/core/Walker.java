package core;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.function.Function;

public class Walker<T> extends SimpleFileVisitor<Path> {
    private static final Path ROOT_PATH = Paths.get("resources").toAbsolutePath();

    private final String folder;
    private final String extension;
    private final Function<String, T> itemLoader;

    private HashMap<String, T> items;

    public Walker(String folder, String extension, Function<String, T> itemLoader) {
        this.folder = folder;
        this.extension = extension;
        this.itemLoader = itemLoader;

        this.items = new HashMap<>();
    }

    public HashMap<String, T> walk() {
        items = new HashMap<>();

        try {
            Files.walkFileTree(
                    ROOT_PATH.resolve(folder),
                    this
            );
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }

        return items;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (!file.toString().endsWith(extension)) return FileVisitResult.CONTINUE;

        String path = ROOT_PATH.relativize(file).toString();
        String name = path
                .substring(8, path.length() - 4)
                .replace('\\', '_')
                .replace('/', '_');

        System.out.println(path);

        items.put(name, itemLoader.apply(path));

        return FileVisitResult.CONTINUE;
    }
}
