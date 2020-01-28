/*
 * Copyright 2016-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.coroutines.jdk9

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.reactive.collect
import java.util.concurrent.Flow.*
import org.reactivestreams.FlowAdapters

/**
 * Transforms the given reactive [Publisher] into [Flow].
 * Use [buffer] operator on the resulting flow to specify the size of the backpressure.
 * More precisely, it specifies the value of the subscription's [request][Subscription.request].
 * `1` is used by default.
 *
 * If any of the resulting
  flow transformations fails, subscription is immediately cancelled and all in-flights elements
 * are discarded.*
 * This function is integrated with `ReactorContext` from `kotlinx-coroutines-reactor` module,
 * see its documentation for additional details.
 */
public fun <T : Any> Publisher<T>.asFlow(): Flow<T> =
        FlowAdapters.toPublisher(this).asFlow()

/**
 * Transforms the given flow to a reactive specification compliant [Publisher].
 *
 * This function is integrated with `ReactorContext` from `kotlinx-coroutines-reactor` module,
 * see its documentation for additional details.
 */
public fun <T : Any> Flow<T>.asPublisher(): Publisher<T> {
    val reactivePublisher : org.reactivestreams.Publisher<T> = this.asPublisher<T>()
    return FlowAdapters.toFlowPublisher(reactivePublisher)
}

/**
 * Subscribes to this [Publisher] and performs the specified action for each received element.
 * Cancels subscription if any exception happens during collect.
 */
public suspend fun <T> Publisher<T>.collect(action: (T) -> Unit) =
    FlowAdapters.toPublisher(this).collect(action)
