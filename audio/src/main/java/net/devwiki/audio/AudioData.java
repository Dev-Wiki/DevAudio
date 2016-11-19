package net.devwiki.audio;

/**
 * 音频数据
 * Created by DevWiki on 2016/11/19.
 */

public class AudioData {

    public static final int DATA_TYPE_PCM = 0x001;
    public static final int DATA_TYPE_AMR = 0x002;

    public int dataType;
    public byte[] data;
}
