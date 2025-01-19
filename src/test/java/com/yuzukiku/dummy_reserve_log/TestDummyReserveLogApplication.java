package com.yuzukiku.dummy_reserve_log;

import org.springframework.boot.SpringApplication;

public class TestDummyReserveLogApplication {

	public static void main(String[] args) {
		SpringApplication.from(DummyReserveLogApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
