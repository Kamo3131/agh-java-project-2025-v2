import client.HashPassword;
public class Main {
    public static void main(String[] args) {
        String password = "KaczOrDo222_l";
        System.out.println(password);
        HashPassword hash = new HashPassword(password, 32, 65000, 512);
        String hashedPassword = hash.hash();
        System.out.println(hashedPassword);
        String password2 = "KaczOrDo222_1";
        System.out.println(password2);
        HashPassword hash2 = new HashPassword(password2, 32, 65000, 512);
        String hashedPassword2 = hash2.hash();
        System.out.println(hashedPassword2);

        System.out.println(hash.verify(password, hashedPassword)+" "+hash.verify(password, hashedPassword2)+"\n"
        +hash2.verify(password2, hashedPassword2)+" "+hash2.verify(password2, hashedPassword)+"\n");
    }
}
