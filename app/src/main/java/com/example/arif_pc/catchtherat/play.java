package com.example.arif_pc.catchtherat;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.os.Vibrator;
        import android.util.DisplayMetrics;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.ProgressBar;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import java.util.Random;
public class play extends Activity
{
    public  int count=1000;
    int _x,_y,score=0,score1=0;
    TextView textView;
    MyView myView;
    ProgressBar mProgressBar;

    Vibrator vibrator;
    Boolean gamePause;
    CountDownTimer countDownTimer;
    public String updateScore(int _score){

        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        int score = prefs.getInt("key", 0);
        if(score >= _score){
            String message = "High Score : "+score+"\n\nYour Score is : " + _score+"\n\nDo you want to play again ?" ;
            return message;
        }
        String message = "New High Score : "+_score+"\n\nDo you want to play again ?";
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("key", _score);
        editor.commit();
        return message;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mProgressBar = (ProgressBar)findViewById(R.id.progressbar);
        mProgressBar.setMax(500);
        textView = (TextView)findViewById(R.id.score);
        final RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.myLayout);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        mProgressBar.getLayoutParams().width = (int)(width*(0.7));
        textView.getLayoutParams().width = (int)(width*(0.3));
        myView = new MyView(this,mProgressBar.getHeight());
     //  setContentView(myView);

        relativeLayout.addView(myView);
        gamePause = false;if(score==0){
        relativeLayout.setBackgroundResource(R.drawable.one);}


        countDownTimer = new CountDownTimer(5000000,5000000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
        myView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() != MotionEvent.ACTION_DOWN || gamePause)
                    return true;
                int x1 = (int) event.getX() - myView.x;
                int y1 = (int) event.getY() - myView.y;
                int dist = (int) Math.sqrt((x1 * x1) + (y1 * y1));
                if (myView.radius<119)
                {
                    myView.radius=  (int) (myView.radius*1.3);
                }
                Log.e("ABC", "dist : " + dist + ", x : " + event.getX() + " , y : " + event.getY() + "  x1 : " + myView.x + " x2 " + myView.y);
                if (dist > (myView.radius*1.1) || myView.isBlack) {
                      vibrator.vibrate(300);

                    gamePause = true;
                    myView.gamePause = true;
                    new AlertDialog.Builder(play.this)
                            .setTitle("Game Over !!")
                            .setMessage(updateScore(score))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    score++;relativeLayout.refreshDrawableState();
                     if(score>20 && score<35 ){relativeLayout.setBackgroundResource(R.drawable.two);}
                     if(score>35 && score<55){
                        relativeLayout.setBackgroundResource(R.drawable.four);
                    }
                    else if(score>55){
                        relativeLayout.setBackgroundResource(R.drawable.five);
                    }
                    score1++;
                    if (count>700){
                    count=count-8;}
                        else  count=count-3;
                    textView.setText("Score : " + score);
                       vibrator.vibrate(20);
                }
                countDownTimer.cancel();
                if (myView.blackCount == myView.black) {
                    myView.isBlack = true;
                }
                myView.blackCount++;
                myView.refresh();
                if (myView.isBlack) {
                    countDownTimer = new CountDownTimer(count, 1) {

                        @Override
                        public void onTick(long millisUntilFinished) {
//                            mProgressBar.setProgress((int)millisUntilFinished);
                        }

                        @Override
                        public void onFinish() {
                           if (gamePause)
                                return;
                                 vibrator.vibrate(60);
                            myView.isBlack = false;
                            myView.refresh();
                        }
                    };
                    countDownTimer.start();
                } else {
                    countDownTimer = new CountDownTimer(count, 1) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            mProgressBar.setProgress((int) millisUntilFinished);
                        }

                        @Override
                        public void onFinish() {
                           if (gamePause)
                                return;
                             vibrator.vibrate(500);
                            gamePause = true;
                            myView.gamePause = true;
                            myView.refresh();
                            new AlertDialog.Builder(play.this)
                                    .setTitle("Time Up !!")
                                    .setMessage(updateScore(score))
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = getIntent();
                                            finish();
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    };
                    countDownTimer.start();
                }
                return true;
            }
        });

    }




        public class MyView extends View
    {

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.rat);
        Bitmap c = BitmapFactory.decodeResource(getResources(),R.drawable.rat1);
        Random random;
        int x,y,black,blackCount=0,maxx,maxy,width,height;
        int radius;

        boolean gamePause,isBlack=false;
        Paint paint;


        public MyView(Context context,int height)
        {



            super(context);

            random = new Random();
            radius = 100;
            black = random.nextInt(10);
            paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            this.height = height;
        }


        public void refresh() {

            this.invalidate();

        }

        @Override
        protected void onDraw(Canvas canvas){

            super.onDraw(canvas);



     if (gamePause) {
                paint.setColor(Color.RED);
                canvas.drawBitmap(b, x - 120, y - 120, paint);
                // canvas.drawCircle(x, y, radius, paint);

                return;
            }

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int   screen= metrics.heightPixels;
            Log.e("" + screen, "MyView ");

           // Log.e("adad", getWidth() + " || " + getHeight());
            if(screen==1280){

                radius = random.nextInt(70) + 55;
            maxx = getWidth() - (2 * radius);
            maxy = getHeight() + height - (2 * radius);
            x = random.nextInt(maxx-5) + radius;
            y = random.nextInt(maxy-15) + radius;

            // canvas.drawColor(Color.WHITE);


            if (isBlack) {
                canvas.drawBitmap(c, x - 90, y - 90, paint);
                paint.setColor(Color.BLACK);
                blackCount = -1;
                black = random.nextInt(10);
            } else {
                canvas.drawBitmap(b, x - 120, y - 120, paint);
                paint.setColor(Color.GREEN);
            }

            }
           else if(screen==480){

                radius = random.nextInt(75) + 55;
                maxx = getWidth() - (2 * radius);
                maxy = getHeight() + height - (2 * radius);
                x = random.nextInt(maxx) + radius;
                y = random.nextInt(maxy) + radius;

                // canvas.drawColor(Color.WHITE);


                if (isBlack) {
                    canvas.drawBitmap(c, x - 90, y - 90, paint);
                    paint.setColor(Color.BLACK);
                    blackCount = -1;
                    black = random.nextInt(10);
                } else {
                    canvas.drawBitmap(b, x - 120, y - 120, paint);
                    paint.setColor(Color.GREEN);
                }

            }
            else if(screen==1920){

                radius = random.nextInt(75) + 85;
                maxx = getWidth() - (2 * radius);
                maxy = getHeight() + height - (2 * radius);
                x = random.nextInt(maxx) + radius;
                y = random.nextInt(maxy) + radius;

                // canvas.drawColor(Color.WHITE);


                if (isBlack) {
                    canvas.drawBitmap(c, x - 150, y - 150, paint);
                  //  canvas.drawCircle(x, y, radius, paint);
                    paint.setColor(Color.BLACK);
                    blackCount = -1;
                    black = random.nextInt(10);
                } else {
                    canvas.drawBitmap(b, x - 200, y - 200, paint);
                 //   canvas.drawCircle(x, y, radius, paint);
                    paint.setColor(Color.GREEN);
                }
            }
            //    canvas.drawCircle(x, y, radius, paint);
        }

    }
}