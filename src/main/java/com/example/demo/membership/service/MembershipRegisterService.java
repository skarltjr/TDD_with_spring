package com.example.demo.membership.service;

import com.example.demo.membership.exception.AlreadyExistMembershipException;
import com.example.demo.membership.model.Membership;
import com.example.demo.membership.model.MembershipType;
import com.example.demo.membership.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MembershipRegisterService {
    private final MembershipRepository membershipRepository;

    public Membership register(String userId, MembershipType membershipType, Integer point) {
        checkIfAlreadyExist(userId, membershipType);
        Membership membership = createMembership(userId, membershipType, point);
        return membershipRepository.save(membership);
    }

    private Membership createMembership(String userId, MembershipType membershipType, Integer point) {
        return Membership.builder()
                .userId(userId)
                .membershipType(membershipType)
                .point(point)
                .build();
    }

    private void checkIfAlreadyExist(String userId, MembershipType membershipType) {
        if (membershipRepository.existsByUserIdAndMembershipType(userId, membershipType)) {
            throw new AlreadyExistMembershipException();
        }
    }
}
