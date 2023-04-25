package com.example.hikingbook.tool

import android.content.Context
import com.example.hikingbook.tool.Translator.mlTranslator
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation.getClient
import com.google.mlkit.nl.translate.TranslatorOptions
import com.zqc.opencc.android.lib.ChineseConverter
import com.zqc.opencc.android.lib.ConversionType


suspend fun downloadModelAndTranslate(
    english: String,
    context: Context,
    success: (String) -> Unit,
    failure: () -> Unit = {}
)
{
//    val conditions = DownloadConditions.Builder().build()
    mlTranslator.downloadModelIfNeeded()
        .addOnSuccessListener {
            mlTranslator.translate(english).addOnSuccessListener {
                val S2TWP = ChineseConverter.convert(it, ConversionType.S2TWP, context)
                success(S2TWP)
            }
        }
        .addOnFailureListener {
            failure()
        }
}

suspend fun translate(english: String, context: Context, getChinese: (String) -> Unit) {
    downloadModelAndTranslate(
        english = english,
        context = context,
        success = {
            getChinese(it)
        },
        failure = {
            getChinese("")
        }
    )
}

object Translator {
    private val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.CHINESE)
        .build()
    val mlTranslator = getClient(options)

    suspend fun downloadModel() {
        mlTranslator.downloadModelIfNeeded()
    }
}
