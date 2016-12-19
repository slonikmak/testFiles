import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HashSumm {

    private static final String FILENAME = "C:\\Users\\Oceanos\\Desktop\\musorzav3.jpg";

    private static final String ALGORITHM = "MD5";

    public static void main(String[] args) {



        FileUtills.getHash(ALGORITHM, Paths.get(FILENAME));

        Path dest = Paths.get("C:\\Users\\Oceanos\\Desktop\\musorzav32.jpg");

        try {
            FileUtills.jointFile(FileUtills.splitFile(Paths.get(FILENAME), 1024), dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileUtills.getHash(ALGORITHM, dest);
    }
}