package com.antonio.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name = "PHONE", uniqueConstraints = { @UniqueConstraint(columnNames = "PHONE_ID") })
public class Phone implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PHONE_ID", unique = true, nullable = false)
	private Long id;

	@Size(min=4, max=11)
	@Column(name = "CONTACTNUMBER", unique = true, nullable = false)
	private String contactnumber;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
	//@JoinColumn(name = "EMPLOYEE_ID", nullable=false)
	//@JsonBackReference
	
	@JoinTable(name="employee_phone", 
	           joinColumns=@JoinColumn(name="PHONE_ID"), 
	           inverseJoinColumns=@JoinColumn(name="EMPLOYEE_ID"),
	           uniqueConstraints = @UniqueConstraint(name = "uq_PhoneMasEmployee",
	           columnNames = {"PHONE_ID", "EMPLOYEE_ID"})
			   )  
	private List<Employee> employees = new ArrayList<Employee>();

	@Column(name = "PHONETYPE")
	private String phonetype;

	public Phone() {

	}

	public Phone(String contactnumber, String phonetype) {
		super();
		this.contactnumber = contactnumber;
		this.phonetype = phonetype;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContactnumber() {
		return contactnumber;
	}

	public void setContactnumber(String contactnumber) {
		this.contactnumber = contactnumber;
	}

	public String getPhonetype() {
		return phonetype;
	}

	public void setPhonetype(String phonetype) {
		this.phonetype = phonetype;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
	@Override
	public String toString() {
		return "Phone [id=" + id + ", contactnumber=" + contactnumber + ", employee= ..." + ", phonetype="
				+ phonetype + "]";
	}
	


}

