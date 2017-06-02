# encfs-java

A modified version of encfs-java for MiXplorer file manager.

The original project:
https://github.com/mrpdaemon/encfs-java

# Changes:
Changed BlockCrypto:
  volume.getBlockCipher() 
    to 
  BlockCrypto.newBlockCipher()

Changed StreamCrypto:
  volume.getStreamCipher() to  StreamCrypto.newStreamCipher()
  
Chnaged Arrays.copyOf() to EncFSUtil.copyOf()
  
Chnaged Arrays.copyOfRange() to EncFSUtil.copyOfRange()

Added synchronized to mothods in EncFSCrypto.java

And some other tiny changes.
