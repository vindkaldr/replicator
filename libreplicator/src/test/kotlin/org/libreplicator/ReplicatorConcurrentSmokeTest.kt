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

package org.libreplicator

import org.junit.Test
import org.libreplicator.api.ReplicatorNode
import org.libreplicator.common.test.ConcurrentExecutor
import org.libreplicator.testdouble.RemoteEventLogObserverDummy

class ReplicatorConcurrentSmokeTest {
    private companion object {
        private val LOG = "log"
    }

    private val libReplicatorFactory = LibReplicatorFactory()

    private val node1 = libReplicatorFactory.createReplicatorNode("nodeId1", "localhost", 12345)
    private val node2 = libReplicatorFactory.createReplicatorNode("nodeId2", "localhost", 12346)
    private val node3 = libReplicatorFactory.createReplicatorNode("nodeId3", "localhost", 12347)

    private val concurrentExecutor = ConcurrentExecutor(threadPoolSize = 3)

    @Test
    fun replicator_shouldHandleUnavailabilityOfNodes() {
        concurrentExecutor.submitExecution { testRunReplicator(node1, listOf(node2, node3)) }
        concurrentExecutor.submitExecution { testRunReplicator(node2, listOf(node1, node3)) }
        concurrentExecutor.submitExecution { testRunReplicator(node3, listOf(node1, node2)) }
        concurrentExecutor.awaitExecutions()
    }

    private fun testRunReplicator(localNode: ReplicatorNode, remoteNodes: List<ReplicatorNode>) {
        val replicator = libReplicatorFactory.createReplicator(localNode, remoteNodes)

        var subscription = replicator.subscribe(RemoteEventLogObserverDummy())
        replicator.replicate(libReplicatorFactory.createLocalEventLog(LOG))
        subscription.unsubscribe()

        subscription = replicator.subscribe(RemoteEventLogObserverDummy())
        replicator.replicate(libReplicatorFactory.createLocalEventLog(LOG))
        subscription.unsubscribe()
    }
}