package ordArray;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrdArrayTest {
    @Test
    void newArrayHaveZeroSize() {
        OrdArray arr = new OrdArray(1);
        assertEquals(arr.size(), 0, "New array must have zero size");
    }
}
