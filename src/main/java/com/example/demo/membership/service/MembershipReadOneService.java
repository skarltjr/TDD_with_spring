package com.example.demo.membership.service;

import com.example.demo.membership.exception.AuthFailWithReadMembershipException;
import com.example.demo.membership.exception.CannotFindWithMembershipIdException;
import com.example.demo.membership.model.Membership;
import com.example.demo.membership.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MembershipReadOneService {
    private final MembershipRepository membershipRepository;


    public Membership getMembership(String userId, Long membershipId) {
        Membership membership = membershipRepository.findById(membershipId).orElseThrow(() -> new CannotFindWithMembershipIdException());
        checkIfOwner(membership, userId);
        return membership;
    }

    private void checkIfOwner(Membership membership, String currentId) {
        if (!membership.getUserId().equals(currentId)) {
            throw new AuthFailWithReadMembershipException();
        }
    }
}
