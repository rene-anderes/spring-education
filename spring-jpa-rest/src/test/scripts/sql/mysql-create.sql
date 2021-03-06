
CREATE TABLE `recipe` (
  `UUID` varchar(255) NOT NULL,
  `ADDINGDATE` datetime DEFAULT NULL,
  `LASTUPDATE` datetime DEFAULT NULL,
  `NOOFPERSON` varchar(10) NOT NULL,
  `PREAMBLE` varchar(8000) DEFAULT NULL,
  `PREPARATION` varchar(8000) NOT NULL,
  `RATING` int(11) NOT NULL,
  `TITLE` varchar(80) NOT NULL,
  `VERSION` int(11) DEFAULT NULL,
  `IMAGE_DESCRIPTION` varchar(50) DEFAULT NULL,
  `IMAGE_URL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`UUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ingredient` (
  `UUID` varchar(255) NOT NULL,
  `ANNOTATION` varchar(80) DEFAULT NULL,
  `DESCRIPTION` varchar(80) NOT NULL,
  `QUANTITY` varchar(20) DEFAULT NULL,
  `RECIPE_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`UUID`),
  KEY `FK_INGREDIENT_RECIPE_ID` (`RECIPE_ID`),
  CONSTRAINT `FK_INGREDIENT_RECIPE_ID` FOREIGN KEY (`RECIPE_ID`) REFERENCES `recipe` (`UUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tags` (
  `RECIPE_ID` varchar(255) DEFAULT NULL,
  `TAGS` varchar(255) DEFAULT NULL,
  KEY `FK_TAGS_RECIPE_ID` (`RECIPE_ID`),
  CONSTRAINT `FK_TAGS_RECIPE_ID` FOREIGN KEY (`RECIPE_ID`) REFERENCES `recipe` (`UUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
