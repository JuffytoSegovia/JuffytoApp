package com.juffyto.juffyto.ui.screens.enp.model

data class EnpQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val solutionSteps: List<SolutionStep>,
    val property: String? = null,
    val hasFormula: Boolean = false,
    val svgDiagram: String? = null  // Nuevo campo para SVG
)

data class SolutionStep(
    val stepNumber: Int,
    val description: String,
    val formulas: List<String> = emptyList(),
    val isHighlighted: Boolean = false
)

object EnpQuestions {
    val questions = listOf(
        // Pregunta 1 - Potenciación
        EnpQuestion(
            id = 1,
            question = "En una empresa se fabrican pernos, los cuales se colocan en bolsas. " +
                    "En cada bolsa se coloca 2⁵ × 3³ pernos. Además, estas bolsas se colocan en paquetes " +
                    "de 2⁴ × 3 bolsas. Para realizar un envío a Arequipa se preparan 2 cajas que contienen " +
                    "2² × 3² paquetes. ¿Cuántos pernos se enviarán a Arequipa?",
            options = listOf(
                "2¹¹ × 3⁶ pernos",
                "2¹¹ × 3⁵ pernos",
                "2¹² × 3⁶ pernos",
                "2² × 3¹⁶ pernos"
            ),
            correctOptionIndex = 2,
            property = "Multiplicación de potencias de igual base: aᵐ × aⁿ = aᵐ⁺ⁿ",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Identifiquemos los datos:",
                    listOf(
                        "Pernos por bolsa = 2⁵ × 3³",
                        "Bolsas por paquete = 2⁴ × 3",
                        "Paquetes por caja = 2² × 3²",
                        "Número de cajas = 2 = 2¹"
                    )
                ),
                SolutionStep(
                    2,
                    "Multiplicamos todo:",
                    listOf("Total = (2⁵ × 3³) × (2⁴ × 3) × (2² × 3²) × 2¹")
                ),
                SolutionStep(
                    3,
                    "Agrupamos las potencias de 2:",
                    listOf("2⁵ × 2⁴ × 2² × 2¹ = 2⁽⁵⁺⁴⁺²⁺¹⁾ = 2¹²")
                ),
                SolutionStep(
                    4,
                    "Agrupamos las potencias de 3:",
                    listOf("3³ × 3¹ × 3² = 3⁽³⁺¹⁺²⁾ = 3⁶")
                ),
                SolutionStep(
                    5,
                    "Por lo tanto:",
                    listOf("Total = 2¹² × 3⁶ pernos"),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 2 - Interés Simple
        EnpQuestion(
            id = 2,
            question = "María prestó S/ 500 a su amigo Juan para invertir en un negocio con una " +
                    "tasa de interés simple del 40% anual y un plazo de 6 meses. ¿Cuál es el monto que " +
                    "devolverá Juan a María al término del plazo establecido para su préstamo?",
            options = listOf(
                "S/ 600.00",
                "S/ 620.00",
                "S/ 740.00",
                "S/ 524.00"
            ),
            correctOptionIndex = 0,
            property = "Fórmula de Interés Simple: I = C × r × t",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Identifiquemos los datos:",
                    listOf(
                        "Capital (C) = S/ 500",
                        "Tasa anual (r) = 40% = 0.40",
                        "Tiempo (t) = 6 meses = 6/12 = 0.5 años"
                    )
                ),
                SolutionStep(
                    2,
                    "Calculamos el interés usando la fórmula I = C × r × t:",
                    listOf(
                        "I = 500 × 0.40 × 0.5",
                        "I = 100"
                    )
                ),
                SolutionStep(
                    3,
                    "Calculamos el monto total a devolver (Capital + Interés):",
                    listOf(
                        "Monto = 500 + 100",
                        "Monto = S/ 600.00"
                    )
                ),
                SolutionStep(
                    4,
                    "Por lo tanto:",
                    listOf("Juan devolverá S/ 600.00 a María"),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 3 - Crecimiento de árboles
        EnpQuestion(
            id = 3,
            question = "Sandra es bióloga y se dedica a estudiar el crecimiento de los árboles. " +
                    "Según su investigación si un árbol crece menos de 15 cm en un año requiere fertilización. " +
                    "Hace un año, uno de los árboles medía 6 metros, desde entonces, el árbol ha crecido 12 cm " +
                    "respecto al año anterior. ¿Qué es lo que reportará Sandra sobre este árbol?",
            options = listOf(
                "El árbol no requiere fertilización porque ahora mide 6,12 m",
                "El árbol requiere fertilización porque mide menos de 6,15 m",
                "El árbol no requiere fertilización porque ha crecido 12 cm",
                "El árbol requiere fertilización porque ahora mide más de 6,15 m"
            ),
            correctOptionIndex = 1,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Analicemos los datos:",
                    listOf(
                        "Medida inicial: 6 metros",
                        "Crecimiento: 12 cm",
                        "Criterio de fertilización: < 15 cm de crecimiento anual"
                    )
                ),
                SolutionStep(
                    2,
                    "Comparamos el crecimiento con el criterio:",
                    listOf(
                        "Crecimiento real: 12 cm",
                        "Crecimiento mínimo requerido: 15 cm",
                        "12 cm < 15 cm"
                    )
                ),
                SolutionStep(
                    3,
                    "Por lo tanto:",
                    listOf(
                        "Como el árbol creció menos de 15 cm (solo 12 cm),",
                        "El árbol SÍ requiere fertilización"
                    ),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 4 - Aproximación decimal
        EnpQuestion(
            id = 4,
            question = "Se sabe que algunas raíces cuadradas tienen infinitas cifras decimales, " +
                    "por ejemplo: √5 = 2,2360679774... ¿Cuál es el valor aproximado a las centésimas de √5?",
            options = listOf(
                "2,2",
                "2,24",
                "2,23",
                "2,236"
            ),
            correctOptionIndex = 1, // Cambiado a 1 (opción b)
            property = "Aproximación decimal a las centésimas (2 decimales)",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Entendamos qué significa aproximar a las centésimas:",
                    listOf(
                        "Centésimas = 2 decimales",
                        "√5 = 2,2360679774..."
                    )
                ),
                SolutionStep(
                    2,
                    "Observamos el tercer decimal para redondear:",
                    listOf(
                        "2,236... → El tercer decimal es 6",
                        "Como 6 ≥ 5, redondeamos el segundo decimal hacia arriba"
                    )
                ),
                SolutionStep(
                    3,
                    "Por lo tanto:",
                    listOf("√5 ≈ 2,24 (aproximado a las centésimas)"),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 5 - Notación científica
        EnpQuestion(
            id = 5,
            question = "La masa del átomo de Hierro es 166 x 10⁻²⁹ Kg. ¿Cuál es la masa de este átomo " +
                    "expresada en notación científica?",
            options = listOf(
                "1,66 x 10⁻²⁷ kg",
                "0,166 x 10⁻²⁸ kg",
                "1,66 x 10⁻²⁹ kg",
                "16,6 x 10⁻²⁰ kg"
            ),
            correctOptionIndex = 0,
            property = "Reglas de notación científica: Un número entre 1 y 10 multiplicado por una potencia de 10",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Recordemos las reglas de notación científica:",
                    listOf(
                        "Debe ser un número entre 1 y 10 × 10ⁿ",
                        "Dato inicial: 166 × 10⁻²⁹ kg"
                    )
                ),
                SolutionStep(
                    2,
                    "Convertimos 166 a un número entre 1 y 10:",
                    listOf(
                        "166 = 1,66 × 10²",
                        "166 × 10⁻²⁹ = 1,66 × 10² × 10⁻²⁹"
                    )
                ),
                SolutionStep(
                    3,
                    "Aplicamos la propiedad de multiplicación de potencias:",
                    listOf(
                        "1,66 × 10² × 10⁻²⁹ = 1,66 × 10⁽²⁻²⁹⁾",
                        "1,66 × 10⁻²⁷"
                    ),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 6 - Razones y proporciones
        EnpQuestion(
            id = 6,
            question = """Para los invitados de una fiesta, se sirvieron agua y limonada en vasos.

            Agua:     🥛 🥛 🥛
            Limonada: 🥤 🥤 🥤 🥤 🥤
        
            Según esta situación, ¿cuál de las siguientes afirmaciones es correcta?""",
            options = listOf(
                "La cantidad de vasos de agua es 3/8 de la cantidad de vasos de limonada",
                "La cantidad de vasos de agua es 5/3 de la cantidad de vasos de limonada",
                "La cantidad de vasos de agua es 8/3 de la cantidad de vasos de limonada",
                "La cantidad de vasos de agua es 3/5 de la cantidad de vasos de limonada"
            ),
            correctOptionIndex = 3,
            property = "Razón entre dos cantidades",
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Identifiquemos los datos:",
                    listOf(
                        "Vasos de agua = 3",
                        "Vasos de limonada = 5"
                    )
                ),
                SolutionStep(
                    2,
                    "Para hallar la razón entre agua y limonada:",
                    listOf(
                        "Razón = Vasos de agua / Vasos de limonada",
                        "Razón = 3/5"
                    )
                ),
                SolutionStep(
                    3,
                    "Por lo tanto:",
                    listOf("La cantidad de vasos de agua es 3/5 de la cantidad de vasos de limonada"),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 7 - Intervalos
        EnpQuestion(
            id = 7,
            question = "Sean los intervalos A= [-2; 2[ y B= ]-1; +∞[,\n" +
                    "¿Cuál es la representación del conjunto (A - B) U (B - A)?",
            options = listOf(
                "[-2; -1] U [2; +∞[",
                "]-2; -1[ U [2; +∞[",
                "[-2; -1] U ]2; +∞[",
                "]-2; -1[ U ]2; +∞["
            ),
            correctOptionIndex = 0,
            property = "Operaciones con conjuntos en intervalos:\n" +
                    "A - B: Elementos que están en A pero no en B\n" +
                    "B - A: Elementos que están en B pero no en A\n" +
                    "U: Unión de conjuntos",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Analicemos los intervalos:",
                    listOf(
                        "A = [-2; 2[ → números desde -2 (incluido) hasta 2 (no incluido)",
                        "B = ]-1; +∞[ → números mayores que -1 (no incluido) hasta infinito"
                    )
                ),
                SolutionStep(
                    2,
                    "Hallamos A - B (elementos en A que no están en B):",
                    listOf(
                        "• Los números de A que son menores o iguales que -1",
                        "• Esto es: desde -2 (incluido) hasta -1 (incluido)",
                        "• Por lo tanto: A - B = [-2; -1]"
                    )
                ),
                SolutionStep(
                    3,
                    "Hallamos B - A (elementos en B que no están en A):",
                    listOf(
                        "• Los números de B que son mayores o iguales que 2",
                        "• Esto es: desde 2 (incluido) hasta infinito",
                        "• Por lo tanto: B - A = [2; +∞["
                    )
                ),
                SolutionStep(
                    4,
                    "Unimos los resultados:",
                    listOf("(A - B) U (B - A) = [-2; -1] U [2; +∞["),
                    isHighlighted = true
                )
            ),
            svgDiagram = """
                <svg viewBox="0 0 500 250" xmlns="http://www.w3.org/2000/svg" style="width: 90%; height: auto; margin-left: 16px;">
                    <!-- Línea base -->
                    <line x1="50" y1="125" x2="450" y2="125" stroke="black" stroke-width="2"/>
                    
                    <!-- Marcadores -->
                    <line x1="100" y1="120" x2="100" y2="130" stroke="black" stroke-width="2"/>
                    <line x1="200" y1="120" x2="200" y2="130" stroke="black" stroke-width="2"/>
                    <line x1="300" y1="120" x2="300" y2="130" stroke="black" stroke-width="2"/>
                    <line x1="400" y1="120" x2="400" y2="130" stroke="black" stroke-width="2"/>
                    
                    <!-- Números -->
                    <text x="95" y="150" text-anchor="middle" font-size="14">-2</text>
                    <text x="195" y="150" text-anchor="middle" font-size="14">-1</text>
                    <text x="300" y="150" text-anchor="middle" font-size="14">0</text>
                    <text x="400" y="150" text-anchor="middle" font-size="14">2</text>
                    <text x="450" y="150" text-anchor="middle" font-size="14">∞</text>
        
                    <!-- Intervalo A -->
                    <line x1="100" y1="75" x2="400" y2="75" stroke="blue" stroke-width="3"/>
                    <circle cx="100" cy="75" r="4" fill="blue"/>
                    <circle cx="400" cy="75" r="4" fill="white" stroke="blue" stroke-width="2"/>
                    <text x="50" y="80" text-anchor="end" fill="blue" font-size="14">A</text>
        
                    <!-- Intervalo B -->
                    <line x1="200" y1="100" x2="450" y2="100" stroke="red" stroke-width="3"/>
                    <circle cx="200" cy="100" r="4" fill="white" stroke="red" stroke-width="2"/>
                    <text x="50" y="105" text-anchor="end" fill="red" font-size="14">B</text>
        
                    <!-- Resultado -->
                    <line x1="100" y1="175" x2="200" y2="175" stroke="green" stroke-width="3"/>
                    <line x1="400" y1="175" x2="450" y2="175" stroke="green" stroke-width="3"/>
                    <circle cx="100" cy="175" r="4" fill="green"/>
                    <circle cx="200" cy="175" r="4" fill="green"/>
                    <circle cx="400" cy="175" r="4" fill="green"/>
                    <text x="80" y="180" text-anchor="end" fill="green" font-size="14">Resultado</text>
                </svg>
            """.trimIndent()
        ),

        // Pregunta 8 - Relación lineal
        EnpQuestion(
            id = 8,
            question = """Carlos hace compras de casacas al por mayor de un mayorista de Puno, el mayorista vende a 45 la unidad y el costo por envío total al por mayor es de 35 soles. Si x representa la cantidad total de casacas e Y el precio total. ¿En qué relación se encuentra x e Y?""",
            options = listOf(
                "Y = 35x + 45",
                "Y = 45x + 35",
                "Y = 45x",
                "Y = 35x"
            ),
            correctOptionIndex = 1,  // opción b
            property = "Relación lineal: Y = mx + b, donde:\n" +
                    "• m = costo por unidad (pendiente)\n" +
                    "• b = costo fijo (término independiente)",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Identificamos los datos del problema:",
                    listOf(
                        "• Precio por casaca = 45 soles/unidad",
                        "• Costo de envío fijo = 35 soles",
                        "• x = cantidad total de casacas",
                        "• Y = precio total a pagar"
                    )
                ),
                SolutionStep(
                    2,
                    "Formamos la ecuación:",
                    listOf(
                        "• Y = (precio por casaca × cantidad) + (costo de envío)",
                        "• Y = 45x + 35"
                    )
                ),
                SolutionStep(
                    3,
                    "Verificamos la lógica:",
                    listOf(
                        "• El precio unitario (45) multiplica a la cantidad (x)",
                        "• El costo de envío (35) es fijo, no depende de x",
                        "• Es una función lineal donde:",
                        "  - 45x representa el costo variable",
                        "  - 35 representa el costo fijo"
                    )
                ),
                SolutionStep(
                    4,
                    "Por lo tanto:",
                    listOf("La relación correcta es Y = 45x + 35"),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 9 - MOVIMIENTO PARABÓLICO - Altura máxima
        EnpQuestion(
            id = 9,
            question = """Luis es un deportista y lanza un balón en forma de parábola, el tiempo máximo que alcanza el balón es de 7 minutos y la altura máxima es 9 metros.
                        
            ¿Cuál de las afirmaciones es correcta?""",
            options = listOf(
                "La distancia máxima es de 7 metros",
                "El balón alcanza dos veces 8 metros",
                "El balón alcanza una vez 5 metros",
                "El tiempo es de 6 minutos"
            ),
            correctOptionIndex = 1, // opción b
            property = "Propiedades del movimiento parabólico:\n" +
                    "• Es simétrico respecto al eje vertical\n" +
                    "• La misma altura se alcanza dos veces (subida y bajada)\n" +
                    "• La altura máxima se alcanza una sola vez",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Analizamos los datos:",
                    listOf(
                        "• Altura máxima = 9 metros",
                        "• Tiempo máximo = 7 minutos",
                        "• Trayectoria parabólica"
                    )
                ),
                SolutionStep(
                    2,
                    "Analizamos cada altura:",
                    listOf(
                        "• 9 metros es la altura máxima (se alcanza una vez)",
                        "• 8 metros está por debajo del máximo",
                        "• Si alcanza 8m subiendo, también lo alcanza bajando",
                        "• Por lo tanto, alcanza 8m dos veces"
                    )
                ),
                SolutionStep(
                    3,
                    "Verificamos las otras opciones:",
                    listOf(
                        "a) La distancia no se puede determinar con los datos dados",
                        "c) No podemos asegurar que alcance 5m solo una vez",
                        "d) El tiempo dado es 7 minutos, no 6"
                    )
                ),
                SolutionStep(
                    4,
                    "Por lo tanto:",
                    listOf("La única afirmación correcta es que el balón alcanza 8 metros dos veces"),
                    isHighlighted = true
                )
            ),
            svgDiagram = """
               <svg viewBox="0 0 40 30" xmlns="http://www.w3.org/2000/svg">
                   <g transform="translate(5,2)">
                       <!-- Eje Y -->
                       <line x1="5" y1="25" x2="5" y2="0" stroke="black" stroke-width="0.5"/>
                       <!-- Eje X -->
                       <line x1="0" y1="25" x2="30" y2="25" stroke="black" stroke-width="0.5"/>
                       
                       <!-- Parábola -->
                       <path d="M 5,25 Q 17.5,5 30,25" fill="none" stroke="blue" stroke-width="1"/>
                       
                       <!-- Altura máxima -->
                       <line x1="17.5" y1="25" x2="17.5" y2="7" stroke="red" stroke-dasharray="1,1" stroke-width="0.5"/>
                       <text x="18.5" y="13" font-size="2.5">9m</text>
                       
                       <!-- Altura 8m -->
                       <line x1="12" y1="25" x2="12" y2="9" stroke="green" stroke-dasharray="1,1" stroke-width="0.5"/>
                       <line x1="23" y1="25" x2="23" y2="9" stroke="green" stroke-dasharray="1,1" stroke-width="0.5"/>
                       <text x="13" y="14" font-size="2">8m</text>
                   </g>
               </svg>
           """.trimIndent()
        ),

        /// Pregunta 10 - SISTEMA DE ECUACIONES - Ventas y cantidades
        EnpQuestion(
            id = 10,
            question = "Carlos y Pedro son dos hermanos que venden chocolates, Carlos lo vende 2 soles y Pedro a 3 soles. Si en total venden 55 soles y Carlos vende 10 chocolates más que Pedro, ¿Cuánto vendió Pedro?",
            options = listOf(
                "21 soles",
                "31 soles",
                "11 soles",
                "33 soles"
            ),
            correctOptionIndex = 0,  // opción a
            property = "Sistema de ecuaciones:\n" +
                    "• Ecuación del dinero total\n" +
                    "• Ecuación de diferencia de cantidades",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Definimos las variables:",
                    listOf(
                        "• x = número de chocolates que vende Pedro",
                        "• x + 10 = número de chocolates que vende Carlos",
                        "• Precio de Carlos = 2 soles",
                        "• Precio de Pedro = 3 soles"
                    )
                ),
                SolutionStep(
                    2,
                    "Planteamos la ecuación del dinero total:",
                    listOf(
                        "• Dinero de Pedro = 3x",
                        "• Dinero de Carlos = 2(x + 10)",
                        "• Total = 55 soles",
                        "• 3x + 2(x + 10) = 55"
                    )
                ),
                SolutionStep(
                    3,
                    "Resolvemos la ecuación:",
                    listOf(
                        "3x + 2x + 20 = 55",
                        "5x + 20 = 55",
                        "5x = 35",
                        "x = 7 chocolates"
                    )
                ),
                SolutionStep(
                    4,
                    "Calculamos cuánto vendió Pedro:",
                    listOf(
                        "Venta de Pedro = cantidad × precio",
                        "Venta de Pedro = 7 × 3 = 21 soles"
                    ),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 11 - SISTEMA DE ECUACIONES - Cantidad de billetes
        EnpQuestion(
            id = 11,
            question = """Se tiene el siguiente sistema de ecuaciones, siendo "x" la cantidad de billetes de 10 e "y" la cantidad de billetes de 20, encuentre la cantidad total de billetes.
           {10x + 20y = 990, 
            y - x = 15}""",
            options = listOf(
                "59",
                "38",
                "61",
                "71"
            ),
            correctOptionIndex = 2, // opción c
            property = "Sistema de ecuaciones lineales 2×2:\n" +
                    "• Método de sustitución\n" +
                    "• Total de billetes = x + y",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Sustituimos la segunda ecuación en la primera:",
                    listOf(
                        "• De y - x = 15, despejamos: y = x + 15",
                        "• Reemplazamos en 10x + 20y = 990:",
                        "• 10x + 20(x + 15) = 990"
                    )
                ),
                SolutionStep(
                    2,
                    "Resolvemos la ecuación:",
                    listOf(
                        "10x + 20x + 300 = 990",
                        "30x + 300 = 990",
                        "30x = 690",
                        "x = 23"
                    )
                ),
                SolutionStep(
                    3,
                    "Hallamos y:",
                    listOf(
                        "y = x + 15",
                        "y = 23 + 15",
                        "y = 38"
                    )
                ),
                SolutionStep(
                    4,
                    "Calculamos el total de billetes:",
                    listOf(
                        "Total = x + y",
                        "Total = 23 + 38 = 61 billetes"
                    ),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 12 - Progresión aritmética
        EnpQuestion(
            id = 12,
            question = "Calcule la suma de los 9 primeros términos de la sucesión: 5, 8, 11, 14,...",
            options = listOf(
                "29",
                "135",
                "127",
                "153"
            ),
            correctOptionIndex = 1,
            property = "Suma de términos de una progresión aritmética: Sn = n(a₁ + aₙ)/2, donde:\n" +
                    "aₙ = a₁ + (n-1)d",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Identificamos los elementos:",
                    listOf(
                        "• Primer término (a₁) = 5",
                        "• Diferencia común (d) = 8 - 5 = 3",
                        "• Número de términos (n) = 9"
                    )
                ),
                SolutionStep(
                    2,
                    "Calculamos el último término (a₉):",
                    listOf(
                        "aₙ = a₁ + (n-1)d",
                        "a₉ = 5 + (9-1)3",
                        "a₉ = 5 + (8)3",
                        "a₉ = 5 + 24 = 29"
                    )
                ),
                SolutionStep(
                    3,
                    "Aplicamos la fórmula de suma:",
                    listOf(
                        "Sₙ = n(a₁ + aₙ)/2",
                        "S₉ = 9(5 + 29)/2",
                        "S₉ = 9(34)/2",
                        "S₉ = 306/2 = 135"
                    )
                ),
                SolutionStep(
                    4,
                    "Por lo tanto:",
                    listOf("La suma de los 9 primeros términos es 135"),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 13 - Ecuación cuadrática
        EnpQuestion(
            id = 13,
            question = "(x + 2)(x + 3) = 12, indicar el conjunto en donde se encuentran las raíces.",
            options = listOf(
                "x = {10; 9}",
                "x = {-6; -4}",
                "x = {-6; -1; 2}",
                "x = {-6; 1; 3}"
            ),
            correctOptionIndex = 3,
            property = "Resolución de ecuaciones cuadráticas por factorización:\n" +
                    "(x + a)(x + b) = k → x² + (a+b)x + ab - k = 0",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Desarrollamos el lado izquierdo:",
                    listOf(
                        "(x + 2)(x + 3) = x² + 3x + 2x + 6",
                        "x² + 5x + 6 = 12"
                    )
                ),
                SolutionStep(
                    2,
                    "Igualamos a cero:",
                    listOf(
                        "x² + 5x + 6 - 12 = 0",
                        "x² + 5x - 6 = 0"
                    )
                ),
                SolutionStep(
                    3,
                    "Factorizamos:",
                    listOf(
                        "x² + 5x - 6 = 0",
                        "(x + 6)(x - 1) = 0"
                    )
                ),
                SolutionStep(
                    4,
                    "Aplicamos propiedad del producto igual a cero:",
                    listOf(
                        "x + 6 = 0  o  x - 1 = 0",
                        "x = -6     o  x = 1"
                    )
                ),
                SolutionStep(
                    5,
                    "Comprobamos en la ecuación original:",
                    listOf(
                        "Si x = -6: (-6 + 2)(-6 + 3) = (-4)(-3) = 12 ✓",
                        "Si x = 1: (1 + 2)(1 + 3) = (3)(4) = 12 ✓"
                    )
                ),
                SolutionStep(
                    6,
                    "Por lo tanto:",
                    listOf("Las soluciones son x = -6 o x = 1"),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 14 - RELACIÓN ENTRE VARIABLES - Función lineal
        EnpQuestion(
            id = 14,
            question = """Se venden almuerzos:
            
            N platos(x) |  3  |  5   |  7  | 12
            Monto(y)     | 18 | 30 | 42 | 72
            
            ¿Cuál es la relación que hay entre el monto y el número de platos?""",
            options = listOf(
                "Y = x/3",
                "Y = x + 3",
                "Y = 6x",
                "Y = 3x"
            ),
            correctOptionIndex = 2, // opción c
            property = "Relación lineal entre variables:\n" +
                    "• Y = mx, donde m es la constante de proporcionalidad\n" +
                    "• Se puede hallar dividiendo y/x para cualquier par de valores",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Analizamos algunos pares de valores:",
                    listOf(
                        "• Para x = 3, y = 18:  18/3 = 6",
                        "• Para x = 5, y = 30:  30/5 = 6",
                        "• Para x = 7, y = 42:  42/7 = 6",
                        "• Para x = 12, y = 72: 72/12 = 6"
                    )
                ),
                SolutionStep(
                    2,
                    "Verificamos el patrón:",
                    listOf(
                        "• En todos los casos y/x = 6",
                        "• Esto significa que el monto es 6 veces el número de platos",
                        "• La relación es de proporcionalidad directa"
                    )
                ),
                SolutionStep(
                    3,
                    "Expresamos la relación:",
                    listOf(
                        "• Si y/x = 6, entonces y = 6x",
                        "• Esto significa que cada plato cuesta 6 soles"
                    )
                ),
                SolutionStep(
                    4,
                    "Por lo tanto:",
                    listOf("La relación entre el monto y el número de platos es Y = 6x"),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 15 - Perímetro
        EnpQuestion(
            id = 15,
            question = """Una torta circular tiene fresas distribuidas uniformemente a lo largo de su borde. Si la mayor distancia que separa a dos fresas es 24 cm, se quiere colocar esta torta en una caja cuadrada que la contenga exactamente. 
            
        ¿Cuál será el perímetro de la base de la caja cuadrada?""",
            options = listOf(
                "24 cm",
                "48 cm",
                "96 cm",
                "120 cm"
            ),
            correctOptionIndex = 2,  // opción c
            property = "• El diámetro de un círculo es la mayor distancia entre dos puntos del círculo\n" +
                    "• La caja cuadrada que contiene exactamente a un círculo tiene lado igual al diámetro del círculo\n" +
                    "• Perímetro del cuadrado = 4 × lado",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Identificar la relación entre el diámetro y las fresas:",
                    listOf(
                        "• La mayor distancia entre dos fresas corresponde al diámetro del círculo",
                        "• Por lo tanto, el diámetro D = 24 cm"
                    )
                ),
                SolutionStep(
                    2,
                    "Determinar el lado de la caja cuadrada:",
                    listOf(
                        "• Para que la torta circular quepa exactamente",
                        "• El lado del cuadrado debe ser igual al diámetro",
                        "• Lado = D = 24 cm"
                    )
                ),
                SolutionStep(
                    3,
                    "Calcular el perímetro de la caja cuadrada:",
                    listOf(
                        "Perímetro = 4 × lado",
                        "Perímetro = 4 × 24 = 96 cm"
                    ),
                    isHighlighted = true
                )
            ),
            svgDiagram = """
                <svg viewBox="0 0 70 50" xmlns="http://www.w3.org/2000/svg">
                    <!-- Caja cuadrada -->
                    <rect x="20" y="10" width="30" height="30" fill="none" stroke="black" stroke-width="1"/>
                    
                    <!-- Círculo de la torta -->
                    <circle cx="35" cy="25" r="15" fill="none" stroke="black" stroke-width="1" stroke-dasharray="2,2"/>
                    
                    <!-- Diámetro -->
                    <line x1="20" y1="25" x2="50" y2="25" stroke="blue" stroke-width="1" stroke-dasharray="2,2"/>
                    <text x="30" y="23" font-size="4">24 cm</text>
                    
                    <!-- Puntos de las fresas -->
                    <circle cx="35" cy="10" r="1" fill="red"/>
                    <circle cx="50" cy="25" r="1" fill="red"/>
                    <circle cx="35" cy="40" r="1" fill="red"/>
                    <circle cx="20" cy="25" r="1" fill="red"/>
                    
                    <!-- Leyenda en la parte inferior con más espacio -->
                    <text x="15" y="48" font-size="4">Caja</text>
                    <text x="45" y="48" font-size="4" fill="red">Fresas</text>
                </svg>
            """.trimIndent()
        ),

        // Pregunta 16 - Probabilidad
        EnpQuestion(
            id = 16,
            question = "Una profesora hizo un juego con cartas para que los alumnos aprendieran los números primeros, los números de las cartas eran 2,3,5,7,11,13,17,19 ¿Cuál es la probabilidad de que Juana saque un número primo?",
            options = listOf(
                "1/8",
                "1",
                "7/8",
                "0"
            ),
            correctOptionIndex = 1,
            property = "Cálculo de probabilidad: casos favorables / casos posibles",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Analicemos los datos:",
                    listOf(
                        "• Total de números: 8",
                        "• Números dados: 2, 3, 5, 7, 11, 13, 17, 19",
                        "• Verificamos cada número"
                    )
                ),
                SolutionStep(
                    2,
                    "Identificamos los números primos:",
                    listOf(
                        "2 → primo (el único primo par)",
                        "3 → primo",
                        "5 → primo",
                        "7 → primo",
                        "11 → primo",
                        "13 → primo",
                        "17 → primo",
                        "19 → primo"
                    )
                ),
                SolutionStep(
                    3,
                    "Calculamos la probabilidad:",
                    listOf(
                        "Casos favorables: 8 números primos",
                        "Casos posibles: 8 números en total",
                        "Probabilidad = 8/8 = 1"
                    )
                ),
                SolutionStep(
                    4,
                    "Por lo tanto:",
                    listOf("La probabilidad es 1 (100%) ya que todos los números de la lista son primos"),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 17 - Calcule el valor de x
        EnpQuestion(
            id = 17,
            question = """En el triángulo rectángulo △ABC, con ∠A=90°, ∠B=30° y ∠C=60°, se tiene lo siguiente:
            • El cateto AC mide 2√3
            • El cateto AB mide x+2, donde x es la incógnita
            • El punto D es donde la bisectriz del ángulo ∠C divide al cateto AB
            • El lado AD mide 2 y el lado BD mide x
            
            Determina el valor de x.""",
            options = listOf(
                "4",
                "2",
                "6",
                "8"
            ),
            correctOptionIndex = 0,
            property = """
            • La bisectriz de un ángulo divide al ángulo en dos partes iguales
            • En un triángulo 30-60-90: si el menor cateto es k
              - Cateto mayor = k√3
              - Hipotenusa = 2k
            • En un triángulo isósceles, dos de sus lados son iguales
            • Este problema puede resolverse por:
              - Propiedades de triángulos notables (30-60-90)
              - Teorema de Pitágoras
              - Proporcionalidad entre triángulos semejantes
            """.trimIndent(),
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Analizar la bisectriz y los triángulos formados:",
                    listOf(
                        "• La línea que divide al ángulo de 60° es una bisectriz",
                        "• El triángulo inferior es un 30-60-90",
                        "• El triángulo superior tiene dos ángulos de 30°, por lo que es isósceles",
                        "• La bisectriz es hipotenusa del triángulo inferior y lado del superior"
                    )
                ),
                SolutionStep(
                    2,
                    "Calcular la bisectriz usando el triángulo inferior (30-60-90):",
                    listOf(
                        "• Cateto menor (altura) = 2 = k",
                        "• Cateto mayor (base) = 2√3 = k√3",
                        "• Hipotenusa (bisectriz) = 4 = 2k",
                        "• Se confirma que es un triángulo 30-60-90 válido"
                    )
                ),
                SolutionStep(
                    3,
                    "Determinar x usando propiedades del triángulo isósceles superior:",
                    listOf(
                        "• Por ser isósceles, dos lados son iguales",
                        "• Uno de estos lados es x",
                        "• El otro lado es la bisectriz que ya calculamos (4)",
                        "• Por lo tanto, x = 4"
                    )
                ),
                SolutionStep(
                    4,
                    "Métodos alternativos de solución:",
                    listOf(
                        "Por Pitágoras en el triángulo inferior:",
                        "• h² = 2² + (2√3)² = 4 + 12 = 16",
                        "• h = 4 (siendo h la bisectriz)",
                        "Por proporcionalidad:",
                        "• La razón entre los catetos del triángulo 30-60-90 es 1:√3",
                        "• Esta misma razón se mantiene en todos los triángulos semejantes"
                    )
                ),
                SolutionStep(
                    5,
                    "Verificación:",
                    listOf(
                        "• La bisectriz mide 4 (hipotenusa del triángulo inferior)",
                        "• El triángulo superior es isósceles con x = 4",
                        "• Se mantienen las proporciones en ambos triángulos",
                        "• El resultado coincide por los tres métodos"
                    ),
                    isHighlighted = true
                )
            ),
            svgDiagram = """
                <svg viewBox="0 0 75 60" xmlns="http://www.w3.org/2000/svg">
                    <!-- Triángulo base -->
                    <path d="M10,45 L60,45 L10,5 Z" fill="none" stroke="black" stroke-width="1.5"/>
                    
                    <!-- Línea del medio -->
                    <line x1="10" y1="25" x2="60" y2="45" stroke="black" stroke-width="1.5"/>
                    
                    <!-- Altura vertical -->
                    <line x1="10" y1="5" x2="10" y2="45" stroke="black" stroke-width="1.5"/>
                    
                    <!-- Marca de ángulo recto -->
                    <path d="M10,45 L15,45 L15,40 L10,40 Z" fill="none" stroke="black" stroke-width="1"/>
                    
                    <!-- Ángulos de 30° -->
                    <text x="35" y="34" font-size="4" fill="blue">30°</text>
                    <text x="35" y="43" font-size="4" fill="blue">30°</text>
                    
                    <!-- Medidas -->
                    <text x="5" y="18" font-size="5">x</text>
                    <text x="5" y="35" font-size="5">2</text>
                    <text x="30" y="53" font-size="5">2√3</text>
                </svg>
            """.trimIndent()
        ),

        // Pregunta 18 - Probabilidad
        EnpQuestion(
            id = 18,
            question = "Se tienen pelotas pequeñas de 3 colores, hay 22 rojos, 24 blancos y 3 verdes. ¿Cuál afirmación es correcta?",
            options = listOf(
                "Es probable que se saque una bola azul",
                "Es seguro que saque uno blanco",
                "Es imposible sacar uno verde",
                "Es probable sacar rojo"
            ),
            correctOptionIndex = 3,  // Es probable sacar rojo
            property = "Cálculo de probabilidad: casos favorables / casos posibles",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Analicemos los datos:",
                    listOf(
                        "• Total de pelotas: 22 rojos + 24 blancos + 3 verdes = 49 pelotas",
                        "• Vamos a calcular la probabilidad de sacar cada color"
                    )
                ),
                SolutionStep(
                    2,
                    "Analicemos cada opción:",
                    listOf(
                        "a. Bola azul: No hay bolas azules (0/49), imposible",
                        "b. Sacar blanco: 24/49 (probabilidad alta, pero no 100%)",
                        "c. Sacar verde: 3/49 (posible, no imposible)",
                        "d. Sacar rojo: 22/49 (probabilidad moderada)"
                    )
                ),
                SolutionStep(
                    3,
                    "Conclusión:",
                    listOf(
                        "• La probabilidad de sacar rojo es 22/49 ≈ 0.45 o 45%",
                        "• Es un evento probable",
                        "• Por lo tanto, la opción 'd' es la correcta"
                    ),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 19 - Puntos Notables
        EnpQuestion(
            id = 19,
            question = """En una región, tres pueblos quieren un hospital que esté a la misma distancia entre todos. Se sitúan 3 puntos distantes formando un triángulo. Se desea situar en el centro del triángulo de tal manera que se encuentre a la misma distancia de los 3 puntos, para ello se debe trazar:""",
            options = listOf(
                "Incentro",
                "Baricentro",
                "Circuncentro",
                "Ortocentro"
            ),
            correctOptionIndex = 2,
            property = "Los puntos notables del triángulo tienen propiedades específicas de equidistancia",
            hasFormula = false,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Analizar el requerimiento:",
                    listOf(
                        "• Se necesita un punto equidistante de tres puntos fijos",
                        "• Los tres puntos son los vértices del triángulo",
                        "• El punto debe estar a igual distancia de cada vértice"
                    )
                ),
                SolutionStep(
                    2,
                    "Examinar los puntos notables:",
                    listOf(
                        "Incentro: equidistante de los lados",
                        "Baricentro: centro de gravedad",
                        "Circuncentro: equidistante de los vértices",
                        "Ortocentro: intersección de alturas"
                    )
                ),
                SolutionStep(
                    3,
                    "El circuncentro es la respuesta porque:",
                    listOf(
                        "• Es el centro de la circunferencia circunscrita",
                        "• Esta circunferencia pasa por los tres vértices",
                        "• Todos los puntos de la circunferencia están a igual distancia del centro"
                    )
                ),
                SolutionStep(
                    4,
                    "Por lo tanto:",
                    listOf(
                        "El circuncentro es el único punto que garantiza igual distancia a los tres pueblos porque es el centro de la circunferencia que pasa por los tres vértices"
                    ),
                    isHighlighted = true
                )
            ),
            svgDiagram = """
                <svg viewBox="0 0 70 50" xmlns="http://www.w3.org/2000/svg">
                   <!-- Circunferencia circunscrita -->
                   <circle cx="35" cy="25" r="20" fill="none" stroke="blue" stroke-width="1" stroke-dasharray="2,2"/>
                   
                   <!-- Triángulo equilátero -->
                   <polygon points="35,6 18,36 52,36" fill="none" stroke="black" stroke-width="1"/>
                   
                   <!-- Vértices -->
                   <circle cx="35" cy="6" r="1.5" fill="black"/>
                   <circle cx="18" cy="36" r="1.5" fill="black"/>
                   <circle cx="52" cy="36" r="1.5" fill="black"/>
                   
                   <!-- Etiquetas de vértices -->
                   <text x="33" y="4" font-size="4">A</text>
                   <text x="15" y="40" font-size="4">B</text>
                   <text x="54" y="40" font-size="4">C</text>
                   
                   <!-- Mediatrices -->
                   <line x1="35" y1="6" x2="35" y2="25" stroke="red" stroke-dasharray="2,2" stroke-width="0.8"/>
                   <line x1="18" y1="36" x2="35" y2="25" stroke="red" stroke-dasharray="2,2" stroke-width="0.8"/>
                   <line x1="52" y1="36" x2="35" y2="25" stroke="red" stroke-dasharray="2,2" stroke-width="0.8"/>
                   
                   <!-- Circuncentro -->
                   <circle cx="35" cy="25" r="1.5" fill="red"/>
                   
                   <!-- Etiqueta del Circuncentro -->
                   <text x="37" y="25" font-size="4" fill="black">O</text>
                </svg>
            """.trimIndent()
        ),

        // Pregunta 20 - Hexágono Regular
        EnpQuestion(
            id = 20,
            question = "Pedro, Max y Luis corren en una plaza, la cual tiene la forma de un hexágono. Pedro sigue el camino ABoD, Max ABoED, y Luis ABoCD. Marque la alternativa correcta.",
            options = listOf(
                "Max y Luis recorren la misma distancia",
                "Pedro recorre más que Max",
                "Luis recorre la misma distancia que Pedro",
                "Luis recorre más que Max"
            ),
            correctOptionIndex = 0,
            property = "Análisis de recorridos en un hexágono regular",
            hasFormula = false,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Analizar las características del hexágono:",
                    listOf(
                        "• El hexágono es regular, por lo que tiene lados y ángulos iguales",
                        "• Esto implica que la distancia entre cada vértice es la misma"
                    )
                ),
                SolutionStep(
                    2,
                    "Comparar los recorridos de Pedro, Max y Luis:",
                    listOf(
                        "• Pedro sigue el camino ABoD",
                        "• Max sigue el camino ABoED",
                        "• Luis sigue el camino ABoCd"
                    )
                ),
                SolutionStep(
                    3,
                    "Conclusión:",
                    listOf(
                        "• Al ser un hexágono regular, la distancia recorrida por Max y Luis es la misma",
                        "• Por lo tanto, la opción correcta es 'a. Max y Luis recorren la misma distancia'"
                    ),
                    isHighlighted = true
                )
            ),
            svgDiagram = """
                <svg viewBox="0 0 70 50" xmlns="http://www.w3.org/2000/svg">
                    <!-- Hexágono -->
                    <polygon points="35,5 55,15 55,35 35,45 15,35 15,15" fill="none" stroke="black" stroke-width="1"/>
                    
                    <!-- Líneas al centro -->
                    <line x1="35" y1="5" x2="35" y2="25" stroke="blue" stroke-width="0.8"/>
                    <line x1="55" y1="15" x2="35" y2="25" stroke="blue" stroke-width="0.8"/>
                    <line x1="55" y1="35" x2="35" y2="25" stroke="blue" stroke-width="0.8"/>
                    <line x1="35" y1="45" x2="35" y2="25" stroke="blue" stroke-width="0.8"/>
                    <line x1="15" y1="35" x2="35" y2="25" stroke="blue" stroke-width="0.8"/>
                    <line x1="15" y1="15" x2="35" y2="25" stroke="blue" stroke-width="0.8"/>
                    
                    <!-- Punto central -->
                    <circle cx="35" cy="25" r="1" fill="black"/>
                    
                    <!-- Vértices -->
                    <circle cx="35" cy="5" r="1" fill="black"/>
                    <circle cx="55" cy="15" r="1" fill="black"/>
                    <circle cx="55" cy="35" r="1" fill="black"/>
                    <circle cx="35" cy="45" r="1" fill="black"/>
                    <circle cx="15" cy="35" r="1" fill="black"/>
                    <circle cx="15" cy="15" r="1" fill="black"/>
                    
                    <!-- Etiquetas -->
                    <text x="33" y="3" font-size="3">A</text>
                    <text x="57" y="16" font-size="3">B</text>
                    <text x="57" y="38" font-size="3">C</text>
                    <text x="33" y="48" font-size="3">D</text>
                    <text x="11" y="38" font-size="3">E</text>
                    <text x="11" y="16" font-size="3">F</text>
                    <text x="32" y="23" font-size="3">o</text>
                    
                    <!-- Marcas de igual longitud (a) -->
                    <text x="37" y="15" font-size="3" fill="white">a</text>
                    <text x="45" y="22" font-size="3" fill="white">a</text>
                    <text x="45" y="33" font-size="3" fill="white">a</text>
                    <text x="37" y="35" font-size="3" fill="white">a</text>
                    <text x="25" y="27" font-size="3" fill="white">a</text>
                    <text x="25" y="19" font-size="3" fill="white">a</text>
                    <text x="12" y="25" font-size="3" fill="white">a</text>
                </svg>
            """.trimIndent()
        ),

        // Pregunta 21 - Área del rombo
        EnpQuestion(
            id = 21,
            question = """Un jardín tiene la forma de un rombo, se trazan las sus diagonales formando triángulos de igual medida, si el área de uno de ellos es de 90 m². Calcule el área del jardín.""",
            options = listOf(
                "360 m²",
                "180 m²",
                "90 m²",
                "270 m²"
            ),
            correctOptionIndex = 0,
            property = """
                • El área de un rombo es igual a (D × d)/2, donde D y d son sus diagonales
                • Las diagonales del rombo dividen al rombo en cuatro triángulos congruentes
                • El área total es la suma de las áreas de los cuatro triángulos
            """.trimIndent(),
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Analizar la información:",
                    listOf(
                        "• El rombo está dividido en 4 triángulos iguales",
                        "• Un triángulo mide 90 m²",
                        "• Los triángulos son congruentes por las propiedades del rombo"
                    )
                ),
                SolutionStep(
                    2,
                    "Calcular el área total:",
                    listOf(
                        "• Área total = 4 × área de un triángulo",
                        "• Área total = 4 × 90 m²"
                    )
                ),
                SolutionStep(
                    3,
                    "Por lo tanto:",
                    listOf(
                        "Área total = 360 m²"
                    ),
                    isHighlighted = true
                )
            ),
            svgDiagram = """
                <svg viewBox="0 0 70 50" xmlns="http://www.w3.org/2000/svg">
                    <!-- Rombo -->
                    <polygon points="35,6 55,25 35,44 15,25" fill="none" stroke="black" stroke-width="0.8"/>
                    
                    <!-- Diagonales -->
                    <line x1="15" y1="25" x2="55" y2="25" stroke="blue" stroke-width="0.6" stroke-dasharray="1.5,1.5"/>
                    <line x1="35" y1="6" x2="35" y2="44" stroke="blue" stroke-width="0.6" stroke-dasharray="1.5,1.5"/>
                    
                    <!-- Triángulo -->
                    <polygon points="35,6 55,25 35,25" fill="#90EE90" fill-opacity="0.5"/>
                    
                    <!-- Área del triángulo centrada perfectamente en el cuadrante verde -->
                    <text x="36" y="20" font-size="4" fill="black">90 m²</text>
                </svg>
            """.trimIndent()
        ),

        // Pregunta 22 - Aristas
        EnpQuestion(
            id = 22,
            question = """Una niña quiere construir una casita para su perro usando fierros como estructura y recubrirla con mallas. La casita tendrá forma de un cubo con una pirámide cuadrangular regular en la parte superior.

            Datos:
            - Arista del cubo = 2 cm
            - Arista lateral de la pirámide = √5 cm
            
            ¿Cuántos centímetros de fierro necesitará para construir toda la estructura?""",
            options = listOf(
                "16 + 4√8 + 4√5 cm",
                "12 + 8√8 + 4√5 cm",
                "20 + 4√8 + 4√5 cm",
                "24 + 4√8 + 4√5 cm"
            ),
            correctOptionIndex = 3,
            property = "• Cubo: 12 aristas iguales\n" +
                    "• Pirámide: 4 aristas laterales + diagonales de refuerzo\n" +
                    "• Diagonal del cuadrado: d = a√2, donde a es el lado",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Calculamos las aristas del cubo:",
                    listOf(
                        "• Un cubo tiene 12 aristas iguales",
                        "• Cada arista mide 2 cm",
                        "• Total para el cubo = 12 × 2 = 24 cm"
                    )
                ),
                SolutionStep(
                    2,
                    "Analizamos las aristas de la pirámide:",
                    listOf(
                        "• Aristas de la base: ya incluidas en el cubo",
                        "• Aristas laterales: 4 aristas de √5 cm cada una",
                        "• Total aristas laterales = 4 × √5 = 4√5 cm"
                    )
                ),
                SolutionStep(
                    3,
                    "Calculamos las diagonales de refuerzo:",
                    listOf(
                        "• Diagonal del cuadrado = √(2² + 2²) = 2√2 cm",
                        "• Son 2 diagonales de 2√2 cm cada una",
                        "• Total diagonales = 2 × 2√2 = 4√8 cm"
                    )
                ),
                SolutionStep(
                    4,
                    "Sumamos todos los fierros:",
                    listOf(
                        "Total = Cubo + Diagonales + Aristas pirámide",
                        "Total = 24 + 4√8 + 4√5 cm"
                    ),
                    isHighlighted = true
                )
            ),
            svgDiagram = """
                <svg viewBox="0 0 70 50" xmlns="http://www.w3.org/2000/svg">
                    <!-- Cubo base completo -->
                    <path d="M15,20 L40,20 L40,45 L15,45 Z" fill="none" stroke="black" stroke-width="1"/>
                    <path d="M40,45 L55,35 L55,10 L40,20" fill="none" stroke="black" stroke-width="1"/>
                    <path d="M15,45 L30,35 L55,35" fill="none" stroke="black" stroke-width="1"/>
                    <path d="M30,35 L30,10 L55,10" fill="none" stroke="black" stroke-width="1"/>
                    
                    <!-- X azul en la base superior del cubo -->
                    <path d="M15,20 L55,10" stroke="blue" stroke-width="1" stroke-dasharray="2,2"/>
                    <path d="M40,20 L30,10" stroke="blue" stroke-width="1" stroke-dasharray="2,2"/>
                    
                    <!-- Pirámide roja -->
                    <path d="M15,20 L37,5 M40,20 L37,5 M55,10 L37,5" fill="none" stroke="red" stroke-width="1"/>
                    <path d="M30,10 L37,5" fill="none" stroke="red" stroke-width="1"/>
                    <circle cx="37" cy="5" r="1" fill="red"/>
                    
                    <!-- Medidas -->
                    <text x="5" y="35" font-size="4">2 cm</text>
                    <text x="52" y="8" fill="red" font-size="4">√5 cm</text>
                    
                    <!-- Leyenda -->
                    <text x="8" y="49" font-size="4">Cubo</text>
                    <text x="25" y="49" font-size="4" fill="red">Pirámide</text>
                    <text x="45" y="49" font-size="4" fill="blue">Diagonal</text>
                </svg>
            """.trimIndent()
        ),

        // Pregunta 23 - ALTURA DE UN CILINDRO - Diferencia de alturas
        EnpQuestion(
            id = 23,
            question = """Pedro llenaba su tanque en forma de un cilindro, si al cortarse el agua solo se llenó la mitad del cilindro y necesita los 3/4 para abastecer a su familia. ¿Qué tenía que hacer Pedro para saber cuánto le falta para abastecer su casa?

        Datos:
        • Radio = 0.5m
        • Altura = 1.2m
            """.trimIndent(),
            options = listOf(
                "Calcular el volumen con una altura de 0.3m",
                "Calcular el volumen del cilindro con una altura de 0.6m",
                "Calcular el volumen del cilindro con una altura de 0.9m",
                "Calcular el volumen del cilindro con una altura de 1.2m"
            ),
            correctOptionIndex = 0,  // Opción a
            property = "Diferencia de volúmenes en un cilindro: ΔV = π × r² × Δh",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Análisis de la situación:",
                    listOf(
                        "• Altura total del cilindro = 1.2m",
                        "• Tiene la mitad: 1.2m ÷ 2 = 0.6m de altura",
                        "• Necesita tres cuartos: 1.2m × (3/4) = 0.9m de altura"
                    )
                ),
                SolutionStep(
                    2,
                    "Para hallar el volumen faltante necesitamos:",
                    listOf(
                        "Altura faltante = Altura necesaria - Altura actual",
                        "Altura faltante = 0.9m - 0.6m = 0.3m"
                    )
                ),
                SolutionStep(
                    3,
                    "Por lo tanto:",
                    listOf(
                        "Para saber cuánto le falta, debe calcular el volumen con una altura de 0.3m",
                        "Esta altura representa la diferencia entre lo que tiene y lo que necesita"
                    ),
                    isHighlighted = true
                )
            ),
            svgDiagram = """
                <svg viewBox="0 0 90 70" xmlns="http://www.w3.org/2000/svg">
                    <!-- Radio - texto movido más arriba -->
                    <text x="50" y="5" font-size="6">r=0.5m</text>
                    
                    <!-- Cilindro base - más pequeño -->
                    <ellipse cx="45" cy="55" rx="20" ry="5" fill="none" stroke="black" stroke-width="1.5"/>
                    <path d="M25,15 L25,55 M65,15 L65,55" fill="none" stroke="black" stroke-width="1.5"/>
                    <ellipse cx="45" cy="15" rx="20" ry="5" fill="none" stroke="black" stroke-width="1.5"/>
                    
                    <!-- Radio línea -->
                    <path d="M45,15 L65,15" stroke="black" stroke-width="1" stroke-dasharray="2,2"/>
                    
                    <!-- Agua (mitad llena) -->
                    <path d="M25,35 L65,35" stroke="blue" stroke-width="1.5" stroke-dasharray="3,3"/>
                    <path d="M25,35 L25,55 L65,55 L65,35" fill="lightblue" fill-opacity="0.3"/>
                    <ellipse cx="45" cy="35" rx="20" ry="5" fill="lightblue" fill-opacity="0.3"/>
                    
                    <!-- Nivel necesario (3/4) -->
                    <path d="M25,25 L65,25" stroke="red" stroke-width="1.5" stroke-dasharray="3,3"/>
                    
                    <!-- Altura -->
                    <text x="71" y="35" font-size="6">1.2m</text>
                    <path d="M70,15 L70,55" stroke="black" stroke-width="1" marker-end="url(#arrow)" marker-start="url(#arrow)"/>
                    
                    <!-- Flechas -->
                    <defs>
                        <marker id="arrow" viewBox="0 0 10 10" refX="5" refY="5"
                            markerWidth="3" markerHeight="3"
                            orient="auto-start-reverse">
                            <path d="M 0 0 L 10 5 L 0 10 z" fill="black"/>
                        </marker>
                    </defs>
                    
                    <!-- Leyenda -->
                    <text x="20" y="65" font-size="5" fill="blue">▬ 1/2</text>
                    <text x="55" y="65" font-size="5" fill="red">▬ 3/4</text>
                </svg>
            """.trimIndent()
        ),

        // Pregunta 24 - Descuentos sucesivos
        EnpQuestion(
            id = 24,
            question = """El precio original de una computadora es 2500. Pedro entró en 2 tiendas donde La Tienda A ofrecía un descuento de 20% más una promoción de 12% de descuento por aniversario, la tienda B ofrecía un descuento de 26% y si usaba tarjeta un descuento del 5% de descuento. ¿En qué tienda debe comprar Pedro?""",
            options = listOf(
                "La tienda A porque sale a menos precio que la tienda B",
                "La tienda B porque sale a un menor precio",
                "La tienda B porque ofrece un descuento sucesivo del 31%",
                "La tienda A porque ofrece un descuento sucesivo del 32%"
            ),
            correctOptionIndex = 1,
            property = """
                • Los descuentos sucesivos se aplican uno después de otro
                • Primer descuento se aplica al precio original
                • Segundo descuento se aplica al precio ya rebajado
                • Los porcentajes de descuento no se suman directamente
            """.trimIndent(),
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Calcular el precio final en la Tienda A:",
                    listOf(
                        "• Precio inicial = 2500",
                        "• Primer descuento 20%: 2500 × 0.8 = 2000",
                        "• Segundo descuento 12%: 2000 × 0.88 = 1760"
                    )
                ),
                SolutionStep(
                    2,
                    "Calcular el precio final en la Tienda B:",
                    listOf(
                        "• Precio inicial = 2500",
                        "• Primer descuento 26%: 2500 × 0.74 = 1850",
                        "• Segundo descuento 5%: 1850 × 0.95 = 1757.50"
                    )
                ),
                SolutionStep(
                    3,
                    "Comparar precios finales:",
                    listOf(
                        "• Tienda A: 1760.00",
                        "• Tienda B: 1757.50",
                        "• Diferencia: 2.50 más barato en Tienda B"
                    ),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 25 - Tabla de frecuencias
        EnpQuestion(
            id = 25,
            question = """Se registran los sueldos de empresa DiorDina tu presi, ordena los datos en una tabla de frecuencias.
            
            1000-1234-1499-1289-1500-1999-1654-1745-1900-2000-2500-2140-2499-2326-2128-2400-2600-2750-2999-2800""",
            options = listOf(
                "[1000-1500] 4, [1500-2000] 5, [2000-2500] 6, [2500-3000] 5",
                "[1000-1500] 2, [1500-2000] 8, [2000-2500] 7, [2500-3000] 3",
                "[1000-1500] 6, [1500-2000] 4, [2000-2500] 5, [2500-3000] 5",
                "[1000-1500] 3, [1500-2000] 6, [2000-2500] 8, [2500-3000] 3"
            ),
            correctOptionIndex = 0,
            property = """
                • Los intervalos deben ser del mismo tamaño
                • Cada dato debe pertenecer a un solo intervalo
                • La frecuencia es el número de datos en cada intervalo
            """.trimIndent(),
            hasFormula = false,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Organizar los datos en intervalos de 500:",
                    listOf(
                        "• [1000-1500]: 1000, 1234, 1499, 1289",
                        "• [1500-2000]: 1500, 1999, 1654, 1745, 1900",
                        "• [2000-2500]: 2000, 2140, 2499, 2326, 2128, 2400",
                        "• [2500-3000]: 2600, 2750, 2999, 2800, 2500"
                    )
                ),
                SolutionStep(
                    2,
                    "Contar la frecuencia en cada intervalo:",
                    listOf(
                        "• [1000-1500] = 4 datos",
                        "• [1500-2000] = 5 datos",
                        "• [2000-2500] = 6 datos",
                        "• [2500-3000] = 5 datos"
                    )
                ),
                SolutionStep(
                    3,
                    "Por lo tanto:",
                    listOf(
                        "[1000-1500] 4, [1500-2000] 5, [2000-2500] 6, [2500-3000] 5"
                    ),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 26 - Crecimiento Exponencial
        EnpQuestion(
            id = 26,
            question = """Al principio, en un recipiente se tiene 120 bacterias. Si 120 * 2ˣ donde x es el tiempo que transcurre en un periodo de 3 horas. Después de 15 horas, Daniela afirma que habrá 120*15 bacterias. ¿Estás de acuerdo con la afirmación de Daniela?""",
            options = listOf(
                "Falso, porque a la cantidad inicial se le debe multiplicar por 32",
                "Verdadero",
                "Falso",
                "Verdadero"
            ),
            correctOptionIndex = 0,
            property = """
               • En crecimiento exponencial: N = N₀ * aˣ
               • N₀ es la cantidad inicial
               • a es la razón de crecimiento
               • x es el número de periodos
           """.trimIndent(),
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Analizar los datos:",
                    listOf(
                        "• Cantidad inicial = 120 bacterias",
                        "• Periodo = 3 horas",
                        "• Tiempo total = 15 horas",
                        "• Por cada periodo se multiplica por 2"
                    )
                ),
                SolutionStep(
                    2,
                    "Calcular número de periodos en 15 horas:",
                    listOf(
                        "• Periodos = 15 ÷ 3 = 5 periodos",
                        "• En cada periodo la población se multiplica por 2",
                        "• En 5 periodos: 2⁵ = 32"
                    )
                ),
                SolutionStep(
                    3,
                    "Por lo tanto:",
                    listOf(
                        "• Cantidad final = 120 * 32",
                        "• No es 120 * 15 como afirma Daniela",
                        "• La respuesta correcta es la opción 'a' porque se debe multiplicar por 32"
                    ),
                    isHighlighted = true
                )
            )
        ),

        //Pregunta 27 - Promedios
        EnpQuestion(
            id = 27,
            question = """En un torneo de baloncesto, de un equipo, uno de los jugadores sufrió una lesión y el entrenador necesita elegir un suplente para reemplazarlo. El entrenador decidió elegir al jugador que tuviera el mejor promedio de puntos obtenidos en sus anteriores partidos. A continuación, se muestran los puntos obtenidos por los dos jugadores que podrían ser elegidos suplentes:
            
            |  Alex  | 14 |  8  | 12 |  8  | 20 |
            | Dylan | 10 | 12 | 10 | 12 | 20 |
            """,
            options = listOf(
                "Alex porque anotó dos veces 8 puntos",
                "Dilan porque anotó dos veces 12 puntos",
                "Dylan porque tuvo el promedio máximo",
                "Alex porque tuvo el promedio máximo"
            ),
            correctOptionIndex = 2,
            property = "Cálculo de promedios para elegir el mejor suplente",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Analizar los datos de los jugadores:",
                    listOf(
                        "Alex: 14, 8, 12, 8, 20 puntos",
                        "Dylan: 10, 12, 10, 12, 20 puntos"
                    )
                ),
                SolutionStep(
                    2,
                    "Calcular el promedio de puntos de cada jugador:",
                    listOf(
                        "Promedio de Alex = (14 + 8 + 12 + 8 + 20) / 5 = 12.4 puntos",
                        "Promedio de Dylan = (10 + 12 + 10 + 12 + 20) / 5 = 12.8 puntos"
                    )
                ),
                SolutionStep(
                    3,
                    "Determinar el jugador con el promedio máximo:",
                    listOf(
                        "Dylan tiene el promedio máximo de 12.8 puntos",
                        "Por lo tanto, la respuesta correcta es: Dylan Porque tuvo el promedio máximo"
                    ),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 28 - Puntos Notables
        EnpQuestion(
            id = 28,
            question = "3 hermanos tienen 3 terrenos de igual área, pero si estos 3 terrenos se juntan se forma un triángulo. ¿Qué punto notable sería el que separa los 3 terrenos?",
            options = listOf(
                "Baricentro",
                "Circuncentro",
                "Incentro",
                "Ortocentro"
            ),
            correctOptionIndex = 0,
            property = """
                • El baricentro es el punto donde se intersecan las medianas
                • Las medianas dividen al triángulo en seis partes iguales
                • Cada mediana divide al triángulo en dos partes de igual área
                • Las tres medianas dividen al triángulo en seis partes iguales
            """.trimIndent(),
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Analizar los requerimientos:",
                    listOf(
                        "• Se necesita dividir el triángulo en 3 partes iguales",
                        "• Debe ser un punto notable del triángulo",
                        "• Las áreas deben ser exactamente iguales"
                    )
                ),
                SolutionStep(
                    2,
                    "Analizar los puntos notables:",
                    listOf(
                        "• El baricentro divide al triángulo en partes iguales",
                        "• Las medianas que se cruzan en el baricentro garantizan áreas iguales",
                        "• Los otros puntos notables no garantizan áreas iguales"
                    )
                ),
                SolutionStep(
                    3,
                    "Por lo tanto:",
                    listOf(
                        "El baricentro es el único punto notable que garantiza tres áreas iguales",
                        "Las medianas dividen correctamente los tres terrenos de los hermanos"
                    ),
                    isHighlighted = true
                )
            ),
            svgDiagram = """
                <svg viewBox="0 0 200 180" xmlns="http://www.w3.org/2000/svg">
                    <!-- Triángulo base -->
                    <path d="M100,20 L180,150 L20,150 Z" fill="none" stroke="black" stroke-width="1.5"/>
                    
                    <!-- Medianas -->
                    <line x1="100" y1="20" x2="100" y2="150" stroke="gray" stroke-width="1" stroke-dasharray="4,4"/>
                    <line x1="20" y1="150" x2="140" y2="85" stroke="gray" stroke-width="1" stroke-dasharray="4,4"/>
                    <line x1="180" y1="150" x2="60" y2="85" stroke="gray" stroke-width="1" stroke-dasharray="4,4"/>
                    
                    <!-- Baricentro (ajustado a 2/3 desde el vértice) -->
                    <circle cx="100" cy="107" r="3" fill="red"/>
                    
                    <!-- Leyenda -->
                    <text x="97" y="102" font-size="8" fill="red">G</text>
                </svg>
            """.trimIndent()
        ),

        // Pregunta 29 - Inecuación lineal
        EnpQuestion(
            id = 29,
            question = "Calcula la inecuación lineal:\n3(x + 2) - 3 < 9",
            options = listOf(
                "x < -2",
                "x > -2",
                "x < 2",
                "x > 2"
            ),
            correctOptionIndex = 2,
            property = "Resolución de inecuaciones lineales:\n" +
                    "1. Despejar la variable manteniendo el sentido de la desigualdad\n" +
                    "2. Al multiplicar o dividir por número negativo, el sentido de la desigualdad cambia",
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Desarrollamos el paréntesis:",
                    listOf(
                        "3(x + 2) - 3 < 9",
                        "3x + 6 - 3 < 9"
                    )
                ),
                SolutionStep(
                    2,
                    "Simplificamos el lado izquierdo:",
                    listOf(
                        "3x + 3 < 9"
                    )
                ),
                SolutionStep(
                    3,
                    "Restamos 3 en ambos lados:",
                    listOf(
                        "3x + 3 - 3 < 9 - 3",
                        "3x < 6"
                    )
                ),
                SolutionStep(
                    4,
                    "Dividimos entre 3 (positivo, mantiene el sentido):",
                    listOf(
                        "3x/3 < 6/3",
                        "x < 2"
                    )
                ),
                SolutionStep(
                    5,
                    "Por lo tanto:",
                    listOf("x < 2"),
                    isHighlighted = true
                )
            )
        ),

        // Pregunta 30 - Volumen del cilindro
        EnpQuestion(
            id = 30,
            question = """Un tanque cilíndrico de agua está lleno hasta la mitad, necesitaban ¾ del tanque lleno para usar. Se sabe que altura del tanque medio lleno era 1.2m con un diámetro de 1 metro. Que se necesita para hallar el volumen de la cantidad de agua que falta para abastecer a una familia""",
            options = listOf(
                "Hallar el volumen considerando 0.3 como altura",
                "Hallar el volumen considerando 0.6 como altura",
                "Hallar el volumen considerando 1.8 como altura",
                "Hallar el volumen considerando 1.8 como altura"
            ),
            correctOptionIndex = 1,
            property = """
                Volumen del cilindro = π × r² × h, donde:
                • r es el radio (diámetro/2)
                • h es la altura
                • Para hallar la diferencia de volumen, se usa Δh
            """,
            hasFormula = true,
            solutionSteps = listOf(
                SolutionStep(
                    1,
                    "Analizar los datos del tanque:",
                    listOf(
                        "• Altura actual (mitad) = 1.2m",
                        "• Diámetro = 1m → radio = 0.5m",
                        "• Necesitan ¾ del tanque",
                        "• Actualmente está a ½ del tanque"
                    )
                ),
                SolutionStep(
                    2,
                    "Calcular altura faltante:",
                    listOf(
                        "• Altura actual = 1.2m (½ del tanque)",
                        "• Altura requerida = ¾ del tanque",
                        "• Diferencia = ¾ - ½ = ¼ del tanque",
                        "• Altura faltante = 0.6m"
                    )
                ),
                SolutionStep(
                    3,
                    "Por lo tanto:",
                    listOf(
                        "Se debe hallar el volumen usando h = 0.6m como altura",
                        "Esta es la altura adicional necesaria para llegar a ¾ del tanque"
                    ),
                    isHighlighted = true
                )
            ),
            svgDiagram = """
                <svg viewBox="0 0 70 50" xmlns="http://www.w3.org/2000/svg">
                    <!-- Cilindro base -->
                    <ellipse cx="35" cy="40" rx="20" ry="5" fill="none" stroke="black" stroke-width="1"/>
                    <ellipse cx="35" cy="10" rx="20" ry="5" fill="none" stroke="black" stroke-width="1"/>
                    <line x1="15" y1="40" x2="15" y2="10" stroke="black" stroke-width="1"/>
                    <line x1="55" y1="40" x2="55" y2="10" stroke="black" stroke-width="1"/>
                    
                    <!-- Nivel actual (mitad) -->
                    <line x1="15" y1="25" x2="55" y2="25" stroke="blue" stroke-width="1" stroke-dasharray="2,2"/>
                    <ellipse cx="35" cy="25" rx="20" ry="5" fill="none" stroke="blue" stroke-width="1" stroke-dasharray="2,2"/>
                    
                    <!-- Nivel requerido (3/4) -->
                    <line x1="15" y1="17" x2="55" y2="17" stroke="red" stroke-width="1" stroke-dasharray="2,2"/>
                    <ellipse cx="35" cy="17" rx="20" ry="5" fill="none" stroke="red" stroke-width="1" stroke-dasharray="2,2"/>
                    
                    <!-- Medidas -->
                    <text x="58" y="26" font-size="3">1.2m</text>
                    <text x="58" y="18" font-size="3">0.6m</text>
                    <text x="35" y="48" font-size="3">∅ 1m</text>
                    
                    <!-- Leyenda -->
                    <text x="5" y="26" font-size="3" fill="blue">½</text>
                    <text x="5" y="18" font-size="3" fill="red">¾</text>
                </svg>
            """.trimIndent()
        )

    )
}