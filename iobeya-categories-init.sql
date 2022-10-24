-- iobeya-categories.sql

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `iobeya`
--

DELIMITER $$
--
-- Procédures
--
CREATE DEFINER=`iobeya`@`%` PROCEDURE `generate1000categories` ()   BEGIN
DECLARE i INT DEFAULT 1; 
WHILE (i <= 1000) DO
    INSERT INTO categories (name) VALUES (concat("category." , i , "."));
    SET i = i+1;
END WHILE;
END$$

CREATE DEFINER=`iobeya`@`%` PROCEDURE `generateChildCategories` ()   BEGIN

DECLARE idepth INT DEFAULT 0; 
DECLARE irand INT DEFAULT 0; 

WHILE (idepth < 10) DO
    SET irand = 0;

    WHILE (irand < 10) DO
        INSERT INTO categories (name, parent)
            SELECT (concat(name,irand,".")), id
            FROM categories
            WHERE depth = idepth AND rand() < 0.2
            ORDER BY RAND();
        SET irand = irand+1;
    END WHILE;

    SET idepth = idepth+1;
END WHILE;
END$$

CREATE DEFINER=`iobeya`@`%` PROCEDURE `getCategoryParents` (IN `category_id` BIGINT(20) UNSIGNED)   BEGIN

	with recursive name_tree as (
	   select *
	   from categories_view
	   where id = category_id
	   union all
	   select c.*
	   from categories_view c
	     join name_tree p on p.parent = c.id
	) 
	select *
	from name_tree
	where id <> category_id;
END$$

CREATE DEFINER=`iobeya`@`%` PROCEDURE `getStats` ()   BEGIN
    SELECT depth, count(depth) AS total
    FROM categories;
END$$

CREATE DEFINER=`iobeya`@`%` PROCEDURE `getStatsByDepth` ()   BEGIN
    SELECT depth, count(depth) AS total
    FROM categories
    GROUP BY depth;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `categories`
--

CREATE TABLE `categories` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(250) NOT NULL,
  `parent` bigint(20) UNSIGNED DEFAULT NULL,
  `depth` tinyint(4) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- Index pour les tables déchargées
--

--
-- Index pour la table `categories`
--
ALTER TABLE `categories`
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `parent_fk` (`parent`);


--
-- Contraintes pour la table `categories`
--
ALTER TABLE `categories`
  ADD CONSTRAINT `parent_fk` FOREIGN KEY (`parent`) REFERENCES `categories` (`id`);


--
-- Déclencheurs `categories`
--
DELIMITER $$
CREATE TRIGGER `CHECK_ALPHANUM_NAME` BEFORE INSERT ON `categories` FOR EACH ROW IF NOT (NEW.name REGEXP '^[A-Za-z0-9.]+$')
   THEN
      BEGIN      
        SIGNAL sqlstate '45000' set message_text = 'NAME MUST BE ALPHANUMERICAL';
      END;
  END IF
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `CHECK_MAX_1000_ROOT_CATEGORIES` BEFORE INSERT ON `categories` FOR EACH ROW IF (NEW.parent IS NULL) AND (SELECT count(id) FROM categories WHERE parent IS NULL) > 1000 THEN
SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'ALREADY HAVE 1000 ROOT CATEGORIES';
END IF
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `CHECK_MAX_10_CHILD_CATEGORIES` BEFORE INSERT ON `categories` FOR EACH ROW IF (SELECT count(parent) FROM categories WHERE id = NEW.parent) > 10 THEN
SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'THIS CATEGORY HAS ALREADY 10 CHILDREN';
END IF
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `CHECK_MAX_10_DEPTH` BEFORE INSERT ON `categories` FOR EACH ROW IF (NEW.parent IS NULL) THEN
  SET NEW.depth = 0; 
ELSEIF (SELECT (depth) FROM categories WHERE id = NEW.parent) > 9 THEN
	SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'THIS PARENT IS AT DEPTH 10, NO MORE CHILDREN ALLOWED';

ELSE
	SET NEW.depth = (SELECT (depth) FROM categories WHERE id = NEW.parent) + 1;
END IF
$$
DELIMITER ;

-- --------------------------------------------------------

CREATE VIEW categories_view AS
    SELECT A.*, (SELECT count(B.parent)  FROM categories AS B WHERE B.parent = A.id ) AS child_count 
    FROM categories AS A GROUP BY A.id ORDER BY A.name; 




COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
