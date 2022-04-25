package com.sachini.booking.repository;

import com.sachini.booking.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
	@Query(value = "SELECT u FROM Users u  WHERE u.username = ?1")
	public Users findByUsername(String username);

	@Query(value = "SELECT * FROM User u WHERE u.user_status =3", nativeQuery = true)
	List<Users> getPendingUsers();
	
	 @Query(value = "SELECT * FROM User WHERE user_id=? && status=?", nativeQuery = true)
	    public List<Users> findByUserId(Integer userId, String status);

	@Query(value = "SELECT * FROM User WHERE user_id=?", nativeQuery = true)
	public List<Users> profileByUserId(Integer userId);
}
