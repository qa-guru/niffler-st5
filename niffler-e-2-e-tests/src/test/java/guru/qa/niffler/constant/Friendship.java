package guru.qa.niffler.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Friendship {

    PENDING_INVITATION("Pending invitation"),
    INVITATION_RECEIVED,
    WITH_FRIENDS("You are friends"),
    DEFAULT;

    private String message;

}