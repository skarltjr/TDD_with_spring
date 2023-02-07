package com.example.demo.membership.repository;

import com.example.demo.membership.model.Membership;
import com.example.demo.membership.model.MembershipType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class MembershipRepositoryTest {
    @Autowired
    private MembershipRepository membershipRepository;
    private final String userId = "userId";
    private final Integer point = 10000;
    private final Long membershipId = -1l;
    private final MembershipType membershipType = MembershipType.NAVER;

    // -------


    @Test
    void membershipRepoIsNotNull() {
        assertThat(membershipRepository).isNotNull();
    }

    /**
     * 멤버십 등록
     * */
    @Test
    @DisplayName("이미 갖고있는 타입 조회")
    void alreadyHaveMembership() {
        Membership membership = createTempMemberShip(null,userId,MembershipType.NAVER,point);
        membershipRepository.save(membership);

        boolean result = membershipRepository.existsByUserIdAndMembershipType(userId, MembershipType.NAVER);
        assertThat(result).isTrue();

    }

    @Test
    @DisplayName("멤버십 등록")
    public void createMembership() {
        Membership membership = createTempMemberShip(null,userId,MembershipType.NAVER,point);

        final Membership result = membershipRepository.save(membership);

        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(membership.getPoint()).isEqualTo(point);
    }


    /**
     * 멤버십 조회 all
     */
    @Test
    @DisplayName("해당 userId로 등록된 멤버십이 없는 경우")
    void membershipIsNotExistedWithUserid() {
        final List<Membership> result = membershipRepository.findByUserId(userId);

        assertThat(result).isEmpty();
        assertThat(result).hasSize(0);
    }

    @Test
    @DisplayName("해당 userId로 등록된 멤버십이 있는 경우 전체 조회")
    void findAllMembershipWithUserid() {
        membershipRepository.save(createTempMemberShip(null,userId, MembershipType.NAVER, point));
        membershipRepository.save(createTempMemberShip(null,userId, MembershipType.KAKAO, point));
        membershipRepository.save(createTempMemberShip(null,userId, MembershipType.LINE, point));

        final List<Membership> result = membershipRepository.findByUserId(userId);

        assertThat(result).hasSize(3);
        for (Membership membership : result) {
            assertThat(membership.getUserId()).isEqualTo(userId);
        }
    }

    /**
     * 멤버십 조회 단건
     */
    @Test
    @DisplayName("해당 membership id로 단건 조회")
    void findMembershipWithMembershipId() {
        Membership savedMembership = membershipRepository.save(createTempMemberShip(membershipId, userId, MembershipType.NAVER, point));

        Optional<Membership> membership = membershipRepository.findById(savedMembership.getId());

        assertThat(membership).isNotEmpty();
        assertThat(membership.get().getUserId()).isEqualTo(savedMembership.getUserId());
        assertThat(membership.get().getMembershipType()).isEqualTo(savedMembership.getMembershipType());
        assertThat(membership.get().getPoint()).isEqualTo(savedMembership.getPoint());
    }

    /**
     * 멤버십 삭제
     */
    @Test
    @DisplayName("멤버십 삭제")
    void deleteMembershipWithMembershipId() {
        Membership savedMembership = membershipRepository.save(createTempMemberShip(null, userId, membershipType, point));
        membershipRepository.deleteById(savedMembership.getId());

        assertThat(membershipRepository.findById(savedMembership.getId())).isEqualTo(Optional.empty());
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
