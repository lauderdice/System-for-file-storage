package cz.vmacura.ear.upload.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
public class Account extends AbstractEntity
{
	public static final int ROLE_DEFAULT = 0;
	public static final int ROLE_ADMIN = 1;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private int role = ROLE_DEFAULT;

	@JsonManagedReference
	@OneToMany
	private List<File> files = new ArrayList<>();

	@OneToMany
	private List<Payment> payments = new ArrayList<>();

	public String getUsername()
	{
		return this.username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public int getRole() {
		return role;
	}
	public void erasePassword() {
		this.password = "null";
	}
	public List<File> getFiles()
	{
		return this.files;
	}
}
