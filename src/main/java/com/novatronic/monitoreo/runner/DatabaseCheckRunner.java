package com.novatronic.monitoreo.runner;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseCheckRunner implements CommandLineRunner {

	private final EntityManager entityManager;

	@Override
	@Transactional(readOnly = true)
	public void run(String... args) {
		log.info("==========================================================");
		log.info(">> COMPROBANDO CONEXION JPA A ORACLE CLOUD <<");
		log.info("==========================================================");

		try {
			Object serverTime = entityManager
					.createNativeQuery("SELECT TO_CHAR(SYSTIMESTAMP, 'YYYY-MM-DD HH24:MI:SS TZR') FROM DUAL")
					.getSingleResult();

			Object currentUser = entityManager
					.createNativeQuery("SELECT SYS_CONTEXT('USERENV', 'SESSION_USER') FROM DUAL").getSingleResult();

			log.info(">> CONEXION A ORACLE CONFIRMADA <<");
			log.info("   - Usuario conectado : {}", currentUser);
			log.info("   - Hora Servidor OCI : {}", serverTime);
			log.info("==========================================================");
		} catch (Exception ex) {
			log.error("Error al consultar la BD: {}", ex.getMessage(), ex);
		}
	}
}