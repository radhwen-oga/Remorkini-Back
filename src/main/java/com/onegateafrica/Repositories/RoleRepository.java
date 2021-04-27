package com.onegateafrica.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onegateafrica.Entities.ERole;
import com.onegateafrica.Entities.Role;
@Repository
public interface RoleRepository  extends JpaRepository<Role, Long> {
	Role findByName(ERole name);

}
