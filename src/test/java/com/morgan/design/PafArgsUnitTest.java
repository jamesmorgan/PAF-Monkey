package com.morgan.design;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.Is;
import org.junit.Test;

import com.morgan.design.args.CommandLinePafArgs;
import com.morgan.design.paf.domain.Mode;

public class PafArgsUnitTest {

	@Test
	public void ShouldParseArgsCorrectly() {
		final String[] args =
				new String[] { "-directory", "YourFullPathDir", "-host", "DbHost", "-username", "DbUser", "-password", "DbPassword" };
		assertThat(CommandLinePafArgs.parseArgs(args).directory, Is.is("YourFullPathDir"));
		assertThat(CommandLinePafArgs.parseArgs(args).host, Is.is("DbHost"));
		assertThat(CommandLinePafArgs.parseArgs(args).mode, Is.is(Mode.SOURCE));
		assertThat(CommandLinePafArgs.parseArgs(args).schema, Is.is("paf"));
		assertThat(CommandLinePafArgs.parseArgs(args).username, Is.is("DbUser"));
		assertThat(CommandLinePafArgs.parseArgs(args).password, Is.is("DbPassword"));
		assertThat(CommandLinePafArgs.parseArgs(args).definitionDirectory, Is.is("src/main/resources/definitions"));
		assertThat(CommandLinePafArgs.parseArgs(args).db, Is.is("mysql"));
		assertThat(CommandLinePafArgs.parseArgs(args).port, Is.is(3306));
	}

	@Test
	public void ShouldParseArgsCorrectlyWhenMongoSpecified() {
		final String[] args =
				new String[] { "-directory", "YourFullPathDir", "-host", "DbHost", "-username", "DbUser", "-password", "DbPassword", "-db",
						"mongo" };

		assertThat(CommandLinePafArgs.parseArgs(args).directory, Is.is("YourFullPathDir"));
		assertThat(CommandLinePafArgs.parseArgs(args).host, Is.is("DbHost"));
		assertThat(CommandLinePafArgs.parseArgs(args).mode, Is.is(Mode.SOURCE));
		assertThat(CommandLinePafArgs.parseArgs(args).schema, Is.is("paf"));
		assertThat(CommandLinePafArgs.parseArgs(args).username, Is.is("DbUser"));
		assertThat(CommandLinePafArgs.parseArgs(args).password, Is.is("DbPassword"));
		assertThat(CommandLinePafArgs.parseArgs(args).port, Is.is(27017));
		assertThat(CommandLinePafArgs.parseArgs(args).db, Is.is("mongo"));
		assertThat(CommandLinePafArgs.parseArgs(args).definitionDirectory, Is.is("src/main/resources/definitions"));
	}
}
