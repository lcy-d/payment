package com.yu.payment.mapper;

import com.yu.payment.domain.Video;
import com.yu.payment.provider.VideoProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoMapper {

    //查找全部
    @Select("select * from video ")
    public List<Video> findAll();

    //根据 id查找
    @Select("select * from video where id =#{id}")
    public Video findById(Integer id);

    // 根据id删除
    @Delete("delete from video where id =#{id}")
    public Integer delById(Integer id);

    //更新
//    @Update("update video set title=#{title} where id = #{id}")
    @UpdateProvider(value = VideoProvider.class, method = "updateVideo")
    public Integer update(Video video);

    //插入
    @Insert("insert into video (title,  summary,  cover_img,  view_num,  price,  create_time,  online,  point)" +
            " values (#{title}, #{summary}, #{coverImg}, #{viewNum}, #{price}, #{create_time}, #{online}, #{point})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    public Integer save(Video video);


}
