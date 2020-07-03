/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.mvvm.utils

import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.cstr
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIControl
import platform.UIKit.UIControlEvents
import platform.darwin.NSObject
import platform.objc.OBJC_ASSOCIATION_RETAIN
import platform.objc.objc_setAssociatedObject
import kotlin.native.ref.WeakReference

fun <T : UIControl> T.setEventHandler(event: UIControlEvents, lambda: T.() -> Unit) {
    val lambdaTarget = ControlLambdaTarget(this, lambda)

    addTarget(
        target = lambdaTarget,
        action = NSSelectorFromString("action"),
        forControlEvents = event
    )

    objc_setAssociatedObject(
        `object` = this,
        key = "event$event".cstr,
        value = lambdaTarget,
        policy = OBJC_ASSOCIATION_RETAIN
    )
}

@ExportObjCClass
private class ControlLambdaTarget<T : Any>(
    ref: T,
    val lambda: T.() -> Unit
) : NSObject() {
    private val weakRef = WeakReference(ref)

    @ObjCAction
    fun action() {
        val ref = weakRef.get() ?: return
        lambda(ref)
    }
}
