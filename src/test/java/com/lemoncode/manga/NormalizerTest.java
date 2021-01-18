package com.lemoncode.manga;

import com.lemoncode.util.UrlNormalizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NormalizerTest {

    @Test
    public void normalizeUrl() {
        String url = "http://abcdefgh.com/////          ";

        String trimmed = UrlNormalizer.normalize(url);

        Assertions.assertEquals("http://abcdefgh.com", trimmed);

    }

}