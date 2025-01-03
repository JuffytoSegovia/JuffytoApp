package com.juffyto.juffyto.ui.screens.calculator.sharing

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import android.graphics.Canvas

object ShareUtils {
    // Mantener el méttodo original para preselección
    fun sharePreselectionReport(preselectionLayout: View) {
        captureAndShare(preselectionLayout, "preselección")
    }

    // Añadir sobrecarga para selección
    fun shareSelectionReport(selectionLayout: View) {
        captureAndShare(selectionLayout, "selección")
    }

    // Méttodo privado que maneja la lógica común
    private fun captureAndShare(layout: View, type: String) {
        try {
            // Capturar la vista del reporte
            val bitmap = Bitmap.createBitmap(
                layout.width,
                layout.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            layout.draw(canvas)

            // Guardar el bitmap
            val imagesFolder = File(layout.context.cacheDir, "images").apply { mkdirs() }
            val imageFile = File(imagesFolder, "reporte_${type}.png")

            FileOutputStream(imageFile).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }

            val contentUri = FileProvider.getUriForFile(
                layout.context,
                "${layout.context.packageName}.fileprovider",
                imageFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                setType("image/*")  // Usando setType en lugar de type =
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_TEXT, """
                    Reporte de $type generado con la app "Juffyto"
                    Instala la app aquí: https://play.google.com/store/apps/details?id=com.juffyto.juffyto
                """.trimIndent())
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            layout.context.startActivity(Intent.createChooser(shareIntent, "Compartir reporte"))
        } catch (e: Exception) {
            Log.e("ShareUtils", "Error al compartir reporte", e)
            Toast.makeText(
                layout.context,
                "No se pudo compartir el reporte",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}