package com.epam.jmp.rest.repository;

import com.epam.jmp.rest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
