package com.trimzo.service;

import com.trimzo.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;

@Service
public class ShortCodeService {

    private static final String ALPHABET =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALPHABET.length();   // 62

    private final ShortUrlRepository shortUrlRepository;

    public ShortCodeService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    /**
     * Generates the next unique short code:
     * pull the next sequence number, then Base62-encode it.
     */
    public String generateCode() {
        long number = shortUrlRepository.nextSequenceValue();
        return encodeBase62(number);
    }

    /**
     * Converts a number into a Base62 string.
     */
    private String encodeBase62(long number) {
        if (number == 0) {
            return String.valueOf(ALPHABET.charAt(0));
        }

        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            int remainder = (int) (number % BASE);
            sb.append(ALPHABET.charAt(remainder));
            number = number / BASE;
        }

        return sb.reverse().toString();
    }
}