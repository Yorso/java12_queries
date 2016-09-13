package com.jorge.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

// ONE SIDE entity => INVERSE END

@Entity
@Table(name="guide")
public class Guide {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="staff_id", nullable=false)
	private String staffId;
	
	@Column // Hibernate will create this column with the name of this attribute => "name" (CASE SENSITIVE!!!)
	private String name;
	
	@Column(name="salary")
	private Integer salary;
	
	// By default, @OneToMany and @ManyToMany associations are lazy
	@OneToMany(mappedBy="guide", 
			   cascade={CascadeType.PERSIST}) // 'guide' is the name of the private attribute in Student.java class => private Guide guide;
			 							  	  // It tells Hibernate to get the set of students that are using the foreign key => guide_id in DB => private Guide guide;
											  // CascadeType.PERSIST: Everything you change in guide row is save in its linked student rows automatically
											  // 'mappedBy' attribute declares this class as not responsible for the relationship => inversed end
	private Set<Student> students = new HashSet<Student>();
	
	public Guide() {}
	
	public Guide(String staffId, String name, Integer salary) {
		this.staffId = staffId;
		this.name = name;
		this.salary = salary;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

	// This makes Guide responsible about the relationship to update data in both entities
	// We can update data from One side (not owner)
	public void addStudent(Student student){
		students.add(student);
		student.setGuide(this);;
	}

	@Override
	public String toString() {
		return "Guide [id=" + id + ", staffId=" + staffId + ", name=" + name + ", salary=" + salary +  "]";
	}
	
}
