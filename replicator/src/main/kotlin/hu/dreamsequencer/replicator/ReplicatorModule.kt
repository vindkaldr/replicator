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

package hu.dreamsequencer.replicator

import com.google.inject.AbstractModule
import hu.dreamsequencer.replicator.module.ReplicatorBoundaryModule
import hu.dreamsequencer.replicator.module.ReplicatorInteractorModule
import hu.dreamsequencer.replicator.module.ReplicatorJsonModule
import hu.dreamsequencer.replicator.module.ReplicatorModelModule
import hu.dreamsequencer.replicator.module.ReplicatorNetworkModule

class ReplicatorModule : AbstractModule() {
    override fun configure() {
        install(ReplicatorBoundaryModule())
        install(ReplicatorInteractorModule())
        install(ReplicatorJsonModule())
        install(ReplicatorModelModule())
        install(ReplicatorNetworkModule())
    }
}
