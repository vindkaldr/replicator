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

package org.libreplicator.server

import org.libreplicator.api.AlreadySubscribedException
import org.libreplicator.api.NotSubscribedException
import org.libreplicator.api.Observer
import org.libreplicator.api.ReplicatorNode
import org.libreplicator.api.Subscription
import org.libreplicator.crypto.api.Cipher
import org.libreplicator.gateway.api.InternetGateway
import org.libreplicator.httpserver.api.HttpServer
import org.libreplicator.json.api.JsonMapper
import org.libreplicator.model.ReplicatorMessage
import org.libreplicator.server.api.ReplicatorServer
import org.slf4j.LoggerFactory
import javax.inject.Inject

const val LIBREPLICATOR_DESCRIPTION = "libreplicator"

class DefaultReplicatorServer @Inject constructor(
        private val jsonMapper: JsonMapper,
        private val cipher: Cipher,
        private val httpServer: HttpServer,
        private val internetGateway: InternetGateway,
        private val localNode: ReplicatorNode
) : ReplicatorServer {
    private companion object {
        private val logger = LoggerFactory.getLogger(DefaultReplicatorServer::class.java)
    }

    private var hasSubscription = false

    override suspend fun subscribe(observer: Observer<ReplicatorMessage>): Subscription {
        logger.trace("Subscribing to server..")
        if (hasSubscription) {
            throw AlreadySubscribedException()
        }
        httpServer.start(localNode.port, "/sync", ReplicatorSyncServlet(jsonMapper, cipher, observer))
        hasSubscription = true

//        async(CommonPool) {
//            internetGateway.addPortMapping(AddPortMapping(localNode.port, InternetProtocol.TCP, localNode.port, LIBREPLICATOR_DESCRIPTION))
//        }.await()

        return object : Subscription {
            override suspend fun unsubscribe() {
                logger.trace("Unsubscribing from server..")
                if (!hasSubscription) {
                    throw NotSubscribedException()
                }
                httpServer.stop()
                hasSubscription = false
//                async(CommonPool) {
//                    internetGateway.deletePortMapping(DeletePortMapping(localNode.port, InternetProtocol.TCP))
//                }
            }
        }
    }

    override fun hasSubscription(): Boolean = hasSubscription
}
