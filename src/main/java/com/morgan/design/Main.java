package com.morgan.design;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.morgan.design.args.CommandLinePafArgs;
import com.morgan.design.paf.service.PafParsingService;

/**
 * @author James Edward Morgan
 */
public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) {
		final CommandLinePafArgs paf = CommandLinePafArgs.parseArgs(args);

		logger.debug("Running PAF-Monkey, args: -mode=[{}] -username=[{}], -password=[{}], -host=[{}], -dir=[{}]", new Object[] { paf.mode,
				paf.username, paf.password, paf.host, paf.directory });

		final ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-paf.xml");
		final PafParsingService service = context.getBean(PafParsingService.class);

		switch (paf.mode) {
			case SOURCE:
				service.sourcePafFiles(paf);
				break;
			default:
				throw new UnsupportedOperationException("Unknown Paf'ing mode specified!");
		}
	}
}
