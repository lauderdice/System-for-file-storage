package cz.vmacura.ear.upload.entities;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
abstract class AbstractEntity implements Serializable
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	public Integer getId()
	{
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
