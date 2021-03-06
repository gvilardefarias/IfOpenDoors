package br.edu.ifpb.ifopendoors.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity 
@Table(name="tb_alocacao")
public class Open {
	
	@Id
    @GeneratedValue
    @Column(name="id_alocacao")
	private int id;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Person person;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Room room;
	

	@Column(name="dt_abertura", insertable=true, nullable = false)
	private String time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String i) {
		this.time = i;
	}
}
