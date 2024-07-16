package guru.qa.niffler.model.gql;

import java.util.Map;
import java.util.Objects;

public record GqlRequest(
        String operationName,
        String query,
        Map<String, Objects> variables
) {
}
