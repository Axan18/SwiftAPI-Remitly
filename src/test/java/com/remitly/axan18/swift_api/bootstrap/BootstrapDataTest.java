package com.remitly.axan18.swift_api.bootstrap;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BootstrapData.class)
class BootstrapDataTest {

}