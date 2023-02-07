package com.example.demo.membership.service;

import com.example.demo.membership.exception.CannotFindWithMembershipException;
import com.example.demo.membership.exception.CannotRemoveOthersMembershipException;
import com.example.demo.membership.model.Membership;
import com.example.demo.membership.model.MembershipType;
import com.example.demo.membership.repository.MembershipRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MembershipRemoveServiceTest {

    private final Long membershipId = -1l;
    private final String userId = "userId";
    private final MembershipType membershipType = MembershipType.NAVER;
    private final Integer point = 10000;
    private MembershipRepository membershipRepository = Mockito.mock(MembershipRepository.class);
    private MembershipRemoveService service = new MembershipRemoveService(membershipRepository);
    @Test
    @DisplayName("존재하지 않는 멤버십은 삭제할 수 없다.")
    void failToRemoveNonExistMembership() {
        BDDMockito.given(membershipRepository.findById(membershipId)).willReturn(Optional.empty());

        Assertions.assertThrows(CannotFindWithMembershipException.class, () -> {
            service.deleteMembershipById(membershipId, userId);
        });
    }

    @Test
    @DisplayName("자신의 멤버십이 아니면 삭제할 수 없다.")
    void failToRemoveOthersMembership() {
        BDDMockito.given(membershipRepository.findById(membershipId))
                .willReturn(Optional.of(createTempMemberShip(membershipId, userId, membershipType, point)));

        String currentUserId = "kiseok";

        Assertions.assertThrows(CannotRemoveOthersMembershipException.class, () -> {
            service.deleteMembershipById(membershipId,currentUserId);
        });
    }

    @Test
    @DisplayName("멤버쉽 삭제")
    void removeMembership() {
        Membership membership = createTempMemberShip(membershipId, userId, membershipType, point);
        BDDMockito.given(membershipRepository.findById(membershipId))
                .willReturn(Optional.of(membership));

        service.deleteMembershipById(membershipId, userId);

        verify(membershipRepository, times(1)).delete(membership);
    }
    private Membership createTempMemberShip(Long membershipId, String userId, MembershipType membershipType, int point) {
        Membership membership = Membership.builder()
                .id(membershipId!=null ? membershipId:null)
                .userId(userId)
                .membershipType(membershipType)
                .point(point)
                .build();
        return membership;
    }

}
