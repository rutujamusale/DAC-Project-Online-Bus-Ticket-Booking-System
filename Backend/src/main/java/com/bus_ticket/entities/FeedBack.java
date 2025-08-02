package com.bus_ticket.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "feedback")
@Entity
public class FeedBack extends BaseEntity{
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private int rating;
	@Column(length = 1000) 
	private String reviewtext;
}
