package com.yuzukiku.dummy_reserve_log;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class DummyReserveLogApplicationTests {

	@Test
	void contextLoads() {
	}

}
