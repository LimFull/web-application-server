package util;

import java.util.UUID;

import org.junit.Test;

public class UUIDTest {
	@Test
	public void uuidtest() {
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid);
	}
}
