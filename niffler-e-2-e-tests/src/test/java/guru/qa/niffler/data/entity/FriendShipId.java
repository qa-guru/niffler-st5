package guru.qa.niffler.data.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class FriendShipId {

    private UUID requester;
    private UUID addressee;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendShipId friendsId = (FriendShipId) o;
        return Objects.equals(requester, friendsId.requester) && Objects.equals(addressee, friendsId.addressee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requester, addressee);
    }

}