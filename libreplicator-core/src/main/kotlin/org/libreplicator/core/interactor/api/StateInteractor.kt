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

package org.libreplicator.core.interactor.api

import org.libreplicator.api.LocalLog
import org.libreplicator.api.RemoteLog
import org.libreplicator.api.RemoteNode
import org.libreplicator.core.model.ReplicatorPayload

interface StateInteractor {
    suspend fun getNodesWithMissingLogs(localLog: LocalLog): Map<RemoteNode, ReplicatorPayload>
    suspend fun getMissingLogs(payload: ReplicatorPayload): List<RemoteLog>
}
