package com.beerkhaton.mealtrackerapi.util;


import com.beerkhaton.mealtrackerapi.dto.enums.UserRole;
import com.beerkhaton.mealtrackerapi.model.User;
import com.beerkhaton.mealtrackerapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Component
public class DefaultInitializer implements ApplicationListener<ContextRefreshedEvent> {


    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder(12);


    @Value("${admin.user.email}")
    private String email;




    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event){

        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            return;
        }

        User user = User.builder()
                .email(email)
                .password(bcryptEncoder.encode("Admin@123"))
                .role(UserRole.ADMIN)
                .name("Admin")
                .createdDate(new Date())
                .department("HR")
                .build();
        userRepository.save(user);
        alreadySetup = true;

    }
}
