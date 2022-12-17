package com.whyranoid.presentation.util

import com.whyranoid.presentation.util.EventFlow.Companion.DEFAULT_REPLAY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.atomic.AtomicBoolean

interface EventFlow<out T> : Flow<T> {

    companion object {
        const val DEFAULT_REPLAY = 2
    }
}

interface MutableEventFlow<T> : EventFlow<T>, FlowCollector<T>

private class EventFlowSlot<T>(val value: T) {

    private val consumed = AtomicBoolean(false)

    fun markConsumed() = consumed.getAndSet(true)
}

private class EventFlowImpl<T>(replay: Int) : MutableEventFlow<T> {

    private val flow: MutableSharedFlow<EventFlowSlot<T>> = MutableSharedFlow(replay)

    override suspend fun collect(collector: FlowCollector<T>) =
        flow.collect { slot ->
            if (slot.markConsumed().not()) {
                collector.emit(slot.value)
            }
        }

    override suspend fun emit(value: T) {
        flow.emit(EventFlowSlot(value))
    }
}

private class ReadOnlyEventFlow<T>(flow: EventFlow<T>) : EventFlow<T> by flow

@Suppress("FunctionName")
fun <T> MutableEventFlow(replay: Int = DEFAULT_REPLAY): MutableEventFlow<T> = EventFlowImpl(replay)

fun <T> MutableEventFlow<T>.asEventFlow(): EventFlow<T> = ReadOnlyEventFlow(this)
