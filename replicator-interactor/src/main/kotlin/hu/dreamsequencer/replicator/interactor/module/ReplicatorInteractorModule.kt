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

package hu.dreamsequencer.replicator.interactor.module

import com.google.inject.PrivateModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import hu.dreamsequencer.replicator.interactor.DefaultLogDispatcher
import hu.dreamsequencer.replicator.interactor.api.LogDispatcher
import hu.dreamsequencer.replicator.interactor.api.LogDispatcherFactory

class ReplicatorInteractorModule : PrivateModule() {
    override fun configure() {
        expose(LogDispatcherFactory::class.java)
        install(FactoryModuleBuilder()
                .implement(LogDispatcher::class.java, DefaultLogDispatcher::class.java)
                .build(LogDispatcherFactory::class.java))
    }
}
