package com.morgan.design.paf.service;

import org.jmock.Expectations;
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

	@Before
	public void setUp() {
		this.pafParsingService = new PafParsingServiceImpl();
		this.pafRepository = this.context.mock(PafRepository.class);
		ReflectionTestUtils.setField(this.pafParsingService, "pafRepository", this.pafRepository);
		ReflectionTestUtils.setField(this.pafParsingService, "maxBatchSize", 10);

	}

	@Test
	public void testme() {

		this.context.checking(new Expectations() {
			{
				one(PafServiceImpUnitTest.this.pafRepository).saveBatch(constructTestArgs(), null, null);
			}
		});

		this.pafParsingService.sourcePafFiles(constructTestArgs());
	}

	private CommandLinePafArgs constructTestArgs() {
		final CommandLinePafArgs args = new CommandLinePafArgs();
		args.definitionDirectory = "src\\main\\resources\\definitions\\";
		args.directory = "src\\test\\resources\\test_data\\data\\";
		// args.host = "127.0.0.1";
		// args.password = "password";
		// args.schema = "paf";
		// args.username = "paf_user";
		return args;
	}

}
