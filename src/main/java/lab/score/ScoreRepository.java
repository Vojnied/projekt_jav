package lab.score;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoreRepository {

    public static final String CAUSE_CENTIPEDE = "CENTIPEDE";
    public static final String CAUSE_QUIT = "QUIT";

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static long findOrCreatePlayer(String nickname) throws ScoreException {
        try {
            String json = mapper.writeValueAsString(Map.of("nickname", nickname));
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/players"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode node = mapper.readTree(response.body());
            return node.get("id").asLong();
        } catch (Exception e) {
            throw new ScoreException("Cannot find or create player", e);
        }
    }

    public static long createSession(long playerId) throws ScoreException {
        try {
            String json = mapper.writeValueAsString(Map.of("playerId", playerId));
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/sessions"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode node = mapper.readTree(response.body());
            return node.get("id").asLong();
        } catch (Exception e) {
            throw new ScoreException("Cannot create session", e);
        }
    }

    public static void endSession(long sessionId, String causeOfDeath) throws ScoreException {
        try {
            String json = mapper.writeValueAsString(Map.of("causeOfDeath", causeOfDeath));
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/sessions/" + sessionId + "/end"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
            client.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            throw new ScoreException("Cannot end session", e);
        }
    }

    public static void saveScore(int points, long playerId, long sessionId, int levelNumber) throws ScoreException {
        try {
            String json = mapper.writeValueAsString(Map.of(
                "points", points,
                "playerId", playerId,
                "sessionId", sessionId,
                "levelNumber", levelNumber
            ));
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/scores"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
            client.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            throw new ScoreException("Cannot save score", e);
        }
    }

    public static List<Score> load() throws ScoreException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/scores"))
                .GET()
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = mapper.readTree(response.body());
            List<Score> result = new ArrayList<>();
            for (JsonNode node : root) {
                long id = node.get("id").asLong();
                String nick = node.get("nickname").asText();
                int points = node.get("points").asInt();
                result.add(new Score(id, nick, points));
            }
            return result;
        } catch (Exception e) {
            throw new ScoreException("Cannot load scores", e);
        }
    }

    public static void delete(Score score) throws ScoreException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/scores/" + score.getId()))
                .DELETE()
                .build();
            client.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            throw new ScoreException("Cannot delete score", e);
        }
    }
}
