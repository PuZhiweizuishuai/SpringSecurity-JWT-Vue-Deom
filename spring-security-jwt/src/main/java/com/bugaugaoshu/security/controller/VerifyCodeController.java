package com.bugaugaoshu.security.controller;

import com.bugaugaoshu.security.service.VerifyCodeService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-27 19:57
 */
@RestController
@RequestMapping("/api")
public class VerifyCodeController {
    private final VerifyCodeService verifyCodeService;

    private static final String IMAGE_FORMAT = "png";

    public VerifyCodeController(VerifyCodeService verifyCodeService) {
        this.verifyCodeService = verifyCodeService;
    }

    private static InputStreamResource imageToInputStreamResource(Image image, String format) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write((RenderedImage) image, format, byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return new InputStreamResource(byteArrayInputStream);
    }

    @GetMapping("/image")
    public HttpEntity image(HttpSession session) throws IOException {
        Image image = verifyCodeService.image(session.getId());
        InputStreamResource inputStreamResource = imageToInputStreamResource(image, IMAGE_FORMAT);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(inputStreamResource);
    }

    @PostMapping("/image/verify")
    public HttpEntity imageVerify(HttpSession session, String code) {
        verifyCodeService.verify(session.getId(), code);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}
