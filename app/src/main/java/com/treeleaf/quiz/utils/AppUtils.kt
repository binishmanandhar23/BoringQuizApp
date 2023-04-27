package com.treeleaf.quiz.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun Context.getBitmapFromAssets(filePath: String): Bitmap =
    assets.open(filePath).use(BitmapFactory::decodeStream)