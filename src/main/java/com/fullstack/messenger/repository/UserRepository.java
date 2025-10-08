package com.fullstack.messenger.repository;

import com.fullstack.messenger.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{
    public boolean existsByUserName(String userName);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isOnline = :isOnline WHERE u.username= :username")
    public void userOnlineStatus(@Param("username") String username, @Param("isOnline") boolean isOnline);

}
