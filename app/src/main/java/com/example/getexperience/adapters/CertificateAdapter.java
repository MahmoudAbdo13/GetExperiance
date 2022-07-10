package com.example.getexperience.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getexperience.R;
import com.example.getexperience.model.CertificateModel;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.CertificateViewHolder> {

    private ArrayList<CertificateModel> certificateModels = new ArrayList<>();

    @NonNull
    @Override
    public CertificateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CertificateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_certificate, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CertificateViewHolder holder, int position) {
        CertificateModel model = certificateModels.get(position);

        holder.name.setText(model.getCertificateName());

        holder.dowenload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL url = null;
                String fileName;

                try {
                    url = new URL(model.getCertificateUrl());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                fileName = url.getPath();
                fileName = fileName.substring(fileName.lastIndexOf("/")+1 );
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url+""));
                request.setTitle(fileName);
                request.allowScanningByMediaScanner();
                request.setAllowedOverMetered(true);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);
                DownloadManager downloadManager = (DownloadManager) v.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);
                Toast.makeText(v.getContext(), "Your Certificate is Downloading Now, Check Notification", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return certificateModels.size();
    }

    public void setList(ArrayList<CertificateModel> certificateModel) {
        this.certificateModels = certificateModel;
        notifyDataSetChanged();

    }

    public class CertificateViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        Button dowenload;

        public CertificateViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_certificate_opportunity_name);
            dowenload = itemView.findViewById(R.id.download_certificate_btn);
        }
    }
}
