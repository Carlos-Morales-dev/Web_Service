package com.example.data.local

/**
 * Capa de localización con contenidos en español sobre Reseñas de Libros y comentarios literarios.
 *
 * La API pública JSONPlaceholder devuelve textos de relleno en pseudo-latín ("lorem ipsum").
 * Este catálogo asocia los identificadores numéricos reales con reseñas literarias de obras célebres
 * y comentarios enriquecedores dentro de la temática de análisis de libros.
 */
object ContenidoEs {

    /**
     * Reseñas de libros en español para las 20 publicaciones activas del sistema.
     * Mapeo: ID de publicación a Pair(Título de Reseña, Cuerpo de Reseña).
     */
    val publicaciones: Map<Int, Pair<String, String>> = mapOf(
        // Usuario 1
        1 to Pair(
            "Reseña: 'Cien años de soledad' de Gabriel García Márquez",
            "Una obra cumbre del realismo mágico que narra la épica historia de la familia Buendía en el pueblo ficticio de Macondo. La prosa poética de García Márquez y la riqueza de sus temas sobre el tiempo, la soledad y la memoria convierten a este libro en un clásico indispensable de la literatura universal."
        ),
        2 to Pair(
            "Reseña: 'Don Quijote de la Mancha' de Miguel de Cervantes",
            "La cúspide de la literatura en español. A través de las disparatadas aventuras del hidalgo Alonso Quijano y su fiel escudero Sancho Panza, Cervantes reflexiona con humor e ironía sobre el idealismo, la realidad, la cordura y la condición humana."
        ),
        // Usuario 2
        11 to Pair(
            "Reseña: '1984' de George Orwell",
            "Una distopía estremecedora sobre el totalitarismo, el Gran Hermano y la manipulación de la verdad. A través de la lucha interna de Winston Smith, Orwell plantea un poderoso llamado de advertencia sobre la libertad de pensamiento y la privacidad que permanece asombrosamente vigente."
        ),
        12 to Pair(
            "Reseña: 'El principito' de Antoine de Saint-Exupéry",
            "Una poética y profunda fábula sobre la amistad, el amor y el sentido de la vida. Acompañando las vivencias del pequeño príncipe en sus viajes entre asteroides, la obra nos recuerda con sencillez que solo con el corazón se puede ver bien y que lo esencial es invisible a los ojos."
        ),
        // Usuario 3
        21 to Pair(
            "Reseña: 'Rayuela' de Julio Cortázar",
            "Una contranovela revolucionaria que invita al lector a construir su propio camino de lectura. Cortázar entrelaza jazz, filosofía existencial y pasiones urbanas entre París y Buenos Aires en una búsqueda literaria deslumbrante e hipnótica."
        ),
        22 to Pair(
            "Reseña: 'Fahrenheit 451' de Ray Bradbury",
            "Una inquietante profecía sobre el peligro de la censura y el empobrecimiento intelectual. En una sociedad donde los libros están estrictamente prohibidos y los bomberos tienen la misión de quemarlos, Guy Montag inicia un camino de despertar espiritual a través de la lectura."
        ),
        // Usuario 4
        31 to Pair(
            "Reseña: 'Orgullo y prejuicio' de Jane Austen",
            "Un brillante e ingenioso retrato social de la Inglaterra rural del siglo XIX. Con diálogos punzantes y personajes memorables como Elizabeth Bennet y el señor Darcy, Austen examina con lucidez el amor verdadero, la empatía y los prejuicios sociales."
        ),
        32 to Pair(
            "Reseña: 'Crimen y castigo' de Fiódor Dostoyevski",
            "Una penetrante exploración psicológica sobre la culpa, la moral y la redención. El joven estudiante Raskólnikov comete un homicidio impulsado por teorías filosóficas, desencadenando una intensa tormenta emocional que lo llevará a confrontar su propia conciencia."
        ),
        // Usuario 5
        41 to Pair(
            "Reseña: 'El señor de los anillos' de J.R.R. Tolkien",
            "El pilar fundamental de la alta fantasía moderna. Una épica travesía de valentía, amistad incondicional y sacrificio a través de la Tierra Media para destruir el Anillo Único y derrotar a las fuerzas de la oscuridad que amenazan la paz del mundo."
        ),
        42 to Pair(
            "Reseña: 'Pedro Páramo' de Juan Rulfo",
            "Una obra maestra de la literatura mexicana que traslada al lector al pueblo enigmático de Comala. Con una estructura fragmentaria y una atmósfera poética única, Rulfo entrelaza los murmullos de los muertos con la memoria y la desolación."
        ),
        // Usuario 6
        51 to Pair(
            "Reseña: 'El hobbit' de J.R.R. Tolkien",
            "Una inolvidable novela de aventuras que narra el viaje del pacífico Bilbo Bolsón junto a una compañía de enanos para recuperar el tesoro custodiado por el temible dragón Smaug. Un relato fascinante lleno de coraje y descubrimiento personal."
        ),
        52 to Pair(
            "Reseña: 'Ensayo sobre la ceguera' de José Saramago",
            "Una desgarradora parábola sobre la condición humana. Cuando una epidemia de ceguera blanca devasta a la población, el egoísmo y la crueldad salen a la superficie, revelando también los destellos más puros de compasión y dignidad."
        ),
        // Usuario 7
        61 to Pair(
            "Reseña: 'Crónica de una muerte anunciada' de Gabriel García Márquez",
            "Una joya narrativa con ritmo de investigación periodística. Desde la primera línea se conoce el trágico destino de Santiago Nasar, y sin embargo, la reconstrucción paso a paso de las omisiones y casualidades del pueblo mantiene una tensión insuperable."
        ),
        62 to Pair(
            "Reseña: 'La metamorfosis' de Franz Kafka",
            "Un relato conmovedor y trágico sobre la alienación y la incomunicación moderna. Gregorio Samsa despierta una mañana convertido en un monstruoso insecto, enfrentando la incomprensión y el paulatino rechazo de su propia familia."
        ),
        // Usuario 8
        71 to Pair(
            "Reseña: 'Los detectives salvajes' de Roberto Bolaño",
            "Una novela coral deslumbrante que sigue las huellas de los poetas infrarrealistas Arturo Belano y Ulises Lima a lo largo de décadas. Un viaje visceral sobre la juventud, la pasión literaria y la búsqueda inalcanzable de la mítica escritora Cesárea Tinajero."
        ),
        72 to Pair(
            "Reseña: 'Ficciones' de Jorge Luis Borges",
            "Una genial colección de cuentos laberínticos donde Borges explora bibliotecas infinitas, espejos, paradojas temporales y biografías ficticias. Un libro indispensable para apreciar la literatura como el más refinado de los juegos intelectuales."
        ),
        // Usuario 9
        81 to Pair(
            "Reseña: 'El retrato de Dorian Gray' de Oscar Wilde",
            "Una fascinante y oscura alegoría sobre el hedonismo, el arte y la moralidad. Mientras el joven Dorian permanece físicamente inalterable disfrutando de todos los placeres, su retrato escondido envejece y refleja la corrupción progresiva de su alma."
        ),
        82 to Pair(
            "Reseña: 'La sombra del viento' de Carlos Ruiz Zafón",
            "Un emotivo homenaje al amor por los libros ambientado en la Barcelona de posguerra. Daniel Sempere descubre un libro maldito en el Cementerio de los Libros Olvidados, desencadenando un apasionante misterio lleno de secretos y romance."
        ),
        // Usuario 10
        91 to Pair(
            "Reseña: 'El amor en los tiempos del cólera' de Gabriel García Márquez",
            "Una inquebrantable e intensa historia de amor que se extiende por más de medio siglo. Florentino Ariza mantiene viva su devoción por Fermina Daza con una paciencia poética que desafía las convenciones sociales y el paso ineludible del tiempo."
        ),
        92 to Pair(
            "Reseña: 'Un mundo feliz' de Aldous Huxley",
            "Una profética y perturbadora visión distópica del futuro. En una sociedad donde la población es condicionada genéticamente y sedada con fármacos para garantizar una estabilidad artificial, se cuestiona el valor de la libertad individual y el dolor como parte de la vida."
        )
    )

    /**
     * Comentarios literarios en español para los 100 comentarios correspondientes a las 20 reseñas de libros.
     * Mapeo: ID de comentario a Pair(Asunto / Título de comentario, Cuerpo de comentario).
     */
    val comentarios: Map<Int, Pair<String, String>> = buildMap {
        // Post 1 (1..5) - Reseña: Cien años de soledad
        put(1, Pair("Excelente análisis literario", "Capturas magistralmente la esencia de Macondo y la complejidad del tiempo en la saga de los Buendía."))
        put(2, Pair("Coincido plenamente", "El realismo mágico de García Márquez alcanza su máxima expresión en esta obra. Un libro imborrable."))
        put(3, Pair("Consejo de lectura", "Recomiendo mucho consultar el árbol genealógico al inicio para disfrutar plenamente de la narrativa."))
        put(4, Pair("Muy bien redactado", "Una reseña sumamente amena que motiva a releer esta joya de las letras hispanoamericanas."))
        put(5, Pair("Recomendación de edición", "La edición conmemorativa de la Real Academia Española incluye análisis críticos fabulosos."))

        // Post 2 (6..10) - Reseña: Don Quijote
        put(6, Pair("Análisis muy acertado", "Cervantes logró plasmar el eterno contrapunto entre la ilusión idealista y el pragmatismo terrenal."))
        put(7, Pair("Personajes inolvidables", "La evolución de la amistad entre Don Quijote y Sancho Panza es el verdadero corazón de la novela."))
        put(8, Pair("Lectura imprescindible", "Es increíble cómo el humor e ironía de esta obra siguen vigentes después de más de cuatro siglos."))
        put(9, Pair("Riqueza del idioma", "El manejo del lenguaje castellano en el Quijote es una lección de belleza para cualquier lector."))
        put(10, Pair("Gran recomendación", "Totalmente de acuerdo en que es una obra cumbre que todo apasionado de la literatura debe recorrer."))

        // Post 11 (51..55) - Reseña: 1984
        put(51, Pair("Visión inquietante", "Orwell anticipó conceptos como la posverdad y la vigilancia masiva con una precisión escalofriante."))
        put(52, Pair("Reflexión de actualidad", "En la era digital y de las redes sociales, la lectura de 1984 se vuelve cada día más urgente."))
        put(53, Pair("El concepto de Neolengua", "La manipulación del lenguaje para limitar la capacidad de pensamiento es el tema más brillante del libro."))
        put(54, Pair("Final impactante", "El desenlace con Winston Smith deja una sensación helada que invita a la reflexión profunda."))
        put(55, Pair("Excelente reseña", "Logras resumir en pocos párrafos el impacto psicológico y político de esta distopía inolvidable."))

        // Post 12 (56..60) - Reseña: El principito
        put(56, Pair("Una joya poética", "El principito es de esos libros que cambian de significado según la etapa de la vida en que los relees."))
        put(57, Pair("Cita favorita", "'Lo esencial es invisible a los ojos' es una lección de humanidad pura que nunca se olvida."))
        put(58, Pair("Para todas las edades", "Aunque parece un cuento infantil, sus reflexiones filosóficas son un bálsamo para los adultos."))
        put(59, Pair("Hermosas ilustraciones", "Las acuarelas originales del propio autor complementan la magia del texto de manera perfecta."))
        put(60, Pair("Totalmente de acuerdo", "Una reseña cálida que transmite exactamente la sensibilidad del libro de Saint-Exupéry."))

        // Post 21 (101..105) - Reseña: Rayuela
        put(101, Pair("Juego literario deslumbrante", "El orden de lectura propuesto por Cortázar rompe con la pasividad tradicional del lector."))
        put(102, Pair("La atmósfera de París", "Los capítulos del Club de la Serpiente y el jazz en París son de una calidad lírica insuperable."))
        put(103, Pair("Horacio y la Maga", "La dinámica entre Horacio Oliveira y la Maga es una de las construcciones románticas más fascinantes."))
        put(104, Pair("Desafío reconfortante", "Al principio cuesta acostumbrarse al ritmo, pero una vez que entras en el juego es imposible salir."))
        put(105, Pair("Gran aporte literario", "Gracias por hacer una reseña tan lúcida de una novela tan rica y compleja como Rayuela."))

        // Post 22 (106..110) - Reseña: Fahrenheit 451
        put(106, Pair("Amor por los libros", "Fahrenheit 451 es una carta de amor a la literatura y al valor del pensamiento independiente."))
        put(107, Pair("Los hombres-libro", "La idea de memorizar libros para salvarlos de la destrucción es uno de los pasajes más conmovedores."))
        put(108, Pair("Crítica al entretenimiento", "Bradbury no solo advirtió sobre la censura, sino sobre la adicción a las pantallas vacías."))
        put(109, Pair("Personaje fascinante", "Clarisse McClellan actúa como el catalizador perfecto para despertar la conciencia de Montag."))
        put(110, Pair("Reseña muy recomendada", "Una reseña excelente para poner en valor la defensa de la cultura y la lectura libre."))

        // Post 31 (151..155) - Reseña: Orgullo y prejuicio
        put(151, Pair("Diálogos brillantes", "La ironía sutil de Jane Austen y la firmeza de Elizabeth Bennet hacen de esta novela una delicia."))
        put(152, Pair("Evolución de los personajes", "Ver cómo Elizabeth y Darcy superan sus propios defectos morales es sumamente gratificante."))
        put(153, Pair("Retrato de época", "Austen critica con elegancia las presiones sociales y económicas que sufrían las mujeres de la época."))
        put(154, Pair("Un clásico del romance", "Demuestra que el verdadero amor requiere autoconocimiento, respeto mutuo y humildad."))
        put(155, Pair("Excelente síntesis", "Muy buena reseña que destaca el valor literario más allá del cliché romántico popular."))

        // Post 32 (156..160) - Reseña: Crimen y castigo
        put(156, Pair("Tensión psicológica pura", "Dostoyevski logra que el lector sienta en carne propia el tormento y la fiebre de Raskólnikov."))
        put(157, Pair("Sonia Semiónovna", "El personaje de Sonia representa la luz de la compasión humana en medio de la oscuridad moral."))
        put(158, Pair("Debate filosófico", "El dilema de si existen hombres 'superiores' por encima de la ley moral está magistralmente abordado."))
        put(159, Pair("Monumental obra rusa", "La profundidad de la literatura rusa decimonónica no tiene comparación. Excelente elección."))
        put(160, Pair("Imprescindible reseña", "Refleja de manera impecable la lucha interna entre el remordimiento y la redención."))

        // Post 41 (201..205) - Reseña: El señor de los anillos
        put(201, Pair("Construcción de mundo única", "Tolkien no solo escribió una historia, creó lenguas, mitologías y una geografía inolvidable."))
        put(202, Pair("La lealtad de Sam", "Samwise Gamyi es el verdadero héroe de la historia. Su lealtad hacia Frodo es conmovedora."))
        put(203, Pair("Valores universales", "La valentía de los pequeños frente a la abrumadora oscuridad transmite un mensaje de esperanza puro."))
        put(204, Pair("Cine y literatura", "Aunque las películas de Jackson son geniales, la riqueza poética de los libros es insustituible."))
        put(205, Pair("Magistral reseña", "Capturas el tono épico y la nobleza de espíritu que caracteriza la obra de Tolkien."))

        // Post 42 (206..210) - Reseña: Pedro Páramo
        put(206, Pair("Atmósfera poética y espectral", "Rulfo logró en menos de cien páginas lo que a otros autores les toma tomos enteros."))
        put(207, Pair("El pueblo de Comala", "Las voces de los muertos susurrando entre las grietas del pueblo crean un ambiente inolvidable."))
        put(208, Pair("Estructura innovadora", "El manejo del tiempo no lineal inspiró directamente a la generación del Boom Latinoamericano."))
        put(209, Pair("Orgullo mexicano", "Una de las obras más bellas y tristes de la literatura hispana. Excelente reseña."))
        put(210, Pair("Impresionante síntesis", "Logras transmitir el misterio y la desolación de Comala con gran soltura."))

        // Post 51 (251..255) - Reseña: El hobbit
        put(251, Pair("Una aventura entrañable", "El crecimiento personal de Bilbo desde su cómoda estancia en Bolsón Cerrado es inspirador."))
        put(252, Pair("El acertijo en la oscuridad", "El encuentro entre Bilbo y Gollum por el Anillo es una de las escenas más tensas de la fantasía."))
        put(253, Pair("Lectura reconfortante", "Ideal tanto para jóvenes que se inician en la fantasía como para adultos nostálgicos."))
        put(254, Pair("Riqueza narrativa", "Tolkien demuestra aquí un tono narrativo ágil, cálido y lleno de detalles maravillosos."))
        put(255, Pair("Muy recomendada", "Reseña impecable que dan ganas de adentrarse de nuevo en los caminos de la Comarca."))

        // Post 52 (256..260) - Reseña: Ensayo sobre la ceguera
        put(256, Pair("Impacto emocional profundo", "Saramago nos confronta con la fragilidad de las instituciones cuando la sociedad colapsa."))
        put(257, Pair("La mujer del médico", "El personaje de la mujer del médico es un faro de lucidez, coraje y dignidad incuestionable."))
        put(258, Pair("Estilo narrativo único", "La puntuación singular de Saramago sumerge al lector en un flujo continuo de conciencia."))
        put(259, Pair("Reflexión moral", "Una lectura que invita a cuestionarnos qué tan ciegos estamos ante la necesidad del prójimo."))
        put(260, Pair("Excelente reseña literaria", "Pones el foco en los dilemas éticos que hacen de esta novela un clásico contemporáneo."))

        // Post 61 (301..305) - Reseña: Crónica de una muerte anunciada
        put(301, Pair("Tensión narrativa perfecta", "Saber el trágico final desde la primera frase no le resta ni un ápice de emoción al relato."))
        put(302, Pair("La culpa colectiva", "García Márquez expone brillantemente cómo el honor desmedido y la cobardía traman la tragedia."))
        put(303, Pair("Estilo periodístico", "La combinación de rigor de informe con la magia del lenguaje convierte a este libro en una joya."))
        put(304, Pair("Lectura ágil", "Se lee de una sola sentada debido a la maestría con la que están entrelazados los testimonios."))
        put(305, Pair("Gran reseña", "Resalta muy bien la fatalidad del destino y la pasividad colectiva como ejes de la obra."))

        // Post 62 (306..310) - Reseña: La metamorfosis
        put(306, Pair("Alegoría de la soledad", "La transformación física de Gregorio es el reflejo exterior de su alienación laboral y familiar."))
        put(307, Pair("La reacción de la familia", "El rechazo paulatino de sus seres queridos es el aspecto más doloroso y real del relato."))
        put(308, Pair("Kafka en estado puro", "Muestra la angustia del individuo atrapado en una existencia absurda y sin salida."))
        put(309, Pair("Impacto duradero", "Es una historia corta pero que deja una huella profunda en la mente de quien la lee."))
        put(310, Pair("Muy acertado", "Tu reseña capta con precisión la atmósfera claustrofóbica y trágica del universo kafkiano."))

        // Post 71 (351..355) - Reseña: Los detectives salvajes
        put(351, Pair("La búsqueda de la poesía", "Bolaño transmite una pasión visceral por la literatura que contagia desde las primeras páginas."))
        put(352, Pair("Estructura polifónica", "La segunda parte estructurada a base de múltiples testimonios en todo el mundo es genial."))
        put(353, Pair("Nostalgia e infrarrealismo", "Un retrato épico y melancólico sobre la juventud rebelde y los sueños literarios en Latinoamérica."))
        put(354, Pair("Belano y Lima", "Dos personajes icónicos que encarnan el mito del poeta errante y sin concesiones."))
        put(355, Pair("Brillante recomendación", "Una reseña sumamente completa que hace honor a la potencia narrativa de Roberto Bolaño."))

        // Post 72 (356..360) - Reseña: Ficciones
        put(356, Pair("Arquitectura de las ideas", "Borges transforma los conceptos de infinito, espejos y laberintos en literatura pura."))
        put(357, Pair("La biblioteca de Babel", "El cuento de la biblioteca infinita es una de las metáforas más deslumbrantes jamás escritas."))
        put(358, Pair("Prosa impecable", "No sobra ni una sola palabra en los relatos borgeanos; su precisión léxica es envidiable."))
        put(359, Pair("Desafío intelectual", "Cada cuento es una invitación a la reflexión filosófica y al placer del juego mental."))
        put(360, Pair("Excelente análisis", "Tu reseña destaca la genialidad de Borges para condensar universos enteros en pocas páginas."))

        // Post 81 (401..405) - Reseña: El retrato de Dorian Gray
        put(401, Pair("Aforismos memorables", "La pluma afilada e ingeniosa de Oscar Wilde brilla en cada página con frases inolvidables."))
        put(402, Pair("La tragedia del hedonismo", "Dorian representa la búsqueda del placer desprovisto de responsabilidad moral o empatía."))
        put(403, Pair("Lord Henry Wotton", "El personaje de Lord Henry actúa como un mefistófeles moderno cuyos discursos resultan seductores."))
        put(404, Pair("El espejo del alma", "La idea de que el cuadro cargue con la fealdad de los pecados mientras el rostro permanece joven es genial."))
        put(405, Pair("Muy buena reseña", "Expone con claridad los dilemas estéticos y morales que hacen inmortal a esta novela."))

        // Post 82 (406..410) - Reseña: La sombra del viento
        put(406, Pair("Magia barcelonesa", "Ruiz Zafón logra que las calles de Barcelona se sientan vivas y cargadas de misterio gótico."))
        put(407, Pair("El Cementerio de los Libros", "El concepto del Cementerio de los Libros Olvidados es el sueño hecho realidad de todo bibliófilo."))
        put(408, Pair("Fermín Romero de Torres", "Fermín es un personaje inolvidable que aporta humor, lealtad y sabiduría popular a la trama."))
        put(409, Pair("Trama adictiva", "Un libro que atrapa desde el primer capítulo y no permite soltar la lectura hasta el final."))
        put(410, Pair("Excelente sugerencia", "Tu reseña captura la emoción y el misterio que envuelven a la historia de Daniel Sempere."))

        // Post 91 (451..455) - Reseña: El amor en los tiempos del cólera
        put(451, Pair("Oda a la paciencia", "Florentino Ariza encarna la devoción romántica más persistente de la historia de la literatura."))
        put(452, Pair("El amor en la vejez", "García Márquez dignifica la pasión y la ternura en los años maduros con una sensibilidad única."))
        put(453, Pair("Atmósfera caribeña", "Los olores, colores y calores del trópico están retratados con la maestría habitual de Gabo."))
        put(454, Pair("Fermina Daza", "Un personaje femenino fuerte, complejo y lleno de carácter que no se deja doblegar."))
        put(455, Pair("Gran comentario", "Una reseña emocionante sobre una de las mejores novelas de amor escritas en nuestro idioma."))

        // Post 92 (456..460) - Reseña: Un mundo feliz
        put(456, Pair("Profecía distópica", "Huxley planteó un futuro donde el control no viene del castigo, sino del placer y el confort anestésico."))
        put(457, Pair("El Soma y el confort", "La reflexión sobre preferir una verdad dolorosa a una felicidad sumisa sigue siendo impactante."))
        put(458, Pair("John el Salvaje", "El choque entre la literatura clásica de Shakespeare y el mundo tecnificado es sublime."))
        put(459, Pair("Debate ético", "Plantea si el progreso tecnológico sin ética nos arrebata lo más noble de nuestra condición humana."))
        put(460, Pair("Imprescindible reseña", "Un análisis lúcido sobre la vigencia de Un mundo feliz en la sociedad del espectáculo actual."))
    }
}
