package hcmute.edu.vn.fruitsclassificationapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import hcmute.edu.vn.fruitsclassificationapp.model.MainModel;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @SuppressLint("RecyclerView")
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder,final int position, @NonNull MainModel model) {
        holder.fruit_name.setText(model.getFruit_name());
        holder.en_food_name.setText(model.getEn_food_name());
        holder.vn_food_name.setText(model.getVn_food_name());
        Glide.with(holder.img.getContext())
                .load(model.getLink_image())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img)
        ;
        //handle button edit
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pop up (dialog) without data
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 1200)
                        .create();

                //fetch and binding data
                //1. access view
                View view = dialogPlus.getHolderView();
                EditText fruitName = view.findViewById(R.id.txtFruitName);
                EditText vnFoodName = view.findViewById(R.id.txtVnFoodName);
                EditText enFoodName = view.findViewById(R.id.txtEnFoodName);
                EditText linkImage = view.findViewById(R.id.txtLinkImage);
                Button btnUpdate = view.findViewById(R.id.btnUpdate);
                //2.binding value from model
                fruitName.setText(model.getFruit_name());
                vnFoodName.setText(model.getVn_food_name());
                enFoodName.setText(model.getEn_food_name());
                linkImage.setText(model.getLink_image());
                //3.show pop up with data binding
                dialogPlus.show();
                //4.handle update button to update data on firebase
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("fruit_name", fruitName.getText().toString());
                        map.put("en_food_name", enFoodName.getText().toString());
                        map.put("vn_food_name", vnFoodName.getText().toString());
                        map.put("link_image", linkImage.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("recommendfoods")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.en_food_name.getContext(), "Data updated successfully", Toast.LENGTH_SHORT).show();
                                        //close pop up
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.en_food_name.getContext(), "Error while ", Toast.LENGTH_SHORT).show();
                                        //close pop up
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });

            }


        });
        //handle button delete
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.en_food_name.getContext());
                builder.setTitle("Are you sure to delete this item?");
                builder.setMessage("Deleted data can't be undo");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("recommendfoods")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.en_food_name.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();

            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView fruit_name, en_food_name,  vn_food_name;

        //used for edit or delete
        Button btnEdit, btnDelete;
        //
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView) itemView.findViewById(R.id.img1);
            fruit_name = (TextView) itemView.findViewById(R.id.fruitName);
            en_food_name = (TextView) itemView.findViewById(R.id.enFoodName);
            vn_food_name = (TextView) itemView.findViewById(R.id.vnFoodName);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button)itemView.findViewById(R.id.btnDelete);
        }
    }
}
