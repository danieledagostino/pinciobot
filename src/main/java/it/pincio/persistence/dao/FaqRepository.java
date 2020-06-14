package it.pincio.persistence.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.pincio.persistence.bean.Faq;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Integer>
{
	
	@Query(nativeQuery = true, name = "Faq.searchReabilityAnswer")
	List<Faq> searchReabilityAnswer(String text, Float DB_REQ_SCORE, Float DB_REQ_HINT);
	
}