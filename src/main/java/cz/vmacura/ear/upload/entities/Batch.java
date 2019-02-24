package cz.vmacura.ear.upload.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "batch")
public class Batch extends AbstractEntity
{
	@ManyToMany
	private List<File> files = new ArrayList<>();

	@OneToMany(cascade = {CascadeType.ALL})
	private List<Url> urls = new ArrayList<>();

	@ManyToOne
	private Account account;

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files)
	{
		this.files = files;
	}

	public List<Url> getUrls()
	{
		return urls;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}

	public void setUrls(List<Url> urls) {
		this.urls = urls;
	}

	public void addUrl(Url url)
	{
		if (!this.urls.contains(url))
			this.urls.add(url);
	}
}
