package com.beerkhaton.mealtrackerapi.schedular;

import com.beerkhaton.mealtrackerapi.dto.enums.MealStatus;
import com.beerkhaton.mealtrackerapi.model.User;
import com.beerkhaton.mealtrackerapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class Scheduler {

    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void changeEmployeeStatus(){
        List<User> fetchAllUser = userRepository.findByMealStatus(MealStatus.INACTIVE);
        List<User> newUser = new ArrayList<>();
        fetchAllUser.forEach(user -> {
            user.setMealStatus(MealStatus.ACTIVE);
            user.setLastLoginDate(new Date());
            newUser.add(user);
        });
        userRepository.saveAll(newUser);
    }
}
