package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.UserJson;

public class RestCreteUserExtension extends CreateUserExtension {
  @Override
  public UserJson createUser(TestUser user) {
    return null;
  }

  @Override
  public UserJson createCategory(TestUser user, UserJson createdUser) {
    return null;
  }

  @Override
  public UserJson createSpend(TestUser user, UserJson createdUser) {
    return null;
  }
}
