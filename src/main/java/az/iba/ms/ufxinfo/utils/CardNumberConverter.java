package az.iba.ms.ufxinfo.utils;

import az.iba.ms.ufxinfo.exceptions.InvalidCardNumberException;
import org.springframework.stereotype.Component;

@Component
public class CardNumberConverter {

    public String convert(String number) {
        return number.toUpperCase().startsWith("N") ? number.substring(1) : getReplaceNumber(number);
    }

    private String getReplaceNumber(String number) {
        if (number.length() < 9) {
            throw new InvalidCardNumberException("Invalid card number " + number);
        }
        String cardNumber = "";
        String newNumber = number.substring(4, number.length() - 4);
        Integer num = Integer.valueOf(Integer.parseInt(newNumber) ^ getKey(number).intValue());
        cardNumber = number.substring(0, 4) + num + number.substring(number.length() - 4);
        if (cardNumber.startsWith("55229")) {
            cardNumber = "552209" + cardNumber.substring(5);
        }
        return cardNumber;
    }

    private Integer getKey(String number) {
        String[] numberSplit = number.replace("", "/").substring(1).split("/");
        String key = "";
        int numLength = numberSplit.length;
        int indicator = Integer.parseInt(numberSplit[numLength - 1]);
        if (indicator % 2 > 0) {
            key =
                    numberSplit[1]
                            + numberSplit[3]
                            + numberSplit[numLength - 3]
                            + numberSplit[numLength - 1]
                            + numberSplit[0]
                            + numberSplit[2]
                            + numberSplit[numLength - 4]
                            + numberSplit[numLength - 2];
        } else {
            key =
                    numberSplit[0]
                            + numberSplit[2]
                            + numberSplit[numLength - 4]
                            + numberSplit[numLength - 2]
                            + numberSplit[1]
                            + numberSplit[3]
                            + numberSplit[numLength - 3]
                            + numberSplit[numLength - 1];
        }
        return Integer.valueOf(Integer.parseInt(key));
    }
}
