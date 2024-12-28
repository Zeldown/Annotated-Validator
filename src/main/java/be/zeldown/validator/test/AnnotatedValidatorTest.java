package be.zeldown.validator.test;

import be.zeldown.validator.Validators;
import be.zeldown.validator.annotation.NotNull;
import be.zeldown.validator.exception.ValidatorException;
import be.zeldown.validator.impl.primitive.number.NumberValidator;
import be.zeldown.validator.impl.primitive.string.StringValidator;

import java.lang.reflect.Field;

public class AnnotatedValidatorTest {

    @NumberValidator.Range(min = -10, max = 10) private static int intValue;
    @NumberValidator.Equals(10)                 private static long longValue;
    @NumberValidator.Greater(10)                private static float floatValue;
    @NumberValidator.Positive                   private static double doubleValue;
    @NumberValidator.Zero                       private static short shortValue;
    @NumberValidator.Odd                        private static byte byteValue;

    @StringValidator.Length(min = 3, max = 10)
    @StringValidator.Regex("[a-zA-Z]+")
    @StringValidator.NotEmpty
    private static String stringValue;

    @NotNull
    private static Object objectValue;

    public static void main(String[] args) {
        test(getField("intValue"), 0, 10);
        test(getField("longValue"), 10, 11);
        test(getField("floatValue"), 11, 10);
        test(getField("doubleValue"), 11, -1);
        test(getField("shortValue"), (short) 0, (short) 1);
        test(getField("byteValue"), (byte) 1, (byte) 2);

        test(getField("stringValue"), "abc", "123");
        test(getField("objectValue"), new Object(), null);
    }

    private static void test(final Field field, final Object validValue, final Object invalidValue) {
        try {
            field.setAccessible(true);
            field.set(null, validValue);
            if (Validators.validate(field, validValue, false) != null) {
                throw new RuntimeException("The value is valid but the validator returned an exception");
            }

            field.set(null, invalidValue);
            if (Validators.validate(field, invalidValue, false) == null) {
                throw new RuntimeException("The value is invalid but the validator returned no exception");
            }

            System.out.println("[+] " + field.getName() + " has been tested successfully");
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Field getField(final String name) {
        try {
            return AnnotatedValidatorTest.class.getDeclaredField(name);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

}