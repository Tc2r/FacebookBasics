package com.tc2r.facebookbasics;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ShareDialog shareDialog;
    ImageView imageView;
    TextView txtName, txtURL, txtGender, txtBd;
    Button btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.wtf("THIS IS WHAT IT SAYS", String.valueOf(this));
        shareDialog = new ShareDialog(this);
        imageView = (ImageView)findViewById(R.id.imgPhoto);
        txtName = (TextView)findViewById(R.id.txtName);
        txtURL = (TextView)findViewById(R.id.txtURL);
        txtGender = (TextView)findViewById(R.id.txtGender);
        txtBd = (TextView)findViewById(R.id.txtBd);

        btnShare = (Button) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               FbShare share = new FbShare(MainActivity.this, "test","test","test","test");
               share.SendShare();


//                if(ShareDialog.canShow(ShareLinkContent.class)){
//                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                            .setContentTitle("SUP EVERYONE!")
//                            .setContentDescription("Coder who learned how to share")
//                            .setContentUrl(Uri.parse("http://twitter.com/tc2r1"))
//                            .setImageUrl(Uri.parse("http://i.imgur.com/85itM2E.png"))
//                            .build();
//
//                    shareDialog.show(linkContent);
//                }
            }
        });





        GetUserInfo();
        // Like

        LikeView likeView = (LikeView) findViewById(R.id.likeView);
        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
        likeView.setObjectIdAndType(
                "Http://twitter.com/tc2r1",
                LikeView.ObjectType.OPEN_GRAPH
        );

        // Share Dialog
        // You cannot preset the shared Link in design time, if you do so, the fb
        // look disabled. You will need to set the code as below

        ShareButton fbShareButton = (ShareButton) findViewById(R.id.fb_share_button);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle("TESTING AN APP")
                .setContentDescription("I AM SIMPLY TESTING AN APPLICATION TO MAKE SURE IT CAN LINK TO FACEBOOK LIKE A BOSS")
                .setContentUrl(Uri.parse("Http://www.twitter.com/tc2r"))
                .setImageUrl(Uri.parse("http://i.imgur.com/85itM2E.png"))
                .build();

        fbShareButton.setShareContent(content);
    }


    private void GetUserInfo() {

        // This code will help us to obtain information from facebook,
        //if you need another field not here, please check
        // https://developers.facebook.com/docs/graph-api/using-graph-api/

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // APPlication Code

                        try {
                        String gender = object.getString("gender");

                        String birthday = object.getString("birthday");
                        String name = object.getString("name");
                        String id = object.getString("id");

                            txtName.setText(name);
                            txtBd.setText(birthday);
                            txtGender.setText(gender);
                            txtURL.setText(id);

                            if(object.has("picture")){
                                String profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                Picasso.with(MainActivity.this)
                                .load(profilePicUrl)
                                .into(imageView);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, gender, name, birthday, picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
