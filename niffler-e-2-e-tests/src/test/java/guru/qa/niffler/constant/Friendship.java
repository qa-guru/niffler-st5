package guru.qa.niffler.constant;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum Friendship {

    PENDING_INVITATION("Pending invitation"),
    INVITATION_RECEIVED,
    WITH_FRIENDS("You are friends"),
    DEFAULT;

    private String message;

    Friendship(String message) {
        this.message = message;
    }

}