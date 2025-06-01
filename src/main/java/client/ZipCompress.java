package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    public long compress(String zipFile) throws IOException {
        File outputFile = new File(zipFile);
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile))) {
            for (String t : sourceFiles) {
                FileInputStream in = new FileInputStream(t);
                try {
                    out.putNextEntry(new ZipEntry(t));
                    int c;
                    while ((c = in.read()) != -1) {
                        out.write(c);
                    }
                    in.close();
                } catch (IOException e) {
                    System.out.println("IOException occurred!");
                }
                return outputFile.length();
            }
        }
        return 0;
    }
}