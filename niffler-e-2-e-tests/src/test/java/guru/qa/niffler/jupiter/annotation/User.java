package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.constant.Friendship;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static guru.qa.niffler.constant.Friendship.DEFAULT;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface User {

    Friendship friendship() default DEFAULT;

}