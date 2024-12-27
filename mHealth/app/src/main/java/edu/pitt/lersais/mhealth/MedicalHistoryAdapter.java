package edu.pitt.lersais.mhealth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.pitt.lersais.mhealth.model.MedicalRecord;


public class MedicalHistoryAdapter extends RecyclerView.Adapter<MedicalHistoryAdapter.MedicalHistoryViewHolder> {

    private final List<MedicalRecord> medicalRecords;

    public MedicalHistoryAdapter(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    @NonNull
    @Override
    public MedicalHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medical_record, parent, false);
        return new MedicalHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalHistoryViewHolder holder, int position) {
        MedicalRecord record = medicalRecords.get(position);
        holder.nameTextView.setText(record.getName());
        holder.dobTextView.setText(record.getDob());
        // Add more fields here if needed
    }

    @Override
    public int getItemCount() {
        return medicalRecords.size();
    }

    public static class MedicalHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView dobTextView;

        public MedicalHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dobTextView = itemView.findViewById(R.id.dobTextView);
            // Initialize more fields here if needed
        }
    }
}
