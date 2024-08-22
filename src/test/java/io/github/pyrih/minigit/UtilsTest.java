package io.github.pyrih.minigit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UtilsTest {

    public static final String EXPECTED = "ks/Os51X2RTtixTQ43ZD3geXrlY=";
    public static final String INPUT = "42";

    @Test
    void shouldEncryptInputStringToSHA1() {
        String actual = Utils.encryptToSHA1(INPUT);

        Assertions.assertEquals(EXPECTED, actual);
    }

    @Test
    void shouldThrowAnExceptionWhenInputIsNull() {
        Exception actual = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> Utils.encryptToSHA1(null)
        );

        Assertions.assertEquals("An input value cannot be null.", actual.getMessage());
    }

    @Test
    void shouldGenerateSHA1HashFromInputString() {
        String input = "this is cool";
        String expected = "60f51187e76a9de0ff3df31f051bde04da2da891";

        String actual = Utils.generateSHA1Hash(input);

        Assertions.assertEquals(expected, actual);
    }
}