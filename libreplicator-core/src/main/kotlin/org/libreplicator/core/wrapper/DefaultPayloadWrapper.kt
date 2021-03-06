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

package org.libreplicator.core.wrapper

import org.libreplicator.core.wrapper.api.PayloadWrapper
import org.libreplicator.crypto.api.Cipher
import org.libreplicator.json.api.JsonMapper
import org.libreplicator.core.model.ReplicatorMessage
import org.libreplicator.core.model.ReplicatorPayload
import javax.inject.Inject

class DefaultPayloadWrapper @Inject constructor(
    private val groupId: String,
    private val jsonMapper: JsonMapper,
    private val cipher: Cipher
) : PayloadWrapper {
    override fun wrap(payload: ReplicatorPayload): ReplicatorMessage {
        return ReplicatorMessage(groupId, cipher.encrypt(jsonMapper.write(payload)))
    }

    override fun unwrap(message: ReplicatorMessage): ReplicatorPayload {
        return jsonMapper.read(cipher.decrypt(message.payload), ReplicatorPayload::class)
    }
}
