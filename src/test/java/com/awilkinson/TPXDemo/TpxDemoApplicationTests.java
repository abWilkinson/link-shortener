package com.awilkinson.TPXDemo;

import com.awilkinson.TPXDemo.model.EncodeDecodeDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TpxDemoApplicationTests {
	private static final String ENCODE_URL = "/encode";
	private static final String DECODE_URL = "/decode";

	@LocalServerPort
	int port;

	@Value("${encodedecode.baseurl}")
	private String baseURL;

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
	}

	@Test
	void URLCanBeEncodedAndDecoded() {
		String originalURL = "http://avalidurl.com";
		String decodedURL = encodeDecode(originalURL);
		assertEquals(originalURL, decodedURL);
	}

	@Test
	void MultipleUrlsCanBeEncodedAndDecoded() {
		String originalURL1 = "http://avalidurl.com";
		String originalURL2 = "http://anothervalidurl.com";

		String encoded1 = encode(originalURL1);
		String encoded2 = encode(originalURL2);

		assertEquals(originalURL1, decode(encoded1));
		assertEquals(originalURL2, decode(encoded2));
	}

	@Test
	void urlNotFoundExceptionIsHandled() {
		String error = given()
				.body(new EncodeDecodeDTO(baseURL + "test"))
				.contentType(ContentType.JSON)
				.post(DECODE_URL)
				.then()
				.statusCode(404)
				.extract()
				.path("msg");

		assertEquals("A URL was not found for this shortened URL.", error);
	}

	@Test
	void invalidURLExceptionIsHandled() {
		String error = given()
				.body(new EncodeDecodeDTO("test"))
				.contentType(ContentType.JSON)
				.post(DECODE_URL)
				.then()
				.statusCode(400)
				.extract()
				.path("msg");

		assertEquals("An invalid encoded URL has been provided.", error);
	}


	private String encodeDecode(String originalURL) {
		String encodedUrl = encode(originalURL);
		return decode(encodedUrl);
	}

	private String encode(String originalURL) {
		return given()
				.body(new EncodeDecodeDTO(originalURL))
				.contentType(ContentType.JSON)
				.post(ENCODE_URL)
				.then()
				.statusCode(201)
				.extract()
				.path("url");
	}

	private String decode(String encodedURL) {
		return given()
				.body(new EncodeDecodeDTO(encodedURL))
				.contentType(ContentType.JSON)
				.post(DECODE_URL)
				.then()
				.statusCode(200)
				.extract()
				.path("url");
	}

}
