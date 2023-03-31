package com.beerkhaton.mealtrackerapi.repository;

import com.beerkhaton.mealtrackerapi.dto.enums.MealStatus;
import com.beerkhaton.mealtrackerapi.dto.enums.UserRole;
import com.beerkhaton.mealtrackerapi.model.User;
import com.beerkhaton.mealtrackerapi.model.UserHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserHistoryRepository extends CrudRepository<UserHistory,String> {


    Page<UserHistory> findByCreatedDateBetween(Date from, Date to,Pageable pageable);
}
