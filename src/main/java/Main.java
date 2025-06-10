import client.HashPassword;
import client.ZipCompress;
import client.ZipDecompress;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
//        String password = "KaczOrDo222_l";
//        System.out.println(password);
//        HashPassword hash = new HashPassword(password, 32, 65000, 512);
//        String hashedPassword = hash.hash();
//        System.out.println(hashedPassword);
//        String password2 = "KaczOrDo222_1";
//        System.out.println(password2);
//        HashPassword hash2 = new HashPassword(password2, 32, 65000, 512);
//        String hashedPassword2 = hash2.hash();
//        System.out.println(hashedPassword2);
//
//        System.out.println(hash.verify(password, hashedPassword)+" "+hash.verify(password, hashedPassword2)+"\n"
//        +hash2.verify(password2, hashedPassword2)+" "+hash2.verify(password2, hashedPassword)+"\n");

        ZipCompress zip = new ZipCompress();
        zip.addSourceFiles("src/main/resources/testZipFiles/Albert.txt");
        System.out.println(zip);
        try {
            File zippedFile = zip.compress("src/main/resources/testZipFiles/Albert.zip");
            ZipDecompress zipDecompress = new ZipDecompress();
            zipDecompress.decompress(zippedFile.getAbsolutePath(), "src/main/resources/testZipFiles/newDic");
        } catch (IOException e){
            System.out.println(e);
        }


    }
}
