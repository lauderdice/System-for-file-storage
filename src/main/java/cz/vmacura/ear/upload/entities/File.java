package cz.vmacura.ear.upload.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "file")
public class File extends AbstractEntity
{
	@JsonBackReference
	@ManyToOne(targetEntity = Account.class)
	private Account account;

	@Column
	private String name;

	@Column
	private String location;

	@Column
	private Date expiration;

	@JsonManagedReference
	@OneToMany(targetEntity = Url.class, cascade = {CascadeType.ALL})
	private List<Url> urls = new ArrayList<>();

	@ManyToMany(targetEntity = Batch.class)
	private List<Batch> batches = new ArrayList<>();

	public Account getAccount()
	{
		return account;
	}

	public String getName()
	{
		return name;
	}

	public String getLocation()
	{
		return location;
	}

	public List<Batch> getBatches() {
		return batches;
	}

	public Date getExpiration()
	{
		return expiration;
	}

    public void setAccount(Account account) {
        this.account = account;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public void addUrl(Url url) {
		if (!urls.contains(url))
			urls.add(url);
    }

    public List<Url> getUrls() {
        return urls;
    }

    public Url getUrl() {
		return urls.get(0);
    }
}