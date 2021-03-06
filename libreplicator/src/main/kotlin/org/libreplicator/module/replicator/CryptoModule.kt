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
import org.libreplicator.component.replicator.ReplicatorScope
import org.libreplicator.crypto.DefaultCipher
import org.libreplicator.crypto.api.Cipher
import org.libreplicator.module.replicator.setting.ReplicatorCryptoSettings

@Module
class CryptoModule(private val cryptoSettings: ReplicatorCryptoSettings) {
    @Provides @ReplicatorScope
    fun provideCipher(): Cipher {
        if (cryptoSettings.isEncryptionEnabled && cryptoSettings.sharedSecret.isNotBlank()) {
            return DefaultCipher(cryptoSettings.sharedSecret)
        }
        return object : Cipher {
            override fun encrypt(content: String): String = content
            override fun decrypt(encryptedContent: String): String = encryptedContent
        }
    }
}
