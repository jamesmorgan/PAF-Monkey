package com.morgan.design.paf.service;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.morgan.design.args.CommandLinePafArgs;
import com.morgan.design.paf.repository.PafRepository;

public class PafServiceImpUnitTest {

	protected Mockery context = new JUnit4Mockery();

	private PafRepository pafRepository;
	private PafParsingService pafParsingService;
	private ReportGenerator reportGenerator;

	@Before
	public void setUp() {
		this.pafParsingService = new PafParsingServiceImpl();
		this.pafRepository = this.context.mock(PafRepository.class);
		ReflectionTestUtils.setField(this.pafParsingService, "pafRepository", this.pafRepository);
		ReflectionTestUtils.setField(this.pafParsingService, "reportGenerator", this.reportGenerator);
		ReflectionTestUtils.setField(this.pafParsingService, "maxBatchSize", 10);

	}

	@Test
	public void testme() {
		//
	}

	private CommandLinePafArgs constructTestArgs() {
		final CommandLinePafArgs args = new CommandLinePafArgs();
		args.definitionDirectory = "src\\main\\resources\\definitions\\";
		args.directory = "src\\test\\resources\\test_data\\data\\";
		args.host = "127.0.0.1";
		args.password = "password";
		args.schema = "paf";
		args.username = "paf_user";
		return args;
	}

}
