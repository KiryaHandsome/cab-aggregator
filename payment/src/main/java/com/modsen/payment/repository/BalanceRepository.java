package com.modsen.payment.repository;

import com.modsen.payment.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BalanceRepository extends JpaRepository<Balance, Integer> {

    Optional<Balance> findByPassengerId(Integer passengerId);
}
