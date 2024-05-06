package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.model.UserType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface User {

    UserType value() default UserType.COMMON;


}
