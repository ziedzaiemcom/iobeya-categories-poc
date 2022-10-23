TRUNCATE TABLE `iobeya`.`categories`;

CALL generate1000categories();

CALL generateChildCategories();
