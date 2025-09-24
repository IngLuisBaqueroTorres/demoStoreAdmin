package com.ingeduardo.demostore.repository;

import com.ingeduardo.demostore.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, Long> {
}
