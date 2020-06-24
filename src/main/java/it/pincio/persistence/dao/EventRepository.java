package it.pincio.persistence.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.pincio.persistence.bean.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>
{
	
	@Query("from Event where owner = :idUser and step > 0 and step < 3 and DATE(insertDate) = current_date()")
	List<Event> searchByCurrentDate(@Param("idUser") String idUser);
	
	@Query("from Event e where e.startDate > current_date() and e.step = 3 and e.cancelled = 'N'")
	List<Event> searchCurrentEvents();
	
	@Query("from Event where owner = :idUser and startDate < current_date() order by insertDate desc")
	List<Event> searchMyOldEvents(@Param("idUser") String idUser);
	
	@Query("from Event where owner = :idUser and startDate > current_date() and step = 3 order by insertDate desc")
	List<Event> searchMyConfirmedEvents(@Param("idUser") String idUser);
	
	@Query("from Event where owner = :idUser and startDate > current_date() and step < 3 and step > 1 and step < 4 order by insertDate desc")
	List<Event> searchMyUncompleteEvents(@Param("idUser") String idUser);
	
	//needs to specify alias with pagination
	@Query("from Event e where e.startDate > current_date() and e.step = 3 and e.cancelled = 'N'")
	Page<Event> searchNextEvent(Pageable pageable);
}