DROP TABLE IF EXISTS `localities`;
CREATE TABLE `localities` (
  `LocalityKey` int(10) unsigned NOT NULL DEFAULT '0',
  `PostTown` varchar(30) NOT NULL DEFAULT '',
  `DependentLocality` varchar(35) NOT NULL DEFAULT '',
  `DoubleDependentLocality` varchar(35) NOT NULL DEFAULT '',
  PRIMARY KEY (`LocalityKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
