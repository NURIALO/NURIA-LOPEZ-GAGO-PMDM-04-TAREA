package dam.pmdm.spyrothedragon.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.models.Collectible;
import dam.pmdm.spyrothedragon.ui.CollectiblesFragment;

public class CollectiblesAdapter extends RecyclerView.Adapter<CollectiblesAdapter.CollectiblesViewHolder> {

    private List<Collectible> list;
    private CollectiblesFragment fragment;

    //vamos a poner el contador de tokes en la segunda imagen del recyclerview
    private int gemClickCount = 0; // contador de toques en la gema
    private static final int GEM_INDEX = 1; //la segunda imagen (índice 1 en la lista)
    private Handler handler = new Handler();

    //modifico el constructor para recibir el fragmento
    public CollectiblesAdapter(CollectiblesFragment fragment, List<Collectible> collectibleList) {
        this.fragment = fragment;
        this.list = collectibleList;
    }

    @Override
    public CollectiblesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new CollectiblesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollectiblesViewHolder holder, int position) {
        Collectible collectible = list.get(position);
        holder.nameTextView.setText(collectible.getName());

        // Cargar la imagen
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(
                collectible.getImage(), "drawable", holder.itemView.getContext().getPackageName()
        );
        holder.imageImageView.setImageResource(imageResId);

        // Detectar si es la segunda imagen (la gema)
        if (position == GEM_INDEX) {
            holder.imageImageView.setOnClickListener(v -> {
                gemClickCount++;
                System.out.println("🟢 Click número: " + gemClickCount);

                if (gemClickCount == 4) {
                    gemClickCount = 0; // Reiniciar contador
                    System.out.println("🔥 Easter Egg Activado!");
                    fragment.activateEasterEgg(); // Llamar a la función en CollectiblesFragment
                }

                //reinicio el contador si no se hace el 4to toque dentro de 1 segundo
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {
                    System.out.println("⏳ Tiempo excedido. Reiniciando contador.");
                    gemClickCount = 0;
                }, 1000);
            });
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CollectiblesViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageImageView;

        public CollectiblesViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
        }
    }


}
