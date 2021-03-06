/*
 *     Copyright (C) 2016  Mihály Szabó
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.libreplicator.core.time

import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.channels.actor
import org.libreplicator.core.time.api.TimeProvider

class SynchronizedTimeProvider(private val timeProvider: TimeProvider) : TimeProvider {
    private val timeProviderActor = actor<TimeProviderMessage> {
        for (message in channel) {
            when (message) {
                is TimeProviderMessage.GetTime -> message.deferred.complete(timeProvider.getTime())
            }
        }
    }

    override suspend fun getTime(): Long {
        val deferred = CompletableDeferred<Long>()
        timeProviderActor.send(TimeProviderMessage.GetTime(deferred))
        return deferred.await()
    }

    private sealed class TimeProviderMessage {
        class GetTime(val deferred: CompletableDeferred<Long>) : TimeProviderMessage()
    }
}
