package com.multi.udong.club.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 모임 멤버 DTO
 *
 * @author 강성현
 * @since 2024 -07-25
 */
@Data
public class ClubMemberDTO {

    private int clubNo;
    private int memberNo;
    private String nickname;
    private String profileSavedName;
    private LocalDateTime appliedAt;
    private LocalDateTime approvedAt;

}
