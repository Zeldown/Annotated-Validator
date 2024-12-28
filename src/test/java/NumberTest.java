import be.zeldown.validator.Validators;
import be.zeldown.validator.impl.primitive.number.NumberValidator;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class NumberTest extends ValidatorTest {

    @NumberValidator.Range(min = -10, max = 10) private Number range;
    @NumberValidator.Equals(10)                 private Number equals;
    @NumberValidator.Greater(10)                private Number greater;
    @NumberValidator.GreaterOrEquals(10)        private Number greaterOrEquals;
    @NumberValidator.Lower(10)                  private Number lower;
    @NumberValidator.LowerOrEquals(10)          private Number lowerOrEquals;
    @NumberValidator.Modulo(10)                 private Number modulo;
    @NumberValidator.Negative                   private Number negative;
    @NumberValidator.Positive                   private Number positive;
    @NumberValidator.Zero                       private Number zero;
    @NumberValidator.NotZero                    private Number notZero;
    @NumberValidator.Odd                        private Number odd;
    @NumberValidator.Even                       private Number even;

    @Test
    public void testRange() {
        test(getField("range"), 0, -11, 11);
    }

    @Test
    public void testEquals() {
        test(getField("equals"), 10, 0);
    }

    @Test
    public void testGreater() {
        test(getField("greater"), 11, 10);
    }

    @Test
    public void testGreaterOrEquals() {
        test(getField("greaterOrEquals"), 10, 9);
    }

    @Test
    public void testLower() {
        test(getField("lower"), 9, 11);
    }

    @Test
    public void testLowerOrEquals() {
        test(getField("lowerOrEquals"), 10, 11);
    }

    @Test
    public void testModulo() {
        test(getField("modulo"), 10, 5);
    }

    @Test
    public void testNegative() {
        test(getField("negative"), -1, 0, 1);
    }

    @Test
    public void testPositive() {
        test(getField("positive"), 1, 0, -1);
    }

    @Test
    public void testZero() {
        test(getField("zero"), 0, -1, 1);
    }

    @Test
    public void testNotZero() {
        test(getField("notZero"), 1, 0);
    }

    @Test
    public void testOdd() {
        test(getField("odd"), 1, 2);
    }

    @Test
    public void testEven() {
        test(getField("even"), 2, 1);
    }

}