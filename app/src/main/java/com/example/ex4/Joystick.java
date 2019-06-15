package com.example.ex4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class Joystick extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private float X_Center;

    private float Y_Center;

    private float Radius_Base;

    private float Radius_tag;

    private JoystickListener joystickCallback;

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    public Joystick(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    public Joystick(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    private void setupDimensions(){
        X_Center = getWidth() / 2;
        Y_Center = getHeight() / 2;
        Radius_Base = Math.min(getWidth(), getHeight()) / 3;
        Radius_tag = Math.min(getWidth(), getHeight()) / 5;
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        setupDimensions();
        drawJoystick(X_Center, Y_Center);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        SingeltonServer myServer= SingeltonServer.getInstance();
        myServer.close();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(view.equals(this)){
            if (event.getAction() != event.ACTION_UP){
                float displacement = (float) Math.sqrt((Math.pow(event.getX() - X_Center,2)) +
                        Math.pow(event.getY() - Y_Center,2));
                if (displacement < Radius_Base){
                    drawJoystick(event.getX(), event.getY());
                    joystickCallback.onJoystickMoved((X_Center - event.getX()) / Radius_Base,
                            (event.getY() - Y_Center) / Radius_Base, getId());
                }
                else {
                    float ratio = Radius_Base / displacement;
                    float constrainedX = X_Center + (event.getX() - X_Center) * ratio;
                    float constrainedY = Y_Center + (event.getY() - Y_Center) * ratio;
                    drawJoystick(constrainedX,constrainedY);
                    joystickCallback.onJoystickMoved((constrainedX - X_Center) / Radius_Base,
                            (Y_Center - constrainedY) / Radius_Base, getId());
                }
            } else{
                drawJoystick(X_Center, Y_Center);
                joystickCallback.onJoystickMoved(0,0,getId());
            }
        }
        return true;
    }
    public interface JoystickListener
    {
        void onJoystickMoved(float xPercent, float yPercent, int source);

    }

    private void drawJoystick(float x, float y) {
        if (getHolder().getSurface().isValid()){
            Canvas drawCanvas = this.getHolder().lockCanvas();
            Paint colors = new Paint();
            drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            colors.setARGB(255,50,50,50);
            drawCanvas.drawCircle(X_Center, Y_Center, Radius_Base,colors);
            colors.setARGB(255,0,0,255);
            drawCanvas.drawCircle(x,y, Radius_tag,colors);
            getHolder().unlockCanvasAndPost(drawCanvas);
        }
    }
}
