package edu.ucne.registrotecnicos.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color

@Composable
fun ConfirmDeletionDialog(
    openDialog: MutableState<Boolean>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String = "Confirmar eliminación",
    message: String = "¿Quieres eliminar este elemento? Esta acción no se puede deshacer.",
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            icon = { Icon(Icons.Filled.Delete, contentDescription = null) },
            title = { Text(text = title) },
            text = {
                Column {
                    Text(message.substringBefore("Esta acción no se puede deshacer."))
                    Text(
                        "Esta acción no se puede deshacer.",
                        color = Color.Red
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        openDialog.value = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
