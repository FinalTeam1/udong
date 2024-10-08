package com.multi.udong.news.service;

import com.multi.udong.club.model.dto.ReportDTO;
import com.multi.udong.common.model.dto.AttachmentDTO;
import com.multi.udong.common.model.dto.LocationDTO;
import com.multi.udong.news.model.dao.NewsDAO;
import com.multi.udong.news.model.dto.*;
import com.multi.udong.notification.model.dto.NotiSetCodeENUM;
import com.multi.udong.notification.service.NotiServiceImpl;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = {Exception.class})
public class NewsServiceImpl implements NewsService {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private final NewsDAO newsDAO;
    private final NotiServiceImpl notiService;

    public NewsServiceImpl(NewsDAO newsDAO, NotiServiceImpl notiService) {
        this.newsDAO = newsDAO;
        this.notiService = notiService;
    }



    @Override
    public String checkIsNewsDeleted(int newsNo) throws Exception {

        return newsDAO.checkIsNewsDeleted(sqlSession, newsNo);

    }

    @Override
    public String checkIsAdDeleted(int adNo) throws Exception {

        return newsDAO.checkIsAdDeleted(sqlSession, adNo);

    }

    @Override
    public int checkNewsWriter(int newsNo) throws Exception {

        return newsDAO.checkNewsWriter(sqlSession, newsNo);

    }

    @Override
    public int checkReplyWriter(int replyNo) throws Exception {

        return newsDAO.checkReplyWriter(sqlSession, replyNo);

    }

    @Override
    public List<NewsDTO> selectNewsList(FilterDTO filterDTO) throws Exception {

        return newsDAO.selectNewsList(sqlSession, filterDTO);

    }

    @Override
    public int selectNewsCount(FilterDTO filterDTO) throws Exception {

        return newsDAO.selectNewsCount(sqlSession, filterDTO);

    }

    @Override
    public List<NewsDTO> selectAdList(FilterDTO filterDTO) throws Exception {

        return newsDAO.selectAdList(sqlSession, filterDTO);

    }

    @Override
    public List<CategoryDTO> selectCategoryList() throws Exception {

        return newsDAO.selectCategoryList(sqlSession);

    }

    @Override
    public List<LocationDTO> selectLocationList() throws Exception {
        return newsDAO.selectLocationList(sqlSession);
    }

    @Override
    public int insertNews(NewsDTO newsDTO) throws Exception {

        int result = 0;

        int newsResult = newsDAO.insertNews(sqlSession, newsDTO);

        if(newsResult == 1) {

            result = 1;

            int attachmentResult = 0;

            List<AttachmentDTO> attachmentList = newsDTO.getAttachments();

            if(attachmentList != null) {

                int newsNo = newsDTO.getNewsNo();

                for(AttachmentDTO attachment : attachmentList) {

                    attachment.setTargetNo(newsNo);
                    attachmentResult = newsDAO.insertNewsImg(sqlSession, attachment);

                }

                if(attachmentResult == attachmentList.size()) {

                    result = 2;

                }
                else {

                    throw new Exception("소식 이미지 업로드에 실패했습니다. 트랜잭션이 롤백을 실행합니다.");

                }

            }

        }
        else {

            throw new Exception("소식 작성에 실패했습니다. 트랜잭션이 롤백을 실행합니다.");

        }

        return result;

    }

    @Override
    public int addNewsViews(int newsNo) throws Exception {

        return newsDAO.addNewsViews(sqlSession, newsNo);

    }

    @Override
    public NewsDTO selectNewsDetail(RequestDTO requestDTO) throws Exception {

        return newsDAO.selectNewsDetail(sqlSession, requestDTO);

    }

    @Override
    public int insertNewsLike(LikeDTO likeDTO) throws Exception {

        return newsDAO.insertNewsLike(sqlSession, likeDTO);

    }

    @Override
    public int deleteNewsLike(LikeDTO likeDTO) throws Exception {

        return newsDAO.deleteNewsLike(sqlSession, likeDTO);

    }

    @Override
    public int insertReply(ReplyDTO replyDTO) throws Exception {

        int result = newsDAO.insertReply(sqlSession, replyDTO);

        if (result > 0) {

            // 알림 전송
            notiService.sendNoti(
                    NotiSetCodeENUM.NEWS_COMMENT,
                    List.of(newsDAO.selectNewsWriterNo(sqlSession, replyDTO.getNewsNo())),
                    replyDTO.getNewsNo(),
                    Map.of()
            );
        }

        return result;

    }

    @Override
    public int updateReply(ReplyDTO replyDTO) throws Exception {

        return newsDAO.updateReply(sqlSession, replyDTO);

    }

    @Override
    public int deleteReply(ReplyDTO replyDTO) throws Exception {

        return newsDAO.deleteReply(sqlSession, replyDTO);

    }

    @Override
    public int insertReplyLike(LikeDTO likeDTO) throws Exception {

        return newsDAO.insertReplyLike(sqlSession, likeDTO);

    }

    @Override
    public int deleteReplyLike(LikeDTO likeDTO) throws Exception {

        return newsDAO.deleteReplyLike(sqlSession, likeDTO);

    }

    @Override
    public int reportNews(ReportDTO reportDTO) throws Exception {

        return newsDAO.reportNews(sqlSession, reportDTO);

    }

    @Override
    public int updateNews(NewsDTO newsDTO) throws Exception {

        return newsDAO.updateNews(sqlSession, newsDTO);

    }

    @Override
    public AttachmentDTO selectAttachment(int fileNo) throws Exception {

        return newsDAO.selectAttachment(sqlSession, fileNo);

    }

    @Override
    public int updateAttachment(AttachmentDTO newImg) throws Exception {

        return newsDAO.updateAttachment(sqlSession, newImg);

    }

    @Override
    public int deleteAttachment(AttachmentDTO deletedImg) throws Exception {

        return newsDAO.deleteAttachment(sqlSession, deletedImg);

    }

    @Override
    public int insertAttachment(AttachmentDTO newImg) throws Exception {

        return newsDAO.insertAttachment(sqlSession, newImg);

    }

    @Override
    public int deleteNews(int newsNo) throws Exception {

        return newsDAO.deleteNews(sqlSession, newsNo);

    }

    @Override
    public List<NewsDTO> selectHotNewsList(FilterDTO filterDTO) throws Exception {

        return newsDAO.selectHotNewsList(sqlSession, filterDTO);

    }

    @Override
    public int insertAd(NewsDTO newsDTO) throws Exception {

        int result = 0;

        int adResult = newsDAO.insertAd(sqlSession, newsDTO);

        if(adResult == 1) {

            int attachmentResult = 0;

            List<AttachmentDTO> attachmentList = newsDTO.getAttachments();

            if(attachmentList != null) {

                AttachmentDTO attachmentDTO = newsDTO.getAttachments().get(0);

                int adNo = newsDTO.getNewsNo();
                attachmentDTO.setTargetNo(adNo);

                attachmentResult = newsDAO.insertAttachment(sqlSession, attachmentDTO);

                if(attachmentResult == 1) {

                    result = 2;

                }
                else {

                    throw new Exception("광고 이미지 업로드에 실패했습니다. 트랜잭션이 롤백을 실행합니다.");

                }


            }

            result = 1;

        }
        else {

            throw new Exception("광고 작성에 실패했습니다. 트랜잭션이 롤백을 실행합니다.");

        }

        return result;

    }

    @Override
    public int addAdViews(int adNo) throws Exception {

        return newsDAO.addAdViews(sqlSession, adNo);

    }

    @Override
    public NewsDTO selectAdDetail(int adNo) throws Exception {

        return newsDAO.selectAdDetail(sqlSession, adNo);

    }

    @Override
    public int deleteAd(int adNo) throws Exception {

        return newsDAO.deleteAd(sqlSession, adNo);

    }


}
