package it.pincio.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.pincio.persistence.bean.ChatUser;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, String>
{

}