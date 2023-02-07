package com.example.demo.membership.service;

import com.example.demo.membership.exception.AuthFailWithReadMembershipException;
import com.example.demo.membership.exception.CannotFindWithMembershipException;
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

public class MembershipReadOneServiceTest {

    private final Long membershipId = -1l;
    private final String userId = "userId";
    private final MembershipType membershipType = MembershipType.NAVER;
    private final Integer point = 10000;
    private MembershipRepository membershipRepository = Mockito.mock(MembershipRepository.class);
    private MembershipReadOneService service = new MembershipReadOneService(membershipRepository);

    @Test
    @DisplayName("자신의 멤버십이 아닌 멤버십은 조회할 수 없다.")
    void cannotReadOthersMembership() {
        BDDMockito.given(membershipRepository.findById(membershipId)).willReturn(Optional.of(createTempMemberShip(membershipId, userId, membershipType, point)));

        final String currentUserId = "kiseok";

        Assertions.assertThrows(AuthFailWithReadMembershipException.class, () -> {
            service.getMembership(currentUserId,membershipId);
        });
    }

    @Test
    @DisplayName("존재하지 않는 id의 멤버십은 조회할 수 없다.")
    void cannotFindWithMembershipId() {
        BDDMockito.given(membershipRepository.findById(membershipId)).willReturn(Optional.empty());


        Assertions.assertThrows(CannotFindWithMembershipException.class, () -> {
            service.getMembership(userId,membershipId);
        });
    }

    @Test
    @DisplayName("멤버십 id로 자신의 멤버십 단건 조회")
    void getOneMyMembership() {
        BDDMockito.given(membershipRepository.findById(membershipId)).willReturn(Optional.of(createTempMemberShip(membershipId, userId, membershipType, point)));

        Membership membership = service.getMembership(userId, membershipId);
        assertThat(membership.getUserId()).isEqualTo(userId);
        assertThat(membership.getId()).isEqualTo(membershipId);

        verify(membershipRepository, times(1)).findById(Mockito.anyLong());
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
