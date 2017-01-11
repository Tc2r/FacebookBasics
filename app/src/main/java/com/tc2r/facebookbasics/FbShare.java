package com.tc2r.facebookbasics;

import android.app.Activity;
import android.net.Uri;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

/**
 * Created by Tc2r on 9/9/2016.
 */
public class FbShare {


    //a contentURL, the link to be shared
    String contentUrl;


    //a contentTitle that represents the title of the content in the link
    String contentTitle;

    //a contentDescription of the content, usually 2-4 sentences
    String contentDesc;

    //a imageURL, the URL of thumbnail image that will appear on the post
    String imageUrl;


    //shareDialog for FB
    private ShareDialog shareDialog;

    public FbShare(Activity activity, String contentDesc, String contentTitle, String contentUrl, String imageUrl) {
        shareDialog = new ShareDialog(activity);
        this.contentDesc = contentDesc;
        this.contentTitle = contentTitle;
        this.contentUrl = contentUrl;
        this.imageUrl = imageUrl;

        SendShare();
    }

    public void SendShare(){
        if(ShareDialog.canShow(ShareLinkContent.class)){
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("SUP EVERYONE!")
                    .setContentDescription("Coder who learned how to share")
                    .setContentUrl(Uri.parse("http://twitter.com/tc2r1"))
                    .setImageUrl(Uri.parse("http://i.imgur.com/85itM2E.png"))
                    .build();

            shareDialog.show(linkContent);
        }

    }
}
