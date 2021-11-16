package com.spot.on.transactions;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Animal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// TODO explain
	private Long id;

	private String name;
	private int age;

}
