package com.example.demo.membership.service;

import com.example.demo.membership.exception.CannotFindWithMembershipException;
import com.example.demo.membership.exception.CannotRemoveOthersMembershipException;
import com.example.demo.membership.model.Membership;
import com.example.demo.membership.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MembershipRemoveService {
    private final MembershipRepository membershipRepository;

    public void deleteMembershipById(Long membershipId,String userId) {
        Membership membership = membershipRepository.findById(membershipId).orElseThrow(() -> new CannotFindWithMembershipException());
        checkIfIsMine(membership, userId);
        membershipRepository.delete(membership);
    }

    private void checkIfIsMine(Membership membership, String currentUserId) {
        if (!membership.getUserId().equals(currentUserId)) {
            throw new CannotRemoveOthersMembershipException();
        }
    }
}
