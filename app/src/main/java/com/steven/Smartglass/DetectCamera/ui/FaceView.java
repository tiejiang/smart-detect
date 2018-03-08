package com.steven.Smartglass.DetectCamera.ui;

import com.steven.Smartglass.DetectCamera.activity.CameraActivity;
import com.steven.Smartglass.DetectCamera.camera.CameraInterface;
import com.steven.Smartglass.DetectCamera.util.EventUtil;
import com.steven.Smartglass.DetectCamera.util.Util;
import com.steven.Smartglass.R;
import com.steven.Smartglass.ResultActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Face;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import static android.app.Activity.RESULT_OK;
import static com.steven.Smartglass.DetectCamera.activity.CameraActivity.Detectfinish;

public class FaceView extends ImageView {
    private static final String TAG = "FaceView";
    private Context mContext;
    private Paint mLinePaint;
    private Face[] mFaces;
    private Matrix mMatrix = new Matrix();
    private RectF mRect = new RectF();
    private Drawable mFaceIndicator = null;

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initPaint();
        mContext = context;
        mFaceIndicator = getResources().getDrawable(R.drawable.ic_face_find_2);
    }


    public void setFaces(Face[] faces) {
        this.mFaces = faces;
        invalidate();
    }

    public void clearFaces() {
        mFaces = null;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        if (mFaces == null || mFaces.length < 1) {
            return;
        }
        boolean isMirror = false;
        int Id = CameraInterface.getInstance().getCameraId();
        if (Id == CameraInfo.CAMERA_FACING_BACK) {
            isMirror = false; //后置Camera无需mirror
        } else if (Id == CameraInfo.CAMERA_FACING_FRONT) {
            isMirror = true;  //前置Camera需要mirror
        }
        Util.prepareMatrix(mMatrix, isMirror, 90, getWidth(), getHeight());
        canvas.save();
        mMatrix.postRotate(0); //Matrix.postRotate默认是顺时针
        canvas.rotate(-0);   //Canvas.rotate()默认是逆时针
        for (int i = 0; i < mFaces.length; i++) {
            mRect.set(mFaces[i].rect);
            mMatrix.mapRect(mRect);
            mFaceIndicator.setBounds(Math.round(mRect.left), Math.round(mRect.top),
                    Math.round(mRect.right), Math.round(mRect.bottom));
            mFaceIndicator.draw(canvas);
//			canvas.drawRect(mRect, mLinePaint);
        }
        canvas.restore();
        super.onDraw(canvas);
        CameraActivity.stopGoogleFaceDetect();
        try {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    CameraInterface.getInstance().doTakePicture();
                }
            }, 1000);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent in = new Intent();
                    Detectfinish.setResult( RESULT_OK, in );
                    Detectfinish.finish();
                }
            }, 2000);
        }catch (Exception e){
            System.out.println("-----------------------照相崩溃");
        }
    }

    private void initPaint() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		int color = Color.rgb(0, 150, 255);
        int color = Color.rgb(98, 212, 68);
//		mLinePaint.setColor(Color.RED);
        mLinePaint.setColor(color);
        mLinePaint.setStyle(Style.STROKE);
        mLinePaint.setStrokeWidth(5f);
        mLinePaint.setAlpha(180);
    }
}
