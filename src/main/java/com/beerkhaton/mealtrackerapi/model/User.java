package com.beerkhaton.mealtrackerapi.model;

import com.beerkhaton.mealtrackerapi.dto.enums.Gender;
import com.beerkhaton.mealtrackerapi.dto.enums.MealStatus;
import com.beerkhaton.mealtrackerapi.dto.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Document
public class User {

    @Id
    private String id;

    private String staffId;

    private String name;

    private String email;

    @JsonIgnore
    private String password;

    private Gender gender;

    private String department;

    private UserRole role;

    private MealStatus mealStatus;

    private Date createdDate;

    private Date lastLoginDate;

    private Date lastUpdated;



}
