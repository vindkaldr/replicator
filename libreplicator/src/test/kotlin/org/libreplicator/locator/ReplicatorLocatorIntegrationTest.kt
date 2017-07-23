/*
 *     Copyright (C) 2016  Mihaly Szabo <szmihaly91@gmail.com/>
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

package org.libreplicator.locator

import kotlinx.coroutines.experimental.runBlocking
import org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder
import org.junit.Assert.assertThat
import org.junit.Test
import org.libreplicator.locator.testdouble.NodeLocatorFake
import org.libreplicator.testdouble.RemoteEventLogObserverMock

class ReplicatorLocatorIntegrationTest {
    private companion object {
        private val LOG_1 = "log1"
        private val LOG_2 = "log2"
    }

    private val nodeLocatorFake = NodeLocatorFake()

    private val localReplicatorFactory = LibReplicatorFactory(nodeLocatorFake)
    private val localNode = localReplicatorFactory.createReplicatorNode("nodeId1", "localhost", 12345)

    private val remoteReplicatorFactory = LibReplicatorFactory(nodeLocatorFake)
    private val remoteNode = remoteReplicatorFactory.createReplicatorNode("nodeId2", "localhost", 12346)

    private val localReplicator = localReplicatorFactory.createReplicator(localNode = localNode,
            remoteNodes = listOf(localReplicatorFactory.createReplicatorNode(remoteNode.nodeId, "", 0)))
    private val remoteReplicator = remoteReplicatorFactory.createReplicator(localNode = remoteNode,
            remoteNodes = listOf(remoteReplicatorFactory.createReplicatorNode(localNode.nodeId, "", 0)))

    private val localLogObserver = RemoteEventLogObserverMock(numberOfExpectedEventLogs = 1)
    private val remoteLogObserver = RemoteEventLogObserverMock(numberOfExpectedEventLogs = 1)

    @Test
    fun replicator_shouldReplicateLogsBetweenNodes() = runBlocking {
        val localSubscription = localReplicator.subscribe(localLogObserver)
        val remoteSubscription = remoteReplicator.subscribe(remoteLogObserver)

        localReplicator.replicate(localReplicatorFactory.createLocalEventLog(LOG_1))
        remoteReplicator.replicate(localReplicatorFactory.createLocalEventLog(LOG_2))

        assertThat(localLogObserver.getObservedLogs(), containsInAnyOrder(LOG_2))
        assertThat(remoteLogObserver.getObservedLogs(), containsInAnyOrder(LOG_1))

        localSubscription.unsubscribe()
        remoteSubscription.unsubscribe()
    }
}
