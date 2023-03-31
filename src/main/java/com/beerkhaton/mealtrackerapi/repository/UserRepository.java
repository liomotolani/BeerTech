package com.beerkhaton.mealtrackerapi.repository;

import com.beerkhaton.mealtrackerapi.dto.enums.MealStatus;
import com.beerkhaton.mealtrackerapi.dto.enums.UserRole;
import com.beerkhaton.mealtrackerapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User,String> {

    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);

    Page<User> findByRole(UserRole role, Pageable pageable);

    List<User> findByMealStatus(MealStatus status);

    Page<User> findByRoleAndMealStatusAndCreatedDate(UserRole role, MealStatus status, Date date, Pageable pageable);
}
