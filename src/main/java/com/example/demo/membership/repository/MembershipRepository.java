package com.example.demo.membership.repository;

import com.example.demo.membership.model.Membership;
import com.example.demo.membership.model.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MembershipRepository extends JpaRepository<Membership, Long> {
    boolean existsByUserIdAndMembershipType(String userId, MembershipType membershipType);

    List<Membership> findByUserId(String userId);

}
