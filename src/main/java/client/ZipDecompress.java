package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The `ZipDecompress` class allows you to extract the contents
 * of a ZIP archive into a specified directory.
 */
public class ZipDecompress{
    /**
     * Decompresses ZIP archive to the designed directory.
     *
     * @param inputFile The name or path of the input ZIP file.
     * @throws IOException if an I/O error occurs during compression.
     */
    public void decompress(String inputFile, String outputDir) throws IOException{
        File destDir = new File(outputDir);
        if(!destDir.exists()){
            destDir.mkdirs();
        }
        try(ZipInputStream in = new ZipInputStream(new FileInputStream(inputFile))){
            ZipEntry entry;
            while((entry = in.getNextEntry()) != null){
                System.out.println("File: "+entry);
                File outFile = new File(destDir, entry.getName());

                if(entry.isDirectory()){
                    outFile.mkdirs();
                }
                else{
                    File parent = outFile.getParentFile();
                    if(!parent.exists()){
                        parent.mkdirs();
                    }
                }

                try(FileOutputStream fos = new FileOutputStream(outFile)){
                    int d;
                    while((d = in.read())!=-1){
                        fos.write(d);
                    }

                }
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }
}