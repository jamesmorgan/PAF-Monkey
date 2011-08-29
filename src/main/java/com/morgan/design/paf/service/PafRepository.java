package com.morgan.design.paf.service;

import java.util.List;

import com.morgan.design.args.CommandLinePafArgs;

public interface PafRepository {

	void savePafEntries(CommandLinePafArgs pafArgs, List entries);

	void updatePafEntries(CommandLinePafArgs pafArgs, List entries);

}
