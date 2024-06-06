package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public record TestData(
        @JsonIgnore String password,
        @JsonIgnore List<CategoryJson> categories,
        @JsonIgnore List<SpendJson> spends,
        @JsonIgnore List<UserJson> friends,
        @JsonIgnore List<UserJson> outcomeInvitations,
        @JsonIgnore List<UserJson> incomeInvitations) {

    public TestData(String password) {
        this(password, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
}
