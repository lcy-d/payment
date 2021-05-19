package com.yu.payment.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yu.payment.domain.Video;
import com.yu.payment.mapper.VideoMapper;
import com.yu.payment.service.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service(value = "videoService")
@Transactional
public class VideoServiceImpl implements IVideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public List<Video> findAll() {
        return videoMapper.findAll();
    }

    @Override
    public Video findById(Integer id) {
        return videoMapper.findById(id);
    }

    @Override
    public Integer delById(Integer id) {
        return videoMapper.delById(id);
    }

    @Override
    public Integer update(Video video) {
        return videoMapper.update(video);
    }

    @Override
    public Integer save(Video video) {
        return videoMapper.save(video);
    }

    @Override
    public List<Video> pageVideo(Integer page, Integer size) {
        Page<Video> objects = PageHelper.startPage(page, size);
        List<Video> all = videoMapper.findAll();
        return all;
    }
}
