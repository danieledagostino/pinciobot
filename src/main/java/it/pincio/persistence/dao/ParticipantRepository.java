package it.pincio.persistence.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.pincio.persistence.bean.Command;
import it.pincio.persistence.bean.Participant;
import it.pincio.persistence.bean.ParticipantId;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, ParticipantId>
{

}