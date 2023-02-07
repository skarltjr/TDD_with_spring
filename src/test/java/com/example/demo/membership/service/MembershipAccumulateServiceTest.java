package com.example.demo.membership.service;

import com.example.demo.membership.exception.CannotFindWithMembershipException;
import com.example.demo.membership.model.Membership;
import com.example.demo.membership.model.MembershipType;
import com.example.demo.membership.repository.MembershipRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class MembershipAccumulateServiceTest {
    private final String userId = "userId";
    private final Integer point = 10000;
    private final Long membershipId = -1l;
    private final MembershipType membershipType = MembershipType.NAVER;
    private final int price = 200;

    private MembershipRepository membershipRepository = mock(MembershipRepository.class);
    private AccumulateService accumulateService = mock(AccumulateService.class);
    private MembershipAccumulateService service = new MembershipAccumulateService(membershipRepository,accumulateService);

    @Test
    @DisplayName("멤버십이 없는 경우 적립할 수 없다.")
    void cannotAccumulateWithoutMembership() {
        BDDMockito.given(membershipRepository.findById(membershipId)).willReturn(Optional.empty());

        Assertions.assertThrows(CannotFindWithMembershipException.class, () -> {
            service.accumulate(userId,membershipId,price);
        });
    }

    @Test
    @DisplayName("멤버십 적립")
    void accumulate() {
        Membership originMembership = createTempMemberShip(membershipId, userId, membershipType, point);
        BDDMockito.given(membershipRepository.findByUserIdAndId(userId,membershipId)).willReturn(Optional.of(originMembership));

        int value = price * 1 / 100;
        BDDMockito.given(accumulateService.calculateAmount(price)).willReturn(value);

        service.accumulate(userId, membershipId, price);

        assertThat(originMembership.getPoint()).isEqualTo(point + value);

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