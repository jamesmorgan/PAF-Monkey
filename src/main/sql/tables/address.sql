DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
 `ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
 `DepartmentName` VARCHAR(60) DEFAULT NULL,
 `OrganisationName` VARCHAR(60) DEFAULT NULL,
 `SubBuildingName` VARCHAR(30) DEFAULT NULL,
 `BuildingName` VARCHAR(50) DEFAULT NULL,
 `BuildingNumber` INT(4) UNSIGNED DEFAULT NULL,
 `DependentThoroughfare` VARCHAR(80) DEFAULT NULL,
 `Thoroughfare` VARCHAR(80) DEFAULT NULL,
 `DoubleDependentLocality` VARCHAR(35) DEFAULT NULL,
 `DependentLocality` VARCHAR(35) DEFAULT NULL,
 `Town` VARCHAR(30) DEFAULT NULL,
 `County` VARCHAR(100) DEFAULT NULL,
 `Postcode` VARCHAR(8) DEFAULT NULL,
 `Udprn` INT(8) UNSIGNED NOT NULL DEFAULT '0',
 PRIMARY KEY (ID),
 KEY paf_address_index1 (`Thoroughfare`),
 KEY paf_address_index2 (`Town`),
 KEY paf_address_index3 (`Postcode`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;