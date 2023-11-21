package com.modsen.ride.service.impl;

import com.modsen.ride.service.CostCalculator;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;

@Service
public class FakeCostCalculator implements CostCalculator {

    private final Random random = new Random();
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Override
    public Float calculate(String from, String to) {
        return roundTo2Digits(random.nextFloat() * 200.f + 3.f);
    }

    private Float roundTo2Digits(Float value) {
        String formattedValue = decimalFormat.format(value);
        return Float.parseFloat(formattedValue);
    }
}
