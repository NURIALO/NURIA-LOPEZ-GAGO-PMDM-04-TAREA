package dam.pmdm.spyrothedragon.ui;



import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dam.pmdm.spyrothedragon.R;

public class VideoFragment extends Fragment {

    private VideoView videoView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        videoView = view.findViewById(R.id.videoView);
        ImageButton btnClose = view.findViewById(R.id.btnCloseVideo);

        //se carga y se reproduce el video
        Uri videoUri = Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + R.raw.spyro_video);
        videoView.setVideoURI(videoUri);
        videoView.start();

        //se permite que el usuario cierre el video manualmente
        btnClose.setOnClickListener(v -> {
            videoView.stopPlayback(); //detiene la reproducción
            requireActivity().getSupportFragmentManager().popBackStack(); // Cierra el fragmento
        });

        //cierro automáticamente el fragmento cuando termina el video
        videoView.setOnCompletionListener(mp -> requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }
}