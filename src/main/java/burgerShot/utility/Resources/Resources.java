package burgerShot.utility.Resources;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.imageio.ImageIO;


public class Resources {

    @SuppressWarnings("rawtypes")
	static Class source = Resources.class;

    private Resources() {
    }

    public static void setSourceClass(@SuppressWarnings("rawtypes") Class source) {
        Resources.source = source;
    }

    public static BufferedImage getImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getResource(path));
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            System.exit(-1);
        }
        return image;
    }
    public static File extract(String path) throws IOException {
        return extract(path, null);
    }

    public static File extract(String source, File destination) throws IOException {
        if (source.startsWith("/")) {
            source = source.substring(1);
        }
        File jarPath = getRunningSource();
        File homeDir = getHomeDir();
        if (destination == null) {
            destination = homeDir;
        }

        InputStream input = null;
        FileOutputStream output = null;
        try {
            if (isRunningFromJar()) {
                JarFile jar = new JarFile(jarPath);
                JarEntry sourceFile = jar.getJarEntry(source);
                String fileName = sourceFile.getName();
                fileName = fileName.substring(fileName.lastIndexOf("/"));
                input = jar.getInputStream(sourceFile);
                destination = new File(destination, fileName);
            } else {
                File sourceFile = new File(homeDir, source);
                input = new FileInputStream(sourceFile);
                destination = new File(destination, sourceFile.getName());
            }
            if (!destination.exists()) {
                destination.getParentFile().mkdirs();
                output = new FileOutputStream(destination);
                while (input.available() > 0) {
                    output.write(input.read());
                }
            }
        } finally {
            try {
                input.close();
                output.close();
            } catch (Exception e) {
            }
        }
        return destination;
    }

    public static File getRunningSource() {
        CodeSource codeSource = source.getProtectionDomain().getCodeSource();
        try {
            return new File(codeSource.getLocation().toURI().getPath());
        } catch (URISyntaxException ex) {
            return null;
        }
    }

    public static File getHomeDir() {
        File runningSource = getRunningSource();
        if (isRunningFromJar()) {
            return runningSource.getParentFile();
        }
        return runningSource;
    }

    public static File getHomeSubDir(String sub) {
        File home = getHomeDir();
        return new File(home, sub);
    }

    public static boolean isRunningFromJar() {
        String className = Resources.class.getName().replace('.', '/');
        String classJar = Resources.class.getResource("/" + className + ".class").toString();
        return classJar.startsWith("jar:");
    }


    public static URL getResource(String resource) {
        return Resources.source.getResource(resource);
    }

    public static InputStream getResourceAsStream(String resource) {
        return Resources.source.getResourceAsStream(resource);
    }
}
