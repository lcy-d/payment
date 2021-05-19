package com.yu.payment.mapper;

import com.yu.payment.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

//用户持久化
@Repository
public interface UserMapper {


    //根据openId 查找一个用户
    @Select("select * from user where openid = #{openId}")
    User findById(String openId);

    //添加一个用户
    @Insert("inser into user (openid, name, head_img, sex, city, create_time)" +
            "values (#{openId},#{name},#{headImg},#{sex},#{city},#{createTime},) ")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    User save(User user);
}
