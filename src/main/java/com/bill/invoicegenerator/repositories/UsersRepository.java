package com.bill.invoicegenerator.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bill.invoicegenerator.entity.UsersEntity;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {

	@Query(value = "SELECT UD.NAME,\r\n" + "	UD.AGE,\r\n" + "	UD.EMAIL_ID,\r\n" + "	UC.CITY_NAME,\r\n"
			+ "	UC.FAMOUS_DISH\r\n" + "FROM USER_DATA AS UD\r\n"
			+ "INNER JOIN USER_CITY AS UC ON UC.CITY_ID = UD.CITY_ID", nativeQuery = true)
	List<Object[]> getTableData(Integer id,Long idd);

	@Query(value = "SELECT UC.CITY_ID,\r\n" + "	UC.CITY_NAME\r\n" + "FROM USER_CITY AS UC", nativeQuery = true)
	List<Object[]> getCities();

}
