package com.example.heshitha.story;

/**
 * Created by Heshitha on 2/5/2015.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public class RoundImage extends Drawable {
    private final Bitmap mBitmap;
    private final Paint mPaint;
    private final RectF mRectF;
    private final int mBitmapWidth;
    private final int mBitmapHeight;

    public RoundImage(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int newWidth = 0;

        if(width == height){
            newWidth = width;
        }else if(width > height){
            newWidth = height;
        }else{
            newWidth = width;
        }

        float startX = 0;
        float startY = 0;

        startX = (width - newWidth) / 2f;
        startY = (height - newWidth) / 2f;

        //float scaleWidth = ((float)newWidth)/width;
        //float scaleHeight = ((float)newWidth)/height;

        //Matrix matrix = new Matrix();
        //matrix.postScale(scaleWidth, scaleHeight);

        mBitmap = Bitmap.createBitmap(bitmap, (int)startX, (int)startY, newWidth, newWidth);
        mRectF = new RectF();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        final BitmapShader shader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(shader);

        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
    }

    @Override
    public void draw(Canvas canvas) {
        //canvas.drawOval(mRectF, mPaint);
        float cx = mBitmapWidth / 2f;
        float cy = mBitmapHeight / 2f;
        float radius = 0;
        if(mBitmapHeight == mBitmapWidth){
            radius = mBitmapHeight / 2f;
        }
        else if(mBitmapHeight > mBitmapWidth){
            radius = mBitmapWidth / 2f;
        }
        else{
            radius = mBitmapHeight / 2f;
        }

        canvas.drawCircle(cx, cy, radius, mPaint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mRectF.set(bounds);
    }

    @Override
    public void setAlpha(int alpha) {
        if (mPaint.getAlpha() != alpha) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmapWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmapHeight;
    }

    public void setAntiAlias(boolean aa) {
        mPaint.setAntiAlias(aa);
        invalidateSelf();
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        mPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @Override
    public void setDither(boolean dither) {
        mPaint.setDither(dither);
        invalidateSelf();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

}
