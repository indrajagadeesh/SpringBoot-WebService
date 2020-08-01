package com.indrajagadeesh.webgradle.repository;

import com.indrajagadeesh.webgradle.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Curd repository interface for database transactions
 */
@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
