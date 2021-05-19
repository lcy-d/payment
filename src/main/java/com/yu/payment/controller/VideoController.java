package com.yu.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.payment.domain.Video;
import com.yu.payment.service.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vi/video")
public class VideoController {

    @Autowired
    private IVideoService videoService;


    private ObjectMapper mapper = new ObjectMapper();


    /**
     * 分页接口
     * @param page
     * @param size
     * @return
     */
    @GetMapping("page")
    public List<Video> pageVideo(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return videoService.pageVideo(page,size);
    }

    /**
     * 根据 id查找
     * @param videoId
     * @return
     */
    @GetMapping("find_by_id")
    public Video findById(@RequestParam(value = "video_id", required = true) int videoId) {
        return videoService.findById(videoId);
    }



}
