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

package org.libreplicator.network

import org.libreplicator.api.Observer
import org.libreplicator.api.ReplicatorNode
import org.libreplicator.api.Subscription
import org.libreplicator.interactor.api.LogRouter
import org.libreplicator.json.api.JsonMapper
import org.libreplicator.json.api.JsonReadException
import org.libreplicator.json.api.JsonWriteException
import org.libreplicator.model.ReplicatorMessage
import org.slf4j.LoggerFactory
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.net.SocketException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.concurrent.thread

class DefaultLogRouter
@Inject constructor(private val jsonMapper: JsonMapper, private val localNode: ReplicatorNode) : LogRouter {
    companion object {
        private val logger = LoggerFactory.getLogger(DefaultLogRouter::class.java)
        private val BUFFER_SIZE_IN_BYTES = 1024 * 1024
    }

    private lateinit var socket: DatagramSocket

    override fun send(remoteNode: ReplicatorNode, message: ReplicatorMessage) = synchronized(this) {
        if (socket.isClosed) {
            return
        }
        try {
            sendMessage(message, remoteNode)
        }
        catch (unknownHostException: UnknownHostException) {
            logger.error(unknownHostException.message, unknownHostException)
        }
        catch (jsonWriteException: JsonWriteException) {
            logger.error(jsonWriteException.message, jsonWriteException)
        }
    }

    override fun subscribe(messageObserver: Observer<ReplicatorMessage>): Subscription = synchronized(this) {
        socket = DatagramSocket(localNode.port)
        listeningForMessages(messageObserver)

        return object : Subscription {
            override fun unsubscribe() {
                socket.close()
            }
        }
    }

    private fun sendMessage(message: ReplicatorMessage, remoteNode: ReplicatorNode) {
        fun getNodeAddress(remoteNode: ReplicatorNode) = InetSocketAddress(remoteNode.url, remoteNode.port)

        val messageAsByteArray = jsonMapper.write(message).toByteArray()

        val packet = DatagramPacket(messageAsByteArray, messageAsByteArray.size, getNodeAddress(remoteNode))
        socket.send(packet)
    }

    private fun listeningForMessages(messageObserver: Observer<ReplicatorMessage>) {
        thread {
            while (true) {
                try {
                    messageObserver.observe(readMessage())
                }
                catch (jsonReadException: JsonReadException) {
                    logger.error(jsonReadException.message, jsonReadException)
                }
                catch (socketException: SocketException) {
                    if (!socket.isClosed) {
                        logger.error(socketException.message, socketException)
                        socket.close()
                    }
                    break
                }
            }
        }
    }

    private fun readMessage(): ReplicatorMessage {
        fun extractMessage(packet: DatagramPacket) = String(packet.data.copyOfRange(0, packet.length))

        val packet = DatagramPacket(ByteArray(BUFFER_SIZE_IN_BYTES), BUFFER_SIZE_IN_BYTES)
        socket.receive(packet)

        return jsonMapper.read(extractMessage(packet), ReplicatorMessage::class)
    }
}