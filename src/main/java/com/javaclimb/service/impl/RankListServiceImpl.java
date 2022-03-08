package com.javaclimb.service.impl;

import com.javaclimb.dao.RankListMapper;
import com.javaclimb.domain.RankList;
import com.javaclimb.service.RankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Service
public class RankListServiceImpl implements RankListService {



    @Autowired
    private RankListMapper rankMapper;

    @Override
    public int rankOfSongListId(Long songListId) {
        // 评分总人数如果为 0，则返回0；否则返回计算出的结果
        int rankNum = rankMapper.selectRankNum(songListId);
        return (rankNum <= 0) ? 0 : rankMapper.selectScoreSum(songListId) / rankNum;
    }

    @Override
    public boolean addRank(RankList rankList) {

        return rankMapper.insertSelective(rankList) > 0;
    }
}
