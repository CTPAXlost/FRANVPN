package com.franvpn.app.ui.databinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * Extension function to inflate view binding
 */
inline fun <T : ViewBinding> ViewGroup.inflateBinding(
    factory: (LayoutInflater, ViewGroup, Boolean) -> T
): T = factory(LayoutInflater.from(context), this, false)
