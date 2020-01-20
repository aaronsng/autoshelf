package lit.lads.cataloguev3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.Bind;

public class Catalogue extends AppCompatActivity {

    private static final String TAG = Catalogue.class.getSimpleName();

    @Bind(R.id.colourEdit)
    EditText colourEdit;
    @Bind(R.id.descEdit)
    EditText descEdit;
    @Bind(R.id.search)
    Button prompter;
    @Bind(R.id.display)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);
        colourEdit = this.findViewById(R.id.colourEdit);
        descEdit = this.findViewById(R.id.descEdit);
        prompter = this.findViewById(R.id.search);
        imageView = this.findViewById(R.id.display);

        Log.i(TAG, "Works!");
        prompter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String colour = colourEdit.getText().toString();
                String desc = descEdit.getText().toString();
                String descSplit[] = readFromFile("config6.txt", Catalogue.this).split("\n");
                for (int i = 1; i < descSplit.length; i++) {
                    String line = descSplit[i].split(" ")[0];
                    Log.i(TAG,  Boolean.toString(line.toLowerCase() == desc.toLowerCase()));
                    Log.i(TAG,  line.toLowerCase());
                    Log.i(TAG,  descSplit[i].split(" ")[1]);
                    if (line.toLowerCase().equals(desc.toLowerCase())) {
                        Log.i(TAG,  descSplit[i].split(" ")[1] + ".png");
                        imageView.setImageBitmap(readImageFromFile(Environment.getExternalStorageDirectory() + File.separator + descSplit[i].split(" ")[1] + ".png"));
                    }
                }
            }
        });
    }
    private Bitmap readImageFromFile(String photoPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
        return bitmap;
    }
    private String readFromFile(String fileName, Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString + "\n");
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


}
