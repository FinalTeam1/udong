package com.multi.udong.cs.service;

import com.multi.udong.common.model.dto.AttachmentDTO;
import com.multi.udong.common.model.dto.TypeDTO;
import com.multi.udong.cs.model.dao.CSMapper;
import com.multi.udong.cs.model.dto.CSAnswerDTO;
import com.multi.udong.cs.model.dto.CSQuestionDTO;
import com.multi.udong.member.model.dto.PageDTO;
import com.multi.udong.notification.model.dto.NotiSetCodeENUM;
import com.multi.udong.notification.service.NotiServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * The type Cs service.
 *
 * @author 김재식
 * @since 2024 -07-31
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CSServiceImpl implements CSService{

    private final CSMapper csMapper;
    private final NotiServiceImpl notiService;

    /**
     * Select que list.
     *
     * @param pageDTO the page dto
     * @return the list
     * @since 2024 -08-01
     */
    @Override
    public List<List<String>> selectQue(PageDTO pageDTO) {

        List<LinkedHashMap<String, Object>> map = csMapper.selectQue(pageDTO);

        List<List<String>> result = new ArrayList<>();
        for (LinkedHashMap<String, Object> row : map) {
            List<String> list = new ArrayList<>();
            for (Object value : row.values()) {
                list.add(value != null ? value.toString() : "");
            }
            result.add(list);
        }

        return result;
    }

    /**
     * Select all que list.
     *
     * @param pageDTO the page dto
     * @return the list
     * @since 2024 -08-02
     */
    @Override
    public List<List<String>> selectAllQue(PageDTO pageDTO) {
        List<LinkedHashMap<String, Object>> map = csMapper.selectAllQue(pageDTO);

        List<List<String>> result = new ArrayList<>();
        for (LinkedHashMap<String, Object> row : map) {
            List<String> list = new ArrayList<>();
            for (Object value : row.values()) {
                list.add(value != null ? value.toString() : "");
            }
            result.add(list);
        }

        return result;
    }

    /**
     * Insert que form.
     *
     * @param csQuestionDTO the cs question dto
     * @param attachments   the attachments
     * @since 2024 -08-01
     */
    @Override
    public void insertQueForm(CSQuestionDTO csQuestionDTO, List<AttachmentDTO> attachments) {
        csMapper.insertQueForm(csQuestionDTO);

        // useGeneratedKeys
        int csNo = csQuestionDTO.getCsNo();

        if (attachments != null && !attachments.isEmpty()) {
            for (AttachmentDTO attachment : attachments) {
                attachment.setTargetNo(csNo);
                attachment.setTypeCode("CS");
                csMapper.insertFile(attachment);
            }
        }
    }

    /**
     * Get all types list.
     *
     * @return list list
     * @since 2024 -08-01
     */
    @Override
    public List<TypeDTO> getAllTypes() {
        return csMapper.getAllTypes();
    }

    /**
     * Que detail cs question dto.
     *
     * @param csNo the cs no
     * @return the cs question dto
     * @since 2024 -08-01
     */
    @Override
    public Map<String, Object> getQueDetail(int csNo) {

        Map <String, Object> map = new HashMap<>();

        CSQuestionDTO csQuestionDTO = csMapper.getQueDetail(csNo);
        if (csQuestionDTO == null) {
            return null;
        } else {
            map.put("csQuestionDTO", csQuestionDTO);
        }

        List<AttachmentDTO> attachments = csMapper.getQueAttachment(csNo);
        map.put("attachments", attachments);

        List<CSAnswerDTO> answers = csMapper.getQueAnswer(csNo);
        map.put("answers", answers);

        return map;
    }

    /**
     * Get attachment attachment dto.
     *
     * @param fileNo the file no
     * @return the attachment dto
     * @since 2024 -08-01
     */
    @Override
    public AttachmentDTO getAttachment(int fileNo) {

        return csMapper.getAttachment(fileNo);
    }

    /**
     * Insert answer que string.
     *
     * @param csAnswerDTO the cs answer dto
     * @param authority   the authority
     * @return the string
     * @since 2024 -08-02
     */
    @Override
    public String insertAnswerQue(CSAnswerDTO csAnswerDTO, String authority) {

        csMapper.insertAnswerQue(csAnswerDTO);
        String createdAt = csMapper.getAnswerCreatedAt(csAnswerDTO);

        if (authority.equals("ROLE_ADMIN")) {
            csMapper.updateQueIsAnswered(csAnswerDTO.getCsNo());

            notiService.sendNoti(
                    NotiSetCodeENUM.CS_ANSWER, // 타입 코드
                    List.of(csMapper.getMemberNoByCsNo(csAnswerDTO.getCsNo())), // 알림 받는 사용자의 memberNo
                    csAnswerDTO.getCsNo(), // targetNo, 상세페이지에 필요한 No(있다면)
                    Map.of() // 추가 파라미터
            );
        }

        return createdAt;
    }

    /**
     * Delete que string.
     *
     * @param csNo      the cs no
     * @param authority
     * @return the string
     * @since 2024 -08-13
     */
    @Override
    public String deleteQue(int csNo, String authority) {

        String isAnswered = csMapper.isQueAnswered(csNo);

        if (authority.equals("ROLE_ADMIN")) {
            csMapper.deleteQue(csNo);
            return "notAnswered";
        }

        if (isAnswered.equals("Y")) {
            return "answered";
        }

        csMapper.deleteQue(csNo);
        return "notAnswered";
    }



}
