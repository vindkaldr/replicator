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

package org.libreplicator.module.replicator

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.experimental.runBlocking
import org.libreplicator.api.LocalNode
import org.libreplicator.api.RemoteNode
import org.libreplicator.component.replicator.ReplicatorScope
import org.libreplicator.core.journal.DefaultReplicatorStateJournal
import org.libreplicator.core.journal.file.DefaultFileHandler
import org.libreplicator.core.journal.file.FileHandler
import org.libreplicator.crypto.api.Cipher
import org.libreplicator.json.api.JsonMapper
import org.libreplicator.core.model.ReplicatorState
import org.libreplicator.module.replicator.setting.ReplicatorJournalSettings

@Module
class JournalModule(
    private val journalSettings: ReplicatorJournalSettings,
    private val localNode: LocalNode,
    private val remoteNodes: List<RemoteNode>) {

    @Provides
    fun bindFileHandler(): FileHandler {
        return DefaultFileHandler()
    }

    @Provides @ReplicatorScope
    fun provideReplicatorState(
            fileHandler: FileHandler,
            jsonMapper: JsonMapper,
            cipher: Cipher
    ): ReplicatorState = runBlocking {
        if (!journalSettings.isJournalingEnabled) {
            return@runBlocking ReplicatorState(localNode, remoteNodes)
        }

        val replicatorStateJournal = DefaultReplicatorStateJournal(
            fileHandler, jsonMapper, cipher,
            journalSettings.directoryOfJournals, localNode, remoteNodes
        )
        val replicatorState = replicatorStateJournal.getReplicatorState().copy(localNode, remoteNodes)

        replicatorState.subscribe(replicatorStateJournal)

        return@runBlocking replicatorState
    }
}
