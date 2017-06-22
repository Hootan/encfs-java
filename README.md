# encfs-java

A modified version of encfs-java for MiXplorer file manager.

The original project:
https://github.com/mrpdaemon/encfs-java

### Changes:

##### Cipher is not thread-safe:
###### BlockCrypto:
  - volume.getBlockCipher() to BlockCrypto.newBlockCipher()
###### StreamCrypto:
  - volume.getStreamCipher() to StreamCrypto.newStreamCipher()
  
##### Compatibility:
  - Arrays.copyOf() to EncFSUtil.copyOf()
  - Arrays.copyOfRange() to EncFSUtil.copyOfRange()
  - new IOException(e) to new IOException(e.getMessage())

##### Wrong codes:
###### EncFSVolume.java:
- getEncryptedFileLength():
```java
long blockLength = volumeConfig.getEncryptedFileBlockSizeInBytes() - headerLength;
```

- getDecryptedFileLength():
```java
long blockLength = volumeConfig.getEncryptedFileBlockSizeInBytes();
```
