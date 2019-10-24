package krunal.com.example.openimagefile;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button button;

    static final Integer WRITE_EXST = 1;
    static final Integer READ_EXST = 2;

    private static final String AUTHORITY=
            BuildConfig.APPLICATION_ID+".myfileprovider";
    static final Integer PERMISSION_REQUEST_ID =4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);

        // Check Permission.
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXST);
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXST);


        button.setOnClickListener(v -> {
            // Check Permission.
            askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXST);
            askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXST);

            // Save the image from mipmap folder to ImageOpenExample folder.
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_image);
            Log.e("bitmap",bitmap.toString());

            Utility.storeImage(bitmap);

            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            String dataPath = extStorageDirectory + "/ImageOpenExample/SampleImage.jpg";

            File currentfile = new File(dataPath);

            Uri outputUri=FileProvider.getUriForFile(this, AUTHORITY, currentfile);
            Intent viewFile = new Intent(Intent.ACTION_VIEW);
            viewFile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            viewFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            viewFile.setDataAndType(outputUri, "image/*");
            startActivity(viewFile);

        });
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
//            switch (requestCode) {
//
//                //Write external Storage
//                case 3:
//                    Log.e("case3","case 3");
//
//                    break;
//                //Read External Storage
//                case 4:
//                    Log.e("case4","case 4");
//                    Intent imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(imageIntent, 11);
//                    break;
//
//            }
//
//            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//        }
//
//    }
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if(requestCode==PERMISSION_REQUEST_ID){

        if (grantResults.length > 1
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

        } else {
            Log.e("permission", "Permission not granted");

        }
    }
}

}
