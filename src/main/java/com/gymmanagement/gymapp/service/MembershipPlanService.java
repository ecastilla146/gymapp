package com.gymmanagement.gymapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymmanagement.gymapp.model.MembershipPlan;
import com.gymmanagement.gymapp.repository.MembershipPlanRepository;

@Service
public class MembershipPlanService {

    private final MembershipPlanRepository membershipPlanRepository;

    @Autowired
    public MembershipPlanService(MembershipPlanRepository membershipPlanRepository) {
        this.membershipPlanRepository = membershipPlanRepository;
    }

    public List<MembershipPlan> findAllPlans() {
        return membershipPlanRepository.findAll();
    }

    public List<MembershipPlan> findActivePlans() {
        return membershipPlanRepository.findByIsActiveTrue();
    }

    public Optional<MembershipPlan> findPlanById(Long id) {
        return membershipPlanRepository.findById(id);
    }

    public MembershipPlan savePlan(MembershipPlan plan) {
        return membershipPlanRepository.save(plan);
    }

    public void deletePlan(Long id) {
        membershipPlanRepository.deleteById(id);
    }
}
