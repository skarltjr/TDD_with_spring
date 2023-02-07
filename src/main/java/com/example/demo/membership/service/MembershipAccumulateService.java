package com.example.demo.membership.service;

import com.example.demo.membership.exception.CannotFindWithMembershipException;
import com.example.demo.membership.model.Membership;
import com.example.demo.membership.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MembershipAccumulateService {
    private final MembershipRepository membershipRepository;
    private final AccumulateService accumulateService;

    public void accumulate(String userId, Long membershipId, int price) {
        Membership membership = membershipRepository.findByUserIdAndId(userId, membershipId).orElseThrow(() -> new CannotFindWithMembershipException());
        int pointToAdd = accumulateService.calculateAmount(price);
        membership.accumulate(pointToAdd);
    }

}
