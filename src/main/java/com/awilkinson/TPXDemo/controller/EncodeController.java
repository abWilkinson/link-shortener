package com.awilkinson.TPXDemo.controller;

import com.awilkinson.TPXDemo.model.EncodeDecodeDTO;
import com.awilkinson.TPXDemo.service.EncodeDecodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/encode")
public class EncodeController {
    private final EncodeDecodeService encodeDecodeService;

    public EncodeController(EncodeDecodeService encodeDecodeService) {
        this.encodeDecodeService = encodeDecodeService;
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EncodeDecodeDTO> encode(@RequestBody EncodeDecodeDTO encodeDecodeDTO) {
        return new ResponseEntity<>(encodeDecodeService.encode(encodeDecodeDTO.getUrl()), HttpStatus.CREATED);
    }
}
