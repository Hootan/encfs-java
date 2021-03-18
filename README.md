# encfs-java

A modified version of encfs-java for MiXplorer file manager.

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
  private static byte[] blockOperation(EncFSVolume volume, byte[] ivSeed,
			byte[] data, int opMode) throws InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException,
			EncFSUnsupportedException {
		//Cipher cipher = volume.getBlockCipher();
		Cipher cipher = BlockCrypto.newBlockCipher();
		EncFSCrypto.cipherInit(volume, opMode, cipher, ivSeed);
		return cipher.doFinal(data);
	}
```
###### StreamCrypto.java:
```java
  public static byte[] streamDecrypt(EncFSVolume volume, byte[] ivSeed,
			byte[] data) throws EncFSUnsupportedException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException {
		//Cipher streamCipher = volume.getStreamCipher();
		Cipher streamCipher = StreamCrypto.newStreamCipher();
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
	.withPbkdf2Provider(new EncFSPBKDF2Provider() {
        @Override
        public byte[] doPBKDF2(String password, int saltLen, byte[] salt, int iterations, int keyLen) {
            try {
                return new PBKDF2Engine(new PBKDF2Parameters("HmacSHA1", "ISO-8859-1", salt, iterations))
                            .deriveKey(password.toCharArray(), keyLen + saltLen);
            } catch (Exception e) {
                MiXLog.e(e);
                return null;
            }
        }
    });
```

#### Added 'synchronized' to all methods in EncFSCrypto.java
