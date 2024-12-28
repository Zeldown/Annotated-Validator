import be.zeldown.validator.impl.primitive.number.NumberValidator;
import be.zeldown.validator.impl.primitive.string.StringValidator;
import org.junit.jupiter.api.Test;

public class StringTest extends ValidatorTest {

    @StringValidator.Length(min = 3, max = 16) private String length;
    @StringValidator.Regex("[a-zA-Z0-9]+")     private String regex;
    @StringValidator.Empty                     private String empty;
    @StringValidator.NotEmpty                  private String notEmpty;

    @Test
    public void testLength() {
        test(getField("length"), "123", "12", "12345678901234567");
    }

    @Test
    public void testRegex() {
        test(getField("regex"), "abc123", "abc123!", "abc 123");
    }

    @Test
    public void testEmpty() {
        test(getField("empty"), "", " ", "abc");
    }

    @Test
    public void testNotEmpty() {
        test(getField("notEmpty"), "abc", "");
    }

}