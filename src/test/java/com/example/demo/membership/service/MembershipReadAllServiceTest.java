package com.example.demo.membership.service;

import com.example.demo.membership.exception.DontHaveAnyMembershipException;
import com.example.demo.membership.model.Membership;
import com.example.demo.membership.model.MembershipType;
import com.example.demo.membership.repository.MembershipRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class MembershipReadAllServiceTest {
    private final String userId = "userId";
    private final Integer point = 10000;
    private MembershipRepository membershipRepository = mock(MembershipRepository.class);
    private MembershipReadAllService service = new MembershipReadAllService(membershipRepository);

    @Test
    @DisplayName("해당 유저의 멤버십이 1개이상인 경우에만 조회 가능")
    void cannotReadAllMembershipIfZero() {
        BDDMockito.given(membershipRepository.findByUserId(userId)).willReturn(Collections.emptyList());

        Assertions.assertThrows(DontHaveAnyMembershipException.class, () -> {
            service.getAllMembership(userId);
        });
    }

    @Test
    @DisplayName("멤버십이 1개이상인 경우 조회")
    void readAllMemberships() {
        BDDMockito.given(membershipRepository.findByUserId(userId)).willReturn(memberships());

        final List<Membership> memberships = service.getAllMembership(userId);

        verify(membershipRepository, times(1)).findByUserId(userId);
    }

    private List<Membership> memberships() {
        return List.of(
                Membership.builder()
                        .id(-1L)
                        .userId(userId)
                        .membershipType(MembershipType.NAVER)
                        .point(point)
                        .build(),
                Membership.builder()
                        .id(-2L)
                        .userId(userId)
                        .membershipType(MembershipType.KAKAO)
                        .point(point)
                        .build(),
                Membership.builder()
                        .id(-3L)
                        .userId(userId)
                        .membershipType(MembershipType.LINE)
                        .point(point)
                        .build()

        );
    }


}
