package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyJson;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.SessionJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import guru.qa.niffler.model.UserJson;

import java.io.IOException;
import java.util.List;

public class GatewayApiClient extends ApiClient {

    private final GatewayApi gatewayApi;

    public GatewayApiClient() {
        super(CONFIG.gatewayUrl());
        this.gatewayApi = retrofit.create(GatewayApi.class);
    }

    public List<CategoryJson> getCategories(String token) {
        try {
            return gatewayApi.getCategories(token).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CategoryJson addCategory(String token, CategoryJson categoryJson) {
        try {
            return gatewayApi.addCategory(token, categoryJson).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CurrencyJson> getAllCurrencies(String token) {
        try {
            return gatewayApi.getAllCurrencies(token).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserJson> friends(String token) {
        try {
            return gatewayApi.friends(token).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeFriend(String token, String targetUsername) {
        try {
            gatewayApi.removeFriend(token, targetUsername).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserJson> incomeInvitations(String token) {
        try {
            return gatewayApi.incomeInvitations(token).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserJson> outcomeInvitations(String token) {
        try {
            return gatewayApi.outcomeInvitations(token).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UserJson sendInvitation(String token, FriendJson friend) {
        try {
            return gatewayApi.sendInvitation(token, friend).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UserJson acceptInvitation(String token, FriendJson invitation) {
        try {
            return gatewayApi.acceptInvitation(token, invitation).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UserJson declineInvitation(String token, FriendJson invitation) {
        try {
            return gatewayApi.declineInvitation(token, invitation).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public SessionJson session(String token) {
        try {
            return gatewayApi.session(token).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SpendJson getSpends(String token) {
        try {
            return gatewayApi.getSpends(token).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SpendJson addSpend(String token, SpendJson spend) {
        try {
            return gatewayApi.addSpend(token, spend).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public SpendJson editSpend(String token, SpendJson spend) {
        try {
            return gatewayApi.editSpend(token, spend).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteSpends(String token, List<String> ids) {
        try {
            gatewayApi.deleteSpends(token, ids).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<StatisticJson> getTotalStatistic(String token) {
        try {
            return gatewayApi.getTotalStatistic(token).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserJson> currentUser(String token) {
        try {
            return gatewayApi.currentUser(token).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserJson> allUsers(String token) {
        try {
            return gatewayApi.allUsers(token).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UserJson updateUserInfo(String token, UserJson user) {
        try {
            return gatewayApi.updateUserInfo(token, user).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}