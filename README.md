# encfs-java

A modified version of encfs-java for MiXplorer file manager.

The original project:
https://github.com/mrpdaemon/encfs-java

### Changes:

##### Cipher is not thread-safe:
###### BlockCrypto.java:
  - volume.getBlockCipher() to BlockCrypto.newBlockCipher()
###### StreamCrypto.java:
  - volume.getStreamCipher() to StreamCrypto.newStreamCipher()
  
##### Compatibility:
  - Arrays.copyOf() to EncFSUtil.copyOf()
  - Arrays.copyOfRange() to EncFSUtil.copyOfRange()
  - new IOException(e) to new IOException(e.getMessage())

##### Wrong codes:
###### EncFSVolume.java:
- getDecryptedFileLength():
```java
if (headerLength > 0) {
  long blockLength = volumeConfig.getEncryptedFileBlockSizeInBytes()
      ;//+ headerLength;

  long numBlocks = ((size - 1) / blockLength) + 1;

  size -= numBlocks * headerLength;
}
```

- getEncryptedFileLength():
```java
if (headerLength > 0) {
  long blockLength = volumeConfig.getEncryptedFileBlockSizeInBytes()
      - headerLength;

  long numBlocks = ((size - 1) / blockLength) + 1;

  size += numBlocks * headerLength;
}
```
