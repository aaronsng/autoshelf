package lit.lads.cataloguev3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Bitmap photo;
    private color color;

    @Bind(R.id.imageview)
    ImageView imageview;
    @Bind(R.id.button)
    Button button;
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.characteristics)
    TextView characteristics;
    @Bind(R.id.coloursX)
    TextView coloursX;
    @Bind(R.id.catalogue)
    Button catalogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = this.findViewById(R.id.button);
        imageview = this.findViewById(R.id.imageview);
        textView = this.findViewById(R.id.textView);
        coloursX = this.findViewById(R.id.coloursX);
        characteristics = this.findViewById(R.id.characteristics);
        catalogue = this.findViewById(R.id.catalogue);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
        catalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Catalogue.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            final String uid = String.valueOf(System.currentTimeMillis());
            photo = (Bitmap) data.getExtras().get("data");
            color = new color(photo);
            imageview.setImageBitmap(photo);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

            File ExternalStorageDirectory = Environment.getExternalStorageDirectory();
            OutputStream fOut = null;
            File file = new File(ExternalStorageDirectory + File.separator+ uid +".png"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            try {
                fOut = new FileOutputStream(file);
                photo.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                fOut.flush(); // Not really required
                fOut.close(); // do not forget to close the stream
                Log.i(TAG, "Success saving bitmap");
            } catch (FileNotFoundException fe) {
                fe.printStackTrace();
                Log.i(TAG, "Failure saving bitmap");

            } catch (IOException io) {
                io.printStackTrace();
                Log.i(TAG, "failure saving bitmap");

            }

            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(photo);
            Toast.makeText(MainActivity.this, "FirebaseVisionImage Set", Toast.LENGTH_LONG);

            //FirebaseVisionImageLabeler detector = FirebaseVision.getInstance()
            //                                                   .getCloudImageLabeler();
            // Or, to set the minimum confidence required:
            FirebaseVisionCloudImageLabelerOptions options =
                    new FirebaseVisionCloudImageLabelerOptions.Builder()
                            .setConfidenceThreshold(0.7f)
                            .build();
            FirebaseVisionImageLabeler detector = FirebaseVision.getInstance().getOnDeviceImageLabeler();
            //FirebaseVisionImageLabeler detector = FirebaseVision.getInstance().getCloudImageLabeler();

            final String readString = readFromFile(MainActivity.this);
            detector.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                            String output = "";
                            String colour = color.getBiggest() + " " + uid + "\n";
                            Log.i(TAG, colour);

                            String write = readString;
                            for (FirebaseVisionImageLabel label: labels) {
                                output += "\n" + label.getText();
                                write += label.getText() + " " + uid + "\n";
                            }
                            characteristics.setText(output);
                            coloursX.setText("WHITE");
                            writeToFile(write, MainActivity.this);
                            //writeToFile(colour, MainActivity.this);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            textView.setText("Failed");
                        }
                    });
        }
    }

    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config6.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config6.txt");

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
            writeToFile("",MainActivity.this);
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
