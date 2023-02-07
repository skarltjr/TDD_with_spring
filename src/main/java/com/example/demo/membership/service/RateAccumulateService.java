package com.example.demo.membership.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RateAccumulateService implements AccumulateService {
    private static final int POINT_RATE = 1;
    @Override
    public int calculateAmount(int price) {
        return price * POINT_RATE / 100;
    }
}
