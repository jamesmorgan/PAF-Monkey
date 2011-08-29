CREATE TABLE `thoroughfare_descriptor` (
  `ThoroughfareDescriptorKey` int(10) unsigned NOT NULL DEFAULT '0',
  `ThoroughfareDescriptor` varchar(20) NOT NULL DEFAULT '',
  `ApprovedAbbreviation` varchar(6) NOT NULL DEFAULT '',
  PRIMARY KEY (`ThoroughfareDescriptorKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;