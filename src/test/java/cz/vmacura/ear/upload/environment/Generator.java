package cz.vmacura.ear.upload.environment;

import cz.vmacura.ear.upload.entities.Account;
import cz.vmacura.ear.upload.entities.Batch;
import cz.vmacura.ear.upload.entities.File;
import cz.vmacura.ear.upload.entities.Url;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

public class Generator {

	private static final Random RAND = new Random();
	public static int randomInt() {
		return RAND.nextInt();
	}

	public static boolean randomBoolean() {
		return RAND.nextBoolean();
	}

	public static Account generateAccount() {
		final Account account = new Account();
		account.setUsername("Username1");
		account.setPassword("pw");
		account.setRole(1);
		return account;
	}

	public static Batch generateBatch() {
		final Batch batch = new Batch();
		Url url = new Url();
		url.setString("sqekfboisy");
		batch.addUrl(url);
		batch.setId(1);
		return batch;
	}

	public static File generateFile()
	{
		final File file = new File();
		file.setName("Filename" + randomInt());
		file.addUrl(new Url());
		return file;
	}
}
