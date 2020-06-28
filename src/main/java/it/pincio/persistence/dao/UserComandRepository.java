package it.pincio.persistence.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import it.pincio.persistence.bean.UserCommand;

@Repository
public interface UserComandRepository extends JpaRepositoryImplementation<UserCommand, Integer>
{

}