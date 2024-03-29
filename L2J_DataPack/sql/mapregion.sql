-- 
-- Table structure for table `mapregion`
-- 
DROP TABLE IF EXISTS mapregion;
CREATE TABLE `mapregion` (
`region` int(11) NOT NULL default '0',
`sec0` int(2) NOT NULL default '0',
`sec1` int(2) NOT NULL default '0',
`sec2` int(2) NOT NULL default '0',
`sec3` int(2) NOT NULL default '0',
`sec4` int(2) NOT NULL default '0',
`sec5` int(2) NOT NULL default '0',
`sec6` int(2) NOT NULL default '0',
`sec7` int(2) NOT NULL default '0',
`sec8` int(2) NOT NULL default '0',
`sec9` int(2) NOT NULL default '0',
PRIMARY KEY (`region`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `mapregion`
-- 

INSERT INTO mapregion VALUES 
(0, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4),
(1, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4),
(2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4),
(3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4),
(4, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4),
(5, 3, 3, 3, 3, 3, 16, 16, 16, 15, 15),
(6, 3, 3, 3, 3, 3, 16, 16, 16, 15, 15),
(7, 3, 3, 3, 3, 3, 14, 14, 15, 15, 15),
(8, 3, 3, 3, 3, 14, 14, 14, 15, 15, 15),
(9, 2, 2, 2, 2, 2, 2, 9, 9, 10, 10),
(10, 2, 2, 2, 2, 2, 9, 9, 10, 10, 10),
(11, 2, 2, 2, 2, 1, 1, 9, 11, 10, 10),
(12, 6, 6, 2, 5, 1, 1, 9, 11, 11, 11),
(13, 6, 6, 5, 5, 7, 7, 8, 8, 8, 8),
(14, 6, 6, 6, 5, 7, 7, 8, 8, 8, 8),
(15, 0, 6, 6, 5, 7, 12, 13, 13, 13, 13),
(16, 0, 0, 6, 6, 12, 12, 13, 13, 13, 13),
(17, 0, 0, 0, 0, 0, 0, 13, 13, 13, 13);