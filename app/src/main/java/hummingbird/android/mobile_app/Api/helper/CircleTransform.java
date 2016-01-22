package hummingbird.android.mobile_app.Api.helper;
import com.squareup.picasso.Transformation;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.BitmapShader;
/**
 * Created by CzeWen on 2016-01-05.
 */
public class CircleTransform implements Transformation {

    public Bitmap transform(Bitmap source){
        int size = Math.min(source.getHeight(), source.getWidth());
        int x = (source.getWidth()-size)/2;
        int y = (source.getHeight()-size)/2;

        Bitmap squared_bitmap = Bitmap.createBitmap(source, x, y, size, size);
        if(squared_bitmap!= source)
            source.recycle();
        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squared_bitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squared_bitmap.recycle();
        return bitmap;
    }

    public String key(){
        return "circle";
    }


}
