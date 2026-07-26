package com.cognizant.ems.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

// Supplies the value used for @CreatedBy / @LastModifiedBy. No authentication
// is wired up in this project, so a fixed "SYSTEM" user is returned -- swap
// this for a SecurityContextHolder-based lookup once Spring Security is added.
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("SYSTEM");
    }
}
