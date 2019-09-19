/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE TABLE IF NOT EXISTS `trabajadores` (
  `idTrabajador` int(11) NOT NULL AUTO_INCREMENT,
  `idActividad` int(11) NOT NULL,
  `numeroTrabajador` varchar(50) DEFAULT NULL,
  `nombreTrabajador` varchar(50) DEFAULT NULL,
  `horaEntradaTrabajador` varchar(20) DEFAULT NULL,
  `horaSalidaTrabajador` varchar(20) DEFAULT NULL,
  `estadoTrabajador` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`idTrabajador`),
  KEY `trabajadores_actividades` (`idActividad`),
  CONSTRAINT `trabajadores_actividades` FOREIGN KEY (`idActividad`) REFERENCES `actividades` (`idActividad`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
