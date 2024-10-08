package com.multi.udong.share.model.dao;

import com.multi.udong.common.model.dto.AttachmentDTO;
import com.multi.udong.share.model.dto.*;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Repository
public class ShareDAO {

    public List<ShaCatDTO> getShaCat(SqlSessionTemplate sqlSession) {

        return sqlSession.selectList("ShareMapper.getShaCat");
    }

    public int insertItem(SqlSessionTemplate sqlSession, ShaItemDTO itemDTO) {
        sqlSession.insert("ShareMapper.insertItem", itemDTO);
        return itemDTO.getItemNo();
    }

    public int insertImg(SqlSessionTemplate sqlSession, List<AttachmentDTO> imgList) {
        return sqlSession.insert("ShareMapper.insertImg", imgList);
    }

    public ShaItemDTO getItemDetail(SqlSessionTemplate sqlSession, ShaItemDTO itemDTO) {

        return sqlSession.selectOne("ShareMapper.getItemDetail", itemDTO);

    }

    public List<AttachmentDTO> getItemImgs(SqlSessionTemplate sqlSession, ShaItemDTO itemDTO) {

        return sqlSession.selectList("ShareMapper.getItemImgs", itemDTO);
    }


    public List<ShaItemDTO> searchItems(SqlSessionTemplate sqlSession, ShaCriteriaDTO criteriaDTO) {
        return sqlSession.selectList("ShareMapper.searchItems", criteriaDTO);
    }


    public int getItemCounts(SqlSessionTemplate sqlSession, ShaCriteriaDTO criteriaDTO) {
        return sqlSession.selectOne("ShareMapper.getItemCounts", criteriaDTO);

    }

    public int insertRequest(SqlSessionTemplate sqlSession, ShaReqDTO reqDTO) {

        return sqlSession.insert("ShareMapper.insertRequest", reqDTO);
    }

    public ShaReqDTO findRequest(SqlSessionTemplate sqlSession, ShaReqDTO reqDTO) {

        return sqlSession.selectOne("ShareMapper.findRequest", reqDTO);
    }

    public int updateItem(SqlSessionTemplate sqlSession, ShaItemDTO itemDTO) {

        return sqlSession.update("ShareMapper.updateItem", itemDTO);
    }

    public int deleteImgList(SqlSessionTemplate sqlSession, List<AttachmentDTO> delImgList) {

        return sqlSession.delete("ShareMapper.deleteImgList", delImgList);
    }

    public int deleteItem(SqlSessionTemplate sqlSession, ShaItemDTO target) {

        return sqlSession.update("ShareMapper.deleteItem", target);
    }

    public int deleteImgByTarget(SqlSessionTemplate sqlSession, AttachmentDTO target) {

        return sqlSession.delete("ShareMapper.deleteImgByTarget", target);
    }

    public int updateItStat(SqlSessionTemplate sqlSession, ShaItemDTO itemDTO) {
        return sqlSession.update("ShareMapper.updateItStat", itemDTO);

    }

    public int plusViewCnt(SqlSessionTemplate sqlSession, int itemNo) {
        return sqlSession.update("ShareMapper.plusViewCnt", itemNo);
    }

    public ShaLikeDTO getShaLike(SqlSessionTemplate sqlSession, ShaLikeDTO likeDTO) {
        return sqlSession.selectOne("ShareMapper.getShaLike", likeDTO);
    }

    public int insertShaLike(SqlSessionTemplate sqlSession, ShaLikeDTO likeDTO) {
        return sqlSession.insert("ShareMapper.insertShaLike", likeDTO);
    }

    public int deleteShaLike(SqlSessionTemplate sqlSession, ShaLikeDTO likeDTO) {
        return sqlSession.delete("ShareMapper.deleteShaLike", likeDTO) ;
    }

    public int plusLikeCnt(SqlSessionTemplate sqlSession, int itemNo) {
        return sqlSession.update("ShareMapper.plusLikeCnt", itemNo);
    }

    public int minusLikeCnt(SqlSessionTemplate sqlSession, int itemNo) {
        return sqlSession.update("ShareMapper.minusLikeCnt", itemNo);
    }

    public int plusReqCnt(SqlSessionTemplate sqlSession, int itemNo) {
        return sqlSession.update("ShareMapper.plusReqCnt", itemNo);
    }

    public List<ShaItemDTO> getLendList(SqlSessionTemplate sqlSession, ShaDreamCriteriaDTO criteriaDTO) {
        return sqlSession.selectList("ShareMapper.getLendList", criteriaDTO);
    }

    public int getLendCounts(SqlSessionTemplate sqlSession, ShaDreamCriteriaDTO criteriaDTO) {
        return sqlSession.selectOne("ShareMapper.getLendCounts", criteriaDTO);
    }

    public List<ShaReqDTO> getReqList(SqlSessionTemplate sqlSession, ShaDreamCriteriaDTO criteriaDTO) {
        return sqlSession.selectList("ShareMapper.getReqList", criteriaDTO);
    }

    public List<ShaRqstDTO> getRequesters(SqlSessionTemplate sqlSession, int reqItem) {
        return sqlSession.selectList("ShareMapper.getRequesters", reqItem);
    }

    public int getReqCounts(SqlSessionTemplate sqlSession, ShaDreamCriteriaDTO criteriaDTO) {
        return sqlSession.selectOne("ShareMapper.getReqCounts", criteriaDTO);
    }

    public int updateReqStat(SqlSessionTemplate sqlSession, ShaReqDTO reqDTO) {
        return sqlSession.update("ShareMapper.updateReqStat", reqDTO);
    }

    public int minusReqCnt(SqlSessionTemplate sqlSession, int itemNo) {
        return sqlSession.update("ShareMapper.minusReqCnt", itemNo);
    }

    public int insertEval(SqlSessionTemplate sqlSession, ShaEvalDTO evalDTO) {
        return sqlSession.insert("ShareMapper.insertEval", evalDTO);
    }

    public int deleteReq(SqlSessionTemplate sqlSession, ShaReqDTO shaReqDTO) {
        return sqlSession.delete("ShareMapper.deleteReq", shaReqDTO);
    }

    public ShaReqDTO getReqByReqNo(SqlSessionTemplate sqlSession, int reqNo) {
        return sqlSession.selectOne("ShareMapper.getReqByReqNo", reqNo);
    }

    public int plusDealCnt(SqlSessionTemplate sqlSession, int itemNo) {
        return sqlSession.update("ShareMapper.plusDealCnt", itemNo);
    }

    public int insertReport(SqlSessionTemplate sqlSession, ShaReportDTO reportDTO) {
        return sqlSession.insert("ShareMapper.insertReport", reportDTO);
    }

    public List<ShaItemDTO> getRaffleItem(SqlSessionTemplate sqlSession) {
        return sqlSession.selectList("ShareMapper.getRaffleItem");
    }

    public int updateReqAfterRaffle(SqlSessionTemplate sqlSession, ShaRqstDTO winner) {
        return sqlSession.update("ShareMapper.updateReqAfterRaffle", winner);
    }

    public int postponeExpiry(SqlSessionTemplate sqlSession, ShaItemDTO itemDTO) {
        return sqlSession.update("ShareMapper.postponeExpiry", itemDTO);
    }

    public List<ShaCatDTO> getCatsInMemReq(SqlSessionTemplate sqlSession, Integer memberNo) {
        return sqlSession.selectList("ShareMapper.getCatsInMemReq", memberNo);
    }

    public List<ShaItemDTO> getHotItems(SqlSessionTemplate sqlSession, ShaCriteriaDTO criteriaDTO) {
        return sqlSession.selectList("ShareMapper.getHotItems", criteriaDTO);
    }

    public int updateScore(SqlSessionTemplate sqlSession, ShaEvalDTO evalDTO) {
        return sqlSession.update("ShareMapper.updateScore", evalDTO);
    }

    public int updateLevel(SqlSessionTemplate sqlSession, ShaEvalDTO evalDTO) {
        return sqlSession.update("ShareMapper.updateLevel", evalDTO);
    }

    public List<MemberItemPreferenceDTO> getMemberItemPreferences(SqlSessionTemplate sqlSession, Long locationCode){
        return sqlSession.selectList("ShareMapper.getMemberItemPreferences", locationCode);
    }

    public List<Integer> getAddRecomItems(SqlSessionTemplate sqlSession, List<Integer> recomItemNums, int i, Long locationCode) {
        return sqlSession.selectList("ShareMapper.getAddRecomItems", Map.of("excludedItems", recomItemNums, "limit", i, "locationCode", locationCode));
    }

    public List<ShaItemDTO> getItemListByItemNums(SqlSessionTemplate sqlSession, List<Integer> recomItemNums) {
        return sqlSession.selectList("ShareMapper.getItemListByItemNums", recomItemNums);
    }

    public ShaItemDTO getItemDetailForCheck(SqlSessionTemplate sqlSession, int itemNo) {
        return sqlSession.selectOne("ShareMapper.getItemDetailForCheck", itemNo);
    }

    public int updateReqReturnDate(SqlSessionTemplate sqlSession, ShaReqDTO reqDTO) {
        return sqlSession.update("ShareMapper.updateReqReturnDate", reqDTO);
    }

    public int hideReqFromDream(SqlSessionTemplate sqlSession, ShaReqDTO reqDTO) {
        return sqlSession.update("ShareMapper.hideReqFromDream", reqDTO);
    }

    public List<Integer> getLikedMembersByItemNo(SqlSessionTemplate sqlSession, int itemNo) {
        return sqlSession.selectList("ShareMapper.getLikedMembersByItemNo", itemNo);
    }

    public List<ShaReqDTO> getUpcomingReturns(SqlSession sqlSession, LocalDateTime returnDate) {
        return sqlSession.selectList("ShareMapper.getUpcomingReturns", returnDate);
    }

    public List<ShaItemDTO> getUpcomingDraws(SqlSession sqlSession, LocalDateTime drawTime) {
        return sqlSession.selectList("ShareMapper.getUpcomingDraws", drawTime);
    }


    public int getMemberLevel(SqlSessionTemplate sqlSession, int eveNo) {
        return sqlSession.selectOne("ShareMapper.getMemberLevel", eveNo);
    }
}
