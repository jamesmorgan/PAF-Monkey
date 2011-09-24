DROP TABLE IF EXISTS `udprn`;
CREATE TABLE `udprn` (
  `UdprnKey` INT(8) UNSIGNED NOT NULL DEFAULT 0,
  `Postcode` VARCHAR(7) NOT NULL DEFAULT '',
  `AddressKey` INT(10) UNSIGNED NOT NULL DEFAULT 0,
  `OrganisationKey` INT(8) UNSIGNED NOT NULL DEFAULT 0,
  `PostcodeType` VARCHAR(1) NOT NULL DEFAULT '',
  `DeliveryPointSuffix` VARCHAR(2) NOT NULL DEFAULT '',
  `SuOrganisationIndicator` VARCHAR(1) NOT NULL DEFAULT '',
  PRIMARY KEY (`Postcode`,`AddressKey`,`OrganisationKey`),
  KEY `udprn_index1` (`UdprnKey`),
  KEY `udprn_index2` (`AddressKey`),
  KEY `udprn_index3` (`OrganisationKey`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `thoroughfare`;
CREATE TABLE `thoroughfare` (
  `ThoroughfareKey` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `ThoroughfareName` VARCHAR(60) NOT NULL DEFAULT '',
  PRIMARY KEY (`ThoroughfareKey`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `thoroughfare_descriptor`;
CREATE TABLE `thoroughfare_descriptor` (
  `ThoroughfareDescriptorKey` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `ThoroughfareDescriptor` VARCHAR(20) NOT NULL DEFAULT '',
  `ApprovedAbbreviation` VARCHAR(6) NOT NULL DEFAULT '',
  PRIMARY KEY (`ThoroughfareDescriptorKey`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `sub_building_name`;
CREATE TABLE `sub_building_name` (
  `SubBuildingNameKey` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `SubBuildingName` VARCHAR(30) NOT NULL DEFAULT '',
  PRIMARY KEY (`SubBuildingNameKey`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `paf_change_log`;
CREATE TABLE `paf_change_log` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT,
  `StartDate` DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00',
  `EndDate` DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00',
  `Mode` ENUM('SOURCE','UPDATE', '') NOT NULL DEFAULT '',
  `BuildNames` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `Localities` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `MailSort` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `Organisations` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `PafAddress` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `SubBuildingName` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `ThoroughfareDescriptor` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `Thoroughfare` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `Udprn` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `paf_address`;
CREATE TABLE `paf_address` (
  `ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `AddressKey` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `PostcodeOutwardCode` VARCHAR(4) NOT NULL DEFAULT '',
  `PostcodeInwardCode` VARCHAR(3) NOT NULL DEFAULT '',
  `Postcode` VARCHAR(8) NOT NULL DEFAULT '',
  `LocalityKey` INT(6) UNSIGNED DEFAULT NULL,
  `ThoroughfareKey` INT(8) UNSIGNED DEFAULT NULL,
  `ThoroughfareDescriptorKey` INT(4) UNSIGNED DEFAULT NULL,
  `DependentThoroughfareKey` INT(8) UNSIGNED DEFAULT NULL,
  `DependentThoroughfareDescriptorKey` INT(4) UNSIGNED DEFAULT NULL,
  `BuildingNumber` INT(4) UNSIGNED DEFAULT NULL,
  `BuildingNameKey` INT(8) UNSIGNED DEFAULT NULL,
  `SubBuildingNameKey` INT(8) UNSIGNED DEFAULT NULL,
  `NumberOfHouseholds` INT(4) UNSIGNED NOT NULL DEFAULT '0',
  `OrganisationKey` INT(8) UNSIGNED DEFAULT NULL,
  `PostcodeType` VARCHAR(1) NOT NULL DEFAULT '',
  `ConcatenationIndicator` VARCHAR(1) NOT NULL DEFAULT '',
  `DeliveryPointSuffix` VARCHAR(2) NOT NULL DEFAULT '',
  `SmallUserOrganisationIndicator` VARCHAR(1) NOT NULL DEFAULT '',
  `POBoxNumber` VARCHAR(6) NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`),
  UNIQUE KEY(`AddressKey`,`OrganisationKey`,`PostcodeType`, `LocalityKey`, `ThoroughfareKey`),
  KEY paf_address_index1 (`AddressKey`),
  KEY paf_address_index2 (`Postcode`),
  KEY paf_address_index3 (`LocalityKey`),
  KEY paf_address_index4 (`ThoroughfareKey`),
  KEY paf_address_index5 (`ThoroughfareDescriptorKey`),
  KEY paf_address_index6 (`DependentThoroughfareKey`),
  KEY paf_address_index7 (`DependentThoroughfareDescriptorKey`),
  KEY paf_address_index8 (`BuildingNameKey`),
  KEY paf_address_index9 (`SubBuildingNameKey`),
  KEY paf_address_index10 (`OrganisationKey`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `organisations`;
CREATE TABLE `organisations` (
  `OrganisationKey` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `PostcodeType` VARCHAR(1) NOT NULL DEFAULT '',
  `OrganisationName` VARCHAR(60) NOT NULL DEFAULT '',
  `DepartmentName` VARCHAR(60) NOT NULL DEFAULT '',
  UNIQUE KEY (`OrganisationKey`, `PostcodeType`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `mailsort`;
CREATE TABLE `mailsort` (
  `ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `PostcodeOutwardCode` VARCHAR(4) NOT NULL DEFAULT '',
  `PostcodeSector` INT(2) UNSIGNED DEFAULT NULL,
  `ResidueIdentifier` INT(2) UNSIGNED DEFAULT NULL,
  `DirectWithinResidueIndicator` INT(2) UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `localities`;
CREATE TABLE `localities` (
  `LocalityKey` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `PostTown` VARCHAR(30) NOT NULL DEFAULT '',
  `DependentLocality` VARCHAR(35) NOT NULL DEFAULT '',
  `DoubleDependentLocality` VARCHAR(35) NOT NULL DEFAULT '',
  PRIMARY KEY (`LocalityKey`)
) ENGINE=INNODB DEFAULT CHARSET=utf8


DROP TABLE IF EXISTS `building_names`;
CREATE TABLE `building_names` (
  `BuildingNameKey` INT(10) UNSIGNED NOT NULL DEFAULT '0',
  `BuildingName` VARCHAR(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`BuildingNameKey`)
) ENGINE=INNODB DEFAULT CHARSET=utf8
