package com.yu.payment.service;

import com.yu.payment.domain.Video;

import java.util.List;

/**
 * 视频业务接口类
 */
public interface IVideoService {

    //查找全部
    public List<Video> findAll();
    //根据 id查找
    public Video findById(Integer id);
    // 根据id删除
    public Integer delById(Integer id);
    //更新
    public Integer update(Video video);
    //插入
    public Integer save(Video video);

    //分页查询
    List<Video> pageVideo(Integer page, Integer size);
}
