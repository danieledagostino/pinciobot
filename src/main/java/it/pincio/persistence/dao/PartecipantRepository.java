package it.pincio.persistence.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.pincio.persistence.bean.Command;
import it.pincio.persistence.bean.Partecipant;
import it.pincio.persistence.bean.PartecipantId;

@Repository
public interface PartecipantRepository extends JpaRepository<Partecipant, PartecipantId>
{
	
}