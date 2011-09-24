DROP TABLE IF EXISTS `building_names`;
CREATE TABLE `building_names` (
  `BuildingNameKey` int(10) unsigned NOT NULL DEFAULT '0',
  `BuildingName` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`BuildingNameKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
