package com.beerkhaton.mealtrackerapi.schedular;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "scheduler", name = "enabled", matchIfMissing = true)
public class SchedulerConfig {

}
