package com.bugaugaoshu.security.service.impl;

import com.bugaugaoshu.security.service.GenerateImageService;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author puzhiwei
 */
@Service
public class GenerateImageServiceImpl implements GenerateImageService {

    private static final int DEFAULT_IMAGE_WIDTH = 80;

    private static final int DEFAULT_IMAGE_HEIGHT = 60;

    private static final int BYTE_MASK = 0xff;

    private static Color randomArgbColor() {
        Random random = new Random();
        int a = random.nextInt() & BYTE_MASK;
        int r = random.nextInt() & BYTE_MASK;
        int g = random.nextInt() & BYTE_MASK;
        int b = random.nextInt() & BYTE_MASK;
        return new Color(r, g, b, a);
    }

    @Override
    public Image imageWithDisturb(String string) {
        return imageWithDisturb(string, DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
    }

    private Image imageWithDisturb(String string, int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(randomArgbColor());
        graphics.drawString(string, width / 2, height / 2);
        return bufferedImage;
    }



}
