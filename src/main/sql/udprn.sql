CREATE TABLE `udprn` (
  `UdprnKey` int(8) UNSIGNED NOT NULL DEFAULT 0,
  `Postcode` varchar(7) NOT NULL DEFAULT '',
  `AddressKey` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `OrganisationKey` int(8) UNSIGNED,
  `PostcodeType` varchar(1) NOT NULL DEFAULT '',
  `DeliveryPointSuffix` varchar(2) NOT NULL DEFAULT '',
  `SuOrganisationIndicator` varchar(1) NOT NULL DEFAULT '',
  PRIMARY KEY (`UdprnKey`),
  KEY `udprn_index1` (`AddressKey`),
  KEY `udprn_index2` (`OrganisationKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
