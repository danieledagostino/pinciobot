package it.pincio.persistence.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.pincio.persistence.bean.Faq;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Integer>
{
	
	@Query(nativeQuery = true, name = "Faq.searchReabilityAnswer")
	List<Faq> searchReabilityAnswer(@Param("question") String text1, @Param("DB_REQ_SCORE") BigDecimal DB_REQ_SCORE, @Param("DB_REQ_HINT") BigDecimal DB_REQ_HINT);
}