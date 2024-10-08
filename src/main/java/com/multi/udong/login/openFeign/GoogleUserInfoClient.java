package com.multi.udong.login.openFeign;

import com.multi.udong.login.model.dto.GoogleUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "googleUserInfo", url = "https://www.googleapis.com")
public interface GoogleUserInfoClient {

    @GetMapping("/oauth2/v2/userinfo")
    GoogleUserInfo getUserInfo(@RequestHeader("Authorization") String bearerToken);
}

