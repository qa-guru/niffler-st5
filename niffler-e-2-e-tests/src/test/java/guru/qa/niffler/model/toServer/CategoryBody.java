package guru.qa.niffler.model.toServer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CategoryBody(
        UUID id,
        String category,
        String username) {
}




