package com.juffyto.juffyto.ui.screens.calculator.preselection.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.juffyto.juffyto.ui.screens.calculator.preselection.model.PreselectionState
import com.juffyto.juffyto.ui.screens.calculator.preselection.model.PreselectionConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreselectionForm(
    state: PreselectionState,
    onUpdateNombre: (String) -> Unit,
    onUpdateModalidad: (String) -> Unit,
    onUpdatePuntajeENP: (String) -> Unit,
    onUpdateSISFOH: (String) -> Unit,
    onUpdateDepartamento: (String) -> Unit,
    onUpdateLenguaOriginaria: (String) -> Unit,
    onNavigateNext: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Campo Nombre
        OutlinedTextField(
            value = state.nombre,
            onValueChange = onUpdateNombre,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.nombreError != null,
            supportingText = state.nombreError?.let { { Text(it) } },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        // Selector de Modalidad
        var expandedModalidad by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expandedModalidad,
            onExpandedChange = { expandedModalidad = it }
        ) {
            OutlinedTextField(
                value = state.modalidad,
                onValueChange = {},
                readOnly = true,
                label = { Text("Modalidad") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedModalidad)
                },
                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth(),
                isError = state.modalidadError != null,
                supportingText = state.modalidadError?.let { { Text(it) } }
            )

            ExposedDropdownMenu(
                expanded = expandedModalidad,
                onDismissRequest = { expandedModalidad = false }
            ) {
                PreselectionConstants.MODALIDADES.forEach { modalidad ->
                    DropdownMenuItem(
                        text = { Text(modalidad) },
                        onClick = {
                            onUpdateModalidad(modalidad)
                            expandedModalidad = false
                        }
                    )
                }
            }
        }

        // Campo Puntaje ENP
        OutlinedTextField(
            value = state.puntajeENP,
            onValueChange = {
                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                    onUpdatePuntajeENP(it)
                }
            },
            label = {
                Text(
                    "Puntaje ENP (0-${PreselectionConstants.MAX_PUNTAJE_ENP})"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = state.puntajeENPError != null,
            supportingText = state.puntajeENPError?.let { { Text(it) } },
            singleLine = true
        )

        // Selector SISFOH
        var expandedSisfoh by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expandedSisfoh,
            onExpandedChange = { expandedSisfoh = it }
        ) {
            OutlinedTextField(
                value = state.clasificacionSISFOH,
                onValueChange = {},
                readOnly = true,
                label = { Text("Clasificación SISFOH") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSisfoh)
                },
                modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth(),
                isError = state.sisfohError != null,
                supportingText = state.sisfohError?.let { { Text(it) } }
            )

            ExposedDropdownMenu(
                expanded = expandedSisfoh,
                onDismissRequest = { expandedSisfoh = false }
            ) {
                val opciones = if (state.modalidad == "Ordinaria") {
                    PreselectionConstants.SISFOH_ORDINARIA
                } else {
                    PreselectionConstants.SISFOH_OTRAS
                }

                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            onUpdateSISFOH(opcion)
                            expandedSisfoh = false
                        }
                    )
                }
            }
        }

        // Selector de Departamento
        var expandedDepartamento by remember { mutableStateOf(false) }
        var showQuintilDialog by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExposedDropdownMenuBox(
                expanded = expandedDepartamento,
                onExpandedChange = { expandedDepartamento = it },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = state.departamento,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Departamento donde culminó la secundaria") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDepartamento)
                    },
                    modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth(),
                    isError = state.departamentoError != null,
                    supportingText = state.departamentoError?.let { { Text(it) } }
                )

                ExposedDropdownMenu(
                    expanded = expandedDepartamento,
                    onDismissRequest = { expandedDepartamento = false }
                ) {
                    PreselectionConstants.DEPARTAMENTOS.forEach { (departamento, puntaje) ->
                        val textoCompleto = "$departamento - $puntaje puntos"
                        DropdownMenuItem(
                            text = { Text(textoCompleto) },
                            onClick = {
                                onUpdateDepartamento(textoCompleto)
                                expandedDepartamento = false
                            }
                        )
                    }
                }
            }

            // Agregar el botón de información
            IconButton(
                onClick = { showQuintilDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Información sobre tasa de transición"
                )
            }
        }

        if (showQuintilDialog) {
            AlertDialog(
                onDismissRequest = { showQuintilDialog = false },
                title = {
                    Text(
                        "📌 Información de Tasa de Transición",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("El puntaje se otorga según el departamento donde culminaste la secundaria:")

                        Text("✅Quintil 1 (10 puntos):", fontWeight = FontWeight.Bold)
                        Text("Amazonas, Ucayali, Ayacucho, Puno, Loreto")

                        Text("✅Quintil 2 (7 puntos):", fontWeight = FontWeight.Bold)
                        Text("San Martín, Cusco, Huánuco, Apurímac, Huancavelica")

                        Text("✅Quintil 3 (5 puntos):", fontWeight = FontWeight.Bold)
                        Text("Áncash, Tacna, Madre de Dios, Moquegua, Pasco, Cajamarca")

                        Text("✅Quintil 4 (2 puntos):", fontWeight = FontWeight.Bold)
                        Text("Arequipa, Piura, Junín, Tumbes")

                        Text("✅Quintil 5 (0 puntos):", fontWeight = FontWeight.Bold)
                        Text("Ica, Lambayeque, Lima, La Libertad")
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showQuintilDialog = false }) {
                        Text("Entendido")
                    }
                }
            )
        }

        // Campo Lengua Originaria (solo para EIB)
        if (state.mostrarLenguaOriginaria) {
            var expandedLengua by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedLengua,
                onExpandedChange = { expandedLengua = it }
            ) {
                OutlinedTextField(
                    value = state.lenguaOriginaria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Lengua Originaria (LO)") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLengua)
                    },
                    modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true).fillMaxWidth(),
                    isError = state.lenguaOriginariaError != null,
                    supportingText = state.lenguaOriginariaError?.let { { Text(it) } }
                )

                ExposedDropdownMenu(
                    expanded = expandedLengua,
                    onDismissRequest = { expandedLengua = false }
                ) {
                    PreselectionConstants.LENGUAS_ORIGINARIAS.forEach { lengua ->
                        DropdownMenuItem(
                            text = { Text(lengua) },
                            onClick = {
                                onUpdateLenguaOriginaria(lengua)
                                expandedLengua = false
                            }
                        )
                    }
                }
            }
        }

        // Botones
        Button(
            onClick = onNavigateNext,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = state.habilitarContinuar
        ) {
            Text("Continuar")
        }

        OutlinedButton(
            onClick = onReset,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpiar Formulario")
        }
    }
}