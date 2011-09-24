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