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

##### Bugs:
###### EncFSVolume.java:
```java
public long getDecryptedFileLength(long encryptedFileLength) {

...

if (headerLength > 0) {
  long blockLength = volumeConfig.getEncryptedFileBlockSizeInBytes()
      ;//+ headerLength;
...
```

```java
public long getEncryptedFileLength(long decryptedFileLength) {

...

if (headerLength > 0) {
  long blockLength = volumeConfig.getEncryptedFileBlockSizeInBytes()
      //+ headerLength;
      - headerLength;
...
```
