package com.umeal.api.restaurant.repository;

import com.umeal.api.restaurant.model.Restaurant;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByOwnerEmail(String email);
}