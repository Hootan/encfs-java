# encfs-java

A modified version of encfs-java.

The original project:
https://github.com/mrpdaemon/encfs-java

### Changes:

##### Cipher is not thread-safe:
###### EncFSVolume.java:
```java
//private Cipher streamCipher;
//private Cipher blockCipher;
...

//streamCipher = StreamCrypto.newStreamCipher();
//blockCipher = BlockCrypto.newBlockCipher();
...

/*public Cipher getStreamCipher() {
	return streamCipher;
}
/*public Cipher getBlockCipher() {
	return blockCipher;
}*/
```
###### BlockCrypto.java:
```java
  
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
		Cipher cipher = newBlockCipher();
		EncFSCrypto.cipherInit(volume, opMode, cipher, ivSeed);
		return cipher.doFinal(data);
	}

```
###### StreamCrypto.java:
```java
  
	private static final ThreadLocal<Cipher> STREAM_CIPHER_CACHE = new ThreadLocal<>();

	// Returns a stream cipher from the thread-local cache
	public static Cipher newStreamCipher() throws EncFSUnsupportedException {
		Cipher cipher = STREAM_CIPHER_CACHE.get();
		if (cipher == null) {
			cipher = EncFSCrypto.getCipher(EncFSCrypto.STREAM_CIPHER);
			STREAM_CIPHER_CACHE.set(cipher);
		}
		return cipher;
	}

    // Stream decryption implementation
    public static byte[] streamDecrypt(EncFSVolume volume, byte[] ivSeed,
                                       byte[] data) throws EncFSUnsupportedException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException {
        Cipher streamCipher = newStreamCipher();
        return streamDecrypt(streamCipher, volume.getMAC(), volume.getKey(),
                volume.getIV(), ivSeed, data);
    }

```
  
##### Compatibility:
  - Arrays.copyOf() to EncFSUtil.copyOf()
  - Arrays.copyOfRange() to EncFSUtil.copyOfRange()
  - new IOException(e) to new IOException(e.getMessage())


#### Added Fast Java PBKDF2 provider
```java
import org.mrpdaemon.sec.encfs.PBKDF2Engine;
import org.mrpdaemon.sec.encfs.PBKDF2Parameters;
...    

new EncFSVolumeBuilder()
	.withFileProvider(encFSFileProvider)
	.withPbkdf2Provider(pbkProvider);

private static final EncFSPBKDF2Provider pbkProvider = new EncFSPBKDF2Provider() {
        @Override
        public byte[] doPBKDF2(String password, byte[] salt, int iterations, int keySize) {
            try {
                SecretKeyFactory f = SecretKeyFactory.getInstance(SupportUtils.hasKitKat() ?
                        "PBKDF2WithHmacSHA1And8bit" : "PBKDF2WithHmacSHA1");
                KeySpec ks = new PBEKeySpec(password.toCharArray(), salt, iterations,
                        keySize + EncFSVolume.IV_LENGTH_IN_BYTES * 8);
                SecretKey key = f.generateSecret(ks);
                return key.getEncoded();

            } catch (Throwable e) {
                Log.e("PBKDF2", TextUtil.msg(e));
                return null;
            }
        }
    };
```

#### Added 'synchronized' to some methods in EncFSCrypto.java
```java
//
```
