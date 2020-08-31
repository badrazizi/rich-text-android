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

import io.square1.parcelable.DynamicParcelableCreator;
import io.square1.richtextlib.EmbedUtils;
import io.square1.richtextlib.R;
import io.square1.richtextlib.ui.RichContentView;
import io.square1.richtextlib.util.NumberUtils;
import io.square1.richtextlib.util.UniqueId;

/**
 * Created by roberto on 23/06/15.
 */
public class DailyMotionSpan extends UrlBitmapSpan implements ClickableSpan {

    public static final int DEFAULT_WIDTH = 480;
    public static final int DEFAULT_HEIGHT = 360;


    public static final Creator<DailyMotionSpan> CREATOR  = DynamicParcelableCreator.getInstance(DailyMotionSpan.class);
    public static final int TYPE = UniqueId.getType();

    private Drawable mDailyMotionIcon;
    private String mDailyMotionId;

    public String getDailyMotionId(){
        return mDailyMotionId;
    }

    public DailyMotionSpan(){
        super();
    }

    public DailyMotionSpan(String dailyMotionId, int maxWidth){
        this(dailyMotionId, DEFAULT_WIDTH, DEFAULT_HEIGHT, maxWidth);
        mDailyMotionId = dailyMotionId;
    }

    public DailyMotionSpan(String dailyMotionId, int width, int height, int maxWidth){
        super(Uri.parse(EmbedUtils.getDailyMotionThumbnailUrl(dailyMotionId)), NumberUtils.INVALID, NumberUtils.INVALID,maxWidth);
        mDailyMotionId = dailyMotionId;
    }


    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void readFromParcel(Parcel src) {
        super.readFromParcel(src);
        mDailyMotionId = src.readString();

    }

    @Override
    public void onSpannedSetToView(RichContentView view) {
        super.onSpannedSetToView(view);

        if(mDailyMotionIcon == null){
            mDailyMotionIcon = view.getContext().getResources().getDrawable(R.drawable.video_play);
            mDailyMotionIcon.setBounds(0,0,mDailyMotionIcon.getIntrinsicWidth(), mDailyMotionIcon.getIntrinsicHeight());
        }

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mDailyMotionId);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        super.draw(canvas,text,start,end,x,top,y,bottom,paint);
        if(getBitmap() == null) return;

        final Rect bitmapBounds = mDailyMotionIcon.getBounds();

        Rect currentImage = getImageBounds();

        int transY = bottom - (bitmapBounds.bottom / 2) - (currentImage.height() / 2) ;

        canvas.save();
        //center
        int containerViewMeasure = mRef.get().getMeasuredWidth();

        x = x + (containerViewMeasure - bitmapBounds.width()) / 2;
        x = x - mRef.get().getPaddingLeft();
        canvas.translate(x, transY);

        if (mDailyMotionIcon != null) {
            mDailyMotionIcon.setBounds(bitmapBounds);
            mDailyMotionIcon.draw(canvas);
        }

        canvas.restore();
    }


    @Override
    public String getAction() {
        return "https://www.dailymotion.com/embed/video/" + getDailyMotionId();
    }
}
