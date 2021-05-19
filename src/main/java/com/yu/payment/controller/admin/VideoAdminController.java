package com.yu.payment.controller.admin;

import com.yu.payment.domain.Video;
import com.yu.payment.service.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api/vi/video")
public class VideoAdminController {

    @Autowired
    private IVideoService videoService;

    /**
     * 根据id删除
     *
     * @param videoId
     * @return
     */
    @DeleteMapping("del_by_id")
    public Integer delById(@RequestParam(value = "video_id", required = true) int videoId) {
        return videoService.delById(videoId);
    }

    /**
     * 更新
     *
     * @param video
     * @return
     */
    @PutMapping("update")
    public Integer update(@RequestBody Video video) {
        return videoService.update(video);
    }

    /**
     * 插入
     *
     * @param video
     * @return
     */
    @PostMapping("save")
    public Integer save(@RequestBody Video video) {
        return videoService.save(video);
    }
}
