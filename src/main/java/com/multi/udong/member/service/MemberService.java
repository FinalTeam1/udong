package com.multi.udong.member.service;

import com.multi.udong.common.model.dto.AttachmentDTO;
import com.multi.udong.member.model.dto.MemAddressDTO;
import com.multi.udong.member.model.dto.MemBusDTO;
import com.multi.udong.member.model.dto.PageDTO;
import com.multi.udong.member.model.dto.MemberDTO;

import java.util.List;
import java.util.Map;

/**
 * The interface Member service.
 *
 * @author 김재식
 * @since 2024 -08-02
 */
public interface MemberService {

    void signup(MemberDTO memberDTO) throws Exception;

    boolean isIdDuplicate(String memberId);

    void signupSeller(MemberDTO memberDTO, MemBusDTO memBusDTO, AttachmentDTO attachmentDTO) throws Exception;

    void insertAddress(MemAddressDTO memAddressDTO) throws Exception;

    void updateMemberSession();

    void updateAddress(MemAddressDTO memAddressDTO) throws Exception;

    void updateProfile(MemberDTO memberDTO, AttachmentDTO attachmentDTO);

    boolean isNicknameDuplicate(String nickname);

    List<List<String>> selectAllAct(String table, PageDTO pageDTO);

    Map<String, Object> selectAllDashBoard(int memberNo);

    String deleteMember(int memberNo);

    Map<String, Object> getMemberInfo(int memberNo);
}
