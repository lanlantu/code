package com.javaclimb.service;

import com.javaclimb.domain.RankList;

public interface RankListService {

    int rankOfSongListId(Long songListId);

    boolean addRank(RankList rankList);
}
