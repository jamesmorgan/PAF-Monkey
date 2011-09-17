package com.morgan.design.paf.service;

import com.morgan.design.args.CommandLinePafArgs;

/**
 * @author James Edward Morgan
 */
public interface PafParsingService {

	/**
	 * @param pafArgs
	 */
	void sourcePafFiles(CommandLinePafArgs pafArgs);

}
