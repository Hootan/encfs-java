package org.mrpdaemon.sec.encfs.PBKDF2;

/*
 * see http://rtner.de/software/PBKDF2.html
 */
interface PRF {
    void init(byte[] P);

    byte[] doFinal(byte[] M);

    int getHLen();
}
