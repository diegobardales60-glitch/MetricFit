-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost:3306
-- Tiempo de generación: 05-12-2025 a las 03:00:27
-- Versión del servidor: 11.8.5-MariaDB-log
-- Versión de PHP: 8.4.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `diegobardales_proyecto`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alimentos`
--

CREATE TABLE `alimentos` (
  `id_alimento` int(11) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `unidad_medida` varchar(50) NOT NULL DEFAULT '100g',
  `calorias` decimal(7,2) NOT NULL DEFAULT 0.00,
  `proteinas` decimal(7,2) NOT NULL DEFAULT 0.00,
  `carbohidratos` decimal(7,2) NOT NULL DEFAULT 0.00,
  `grasas` decimal(7,2) NOT NULL DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

--
-- Volcado de datos para la tabla `alimentos`
--

INSERT INTO `alimentos` (`id_alimento`, `nombre`, `unidad_medida`, `calorias`, `proteinas`, `carbohidratos`, `grasas`) VALUES
(1, 'Pechuga de Pollo (cocida)', '100g', 165.00, 31.00, 0.00, 3.60),
(2, 'Arroz Blanco (cocido)', '100g', 130.00, 2.70, 28.00, 0.30),
(3, 'Huevo (cocido)', 'unidad (50g)', 78.00, 6.30, 0.60, 5.30),
(4, 'Manzana', 'unidad (180g)', 95.00, 0.50, 25.00, 0.30),
(5, 'Salmón (cocido)', '100g', 206.00, 22.00, 0.00, 12.00),
(6, 'Brócoli (cocido)', '100g', 35.00, 2.40, 7.20, 0.40),
(7, 'Aceite de Oliva', '1 cucharada (15ml)', 119.00, 0.00, 0.00, 13.50),
(8, 'Papa (cocida)', '100g', 87.00, 1.90, 20.10, 0.10);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `consumo_diario`
--

CREATE TABLE `consumo_diario` (
  `id_consumo` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `id_alimento` int(11) NOT NULL,
  `cantidad` decimal(6,2) NOT NULL,
  `fecha` date NOT NULL,
  `tipo_comida` varchar(50) DEFAULT 'Otros'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ejercicios`
--

CREATE TABLE `ejercicios` (
  `id_ejercicio` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `zona_muscular` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `gif_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

--
-- Volcado de datos para la tabla `ejercicios`
--

INSERT INTO `ejercicios` (`id_ejercicio`, `nombre`, `zona_muscular`, `descripcion`, `gif_url`) VALUES
(1, 'Press de Banca con Barra', 'Pecho', 'Acostado en un banco, baja la barra al pecho y empuja hacia arriba.', 'gifs/press_banca.gif'),
(2, 'Aperturas con Mancuernas', 'Pecho', 'Acostado en un banco, abre y cierra los brazos con mancuernas.', NULL),
(3, 'Fondos en Paralelas', 'Pecho', 'Sujétate en barras paralelas, baja el cuerpo y empuja hacia arriba.', NULL),
(4, 'Dominadas (Pull-ups)', 'Espalda', 'Cuélgate de una barra y jala tu cuerpo hacia arriba hasta que la barbilla la supere.', 'gifs/dominadas.gif'),
(5, 'Remo con Barra', 'Espalda', 'Inclina el torso hacia adelante y jala la barra hacia tu abdomen.', NULL),
(6, 'Jalón al Pecho (Polea)', 'Espalda', 'Sentado, jala una barra desde una polea alta hacia tu pecho.', NULL),
(7, 'Sentadillas con Barra', 'Pierna', 'Con la barra en la espalda, baja las caderas como si te sentaras y vuelve a subir.', 'gifs/sentadillas.gif'),
(8, 'Prensa de Piernas', 'Pierna', 'Sentado en una máquina, empuja una plataforma con los pies.', NULL),
(9, 'Peso Muerto Rumano', 'Pierna', 'Con una barra, baja el torso con las piernas casi rectas sintiendo el femoral.', NULL),
(10, 'Extensiones de Cuádriceps', 'Pierna', 'Sentado en una máquina, extiende las piernas contra una resistencia.', NULL),
(11, 'Press Militar con Barra', 'Hombro', 'De pie o sentado, empuja una barra desde tus hombros hacia arriba.', NULL),
(12, 'Elevaciones Laterales', 'Hombro', 'De pie, levanta mancuernas hacia los lados hasta la altura de los hombros.', NULL),
(13, 'Pájaros (Posterior)', 'Hombro', 'Inclinado, levanta mancuernas hacia los lados simulando el aleteo.', NULL),
(14, 'Curl de Bíceps con Mancuernas', 'Bíceps', 'De pie o sentado, flexiona el codo para levantar las mancuernas.', NULL),
(15, 'Curl Martillo', 'Bíceps', 'Igual que el curl normal, pero con las palmas de las manos mirándose.', NULL),
(16, 'Extensiones de Tríceps (Polea)', 'Tríceps', 'De pie, empuja una barra o cuerda de polea alta hacia abajo.', NULL),
(17, 'Press Francés', 'Tríceps', 'Acostado, baja una barra o mancuernas hacia tu frente y extiende.', NULL),
(18, 'Plancha Abdominal', 'Core', 'Mantén el cuerpo recto en posición de flexión, apoyado en antebrazos.', NULL),
(19, 'Press de Banca con Barra', 'Pecho', 'Compuesto básico para fuerza y masa en todo el pectoral.', NULL),
(20, 'Press de Banca Inclinado', 'Pecho', 'Enfocado en la cabeza clavicular (parte superior).', NULL),
(21, 'Press de Banca Declinado', 'Pecho', 'Enfocado en la parte inferior del pectoral.', NULL),
(22, 'Press con Mancuernas Plano', 'Pecho', 'Mayor rango de movimiento y estabilización que la barra.', NULL),
(23, 'Press Inclinado con Mancuernas', 'Pecho', 'Pectoral superior con corrección de asimetrías.', NULL),
(24, 'Aperturas con Mancuernas', 'Pecho', 'Aislamiento puro, énfasis en el estiramiento.', NULL),
(25, 'Cruce de Poleas (Alto)', 'Pecho', 'Enfoque en la parte baja y corte del pecho.', NULL),
(26, 'Cruce de Poleas (Bajo)', 'Pecho', 'Enfoque en la parte superior del pecho.', NULL),
(27, 'Fondos en Paralelas (Dips)', 'Pecho', 'Excelente para la parte inferior y tríceps.', NULL),
(28, 'Flexiones (Push-ups)', 'Pecho', 'Ejercicio calisténico fundamental.', NULL),
(29, 'Flexiones Diamante', 'Pecho', 'Énfasis en la parte interna y tríceps.', NULL),
(30, 'Pec Deck (Contractora)', 'Pecho', 'Aislamiento controlado sin riesgo de caída.', NULL),
(31, 'Press en Máquina Sentado', 'Pecho', 'Trabajo seguro con carga constante.', NULL),
(32, 'Pullover con Mancuerna', 'Pecho', 'Expansión de caja torácica y trabajo de serratos.', NULL),
(33, 'Press Landmine', 'Pecho', 'Pectoral superior y deltoides, funcional.', NULL),
(34, 'Dominadas (Pull-ups)', 'Espalda', 'El mejor constructor de anchura de espalda.', NULL),
(35, 'Dominadas Supinas (Chin-ups)', 'Espalda', 'Dorsales y gran activación de bíceps.', NULL),
(36, 'Jalón al Pecho (Agarre Ancho)', 'Espalda', 'Amplitud de espalda en polea.', NULL),
(37, 'Jalón al Pecho (Agarre Estrecho)', 'Espalda', 'Enfoque en la parte baja del dorsal.', NULL),
(38, 'Remo con Barra (Bent Over Row)', 'Espalda', 'Constructor de densidad y grosor.', NULL),
(39, 'Remo con Mancuerna (Unilateral)', 'Espalda', 'Aislamiento unilateral con soporte.', NULL),
(40, 'Remo en Polea Baja (Gironda)', 'Espalda', 'Grosor de la espalda media y baja.', NULL),
(41, 'Remo en Máquina T', 'Espalda', 'Carga pesada con apoyo en el pecho.', NULL),
(42, 'Peso Muerto Convencional', 'Espalda', 'Fuerza total, erectores espinales y trapecios.', NULL),
(43, 'Rack Pulls', 'Espalda', 'Parte final del peso muerto, enfoque en trapecios.', NULL),
(44, 'Hiperextensiones', 'Espalda', 'Salud lumbar y fortalecimiento de espalda baja.', NULL),
(45, 'Pull Over en Polea Alta', 'Espalda', 'Aislamiento de dorsales con brazos rectos.', NULL),
(46, 'Face Pull', 'Espalda', 'Salud del hombro, manguito rotador y deltoides posterior.', NULL),
(47, 'Remo Pendlay', 'Espalda', 'Potencia explosiva desde el suelo.', NULL),
(48, 'Shrugs (Encogimientos) con Barra', 'Espalda', 'Aislamiento de trapecios superiores.', NULL),
(49, 'Sentadilla con Barra (Squat)', 'Pierna', 'El rey de los ejercicios de pierna.', NULL),
(50, 'Sentadilla Frontal', 'Pierna', 'Mayor énfasis en cuádriceps y core.', NULL),
(51, 'Prensa de Piernas 45º', 'Pierna', 'Carga pesada sin comprimir la columna.', NULL),
(52, 'Sentadilla Hack', 'Pierna', 'Enfoque total en cuádriceps.', NULL),
(53, 'Zancadas (Lunges)', 'Pierna', 'Unilateral para glúteo y cuádriceps.', NULL),
(54, 'Zancadas Búlgaras', 'Pierna', 'El mejor unilateral para glúteo y equilibrio.', NULL),
(55, 'Extensiones de Cuádriceps', 'Pierna', 'Aislamiento final para el vasto.', NULL),
(56, 'Peso Muerto Rumano', 'Pierna', 'Cadena posterior: isquios y glúteos.', NULL),
(57, 'Peso Muerto Sumo', 'Pierna', 'Énfasis en aductores y glúteos.', NULL),
(58, 'Curl Femoral Tumbado', 'Pierna', 'Aislamiento de isquiosurales.', NULL),
(59, 'Curl Femoral Sentado', 'Pierna', 'Mayor estiramiento del isquio.', NULL),
(60, 'Hip Thrust', 'Pierna', 'El ejercicio #1 para crecimiento de glúteos.', NULL),
(61, 'Patada de Glúteo en Polea', 'Pierna', 'Modelado y aislamiento del glúteo.', NULL),
(62, 'Elevación de Talones De Pie', 'Pierna', 'Gemelos (Gastrocnemio).', NULL),
(63, 'Elevación de Talones Sentado', 'Pierna', 'Sóleo (músculo bajo el gemelo).', NULL),
(64, 'Sentadilla Goblet', 'Pierna', 'Ideal para aprender técnica.', NULL),
(65, 'Press Militar con Barra', 'Hombro', 'Fuerza bruta y estabilidad overhead.', NULL),
(66, 'Press de Hombros con Mancuernas', 'Hombro', 'Mayor rango y simetría.', NULL),
(67, 'Press Arnold', 'Hombro', 'Recorrido completo, activa las 3 cabezas.', NULL),
(68, 'Elevaciones Laterales', 'Hombro', 'El constructor de la anchura (cabeza lateral).', NULL),
(69, 'Elevaciones Frontales', 'Hombro', 'Aislamiento de la cabeza anterior.', NULL),
(70, 'Pájaros (Reverse Flyes)', 'Hombro', 'Cabeza posterior del hombro.', NULL),
(71, 'Remo al Mentón (Upright Row)', 'Hombro', 'Trapecios y deltoides laterales.', NULL),
(72, 'Elevaciones Laterales en Polea', 'Hombro', 'Tensión constante en todo el recorrido.', NULL),
(73, 'Press Militar en Multipower', 'Hombro', 'Enfoque en la fuerza sin estabilización.', NULL),
(74, 'Curl con Barra Recta', 'Brazos', 'Carga máxima para bíceps.', NULL),
(75, 'Curl con Barra Z', 'Brazos', 'Menor estrés en muñecas.', NULL),
(76, 'Curl con Mancuernas Alterno', 'Brazos', 'Supinación para pico del bíceps.', NULL),
(77, 'Curl Martillo', 'Brazos', 'Braquial y antebrazo (anchura del brazo).', NULL),
(78, 'Curl Predicador (Banco Scott)', 'Brazos', 'Aislamiento estricto, evita trampas.', NULL),
(79, 'Curl Concentrado', 'Brazos', 'Pico del bíceps, conexión mente-músculo.', NULL),
(80, 'Press Francés', 'Brazos', 'Constructor de masa para la cabeza larga del tríceps.', NULL),
(81, 'Rompecráneos (Skullcrushers)', 'Brazos', 'Variante intensa del press francés.', NULL),
(82, 'Extensiones de Tríceps en Polea', 'Brazos', 'Bombeo y contracción final.', NULL),
(83, 'Fondos entre Bancos', 'Brazos', 'Tríceps con peso corporal.', NULL),
(84, 'Patada de Tríceps', 'Brazos', 'Contracción máxima.', NULL),
(85, 'Extensiones tras nuca (Copa)', 'Brazos', 'Estiramiento máximo del tríceps.', NULL),
(86, 'Curl de Muñeca', 'Brazos', 'Fortalecimiento de antebrazos.', NULL),
(87, 'Crunch Abdominal', 'Abdominales', 'Contracción básica del recto abdominal.', NULL),
(88, 'Elevación de Piernas Colgado', 'Abdominales', 'Zona inferior del abdomen.', NULL),
(89, 'Plancha (Plank)', 'Abdominales', 'Estabilidad y resistencia isométrica.', NULL),
(90, 'Rueda Abdominal', 'Abdominales', 'Ejercicio avanzado de extensión.', NULL),
(91, 'Russian Twist', 'Abdominales', 'Oblicuos y rotación.', NULL),
(92, 'Vacío Abdominal', 'Abdominales', 'Trabajo del transverso (cintura estrecha).', NULL),
(93, 'Woodchopper en Polea', 'Abdominales', 'Fuerza rotacional y oblicuos.', NULL),
(94, 'Mountain Climbers', 'Abdominales', 'Core dinámico y cardio.', NULL),
(95, 'Cinta de Correr (Running)', 'Cardio', 'Quema calórica alta constante.', NULL),
(96, 'Caminata Inclinada', 'Cardio', 'Quema grasa sin impacto articular.', NULL),
(97, 'Bicicleta Estática', 'Cardio', 'Cardio seguro para rodillas.', NULL),
(98, 'Elíptica', 'Cardio', 'Trabajo de cuerpo completo suave.', NULL),
(99, 'Máquina de Remo', 'Cardio', 'Resistencia muscular y cardiovascular.', NULL),
(100, 'Saltar la Comba', 'Cardio', 'Coordinación, agilidad y quema rápida.', NULL),
(101, 'Burpees', 'Cardio', 'Acondicionamiento metabólico total.', NULL),
(102, 'Kettlebell Swing', 'Cardio', 'Potencia de cadera y cardio.', NULL),
(103, 'Box Jump', 'Cardio', 'Potencia explosiva de piernas.', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `items_rutina`
--

CREATE TABLE `items_rutina` (
  `id_item_rutina` int(11) NOT NULL,
  `id_rutina` int(11) NOT NULL,
  `id_ejercicio` int(11) NOT NULL,
  `series` int(11) DEFAULT 3,
  `repeticiones` int(11) DEFAULT 10,
  `peso_kg` decimal(5,2) DEFAULT 0.00,
  `tipo_serie` varchar(50) DEFAULT 'Normal',
  `notas` text DEFAULT NULL,
  `orden` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `progreso`
--

CREATE TABLE `progreso` (
  `id_progreso` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `id_ejercicio` int(11) NOT NULL,
  `peso` decimal(6,2) NOT NULL,
  `repeticiones` int(11) NOT NULL,
  `series` int(11) NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

--
-- Volcado de datos para la tabla `progreso`
--

INSERT INTO `progreso` (`id_progreso`, `id_usuario`, `id_ejercicio`, `peso`, `repeticiones`, `series`, `fecha`) VALUES
(1, 1, 1, 60.00, 10, 3, '2025-10-01 10:00:00'),
(2, 1, 1, 62.50, 8, 3, '2025-10-08 10:00:00'),
(3, 1, 7, 80.00, 8, 4, '2025-10-03 11:00:00'),
(4, 2, 1, 0.00, 0, 0, '2025-11-15 00:00:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rutinas`
--

CREATE TABLE `rutinas` (
  `id_rutina` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `nombre_rutina` varchar(100) NOT NULL,
  `dia_semana` varchar(50) DEFAULT NULL,
  `imagen_nombre` varchar(100) DEFAULT 'icon_default.png'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuario` int(11) NOT NULL,
  `nombre_usuario` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `edad` int(11) DEFAULT 0,
  `peso_actual` decimal(5,2) DEFAULT 0.00,
  `objetivo` text DEFAULT NULL,
  `fecha_registro` timestamp NOT NULL DEFAULT current_timestamp(),
  `altura` int(11) DEFAULT 170,
  `peso_objetivo` decimal(5,2) DEFAULT 70.00,
  `genero` varchar(20) DEFAULT 'Hombre',
  `nivel_actividad` int(11) DEFAULT 1,
  `tipo_meta` int(11) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `nombre_usuario`, `email`, `contrasena`, `edad`, `peso_actual`, `objetivo`, `fecha_registro`, `altura`, `peso_objetivo`, `genero`, `nivel_actividad`, `tipo_meta`) VALUES
(1, 'TestUser', 'test@metricfit.com', '123', 25, 75.00, 'Probar la aplicación MetricFit y ganar 5kg de músculo.', '2025-11-16 00:08:42', 170, 70.00, 'Hombre', 1, 1),
(2, 'Diego', 'bardales@gmail.com', '959373020', 20, 85.00, '3262', '2025-11-16 00:21:23', 185, 90.00, 'Hombre', 2, 2);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `alimentos`
--
ALTER TABLE `alimentos`
  ADD PRIMARY KEY (`id_alimento`);

--
-- Indices de la tabla `consumo_diario`
--
ALTER TABLE `consumo_diario`
  ADD PRIMARY KEY (`id_consumo`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `id_alimento` (`id_alimento`);

--
-- Indices de la tabla `ejercicios`
--
ALTER TABLE `ejercicios`
  ADD PRIMARY KEY (`id_ejercicio`);

--
-- Indices de la tabla `items_rutina`
--
ALTER TABLE `items_rutina`
  ADD PRIMARY KEY (`id_item_rutina`),
  ADD KEY `id_rutina` (`id_rutina`),
  ADD KEY `id_ejercicio` (`id_ejercicio`);

--
-- Indices de la tabla `progreso`
--
ALTER TABLE `progreso`
  ADD PRIMARY KEY (`id_progreso`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `id_ejercicio` (`id_ejercicio`);

--
-- Indices de la tabla `rutinas`
--
ALTER TABLE `rutinas`
  ADD PRIMARY KEY (`id_rutina`),
  ADD KEY `id_usuario` (`id_usuario`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `nombre_usuario` (`nombre_usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `alimentos`
--
ALTER TABLE `alimentos`
  MODIFY `id_alimento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `consumo_diario`
--
ALTER TABLE `consumo_diario`
  MODIFY `id_consumo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `ejercicios`
--
ALTER TABLE `ejercicios`
  MODIFY `id_ejercicio` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=104;

--
-- AUTO_INCREMENT de la tabla `items_rutina`
--
ALTER TABLE `items_rutina`
  MODIFY `id_item_rutina` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `progreso`
--
ALTER TABLE `progreso`
  MODIFY `id_progreso` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `rutinas`
--
ALTER TABLE `rutinas`
  MODIFY `id_rutina` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `consumo_diario`
--
ALTER TABLE `consumo_diario`
  ADD CONSTRAINT `consumo_diario_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE,
  ADD CONSTRAINT `consumo_diario_ibfk_2` FOREIGN KEY (`id_alimento`) REFERENCES `alimentos` (`id_alimento`) ON DELETE CASCADE;

--
-- Filtros para la tabla `items_rutina`
--
ALTER TABLE `items_rutina`
  ADD CONSTRAINT `items_rutina_ibfk_1` FOREIGN KEY (`id_rutina`) REFERENCES `rutinas` (`id_rutina`) ON DELETE CASCADE,
  ADD CONSTRAINT `items_rutina_ibfk_2` FOREIGN KEY (`id_ejercicio`) REFERENCES `ejercicios` (`id_ejercicio`) ON DELETE CASCADE;

--
-- Filtros para la tabla `progreso`
--
ALTER TABLE `progreso`
  ADD CONSTRAINT `fk_progreso_ejercicio` FOREIGN KEY (`id_ejercicio`) REFERENCES `ejercicios` (`id_ejercicio`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_progreso_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `rutinas`
--
ALTER TABLE `rutinas`
  ADD CONSTRAINT `rutinas_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
