package Client.Model;

import com.google.gson.Gson;

import java.nio.file.Files;
import java.nio.file.Path;

public class LocalDeckSaver {
    private static final Gson gson = new Gson();

    public static Result saveDeck(SavedDeck savedDeck, String filePath, boolean overWrite) {
        Path path = Path.of(filePath);
        if (Files.exists(path) && !overWrite) {
            return new Result(false, "File already exists. Use -o to overwrite.");
        }
        try {
            String json = gson.toJson(savedDeck);
            Files.write(path, json.getBytes());
            return new Result(true, "Deck saved successfully.");
        } catch (Exception e) {
            return new Result(false, "Error saving deck.");
        }
    }
}
