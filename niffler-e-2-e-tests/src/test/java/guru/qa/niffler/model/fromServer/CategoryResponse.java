package guru.qa.niffler.model.fromServer;

public record CategoryResponse(
        String category,
        String id,
        String username
) {
}
