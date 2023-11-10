package org.example;

import java.util.UUID;

public class Utils {
    static String generateRandomEmail () {
        String uniqueId = UUID.randomUUID().toString().replace("-", "");
        return "olenkatyut+test_" + uniqueId + "@gmail.com";
    }
}
