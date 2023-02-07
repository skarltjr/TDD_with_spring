package com.example.demo.membership.service;

import com.example.demo.membership.exception.AlreadyExistMembershipException;
import com.example.demo.membership.model.Membership;
import com.example.demo.membership.model.MembershipType;
import com.example.demo.membership.repository.MembershipRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MembershipRegisterServiceTest {
    private final String userId = "userId";
    private final Integer point = 10000;
    private final Long membershipId = -1l;
    private final MembershipType membershipType = MembershipType.NAVER;
    private MembershipRepository membershipRepository = mock(MembershipRepository.class);
    private MembershipRegisterService service = new MembershipRegisterService(membershipRepository);

    @Test
    @DisplayName("이미 존재하는 멤버십으론 멤버십 등록 불가")
    void failToCreateMembershipWithAlreadyExist() {
        BDDMockito.given(membershipRepository.existsByUserIdAndMembershipType(userId, membershipType)).willReturn(true);

        Assertions.assertThrows(AlreadyExistMembershipException.class, () -> {
            service.register(userId, membershipType, point);
        });
    }

    @Test
    @DisplayName("멤버십 등록")
    void createMembership() {
        BDDMockito.given(membershipRepository.existsByUserIdAndMembershipType(userId, membershipType)).willReturn(false);
        BDDMockito.given(membershipRepository.save(any(Membership.class))).willReturn(createTempMemberShip(membershipId,userId,membershipType,point));

        final Membership membership = service.register(userId, membershipType, point);

        assertThat(membership.getId()).isNotNull();
        assertThat(membership.getMembershipType()).isEqualTo(MembershipType.NAVER);

        verify(membershipRepository, times(1)).existsByUserIdAndMembershipType(userId, membershipType);
        verify(membershipRepository, times(1)).save(any(Membership.class));
    }

    private Membership createTempMemberShip(Long membershipId,String userId, MembershipType membershipType, int point) {
        Membership membership = Membership.builder()
                .id(membershipId!=null ? membershipId:null)
                .userId(userId)
                .membershipType(membershipType)
                .point(point)
                .build();
        return membership;
    }
}


