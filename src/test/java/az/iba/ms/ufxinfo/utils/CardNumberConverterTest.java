package az.iba.ms.ufxinfo.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import az.iba.ms.ufxinfo.exceptions.InvalidCardNumberException;
import org.junit.jupiter.api.Test;

class CardNumberConverterTest {

    private final CardNumberConverter cardNumberConverter = new CardNumberConverter();

    @Test
    public void shouldConvertGivenNumbers() {

        String result = cardNumberConverter.convert("412729348000931");

        assertThat(result).isEqualTo("4127208121070931");
    }

    @Test
    public void shouldThrowException_WhenCardNumberLengthIsLessThen9() {

        assertThrows(
                InvalidCardNumberException.class,
                () ->
                        cardNumberConverter.convert("44445555"));
    }
}
