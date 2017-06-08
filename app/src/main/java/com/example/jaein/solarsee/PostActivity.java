package com.example.jaein.solarsee;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.jaein.solarsee.LoginActivity.font;
import static com.example.jaein.solarsee.LoginActivity.loginId;
import static com.example.jaein.solarsee.LoginActivity.photoInfo;

/**
 * Created by jaein on 2017-05-23.
 */

public class PostActivity extends AppCompatActivity {

    Spinner spin;
    final int CAMERA_CODE = 1000;
    final int GALLERY_CODE = 2000;
    ImageView imageView;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    FirebaseStorage storage;
    String imagePath;
    Uri imgUri;
    EditText et_content;
    String content;
    static String filename;
    String str_spin;
    ArrayList<String> loca_list;
    SpinnerAdapter adapter;
    Button postBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        init();
    }

    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        imageView = (ImageView) findViewById(R.id.imageView);
        spin = (Spinner) findViewById(R.id.spinner);
        storage = FirebaseStorage.getInstance();
        et_content = (EditText)findViewById(R.id.content);
        postBtn = (Button)findViewById(R.id.postBtn);
        postBtn.setTypeface(font);

        loca_list = new ArrayList<>();
        loca_list.add("정동진");
        loca_list.add("간절곶");
        loca_list.add("이기대");
        loca_list.add("성산일출봉");
        loca_list.add("호미곶");
        loca_list.add("하늘공원");
        loca_list.add("꽃지해안공원");
        loca_list.add("변산반도");
        loca_list.add("땅끝마을");
        loca_list.add("향일암");
        loca_list.add("지리산천왕봉");
        loca_list.add("태백산");


        adapter = new SpinnerAdapter(this, R.layout.spinner_entry, loca_list);
        spin.setAdapter(adapter);


    }


    private void SelectCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File picture = savePictureFile();
        imgUri = Uri.fromFile(picture);
        if (picture != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
            startActivityForResult(intent, CAMERA_CODE);
        }
    }

    private void SelectGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_CODE);
    }

    private File savePictureFile() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
        Date now = new Date();
        String imageFileName = formatter.format(now);
            File pictureStorage = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM), "SOLARSEE/");
            // 만약 장소가 존재하지 않는다면 폴더를 새롭게 만든다.
            if (!pictureStorage.exists()) { /** * mkdir은 폴더를 하나만 만들고, * mkdirs는 경로상에 존재하는 모든 폴더를 만들어준다. */
                pictureStorage.mkdirs();
            }
            try {
                File file = File.createTempFile(imageFileName, ".png", pictureStorage);
                // ImageView에 보여주기위해 사진파일의 절대 경로를 얻어온다.
                imagePath = file.getAbsolutePath();
                // 찍힌 사진을 "갤러리" 앱에 추가한다.
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                File f = new File(imagePath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                return file;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case GALLERY_CODE:
                    SendPicture(data); //갤러리에서 가져오기
                    break;
                case CAMERA_CODE:
                    imageView.setImageURI(imgUri);
                    break;

                default:
                    break;
            }

        }
    }



    private void SendPicture(Intent data) {

        imgUri = data.getData();
        imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        imageView.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
    }

    public Bitmap rotate(Bitmap src, int degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }


    //사진 절대 경로 구하기
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    //회전 값 저장 함수
    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public void photoBtnClicked(View view) {
        switch (view.getId()) {
            case R.id.cameraBtn:
                int permissionCheck = ContextCompat.checkSelfPermission(PostActivity.this, android.Manifest.permission.CAMERA);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(PostActivity.this, new String[]{android.Manifest.permission.CAMERA}, 0);
                } else {
                    SelectCamera();
                }
                break;
            case R.id.galleryBtn:
                SelectGallery();
                break;
            case R.id.postBtn:
                str_spin = loca_list.get(spin.getSelectedItemPosition());
                uploadFile();
                break;
        }
    }

    private void uploadFile() {
        content = et_content.getText().toString();

        //업로드할 파일이 있으면 수행
        if (imgUri != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            filename = formatter.format(now) + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://solarsee-859f7.appspot.com").child("solarsee_images/" + filename);
            //올라가거라...
            storageRef.putFile(imgUri)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            saveFileToDB();
                            finish();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
}
    private void saveFileToDB(){
        Query photo_query = photoInfo.orderByChild("p_date").equalTo(filename);
        photo_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null) {  // 일치하는 이름이 없을때
                    int spot_pos = filename.indexOf(".");
                    filename = filename.substring(0,spot_pos);
                    // .png 앞부분까지 자름
                    photo pt = new photo(filename, loginId, content, str_spin, "");
                    photoInfo.child(filename).setValue(pt);
                    Toast.makeText(PostActivity.this, "디비저장 완료", Toast.LENGTH_SHORT).show();
                }
            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == 0) {
                Toast.makeText(this, "카메라 권한이 승인됨", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "카메라 권한이 거절됨", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void verifyStoragePermissions(Activity activity){
        int writePermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (writePermission != PackageManager.PERMISSION_GRANTED
                || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public class SpinnerAdapter extends ArrayAdapter<String> {
        Context context;
        List<String> a_list;

        public SpinnerAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            this.context = context;
            a_list = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(font);
            view.setGravity(Gravity.CENTER);
            view.setPadding(0,15,0,15);
            return view;
        }

        /**
         * 기본 스피너 View 정의
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(font);
            view.setGravity(Gravity.CENTER);
            return view;
        }
    }
}