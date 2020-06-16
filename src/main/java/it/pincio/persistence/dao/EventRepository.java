package it.pincio.persistence.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.pincio.persistence.bean.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>
{
	
	List<Event> searchByCurrentDate(@Param("idUser") String idUser);
	
	List<Event> searchCurrentEvents();
	
	List<Event> searchMyOldEvents(@Param("idUser") String idUser);
	
	List<Event> searchMyConfirmedEvents(@Param("idUser") String idUser);
	
	List<Event> searchMyUncompleteEvents(@Param("idUser") String idUser);
}