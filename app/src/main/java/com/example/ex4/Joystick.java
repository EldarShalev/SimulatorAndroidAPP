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

    private float centerX;

    private float centerY;

    private float baseRadius;

    private float hatRadius;

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
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        baseRadius = Math.min(getWidth(), getHeight()) / 3;
        hatRadius = Math.min(getWidth(), getHeight()) / 5;
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        setupDimensions();
        drawJoystick(centerX,centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        SingeltonServer myServer= SingeltonServer.getInstance();
        myServer.close();
        System.out.println("Close!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(view.equals(this)){
            if (event.getAction() != event.ACTION_UP){
                float displacement = (float) Math.sqrt((Math.pow(event.getX() - centerX,2)) +
                        Math.pow(event.getY() - centerY,2));
                if (displacement < baseRadius){
                    drawJoystick(event.getX(), event.getY());
                    joystickCallback.onJoystickMoved((centerX - event.getX()) / baseRadius,
                            (event.getY() - centerY) / baseRadius, getId());
                }
                else {
                    float ratio = baseRadius / displacement;
                    float constrainedX = centerX + (event.getX() - centerX) * ratio;
                    float constrainedY = centerY + (event.getY() - centerY) * ratio;
                    drawJoystick(constrainedX,constrainedY);
                    joystickCallback.onJoystickMoved((constrainedX - centerX) / baseRadius,
                            (centerY - constrainedY) / baseRadius, getId());
                }
            } else{
                drawJoystick(centerX,centerY);
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
            drawCanvas.drawCircle(centerX,centerY,baseRadius,colors);
            colors.setARGB(255,0,0,255);
            drawCanvas.drawCircle(x,y,hatRadius,colors);
            getHolder().unlockCanvasAndPost(drawCanvas);
        }
    }
}
