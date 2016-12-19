import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Oceanos on 16.12.2016.
 */
public class FileUtills {
    public static String getHash(String algorithm, Path filePath){
        String summ = "";
        try {
            // Получаем контрольную сумму для файла в виде массива байт
            final MessageDigest md = MessageDigest.getInstance(algorithm);
            final FileInputStream fis = new FileInputStream(filePath.toString());
            byte[] dataBytes = new byte[1024];
            int bytesRead;
            while((bytesRead = fis.read(dataBytes)) > 0) {
                md.update(dataBytes, 0, bytesRead);
            }
            byte[] mdBytes = md.digest();

            // Переводим контрольную сумму в виде массива байт в
            // шестнадцатеричное представление
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < mdBytes.length; i++) {
                sb.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            System.out.println("Контрольная сумма: " + sb.toString());
            summ = sb.toString();
        } catch (NoSuchAlgorithmException | IOException ex) {
            Logger.getLogger(HashSumm.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return summ;
    }

    public static List<byte[]> splitFile(Path file, int size) throws IOException {
        List<byte[]> bytes = new ArrayList<>();
        byte[] dataBytes = new byte[size];
        InputStream fis = Files.newInputStream(file);
        int bytesRead;
        int readedBytes = 0;
        while((bytesRead = fis.read(dataBytes)) > 0) {
            if (bytesRead<size){
                byte[] newBytes = new byte[bytesRead];
                System.arraycopy(dataBytes, 0, newBytes, 0, bytesRead);
                bytes.add(newBytes);
                break;
            }
           bytes.add((byte[])dataBytes.clone());
            readedBytes += bytesRead;
        }
        fis.close();
        return bytes;
    }

    public static void jointFile(List<byte[]> parts, Path destination) throws IOException {
        byte[] b = parts.get(parts.size()-1);
        for (int i = 0; i < b.length; i++) {
            System.out.println("b: "+b[i]);
        }

        if (!Files.exists(destination)){
            Files.createFile(destination);
        }
        OutputStream os = Files.newOutputStream(destination);
        parts.forEach(bytes -> {
            try {
                os.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        os.flush();
        os.close();

    }

    public static void readAttr(Path path) throws IOException {
        System.out.println("Basic file attributes:");
        //Windows:
        //lastAccessTime=2016-12-16T13:35:54.44938Z, lastModifiedTime=2016-12-16T13:50:42.370794Z,
        // size=245, creationTime=2016-12-16T13:35:54.44938Z, isSymbolicLink=false, isRegularFile=true,
        // fileKey=null, isOther=false, isDirectory=false
        System.out.println(Files.readAttributes(path, "*"));


    }

    public static void getFileAttributesTypes(){
        FileSystem fileSystem = FileSystems.getDefault();
        Set<String> fileSystemViews = fileSystem.supportedFileAttributeViews();

        // We iterate over the available file attribute views
        // of a file system
        for (String fileSystemView : fileSystemViews) {
            System.out.println(fileSystemView);
        }
    }

    public static void main(String[] args) {
        Path path = Paths.get("C:\\Users\\Oceanos\\Desktop\\21.txt");
        try {
            readAttr(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getFileAttributesTypes();
    }
}
