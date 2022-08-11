package com.awilkinson.TPXDemo.service;

import com.awilkinson.TPXDemo.domain.ShortenedURL;
import com.awilkinson.TPXDemo.exception.URLNotFoundException;
import com.awilkinson.TPXDemo.exception.InvalidURLException;
import com.awilkinson.TPXDemo.repository.ShortenedURLRepository;
import org.hashids.Hashids;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EncodeDecodeServiceTest {
    @MockBean
    private ShortenedURLRepository shortenedURLRepository;

    @Autowired
    private EncodeDecodeService encodeDecodeService;

    @Value("${encodedecode.baseurl}")
    private String baseURL;

    @Value("${encodedecode.salt}")
    private String salt;

    @Test
    public void canEncodeURL() {
        when(shortenedURLRepository.save(any())).thenReturn(new ShortenedURL(1L, "http://test.com"));
        String encodedUrl = encodeDecodeService.encode("http://test.com").getUrl();

        assertTrue(encodedUrl.startsWith(baseURL));
    }

    @Test
    public void canDecodeURL() {
        Hashids hashids = new Hashids(salt);
        String encoded = hashids.encode(1);
        when(shortenedURLRepository.findById(1L)).thenReturn(Optional.of(new ShortenedURL(1L, "http://test.com")));
        String decodedURL = encodeDecodeService.decode(baseURL + encoded).getUrl();

        assertEquals("http://test.com", decodedURL);
    }

    @Test
    public void encodeShouldHandleInvalidURL() {
        InvalidURLException exception = Assertions.assertThrows(InvalidURLException.class, () ->
                encodeDecodeService.encode("aninvalidurl"));

        Assertions.assertEquals("An invalid URL has been provided.", exception.getMessage());
    }

    @Test
    public void decodeShouldHandleInvalidURL() {
        InvalidURLException exception = Assertions.assertThrows(InvalidURLException.class, () ->
                encodeDecodeService.decode("http://withnobaseurl/test"));

        Assertions.assertEquals("An invalid encoded URL has been provided.", exception.getMessage());
    }
    @Test
    public void decodeShouldHandleInvalidEncoding() {
        URLNotFoundException exception = Assertions.assertThrows(URLNotFoundException.class, () ->
                encodeDecodeService.decode(baseURL + "missing"));

        Assertions.assertEquals("A URL was not found for this shortened URL.", exception.getMessage());
    }

    @Test
    public void decodeShouldHandleNoMatchFound() {
        Hashids hashids = new Hashids(salt);
        String encoded = hashids.encode(1);

        URLNotFoundException exception = Assertions.assertThrows(URLNotFoundException.class, () ->
                encodeDecodeService.decode(baseURL + encoded));

        Assertions.assertEquals("A URL was not found for this shortened URL.", exception.getMessage());
    }
}
