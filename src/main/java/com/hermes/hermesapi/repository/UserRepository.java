package com.hermes.hermesapi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hermes.hermesapi.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByEmail(String email);

 }
