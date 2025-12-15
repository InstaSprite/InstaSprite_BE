package org.olaz.instasprite_be.domain.search.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import lombok.Getter;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "searches")
public class Search {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "search_id")
	private Long id;

	@Column(name = "search_count")
	private Long count;

	@Column(insertable = false, updatable = false)
	private String dtype;

	protected Search() {
		this.count = 0L;
	}

	public void upCount() {
		this.count++;
	}

	@Transient
	public void setDtype() {
		this.dtype = getClass().getAnnotation(DiscriminatorValue.class).value();
	}

}
