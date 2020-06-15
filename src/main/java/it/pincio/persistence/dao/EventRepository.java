package it.pincio.persistence.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.pincio.persistence.bean.Event;
import it.pincio.persistence.bean.Faq;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>
{
	
	List<Event> searchByCurrentDate(@Param("idUser") String idUser);
	
	List<Event> searchCurrentEvents();
}