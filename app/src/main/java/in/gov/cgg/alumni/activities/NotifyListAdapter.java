package in.gov.cgg.alumni.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import in.gov.cgg.alumni.R;

public class NotifyListAdapter extends RecyclerView.Adapter<NotifyListAdapter.MyViewHolder> {

    private final List<NotifyModel> data_dashbord;
    Context context;


    public NotifyListAdapter(NotifyListActivity notifyListActivity, List<NotifyModel> notifyList) {
        this.context = notifyListActivity;
        this.data_dashbord = notifyList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.notify_list_item, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        myViewHolder.tv_fromname.setText(data_dashbord.get(i).getFromname());

        if (data_dashbord.get(i).getSubject().isEmpty()) {
            myViewHolder.tv_subject.setText(data_dashbord.get(i).getMessage());
            myViewHolder.tv_msg.setText("");
        } else {
            myViewHolder.tv_subject.setText(data_dashbord.get(i).getSubject());
            myViewHolder.tv_msg.setText(data_dashbord.get(i).getMessage());
        }
        myViewHolder.tv_time.setText(data_dashbord.get(i).getDatestamp() + "\n" + data_dashbord.get(i).getTimestamp());

        myViewHolder.rootview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    LayoutInflater factory = LayoutInflater.from(context);
                    final View view = factory.inflate(R.layout.capture, null);
                    TextView name, subject, message, okay;
                    ImageView close;
                    name = view.findViewById(R.id.name);
                    subject = view.findViewById(R.id.subject);
                    message = view.findViewById(R.id.message);
                    okay = view.findViewById(R.id.okay);
                    close = view.findViewById(R.id.closeImage);
                    name.setText(data_dashbord.get(i).getFromname());
                    subject.setText(data_dashbord.get(i).getSubject());
                    message.setText(data_dashbord.get(i).getMessage());
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setView(view);
                    builder1.setCancelable(true);
                    final AlertDialog alert11 = builder1.create();
                    alert11.show();
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert11.dismiss();
                        }
                    });
                    okay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_TEXT,data_dashbord.get(i).getMessage());
                            //shareIntent.putExtra(Intent.EXTRA_SUBJECT, );
                            context.startActivity(Intent.createChooser(shareIntent, "Share"));


                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        if (!data_dashbord.get(i).getIma().isEmpty()) {
//            Picasso.with(context)
//                    .load(data_dashbord.get(i).getIma())
//                    .placeholder(R.drawable.loadingimg_red)
//                    .error(R.color.colorPrimary).into(myViewHolder.circleImageView);
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.loadingimg_red)
                            .error(R.drawable.user))
                    .load(data_dashbord.get(i).getIma())
                    .into(myViewHolder.circleImageView);

        } else {
            myViewHolder.circleImageView.setImageResource(R.drawable.user);
        }
    }


    @Override
    public int getItemCount() {
        return (null != data_dashbord ? data_dashbord.size() : 0);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_fromname, tv_subject, tv_msg, tv_time;
        ImageView circleImageView;
        RelativeLayout rootview;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_fromname = itemView.findViewById(R.id.tv_fromname);
            tv_subject = itemView.findViewById(R.id.tv_subject);
            tv_msg = itemView.findViewById(R.id.tv_msg);
            tv_time = itemView.findViewById(R.id.tv_time);
            circleImageView = itemView.findViewById(R.id.img_notify);
            rootview = itemView.findViewById(R.id.root_view);
        }
    }
}
