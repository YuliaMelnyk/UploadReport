package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.IssueReport;
import com.example.repository.IssueRepository;

@RestController
@RequestMapping("/api/issues")
public class IssueRestController {

	@Autowired
	public IssueRepository issueRepository;

	@GetMapping
	public List<IssueReport> getIssues() {
		return issueRepository.findAllButPrivate();
	}

	@GetMapping("/{id}")
	public ResponseEntity<IssueReport> getIssue(@PathVariable("id") Optional<IssueReport> issueReportOptional) {
		if (!issueReportOptional.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(issueReportOptional.get(), HttpStatus.OK);
	}
}
