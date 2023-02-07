package com.example.demo.membership.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MembershipType {
    NAVER("네이버"),
    KAKAO("카카오"),
    LINE("라인");

    private final String membershipName;
}
