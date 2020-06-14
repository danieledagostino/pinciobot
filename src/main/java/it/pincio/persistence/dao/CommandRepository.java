package it.pincio.persistence.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.pincio.persistence.bean.Command;
import it.pincio.persistence.bean.Faq;

@Repository
public interface CommandRepository extends JpaRepository<Command, Integer>
{
	List<Command> getAllActiveCommand();
	
}