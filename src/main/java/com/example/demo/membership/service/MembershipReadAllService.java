package com.example.demo.membership.service;

import com.example.demo.membership.exception.DontHaveAnyMembershipException;
import com.example.demo.membership.model.Membership;
import com.example.demo.membership.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MembershipReadAllService {
    private final MembershipRepository membershipRepository;

    public List<Membership> getAllMembership(String userId) {
        List<Membership> memberships = membershipRepository.findByUserId(userId);
        checkIfIsEmpty(memberships);
        return memberships;
    }

    private void checkIfIsEmpty(List<Membership> memberships) {
        if (memberships.isEmpty() || memberships == null) {
            throw new DontHaveAnyMembershipException();
        }
    }
}
