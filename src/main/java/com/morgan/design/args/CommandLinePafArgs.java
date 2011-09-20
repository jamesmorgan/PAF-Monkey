package com.morgan.design.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.morgan.design.paf.domain.Mode;

/**
 * @author James Edward Morgan
 */
public class CommandLinePafArgs {

	private static final int MONGO_DEFAULT_PORT = 27017;
	private static final int MYSQL_DEFAULT_PORT = 3306;

	private static final String MYSQL = "mysql";
	private static final String MONGO = "mongo";

	public static final String DEFINITIONS_DIR = "src/main/resources/definitions";

	public static CommandLinePafArgs parseArgs(final String[] args) {
		final CommandLinePafArgs paf = new CommandLinePafArgs();
		final JCommander jCommander = new JCommander(paf);
		jCommander.addConverterFactory(new ConverterFactory());
		jCommander.parse(args);

		if (0 == paf.port) {
			paf.port = MYSQL.equalsIgnoreCase(paf.db)
					? MYSQL_DEFAULT_PORT
					: MONGO_DEFAULT_PORT;
		}
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
	public String db = MYSQL;

	public final boolean mongoEnabled() {
		return MONGO.equalsIgnoreCase(this.db);
	}
}
