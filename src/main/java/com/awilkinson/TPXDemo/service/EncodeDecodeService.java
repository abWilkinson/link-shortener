package com.awilkinson.TPXDemo.service;

import com.awilkinson.TPXDemo.domain.ShortenedURL;
import com.awilkinson.TPXDemo.exception.URLNotFoundException;
import com.awilkinson.TPXDemo.exception.InvalidURLException;
import com.awilkinson.TPXDemo.model.EncodeDecodeDTO;
import com.awilkinson.TPXDemo.repository.ShortenedURLRepository;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Optional;

/**
 * EncodeDecodeService
 *
 * Service to perform encoding and decoding of URLs.
 */
@Service
public class EncodeDecodeService {
    private final String baseURL;
    private final ShortenedURLRepository shortenedURLRepository;
    private final Hashids hashids;

    public EncodeDecodeService(ShortenedURLRepository shortenedURLRepository,
                               @Value("${encodedecode.baseurl}") String baseURL,
                               @Value("${encodedecode.salt}") String salt) {
        this.shortenedURLRepository = shortenedURLRepository;
        this.baseURL = baseURL;
        this.hashids = new Hashids(salt);
    }

    /**
     * Perform URL encoding of a full URL. The shortened URL will be a combination of the baseURL from
     * application.properties and and encoding of the database ID.
     *
     * @param fullURL from the request
     * @return EncodeDecodeDTO containing the encoded URL.
     */
    public EncodeDecodeDTO encode(String fullURL) {
        if (isValidURL(fullURL)) {
            ShortenedURL savedURL = shortenedURLRepository.save(new ShortenedURL(fullURL));
            return new EncodeDecodeDTO(baseURL + hashids.encode(savedURL.getId()));
        }
        throw new InvalidURLException("An invalid URL has been provided.");
    }

    /**
     * Decodes the shortened URL back into the DB id and looks up the original URL.
     *
     * @param encodedURL from the request
     * @return EncodeDecodeDTO containing the original URL
     */
    public EncodeDecodeDTO decode(String encodedURL) {
        String[] split = encodedURL.split(baseURL);
        if (split.length == 2) {
            long[] decodedIds = hashids.decode(split[1]);
            if (decodedIds.length == 0) {
                throw new URLNotFoundException("A URL was not found for this shortened URL.");
            }
            Optional<ShortenedURL> savedURL = shortenedURLRepository.findById(decodedIds[0]);
            if (savedURL.isEmpty()) {
                throw new URLNotFoundException("A URL was not found for this shortened URL.");
            }
            return new EncodeDecodeDTO(savedURL.get().getFullURL());
        }
        throw new InvalidURLException("An invalid encoded URL has been provided.");
    }

    /**
     * Basic implementation to test a valid URL.
     */
    private boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
