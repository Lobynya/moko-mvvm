/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.mvvm.dispatcher

expect class EventsDispatcher<ListenerType : Any>() {
    fun dispatchEvent(block: ListenerType.() -> Unit)
}
