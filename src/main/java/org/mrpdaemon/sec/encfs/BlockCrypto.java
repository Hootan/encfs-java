/*
 * EncFS Java Library
 * Copyright (C) 2013 encfs-java authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */
package org.mrpdaemon.sec.encfs;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidAlgorithmParameterException;

// Static methods for block cryptography
class BlockCrypto {

	private static final ThreadLocal<Cipher> BLOCK_CIPHER_CACHE = new ThreadLocal<>();

	// Returns a block cipher object from the thread-local cache
	static Cipher newBlockCipher() throws EncFSUnsupportedException {
		Cipher cipher = BLOCK_CIPHER_CACHE.get();
		if (cipher == null) {
			cipher = EncFSCrypto.getCipher(EncFSCrypto.BLOCK_CIPHER);
			BLOCK_CIPHER_CACHE.set(cipher);
		}
		return cipher;
	}

	// Common method to perform a block operation
	private static byte[] blockOperation(EncFSVolume volume, byte[] ivSeed,
			byte[] data, int opMode) throws InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException,
			EncFSUnsupportedException {
		//Cipher cipher = volume.getBlockCipher();
		Cipher cipher = BlockCrypto.newBlockCipher();
		EncFSCrypto.cipherInit(volume, opMode, cipher, ivSeed);
		return cipher.doFinal(data);
	}

	// Perform block encryption
	protected static byte[] blockDecrypt(EncFSVolume volume, byte[] ivSeed,
			byte[] data) throws InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException,
            EncFSUnsupportedException {
		return blockOperation(volume, ivSeed, data, Cipher.DECRYPT_MODE);
	}

	// Perform block decryption
	protected static byte[] blockEncrypt(EncFSVolume volume, byte[] ivSeed,
			byte[] data) throws IllegalBlockSizeException,
			InvalidAlgorithmParameterException, BadPaddingException,
            EncFSUnsupportedException {
		return blockOperation(volume, ivSeed, data, Cipher.ENCRYPT_MODE);
	}
}
