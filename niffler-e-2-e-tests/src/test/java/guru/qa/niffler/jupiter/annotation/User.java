package guru.qa.niffler.jupiter.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface User {
    public enum UserType {
        INVITATION_SEND, WITH_FRIEND, INVITATION_RECIEVED, DEFAULT_USER
    }
    UserType value() default UserType.DEFAULT_USER;


}
