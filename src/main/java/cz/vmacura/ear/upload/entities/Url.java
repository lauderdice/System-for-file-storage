package cz.vmacura.ear.upload.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "url")
public class Url extends AbstractEntity
{

	@JsonBackReference
	@ManyToOne
	private File file;

	@ManyToOne
	private Batch batch;

	@Column
	private String string;

	public  String getString() {
		return string;
	}

	public File getFile() {
		return file;
	}

    public void setFile(File file) {
        this.file = file;
    }

    public void setString(String string) {
        this.string = string;
    }

	public void setBatch(Batch batch) {
		this.batch = batch;
	}
}
