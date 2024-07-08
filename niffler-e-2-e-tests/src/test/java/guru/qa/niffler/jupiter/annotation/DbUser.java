package guru.qa.niffler.jupiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Атрибут для пометки параметров тестовых методов
@Target(ElementType.METHOD) // Указывает, что этот атрибут может быть применен только к параметрам методов
@Retention(RetentionPolicy.RUNTIME) // Указывает, что этот атрибут будет доступен на этапе выполнения
public @interface DbUser {
    // Этот атрибут не имеет свойств
}