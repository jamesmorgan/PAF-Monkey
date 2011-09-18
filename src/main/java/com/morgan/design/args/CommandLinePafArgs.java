package com.morgan.design.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.morgan.design.paf.domain.Mode;

/**
 * @author James Edward Morgan
 */
@Parameters(commandDescription = "MySql Configs and defaults")
public class CommandLinePafArgs implements DbConfig {

	public static final String DEFINITIONS_DIR = "src/main/resources/definitions";

	public static CommandLinePafArgs parseArgs(final String[] args) {
		final CommandLinePafArgs paf = new CommandLinePafArgs();
		final JCommander jCommander = new JCommander(paf, args);
		jCommander.addConverterFactory(new ConverterFactory());

		return paf;
	}

	@Parameter(names = { "--verbose" }, description = "Enable verbose logging", required = false)
	public Boolean verbose = Boolean.FALSE;

	@Parameter(names = { "-directory" }, description = "The directory where the PAF data is stored", required = true)
	public String directory;

	@Parameter(names = { "-definitionDirectory" }, description = "The directory where the definition files data are held", required = false)
	public String definitionDirectory = DEFINITIONS_DIR;

	@Parameter(names = { "-mode" }, description = "The mode to run in when reading paf files", required = false)
	public Mode mode = Mode.SOURCE;

	@Parameter(names = { "-host" }, description = "The database host to be used", required = true)
	public String host;

	@Parameter(names = { "-port" }, description = "The database port, Default MySql = 3306, MongoDB = 27017", required = false)
	public int port;

	@Parameter(names = { "-schema" }, description = "The database schema to be used", required = false)
	public String schema = "paf";

	@Parameter(names = { "-username" }, description = "The username for the database", required = true)
	public String username;

	@Parameter(names = { "-password" }, description = "The password for the database", required = true)
	public String password;

	@Parameter(names = { "-db" }, description = "Type of persistence, mongo or mysql", required = false)
	public String db = "mysql";

	@Override
	public int getPort() {
		if (0 != this.port) {
			return this.port;
		}
		return "mysql".equalsIgnoreCase(this.db)
				? 3306
				: 27017;
	}

	public boolean mongoEnabled() {
		return "mongo".equalsIgnoreCase(this.db);
	}
}
