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

import org.libreplicator.api.ReplicatorNode
import org.libreplicator.locator.api.NodeLocator
import javax.inject.Inject

class DefaultNodeLocator @Inject constructor(): NodeLocator {
    override fun addNode(node: ReplicatorNode) {}
    override fun removeNode(nodeId: String) {}
    override fun getNode(nodeId: String): ReplicatorNode? = null
}
