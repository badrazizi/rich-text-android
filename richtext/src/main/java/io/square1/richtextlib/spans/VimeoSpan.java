/*
 * Copyright (c) 2015. Roberto  Prato <https://github.com/robertoprato>
 *
 *  *
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package io.square1.richtextlib.spans;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.text.TextUtils;

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.R;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.util.UniqueId;

/**
 * Created by roberto on 23/06/15.
 */
public class VimeoSpan extends UrlBitmapSpan implements ClickableSpan {

    public static final int DEFAULT_WIDTH = 480;
    public static final int DEFAULT_HEIGHT = 360;


    public static final Creator<VimeoSpan> CREATOR  = DynamicParcelableCreator.getInstance(VimeoSpan.class);
    public static final int TYPE = UniqueId.getType();

    private Drawable mVimeoIcon;
    private String mVimeoId;

    public String getVimeoId(){
        return mVimeoId;
    }

    public VimeoSpan(){
        super();
    }

    public VimeoSpan(String vimeoId, int maxWidth){
        this(vimeoId, DEFAULT_WIDTH,DEFAULT_HEIGHT,maxWidth);
        mVimeoId = vimeoId;
    }

    public VimeoSpan(String vimeoId, int width, int height, int maxWidth){
        super(Uri.EMPTY, NumberUtils.INVALID, NumberUtils.INVALID, maxWidth);
        EmbedUtils.getVimeoThumbnailUrl(vimeoId, new EmbedUtils.ThumbnailUrlCallback() {
            @Override
            public void onReceived(String result) {
                if(result != null || !TextUtils.isEmpty(result)) {
                    setUri(Uri.parse(result));
                }
            }
        });

        mVimeoId = vimeoId;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void readFromParcel(Parcel src) {
        super.readFromParcel(src);
        mVimeoId = src.readString();

    }

    @Override
    public void onSpannedSetToView(RichContentView view) {
        super.onSpannedSetToView(view);

        if(mVimeoIcon == null){
            mVimeoIcon = view.getContext().getResources().getDrawable(R.drawable.video_play);
            mVimeoIcon.setBounds(0,0,mVimeoIcon.getIntrinsicWidth(), mVimeoIcon.getIntrinsicHeight());
        }

    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mVimeoId);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        super.draw(canvas,text,start,end,x,top,y,bottom,paint);
        if(getBitmap() == null) return;

        final Rect bitmapBounds = mVimeoIcon.getBounds();

        Rect currentImage = getImageBounds();

        int transY = bottom - (bitmapBounds.bottom / 2) - (currentImage.height() / 2) ;

        canvas.save();
        //center
        int containerViewMeasure = mRef.get().getMeasuredWidth();

        x = x + (containerViewMeasure - bitmapBounds.width()) / 2;
        x = x - mRef.get().getPaddingLeft();
        canvas.translate(x, transY);

        if (mVimeoIcon != null) {
            mVimeoIcon.setBounds(bitmapBounds);
            mVimeoIcon.draw(canvas);
        }

        canvas.restore();
    }


    @Override
    public String getAction() {
        return "https://player.vimeo.com/video/" + getVimeoId();
    }
}
