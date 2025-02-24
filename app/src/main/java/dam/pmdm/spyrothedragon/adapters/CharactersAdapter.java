package dam.pmdm.spyrothedragon.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.models.Character;
import dam.pmdm.spyrothedragon.ui.CharactersFragment;
import dam.pmdm.spyrothedragon.ui.FireAnimationView;

import java.util.List;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder> {

    private List<Character> list;
    private Context context;
    private CharactersFragment fragment;  // 🔹 Guardamos la referencia al fragmento

    public CharactersAdapter(Context context, CharactersFragment fragment, List<Character> charactersList) {
        this.context = context;
        this.fragment = fragment; // 🔹 Guardamos el fragmento
        this.list = charactersList;
    }

    @Override
    public CharactersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new CharactersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CharactersViewHolder holder, int position) {
        Character character = list.get(position);
        holder.nameTextView.setText(character.getName());

        //cargo la imagen del personaje
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(character.getImage(), "drawable", holder.itemView.getContext().getPackageName());
        holder.imageImageView.setImageResource(imageResId);

        //se detecta pulsación prolongada solo en Spyro
        if (character.getName().equalsIgnoreCase("Spyro")) {
            holder.imageImageView.setOnLongClickListener(v -> {
                FireAnimationView fireAnimationView = fragment.getFireAnimationView(); // 🔹 Ahora accedemos correctamente al fragmento

                if (fireAnimationView != null) {
                    //le pongo sonido cuando sale el fuego
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.fire_dragon);
                    mediaPlayer.start();

                    //se inicia la animación
                    fireAnimationView.setVisibility(View.VISIBLE);
                    fireAnimationView.startFireAnimation();

                    //se libera el recurso después de que termine el sonido
                    mediaPlayer.setOnCompletionListener(mp -> mp.release());
                }
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CharactersViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageImageView;

        public CharactersViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
        }
    }
}

