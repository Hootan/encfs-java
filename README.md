# encfs-java

A modified version of encfs-java for MiXplorer file manager.

BusyBox v1.29.3 from > https://github.com/mrpdaemon/encfs-java

##### Modified:
###### libbb/missing_syscalls.c:
```c
Line 42:
/*# if __ANDROID_API__ < 21
int tcdrain(int fd)
{
	return ioctl(fd, TCSBRK, 1);
}
# endif*/
```
