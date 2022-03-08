package com.javaclimb.dao;

import com.javaclimb.domain.ListSong;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListSongMapper {




    int insertSelective(ListSong record);

    ListSong selectByPrimaryKey(Integer id);



    int updateByPrimaryKey(ListSong record);

    int updateListSongMsg(ListSong record);

    int deleteListSong(Integer songId);

    List<ListSong> allListSong();

    List<ListSong> listSongOfSongId(Integer songListId);
}
