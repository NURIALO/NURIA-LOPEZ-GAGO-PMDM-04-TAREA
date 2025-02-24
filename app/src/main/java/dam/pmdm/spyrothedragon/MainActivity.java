package dam.pmdm.spyrothedragon;
import android.media.MediaPlayer;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding;
import dam.pmdm.spyrothedragon.databinding.GuideBinding;


public class MainActivity extends AppCompatActivity {
    //declaramos las variables
    private static final String TAG = "MainActivity";
    private MediaPlayer backgroundMusic;
    private GuideBinding guideBinding;
    private ActivityMainBinding binding;
    private NavController navController;
    boolean needGuide = true;
    private AnimatorSet currentAnimation;
    private MenuItem aboutMenuItem;
    private int currentStep = 0;  // variable para rastrear el paso actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        guideBinding = GuideBinding.inflate(getLayoutInflater());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //inicio música solo si no está ya sonando
        if (backgroundMusic == null) {
            Log.d("MainActivity", "Iniciando música de fondo");
            backgroundMusic = MediaPlayer.create(this, R.raw.background_music);
           backgroundMusic.setLooping(true);
            backgroundMusic.start();
        } else {
            Log.d("MainActivity", "Música ya estaba en reproducción");
        }

        //se configura la Toolbar personalizada
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        // configuro el NavController y BottomNavigationView
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(binding.navView, navController);
            NavigationUI.setupActionBarWithNavController(this, navController);
        }
        //oculto la flecha en la toolbar
        if (navController != null) {
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (destination.getId() == R.id.navigation_characters ||
                        destination.getId() == R.id.navigation_worlds ||
                        destination.getId() == R.id.navigation_collectibles) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false); // 🔹 Oculta la flecha
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // 🔹 Muestra la flecha en otros fragmentos
                }
            });
        }
        //configuro el listener para el BottomNavigationView
        binding.navView.setOnItemSelectedListener(this::selectedBottomMenu);

        getBooleanFromSharedPreferences();

        if (needGuide) {
            FrameLayout rootLayout = findViewById(R.id.mainFrame);
            rootLayout.addView(guideBinding.getRoot());

            guideBinding.getRoot().setVisibility(View.VISIBLE);
            configureGuide();
        }
    }

    /**
     * Detiene y libera la música de fondo.
     */
    private void stopMusic() {
        if (backgroundMusic != null) {
            Log.d("MainActivity", " Intentando detener la música...");
            try {
                if (backgroundMusic.isPlaying()) {
                    backgroundMusic.stop();   //  Detener la música
                    Log.d("MainActivity", "✅ Música detenida");
                }
                backgroundMusic.release();     //hay que liberar recursos
                backgroundMusic = null;        //evito referencia a objeto eliminado
                Log.d("MainActivity", " MediaPlayer liberado completamente");
            } catch (IllegalStateException e) {
                Log.e("MainActivity", " Error al detener la música: " + e.getMessage());
            }
        } else {
            Log.d("MainActivity", " backgroundMusic ya es null, no hay música que detener.");
        }
    }


    private void getBooleanFromSharedPreferences() {

    }

    private void configureGuide() {
        Log.d(TAG, "Iniciando la guía");
        // Cargar GIF animado de Spyro en el ImageView
        Glide.with(this)
                .asGif()
                .load(R.drawable.spyro_volando)
                .into(guideBinding.imgSpyroLogoVolando);


        guideBinding.guideLayout.setVisibility(View.VISIBLE);
        guideBinding.welcome.setVisibility(View.VISIBLE);
        guideBinding.btnExit.setVisibility(View.VISIBLE);
        guideBinding.textStep.setVisibility(View.INVISIBLE);
        guideBinding.textStep.setText("");
        guideBinding.btnNext.setVisibility(View.GONE);



        guideBinding.btnStartGuide.setOnClickListener(v -> {
            stopMusic();
            playSound(R.raw.start_sound);
            Log.d(TAG, "Botón de inicio de la guía pulsado");

            //en este paso uso animación de desvanecimiento
            guideBinding.welcome.animate()
                    .alpha(0f)
                    .setDuration(500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            //se elimina `welcome` de la jerarquía
                            ViewGroup parent = (ViewGroup) guideBinding.welcome.getParent();
                            if (parent != null) {
                                parent.removeView(guideBinding.welcome);
                            }

                            //fuerzo a que se refresque la UI
                            guideBinding.getRoot().invalidate();
                            guideBinding.getRoot().requestLayout();

                            //muestro los elementos de la guía
                            guideBinding.textStep.setVisibility(View.GONE);
                            guideBinding.btnNext.setVisibility(View.VISIBLE);

                            //inicio la guía en este paso
                            guideBinding.textStep.postDelayed(() -> {
                                currentStep = 1;
                                nextGuideStep();
                            }, 200);
                        }
                    });
        });
        guideBinding.btnExit.setOnClickListener(v -> {
            stopMusic();
            playSound(R.raw.exitsound); // sonido al pulsar salir
            exitGuide(v);
        });

        guideBinding.btnNext.setOnClickListener(v -> {
            playSound(R.raw.nextsound); //sonido al pulsar siguiente
            nextGuideStep();
        });


    }

    private void nextGuideStep() {
        guideBinding.textStep.setVisibility(View.INVISIBLE);

        guideBinding.textStep.postDelayed(() -> {
            //se detiene cualquier animación del icono pulsante antes de cambiar de paso
            stopPulseAnimation(guideBinding.pulseImage);
            stopPulseAnimation(guideBinding.pulseImage2);
            stopPulseAnimation(guideBinding.pulseImage3);
            stopPulseAnimation(guideBinding.pulseImage4);

            switch (currentStep) {
                case 1:
                    showGuideStep(guideBinding.pulseImage, "Aquí podrás explorar a todos los personajes del mundo de Spyro.", R.id.navigation_characters);
                    animateRotate(guideBinding.textStep);
                    break;
                case 2:
                    showGuideStep(guideBinding.pulseImage2, "Entra y descubrirás los fascinantes mundos de Spyro", R.id.navigation_worlds);
                    animateSlide(guideBinding.textStep);
                    break;
                case 3:
                    showGuideStep(guideBinding.pulseImage3, "Podrás encontrar todos los coleccionables de Spyro", R.id.navigation_collectibles);
                    animateScale(guideBinding.textStep);
                    break;
                case 4:
                    //guideBinding.textStep.setVisibility(View.VISIBLE);
                    //guideBinding.textStep.setAlpha(0f);
                    showGuideStep(guideBinding.pulseImage4, "Aquí encontrarás más información sobre la app", R.id.navigation_characters);
                    animateFade(guideBinding.textStep);
                    break;
                case 5:
                    openAboutMenu();
                    break;
                default:
                    //por si no se da a aceptar en show dialog y se cierra el botón siguiente lleva al guideSummary
                    showGuideSummary();

            }
            //ACTUALIZAR LA POSICION DEL BOTÓN "SALTAR GUIA" SEGUN EL PASO
            updateExitButtonPosition();

            currentStep++;
        }, 200);
    }
    private void updateExitButtonPosition() {
        guideBinding.btnExit.post(() -> {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) guideBinding.btnExit.getLayoutParams();

            if (currentStep == 0) {
                // 🟣 Pantalla de bienvenida -> Arriba a la izquierda
                params.gravity = Gravity.TOP | Gravity.START;
                params.setMargins(40, 40, 20, 0);
            } else {
                // 🔥 En las demás pantallas, colocamos "Saltar Guía" debajo de "Siguiente"
                guideBinding.btnNext.post(() -> {
                    float btnNextX = guideBinding.btnNext.getX();
                    float btnNextBottom = guideBinding.btnNext.getY() + guideBinding.btnNext.getHeight();
                    float btnNextWidth = guideBinding.btnNext.getWidth();

                    // 🔹 Ajustamos la posición para que quede alineado debajo de "Siguiente"
                    params.gravity = Gravity.TOP;
                    params.leftMargin = (int) (btnNextX + (btnNextWidth / 2)); // Centrar un poco
                    params.topMargin = (int) btnNextBottom + 180;
                    params.rightMargin = 0;
                    params.bottomMargin = 0;

                    guideBinding.btnExit.setLayoutParams(params);
                    guideBinding.btnExit.requestLayout();
                });
            }
        });
    }

    private void showGuideSummary() {
        Log.d(TAG, "Mostrando pantalla final de la guía");
        Glide.with(this)
                .asGif()
                .load(R.drawable.end_spyro)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(70))) // bordes redondeados
                .into(guideBinding.imgSpyroEnd);

        //oculto los elementos de la guía anterior
        guideBinding.textStep.setVisibility(View.GONE);
        guideBinding.btnNext.setVisibility(View.GONE);
        guideBinding.btnExit.setVisibility(View.GONE); //oculto el botón pulsar guía pero sólo en esta pantalla

        //se muestra la pantalla final
        guideBinding.guideSummary.setVisibility(View.VISIBLE);

        //configuro botón comenzar
        guideBinding.btnFinishGuide.setOnClickListener(v -> {
            playSound(R.raw.start_sound); //sonido al pulsar comenzar
            stopMusic(); //se para la música de fondo si está activa, así si ha funcionado
            Log.d(TAG, "Guía finalizada desde el botón");
            exitGuide(v);
        });

        //configuro el clic en el GIF para cerrar la guía
        guideBinding.imgSpyroEnd.setOnClickListener(v -> {
            Log.d(TAG, "Guía finalizada desde el GIF");
            exitGuide(v);
        });
    }
    private void stopPulseAnimation(View pulseImage) {
        if (pulseImage != null) {
            pulseImage.clearAnimation();  //se detiene la animación
            pulseImage.setVisibility(View.GONE);  //se oculta el icono pulsante
        }
    }

    private void playSound(int soundResId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, soundResId);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release(); //libero memoria cuando termine el sonido
        });
    }


    private void animateRotate(View view) {
        view.setVisibility(View.INVISIBLE);
        view.setRotation(-360f); //empieza girado completamente hacia atrás
        view.setAlpha(0f); //se inicia invisible

        view.animate()
                .rotation(0f) //gira hasta la posición correcta
                .alpha(1f) //aparece poco a poco
                .setDuration(1500) //duración más larga para mayor impacto
                .setInterpolator(new android.view.animation.OvershootInterpolator(3f)) //rebote final exagerado
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                    }
                });
    }

    //efecto de desvanecimiento (Fade In)
    private void animateFade(View view) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1f)
                .setDuration(6000)
                .setListener(null);
    }

    //efecto de desplazamiento lateral (Slide In)
    private void animateSlide(View view) {
        view.setVisibility(View.INVISIBLE);
        view.setTranslationX(800f); //Comienza aún más lejos
        view.animate()
                .translationX(0f)
                .setDuration(1200) //duración más larga para un efecto más dramático
                .setInterpolator(new android.view.animation.OvershootInterpolator(2f)) //Rebote más exagerado
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void animateScale(View view) {
        view.setVisibility(View.INVISIBLE);
        view.setScaleX(0f);
        view.setScaleY(0f);
        view.animate()
                .scaleX(1.2f) //primero crece más de su tamaño
                .scaleY(1.2f)
                .setDuration(1200)
                .setInterpolator(new android.view.animation.BounceInterpolator()) //rebote gigante
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.animate()
                                .scaleX(1f) //vuelve a su tamaño normal después del rebote
                                .scaleY(1f)
                                .setDuration(300)
                                .setInterpolator(new android.view.animation.OvershootInterpolator(1f));
                    }
                });
    }


    private void showGuideStep(View pulseImage, String text, int navDestination) {
        Log.d(TAG, "Mostrando paso de la guía: " + text);

        guideBinding.textStep.postDelayed(() -> {
            guideBinding.textStep.setText(text);
            guideBinding.textStep.setVisibility(View.VISIBLE); //muestro cuando el texto ya está listo
        }, 10); //ajusta el tiempo de retraso si es necesario



        //animaciones del icono pulsante
        pulseImage.setVisibility(View.VISIBLE);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(pulseImage, "scaleX", 0.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(pulseImage, "scaleY", 0.5f, 1f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(pulseImage, "alpha",  0.5f, 1f, 0.5f);

        scaleX.setRepeatCount(45);
        scaleY.setRepeatCount(45);

        //se ejecutan animaciones en conjunto
        currentAnimation = new AnimatorSet();
        currentAnimation.playTogether(scaleX, scaleY, fadeInOut);
        currentAnimation.setDuration(1000);
        currentAnimation.start();

        currentAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                pulseImage.setVisibility(View.GONE);

            }


            @Override
            public void onAnimationCancel(Animator animation) {
                pulseImage.setVisibility(View.GONE);
            }
        });
    }


    private void exitGuide(View view) {
        Log.d(TAG, "Saliendo de la guía");

        guideBinding.guideLayout.animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        guideBinding.guideLayout.setVisibility(View.GONE);

                        //se elimina la vista de la jerarquía
                        ViewGroup parent = (ViewGroup) guideBinding.guideLayout.getParent();
                        if (parent != null) {
                            parent.removeView(guideBinding.guideLayout);
                            Log.d(TAG, "guideLayout eliminado del contenedor");
                        }

                        //refresco la UI
                        guideBinding.getRoot().invalidate();
                        guideBinding.getRoot().requestLayout();

                        //aseguro que el juego (pantalla principal) está visible
                        binding.navView.setVisibility(View.VISIBLE);
                        binding.toolbar.setVisibility(View.VISIBLE);
                    }
                });
    }

    private boolean selectedBottomMenu(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_characters) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            navController.navigate(R.id.navigation_characters);

        } else if (menuItem.getItemId() == R.id.nav_worlds) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); //oculto la flecha en Mundos
            navController.navigate(R.id.navigation_worlds);

        } else if (menuItem.getItemId() == R.id.nav_collectibles) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); //oculto la flecha en Coleccionables
            navController.navigate(R.id.navigation_collectibles);

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //infla el menú
        getMenuInflater().inflate(R.menu.about_menu, menu);
        aboutMenuItem = menu.findItem(R.id.action_info); //guardo la referencia
        return true;
    }

    private void openAboutMenu() {
        if (aboutMenuItem != null) {
            onOptionsItemSelected(aboutMenuItem); //llamo manualmente al evento del botón
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //gestiona el clic en el ítem de información
        if (item.getItemId() == R.id.action_info) {
            showInfoDialog();  // Muestra el diálogo
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showInfoDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_about)
                .setMessage(R.string.text_about)
                .setPositiveButton(R.string.accept, (dialog, which) -> {
                    dialog.dismiss(); //cierra el diálogo
                    showGuideSummary(); //muestra la pantalla de despedida de la guía
                })
                .show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic(); //detiene la música cuando la actividad se cierre
    }
}