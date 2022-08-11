package com.awilkinson.TPXDemo.model;

import javax.validation.constraints.NotBlank;

/**
 * The expected request and response DTO, which only contains a url to be encoded or decoded.
 */
public class EncodeDecodeDTO {
    @NotBlank
    private String url;

    /**
     * Empty constructor needed for de-serialization
     */
    public EncodeDecodeDTO() {

    }

    public EncodeDecodeDTO(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
