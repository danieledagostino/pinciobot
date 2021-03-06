package it.pincio.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.pincio.persistence.bean.Command;

@Repository
public interface CommandRepository extends JpaRepository<Command, Integer>
{
	
}