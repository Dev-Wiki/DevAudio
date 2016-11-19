package net.devwiki.audio;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * AMR录音类
 * Created by DevWiki on 2016/11/19.
 */

public class AmrRecorder {

    private static final String TAG = "AudioRecorder";

    //录音设备为麦克风
    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    //音频采样率8000Hz,因为占用空间小，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    private static final int SAMPLE_RATE = 8000;
    //音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持
    private static final short AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    //音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
    private static final short CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;

    private AudioRecord mAudioRecord;
    private String mAmrPath;
    private Context mContext;
    private AudioManager mAudioManager;
    private FocusChangeListener mChangeListener;
    private boolean initResult = false;

    public AmrRecorder(Context context) {
        mContext = context;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mChangeListener = new FocusChangeListener();
    }

    public boolean initRecorder() {
        int bufferSizeInBytes = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
        if (bufferSizeInBytes == AudioRecord.ERROR_BAD_VALUE || bufferSizeInBytes == AudioRecord.ERROR) {
            return false;
        }
        try {
            mAudioRecord = new AudioRecord(AUDIO_SOURCE, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, bufferSizeInBytes);
            initResult = mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED;
        } catch (IllegalArgumentException e) {
            initResult = false;
        }
        return initResult;
    }

    public void startRecord(String filePath) {

    }

    private class FocusChangeListener implements AudioManager.OnAudioFocusChangeListener {

        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.w(TAG, "音频焦点发生变化:" + focusChange);
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    //短暂失去焦点
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    //永久失去焦点,要释放资源
                    Log.e(TAG, "音频焦点丢失!!!!!!!");
                    // Stop playback
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    //暂时失去AudioFocus，但是可以继续播放，不过要在降低音量
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    // 重新获得焦点
                    break;
                default:

                    break;
            }
        }
    }
}
