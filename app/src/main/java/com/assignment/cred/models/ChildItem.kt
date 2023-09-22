package com.assignment.cred.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChildItem(val title: String, val subtitle: String) : Parcelable