package dam.pmdm.spyrothedragon.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class FireAnimationView extends View {

    private Paint paint;
    private Path firePath;
    private float flameHeight = 0;
    private boolean isAnimating = false;
    private Random random = new Random();

    public FireAnimationView(Context context) {
        super(context);
        init();
    }

    public FireAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        firePath = new Path();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isAnimating) {
            int width = getWidth();
            int height = getHeight();

            firePath.reset();

            //punto de inicio de la llama (desde la boca de Spyro)
            int startX = (int) (width * 0.61);
            int startY = (int) (height * 0.67);

            firePath.moveTo(startX, startY);

            //base de la llama (Rojo anaranjado, menos agresivo)
            int baseWidth = 360;
            int baseHeight = 90;
            int numWaves = 3;

            for (int i = 0; i < numWaves; i++) {
                int waveX = startX - baseWidth / 2 + (i * (baseWidth / numWaves));
                int waveY = (int) (startY + flameHeight + random.nextInt(40) - 30);
                firePath.quadTo(waveX, waveY, waveX + (baseWidth / numWaves), startY + flameHeight + baseHeight);
            }

            paint.setColor(Color.argb(230, 255, 80, 0)); // 🔥 Rojo anaranjado en la base
            canvas.drawPath(firePath, paint);

            //capa intermedia (Naranja fuerte con CURVA PRONUNCIADA A LA IZQUIERDA en la parte derecha)
            firePath.reset();
            firePath.moveTo(startX, startY);
            firePath.quadTo(startX - 70, startY + flameHeight - 50, startX, startY + flameHeight - 110);
            firePath.quadTo(startX + 140, startY + flameHeight - 50, startX - 70, startY - 15);  // 🔥 CURVA MÁS PRONUNCIADA Y SUAVE EN LA PARTE SUPERIOR

            paint.setColor(Color.argb(220, 255, 140, 0)); // 🔥 Naranja fuerte
            canvas.drawPath(firePath, paint);

            //parte superior (Amarillo brillante, ahora sin picos)
            firePath.reset();
            firePath.moveTo(startX, startY);
            firePath.quadTo(startX - 50, startY + flameHeight / 2, startX, startY + flameHeight / 2 + 40);
            firePath.quadTo(startX + 90, startY + flameHeight / 2 - 20, startX - 40, startY - 10);  // 🔥 SUAVIZADA LA CURVA SUPERIOR

            paint.setColor(Color.argb(200, 255, 215, 0)); // 🔥 Amarillo puro
            canvas.drawPath(firePath, paint);

            //toque final (Amarillo casi blanco para el brillo, eliminando picos arriba)
            firePath.reset();
            firePath.moveTo(startX, startY);
            firePath.quadTo(startX - 30, startY + flameHeight / 3, startX, startY + flameHeight / 3 + 20);
            firePath.quadTo(startX + 50, startY + flameHeight / 3 - 10, startX - 30, startY - 5);  // 🔥 SUAVIZADA LA CURVA FINAL

            paint.setColor(Color.argb(180, 255, 250, 200)); // 🔥 Amarillo pálido casi blanco
            canvas.drawPath(firePath, paint);
        }
    }


    public void startFireAnimation() {
        isAnimating = true;
        flameHeight = 0;
        Handler handler = new Handler();

        Runnable animator = new Runnable() {
            @Override
            public void run() {
                if (flameHeight < 100) {  //se aumenta la altura para un mejor efecto
                    flameHeight += 100;
                    invalidate();
                    handler.postDelayed(this, 50);
                } else {
                    isAnimating = false;
                    postDelayed(() -> setVisibility(View.GONE), 2600); //se oculta después de un tiempo
                }
            }
        };

        handler.post(animator);
    }
}