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

package org.libreplicator.json.mixin

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.libreplicator.model.EventLog
import org.libreplicator.model.ReplicatorMessage
import org.libreplicator.model.TimeTable

class JournalEntryMixin
@JsonCreator constructor(@JsonProperty("eventLogs") val eventLogs: Set<EventLog>,
                         @JsonProperty("timeTable") val timeTable: TimeTable,
                         @JsonProperty("replicatorMessage") val replicatorMessage: ReplicatorMessage) {
    @JsonIgnore val id = 0L
    @JsonIgnore val committed = false
    @JsonIgnore val closed = false
}