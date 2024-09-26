package in.gov.cgg.alumni;


import android.content.Context;
import android.graphics.drawable.GradientDrawable;

import androidx.core.content.ContextCompat;
import in.gov.cgg.alumni.R;

/**
 * Created by niharika.p on 26-06-2019.
 */

public class Gradient {
    Context context;
    GradientDrawable gradientDrawable;

    public Gradient(Context context){
        this.context=context;
    }

    public GradientDrawable getGradient()
    {
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(context, R.color.color1),
                        ContextCompat.getColor(context, R.color.color2),
                        ContextCompat.getColor(context, R.color.color3),
                        ContextCompat.getColor(context, R.color.color4),
                        ContextCompat.getColor(context, R.color.color5),
                        ContextCompat.getColor(context, R.color.colorPrimary)});

        return gradientDrawable;
    }



}
