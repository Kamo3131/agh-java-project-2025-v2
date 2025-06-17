package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javafx.concurrent.Task;

/**
 * The ZipCompress class allows adding source files and
 * compressing them into a ZIP archive.
 */
public class ZipCompress {
    private final ArrayList<String> sourceFiles;
    /**
     * Default constructor for the ZipCompress class.
     * Initializes an empty list of source files.
     */
    public ZipCompress() {
        sourceFiles = new ArrayList<>();
    }

    /**
     * Adds one or more source files to the list.
     *
     * @param file The file paths to be added.
     * @throws IllegalArgumentException if no arguments are passed.
     */
    public void addSourceFiles(String... file) {
        if (file.length != 0) {
            sourceFiles.addAll(Arrays.asList(file));
        } else {
            throw new IllegalArgumentException("No arguments passed to the function!");
        }
    }

    public ArrayList<String> getSourceFiles() {
        return sourceFiles;
    }
    public void clearSourceFiles() {
        sourceFiles.clear();
    }
    /**
     * Removes one or more files from the source file list.
     *
     * @param file The file paths to be removed.
     */
    public void deleteSourceFiles(String... file) {
        for (String a : file) {
            sourceFiles.remove(a);
        }
    }
    /**
     * @return Returns number of files in SourceFiles.
     */
    public int sizeSourceFiles() {
        return sourceFiles.size();
    }

    /**
     * Returns a string representation of the source file list.
     *
     * @return A string containing all file paths, each on a new line.
     */
    @Override
    public String toString() {
        String output = "";
        for (String file : sourceFiles) {
            output += file + "\n";
        }
        return output;
    }

    /**
     * Compresses all source files into a single ZIP archive.
     *
     * @param zipFile The name or path of the output ZIP file.
     * @throws IOException if an I/O error occurs during compression.
     */
    public File compress(String zipFile, Consumer<Double> progressCounter) throws IOException {
        File outputFile = new File(zipFile);
        long totalBytes = calculateTotalSizeOfSourceFiles();
        long bytesCompressed = 0;
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile))) {
            for (String t : sourceFiles) {
                File file = new File(t);
                FileInputStream in = new FileInputStream(t);
                try {
                    out.putNextEntry(new ZipEntry(file.getName()));

                    byte[] buffer = new byte[1024];
                    int c;
                    double progress = 0;
                    while ((c = in.read(buffer)) >= 0) {
                        out.write(buffer, 0, c);
                        bytesCompressed += c;
                        if(progressCounter!=null){
                            progress = (double) bytesCompressed / totalBytes;
                            progressCounter.accept(progress);
                        }
                    }
                    in.close();
                } catch (IOException e) {
                    System.out.println("IOException occurred!");
                }

            }
            return outputFile;
        }
    }

    public File compress(String zipFile) throws IOException {
        File outputFile = new File(zipFile);
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile))) {
            for (String t : sourceFiles) {
                File file = new File(t);
                FileInputStream in = new FileInputStream(t);
                try {
                    out.putNextEntry(new ZipEntry(file.getName()));

                    byte[] buffer = new byte[1024];
                    int c;
                    while ((c = in.read(buffer)) >= 0) {
                        out.write(buffer, 0, c);
                    }
                    in.close();
                } catch (IOException e) {
                    System.out.println("IOException occurred!");
                }

            }
            return outputFile;
        }
    }
    private long calculateTotalSizeOfSourceFiles() {
        long total = 0;
        for (String filePath : sourceFiles) {
            File file = new File(filePath);
            if (file.exists()) {
                total += file.length();
            }
        }
        return total;
    }
}

