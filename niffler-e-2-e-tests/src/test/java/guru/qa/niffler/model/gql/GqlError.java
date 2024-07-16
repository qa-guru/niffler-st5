package guru.qa.niffler.model.gql;

import java.util.List;
import java.util.Map;

public record GqlError(
        String message,
        List<Map<String, Object>> locations,
        List<String> path,
        Map<String, Object> extensions
) {
}
