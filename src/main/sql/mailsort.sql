CREATE TABLE `mailsort` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `PostcodeOutwardCode` varchar(4) NOT NULL DEFAULT '',
  `PostcodeSector` int(2) unsigned DEFAULT NULL,
  `ResidueIdentifier` int(2) unsigned DEFAULT NULL,
  `DirectWithinResidueIndicator` int(2) unsigned DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
