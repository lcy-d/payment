package com.yu.payment.service;

import com.yu.payment.domain.Video;
import javafx.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IVideoServiceTest {

    @Autowired
    private IVideoService videoService;

    @Test
    public void findAll() {
        List<Video> all = videoService.findAll();
        assertNotNull(all);
        for (Video video : all) {
            System.out.println(video);
        }
    }

    @Test
    public void findById() {
        Video byId = videoService.findById(1);
        assertNotNull(byId);
        System.out.println(byId.getTitle());
    }

    @Test
    public void delById() {
    }

    @Test
    public void update() {
    }

    @Test
    public void save() {
    }

    @Test
    public void pageVideo() {
    }
}