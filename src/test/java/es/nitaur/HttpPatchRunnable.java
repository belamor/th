package es.nitaur;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;

public class HttpPatchRunnable implements Runnable {

    public static final String ANSWER_QUESTION_API_FIRST_QUESTION = "/api/quizzes/questions/1/answers";
    public static final String LOCALHOST = "localhost";
    public static final String HTTP = "http";
    public static final Integer MAX_RETRIES = 10;

    private final int port;
    private final int idx;

    public HttpPatchRunnable(int port, int idx) {
        this.port = port;
        this.idx = idx;
    }

    @Override
    public void run() {
        try {
            URI uri = new URIBuilder()
                    .setScheme(HTTP)
                    .setHost(LOCALHOST)
                    .setPort(port)
                    .setPath(ANSWER_QUESTION_API_FIRST_QUESTION)
                    .build();

            HttpPatch patch = new HttpPatch(uri);
            patch.setHeader("Content-type", "application/json");
            patch.setEntity(new StringEntity("[{\"answer\":\"Test " + idx + "\"}, {\"answer\": \"TEST " + idx + "\"}]"));

            System.out.println("Executing request # " + idx + patch.getRequestLine());

            executeRequest(patch, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void executeRequest(HttpPatch patch, int retryIdx) throws IOException, InterruptedException {
        if (retryIdx > MAX_RETRIES) {
            return;
        }

        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(patch);
        if (HttpStatus.INTERNAL_SERVER_ERROR.value() == response.getStatusLine().getStatusCode()) {
            System.out.println("Call failed, re-executing request # " + idx + patch.getRequestLine());
            Thread.sleep(100);
            executeRequest(patch, ++retryIdx);
        }
    }

}
