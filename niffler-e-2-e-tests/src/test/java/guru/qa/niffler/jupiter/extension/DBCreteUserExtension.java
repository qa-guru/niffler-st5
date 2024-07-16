package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.entity.CurrencyValues;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.data.repository.UserRepositoryHibernate;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.DataUtils;

import java.util.Arrays;

public class DBCreteUserExtension extends CreateUserExtension {

    private final UserRepository userRepository = new UserRepositoryHibernate();

    @Override
    public UserJson createUser(TestUser user) {
        String username = user.username().isEmpty()
                ? DataUtils.generateRandomUsername()
                : user.username();
        String password = user.password().isEmpty()
                ? "12345"
                : user.password();

        UserAuthEntity userAuth = new UserAuthEntity();
        userAuth.setUsername(username);
        userAuth.setPassword(password);
        userAuth.setEnabled(true);
        userAuth.setAccountNonExpired(true);
        userAuth.setAccountNonLocked(true);
        userAuth.setCredentialsNonExpired(true);
        AuthorityEntity[] authorities = Arrays.stream(Authority.values()).map(
                a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(a);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        userAuth.addAuthorities(authorities);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setCurrency(CurrencyValues.RUB);

        userRepository.createUserInAuth(userAuth);
        userRepository.createUserInUserdata(userEntity);
        return new UserJson(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getFirstname(),
                userEntity.getSurname(),
                guru.qa.niffler.model.CurrencyValues.valueOf(userEntity.getCurrency().name()),
                userEntity.getPhoto() == null ? "" : new String(userEntity.getPhoto()),
                userEntity.getPhotoSmall() == null ? "" : new String(userEntity.getPhotoSmall()),
                null,
                new TestData(
                        password
                )
        );
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
