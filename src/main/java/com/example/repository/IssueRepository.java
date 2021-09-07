package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.entity.IssueReport;

public interface IssueRepository extends CrudRepository<IssueReport, Long> {
	@Query(value = "SELECT i FROM IssueReport i WHERE markedAsPrivate = false")
	List<IssueReport> findAllButPrivate();

	List<IssueReport> findAllByEmail(String email);
}
