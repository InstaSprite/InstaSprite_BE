package org.olaz.instasprite_be.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration for JPA Auditing
 * Enables automatic population of @CreatedDate, @LastModifiedDate, etc.
 * 
 * Make sure your entities extend BaseEntity or use these annotations:
 * - @CreatedDate
 * - @LastModifiedDate
 * - @CreatedBy
 * - @LastModifiedBy
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}

