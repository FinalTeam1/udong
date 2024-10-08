package com.multi.udong.sale.model.dao;

import com.multi.udong.common.model.dto.AttachmentDTO;
import com.multi.udong.sale.model.dto.SaleDTO;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Sale dao.
 *
 * @author 윤정해
 * @since 2024 -07-24
 */
@Repository //@Repository 데이터접근객체임을 나타냄
public class SaleDAO {
    private final SqlSession sqlSession;

    @Autowired
    public SaleDAO(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    /**
     * Insert sale int.
     *
     * @param sqlSession the sql session
     * @param saleDTO    the sale dto
     * @return the int
     * @throws Exception the exception
     * @since 2024 -07-24
     */
    public int insertSale(SqlSessionTemplate sqlSession, SaleDTO saleDTO) throws Exception{
        sqlSession.insert("SaleMapper.insertSale", saleDTO);
        return saleDTO.getSaleNo(); //새로운 땡처리 정보 데이터 추가
    }

    /**
     * Insert attachments int.
     *
     * @param sqlSession the sql session
     * @param imgList    the img list
     * @return the int
     * @since 2024 -07-24
     */
    public int insertAttachments(SqlSessionTemplate sqlSession, List<AttachmentDTO> imgList) {
        return sqlSession.insert("SaleMapper.insertAttachments", imgList); //여러 이미지 첨부파일 정보를 데이터에 추가
    }

    /**
     * Gets all sales.
     *
     * @param sqlSession the sql session
     * @return the all sales
     */
    public List<SaleDTO> getAllSalesWithAttachments(SqlSessionTemplate sqlSession) {
        return sqlSession.selectList("SaleMapper.getAllSalesWithAttachments");
    }

    public List<SaleDTO> search(SqlSessionTemplate sqlSession, String keyword) {
        return search(sqlSession, keyword, false);
    }
    public List<SaleDTO> getAllActiveWithAttachments(SqlSessionTemplate sqlSession) {
        return sqlSession.selectList("SaleMapper.getAllActiveWithAttachments");
    }

    public List<SaleDTO> search(SqlSessionTemplate sqlSession, String keyword, Boolean excludeExpired) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("excludeExpired", excludeExpired);
        return sqlSession.selectList("SaleMapper.searchSales", params);
    }
    public SaleDTO getSaleById(SqlSession sqlSession, int saleNo) {
        return sqlSession.selectOne("SaleMapper.getSaleById", saleNo);
    }


    public void incrementViews(int saleNo) {
        sqlSession.update("SaleMapper.incrementViews", saleNo);
    
    }
    public List<AttachmentDTO> getAttachmentsBySaleNo(SqlSessionTemplate sqlSession, int saleNo) {
        return sqlSession.selectList("SaleMapper.getAttachmentsBySaleNo", saleNo);
    }
    public void deleteSale(int saleNo) throws Exception {
        sqlSession.delete("SaleMapper.deleteSale", saleNo);
    }
    public void updateSaleStatus(int saleNo, String status) {
        Map<String, Object> params = new HashMap<>();
        params.put("saleNo", saleNo);
        params.put("status", status);
        sqlSession.update("SaleMapper.updateSaleStatus", params);
    }

    public SaleDTO findById(int saleNo) {
        return sqlSession.selectOne("SaleMapper.getSaleById", saleNo);
    }
}