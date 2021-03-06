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

package org.libreplicator.core.router.testdouble

import org.libreplicator.api.Observer
import org.libreplicator.api.Subscription
import org.libreplicator.core.server.api.ReplicatorServer

class ReplicatorServerStub constructor(private val subscription: Subscription) : ReplicatorServer {
    var observedObserver: Observer<String>? = null
    private var subscriptionCount = 0

    override suspend fun subscribe(observer: Observer<String>): Subscription {
        observedObserver = observer
        subscriptionCount += 1
        return subscription
    }

    fun hasBeenSubscribedTo() = subscriptionCount > 0
    fun hasBeenSubscribedToOnlyOnce() = subscriptionCount == 1
}
