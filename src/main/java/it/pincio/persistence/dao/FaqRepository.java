package it.pincio.persistence.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.pincio.persistence.bean.Faq;

@Repository
public interface FaqRepository extends CrudRepository<Faq, Integer>
{
	List<Faq> searchReabilityAnswer(String text1, String text2, String text3, Float DB_REQ_SCORE, Float DB_REQ_HINT);
	
}