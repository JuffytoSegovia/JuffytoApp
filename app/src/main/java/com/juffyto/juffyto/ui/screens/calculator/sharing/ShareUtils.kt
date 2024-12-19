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
    fun shareReport(
        preselectionLayout: View, // Pasamos la vista del reporte
    ) {
        try {
            // Capturar la vista del reporte
            val bitmap = Bitmap.createBitmap(
                preselectionLayout.width,
                preselectionLayout.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            preselectionLayout.draw(canvas)

            // Guardar el bitmap
            val imagesFolder = File(preselectionLayout.context.cacheDir, "images").apply { mkdirs() }
            val imageFile = File(imagesFolder, "reporte_preselección.png")

            FileOutputStream(imageFile).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }

            val contentUri = FileProvider.getUriForFile(
                preselectionLayout.context,
                "${preselectionLayout.context.packageName}.fileprovider",
                imageFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_TEXT, """
                    Reporte generado con la app "Juffyto"
                    Instala la app aquí: https://play.google.com/store/apps/details?id=com.juffyto.juffyto
                """.trimIndent())
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            preselectionLayout.context.startActivity(Intent.createChooser(shareIntent, "Compartir reporte"))
        } catch (e: Exception) {
            Log.e("ShareUtils", "Error al compartir reporte", e)
            Toast.makeText(
                preselectionLayout.context,
                "No se pudo compartir el reporte",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}