import be.zeldown.validator.Validators;
import be.zeldown.validator.exception.ValidatorException;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public abstract class ValidatorTest {

    protected void test(final Field field, final Object validValue, final Object... invalidValues) {
        ValidatorException exception = null;
        try {
            field.setAccessible(true);

            field.set(this, validValue);
            exception = Validators.validate(field, validValue, false);
            assertNull(exception, "The value is valid but the validator returned an exception");

            for (final Object invalidValue : invalidValues) {
                field.set(this, invalidValue);
                exception = Validators.validate(field, invalidValue, false);
                assertNotNull(exception, "The value is invalid but the validator returned no exception");
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Field getField(final String name) {
        try {
            return this.getClass().getDeclaredField(name);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

}
