package cz.vmacura.ear.upload.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payment")
public class Payment extends AbstractEntity
{
	@ManyToOne
	private Account account;

	@Column
	private double amount;

	@Column
	private Date date;

	@Column
	private String type;

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public double getAmount()
	{
		return amount;
	}

	public void setAmount(double amount)
	{
		this.amount = amount;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}
}
