import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    public static final String uri = "https://api.nasa.gov/planetary/apod?api_key=1MqIfRLLPPGLZCaylmm2VKuOANP9vspIbDJduBUB";
    public static final ObjectMapper mapper = new ObjectMapper();
    public static final String dir = "c:" + File.separator + "Users" + File.separator + "ziDmitryE" +
            File.separator + "Desktop" + File.separator + "Новая папка" + File.separator + "НАСА";

    public static void main(String[] args) throws IOException {

        final HttpUriRequest request = new HttpGet(uri);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {
            Image imageData = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {
            });
            HttpUriRequest request2 = new HttpGet(imageData.getHdurl());
            try (CloseableHttpResponse response2 = httpClient.execute(request2)) {
                File file = new File(dir, getName(imageData.getHdurl()));
                try {
                    if (file.createNewFile()) ;
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] bytes = response2.getEntity().getContent().readAllBytes();
                    fos.write(bytes, 0, bytes.length);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    static String getName(String text) {
        String[] data = text.split("/");
        return data[data.length - 1];
    }
}
