DROP TABLE IF EXISTS `organisations`;
CREATE TABLE `organisations` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `OrganisationKey` int(10) unsigned NOT NULL DEFAULT '0',
  `PostcodeType` varchar(1) NOT NULL DEFAULT '',
  `OrganisationName` varchar(60) NOT NULL DEFAULT '',
  `DepartmentName` varchar(60) NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`),
  UNIQUE KEY (`OrganisationKey`, `PostcodeType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;