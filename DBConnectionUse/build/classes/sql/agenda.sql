-- Version del servidor: 5.6.24


SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+05:00";


--
-- Base de datos: `agenda`
--

CREATE DATABASE agenda;

USE agenda;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `contactos`
--

CREATE TABLE IF NOT EXISTS `contactos` (
  `idContacto` int(11) NOT NULL PRIMARY KEY,
  `nombre` varchar(50) COLLATE latin1_spanish_ci NOT NULL,
  `telefono` varchar(10) COLLATE latin1_spanish_ci NOT NULL,
  `email` varchar(50) COLLATE latin1_spanish_ci NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci;

--
-- Volcado de datos para la tabla `contactos`
--

INSERT INTO `contactos` (`idContacto`, `nombre`, `telefono`, `email`) VALUES
(1, 'Dejah Thoris', '444444444', 'dejah@barson.com'),
(2, 'John Carter', '3333333333', 'john@tierra.com');
