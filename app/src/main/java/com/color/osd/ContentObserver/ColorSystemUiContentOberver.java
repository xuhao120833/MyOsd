package com.color.osd.ContentObserver;

import static com.color.osd.ui.Source_View.i1;
import static com.color.osd.ui.Source_View.i2;
import static com.color.osd.ui.Source_View.i3;
import static com.color.osd.ui.Source_View.i4;

import android.content.Context;
import android.database.ContentObserver;
import android.provider.Settings;
import android.util.Log;
import android.os.Handler;

import com.color.osd.R;
import com.color.osd.broadcast.SourceReceiver;

import com.color.osd.ui.Source_View;

public class ColorSystemUiContentOberver extends ContentObserver {
    private static final String SYSTEMUI_OPEN_OTHER_SOURCE = "systemui_open_other_source";
    int fswitch = 0;
    Context mcontext;




    public ColorSystemUiContentOberver( Context mContext) {
        super(new Handler());
        mcontext = mContext;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        fswitch = Settings.System.getInt(mcontext.getContentResolver(),
                SYSTEMUI_OPEN_OTHER_SOURCE, 1);
        Log.d("fswitch值为", String.valueOf(fswitch));
        if (fswitch == 1) {
            if (SourceReceiver.volume_image1) {
                i1.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal4_slect_useful));
            } else {
                i1.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal4_slect));
            }
            Source_View.msetDrawable(Source_View.select);
            Source_View.select = 1;
        }

        if (fswitch == 2) {
            Source_View.msetDrawable(Source_View.select);
            i2.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal2_slect_useful));
            Source_View.select = 2;
        }


        if (fswitch == 3) {
            if (SourceReceiver.volume_image3) {
                i3.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal4_slect_useful));
            } else {
                i3.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal4_slect));
            }
            Source_View.msetDrawable(Source_View.select);
            Source_View.select = 3;
        }

        if (fswitch == 4) {
            if (SourceReceiver.volume_image3) {
                i4.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal4_slect_useful));
            } else {
                i4.setImageDrawable(mcontext.getResources().getDrawable(R.drawable.signal4_slect));
            }
            Source_View.msetDrawable(Source_View.select);
            Source_View.select = 4;
        }
    }

}