package com.finki.explorandija.repository;

import com.finki.explorandija.model.UserDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDiscountRepository extends JpaRepository<UserDiscount, Long> {
    List<UserDiscount> findByUserId(Long userId);
    Optional<UserDiscount> findByUserIdAndDiscountId(Long userId, Long discountId);
    boolean existsByUserIdAndDiscountId(Long userId, Long discountId);
} 