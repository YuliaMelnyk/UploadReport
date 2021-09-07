package com.example.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "issues")
@Data
@NoArgsConstructor
public class IssueReport {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	private String email;
	private String url;
	private String description;
	private Boolean markedAsPrivate;
	private Boolean updates;
	private Boolean done;
	private Date created;
	private Date updated;
}
