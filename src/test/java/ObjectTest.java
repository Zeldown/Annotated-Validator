import be.zeldown.validator.annotation.NotNull;
import be.zeldown.validator.impl.primitive.string.StringValidator;
import org.junit.jupiter.api.Test;

public class ObjectTest extends ValidatorTest {

    @NotNull private Object object;

    @Test
    public void testNotNull() {
        test(getField("object"), new Object(), new Object[]{null});
    }

}