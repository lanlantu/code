package com.javaclimb.dao;

import com.javaclimb.domain.Singer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SingerMapper {

    int insert(Singer record);

    int insertSelective(Singer record);

    Singer selectByPrimaryKey(Integer id);



    int updateByPrimaryKey(Singer record);

    int updateSingerMsg(Singer record);

    int updateSingerPic(Singer record);

    int deleteSinger(Integer id);

    List<Singer> allSinger();

    List<Singer> singerOfName(String name);

    List<Singer> singerOfSex(Integer sex);
}
