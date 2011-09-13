CREATE TABLE `paf_change_log` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `StartDate` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `EndDate` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `Mode` enum('SOURCE','UPDATE', '') NOT NULL DEFAULT '',
  `BuildNames` int(10) unsigned NOT NULL DEFAULT '0',
  `Localities` int(10) unsigned NOT NULL DEFAULT '0',
  `MailSort` int(10) unsigned NOT NULL DEFAULT '0',
  `Organisations` int(10) unsigned NOT NULL DEFAULT '0',
  `PafAddress` int(10) unsigned NOT NULL DEFAULT '0',
  `SubBuildingName` int(10) unsigned NOT NULL DEFAULT '0',
  `ThoroughfareDescriptor` int(10) unsigned NOT NULL DEFAULT '0',
  `Thoroughfare` int(10) unsigned NOT NULL DEFAULT '0',
  `Udprn` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
